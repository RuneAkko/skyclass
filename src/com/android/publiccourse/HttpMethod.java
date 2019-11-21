package com.android.publiccourse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


public class HttpMethod {

    final static String CONTENTTYPE_JSON = "application/json";
    final static String CONTENTTYPE_XML = "application/xml";
    final static String CONTENTTYPE_Content = "";
    private static final int REQUEST_TIMEOUT = 20 * 1000;// 设置请求超时10秒钟
    private static final int SO_TIMEOUT = 20 * 1000; // 设置等待数据超时时间10秒钟

    public static String getContent(String url, Map<String, String> paras, Map<String, String> handers, int httpMethod) {
        HttpResponse response = null;
        if (httpMethod == Constants.HTTP_GET) {
            response = getResponse(url, paras, handers, REQUEST_TIMEOUT, SO_TIMEOUT);
        } else if (httpMethod == Constants.HTTP_POST) {
            HashMap<String, String> httpParas = null;
            response = postFormResponse(url, httpParas, handers, paras, REQUEST_TIMEOUT, SO_TIMEOUT);
        }
        if (response == null)
            return "";
        // String content = "";
        StringBuilder content = new StringBuilder();
        HttpEntity httpEntity = response.getEntity();
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                // content += line;
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BarException("Http获取内容错误 " + e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Log.d(Constants.TASK_LOG_TAG, "\r\n");
        // Log.d(Constants.TASK_LOG_TAG, "get URL:" + url);
        // Log.d(Constants.TASK_LOG_TAG, "get content:" + content);
        // Log.d(Constants.TASK_LOG_TAG, "\r\n");
        return content.toString();
    }

    // <editor-fold desc="get">
    public static HttpResponse getJsonResponse(String url, Map<String, String> paras, Map<String, String> handers, int connectionTimeout, int soTimeout) {
        return getResponse(url, paras, handers, CONTENTTYPE_JSON, connectionTimeout, soTimeout);
    }

    public static HttpResponse getXmlResponse(String url, Map<String, String> paras, Map<String, String> handers, int connectionTimeout, int soTimeout) {
        return getResponse(url, paras, handers, CONTENTTYPE_XML, connectionTimeout, soTimeout);
    }

    public static HttpResponse getResponse(String url, Map<String, String> paras, Map<String, String> handers, int connectionTimeout, int soTimeout) {
        return getResponse(url, paras, handers, CONTENTTYPE_Content, connectionTimeout, soTimeout);
    }

    public static HttpResponse getResponse(String url, Map<String, String> paras, Map<String, String> handers, String contentType, int connectionTimeout, int soTimeout) {
        // System.out.println("------------------Get---------------------------");
        String paraString = buildUrlParams(paras);
        // System.out.println("send_baseurl:" + url);
        // System.out.println("send_para:" + paraString);
        if (paraString.length() > 0)
            url = url + "?" + paraString;
        // System.out.println("send_url:" + url);
        HttpGet httpRequest = new HttpGet(url);

        return httpResponse(httpRequest, handers, contentType, connectionTimeout, soTimeout);
    }

    // </editor-fold>

    // <editor-fold desc="post">
    public static HttpResponse postFormResponse(String url, Map<String, String> paras, Map<String, String> handers, Map<String, String> datas, int connectionTimeout, int soTimeout) {
        List<NameValuePair> postdata = new LinkedList<NameValuePair>();
        if (datas != null) {
            for (Map.Entry<String, String> entry : datas.entrySet()) {
                // System.out.println("http post data  [key:" + entry.getKey()
                // + "] [value:" + entry.getValue().toString() + "]");
                postdata.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            HttpEntity entity = new UrlEncodedFormEntity(postdata, HTTP.UTF_8);
            return postResponse(url, paras, handers, entity, "", connectionTimeout, soTimeout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BarException("Http获取内容错误 " + e.getMessage(), e);
        }
    }

    public static HttpResponse postFormFileResponse(String url, Map<String, String> paras, Map<String, String> handers, Map<String, Object> datas, List<File> files, int connectionTimeout, int soTimeout) {
        List<NameValuePair> postdata = new LinkedList<NameValuePair>();
        if (datas != null) {
            for (Map.Entry<String, Object> entry : datas.entrySet()) {
                // System.out.println("http post data  [key:" + entry.getKey()
                // + "] [value:" + entry.getValue().toString() + "]");
                postdata.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }

        List<HttpEntity> entities = new LinkedList<HttpEntity>();
        try {
            HttpEntity entity = new UrlEncodedFormEntity(postdata, HTTP.UTF_8);
            entities.add(entity);
            if (files != null) {
                for (File file : files) {
                    FileEntity fileEntity = new FileEntity(file, "binary/octet-stream");
                    entities.add(fileEntity);
                }
            }
            return postResponse(url, paras, handers, entities, "", connectionTimeout, soTimeout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("http get url error UnsupportedEncodingException" + e.getMessage(), e);
        }
    }

    public static HttpResponse postJsonResponse(String url, Map<String, String> paras, Map<String, String> handers, Map<String, Object> datas, int connectionTimeout, int soTimeout) {
        JSONObject json = JsonUtil.getObjectFromMap(datas);
        return postJsonResponse(url, paras, handers, json.toString(), connectionTimeout, soTimeout);
    }

    public static HttpResponse postJsonResponse(String url, Map<String, String> paras, Map<String, String> handers, String jsonContent, int connectionTimeout, int soTimeout) {
        return postContentResponse(url, paras, handers, jsonContent, CONTENTTYPE_JSON, connectionTimeout, soTimeout);
    }

    public static HttpResponse postXmlResponse(String url, Map<String, String> paras, Map<String, String> handers, String xmlContent, int connectionTimeout, int soTimeout) {
        return postContentResponse(url, paras, handers, xmlContent, CONTENTTYPE_XML, connectionTimeout, soTimeout);
    }

    public static HttpResponse postContentResponse(String url, Map<String, String> paras, Map<String, String> handers, String postContent, String contentType, int connectionTimeout, int soTimeout) {
        try {
            StringEntity entity = new StringEntity(postContent, HTTP.UTF_8);
            return postResponse(url, paras, handers, entity, contentType, connectionTimeout, soTimeout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("http get url error UnsupportedEncodingException" + e.getMessage(), e);
        }
    }

    protected static HttpResponse postResponse(String url, Map<String, String> paras, Map<String, String> handers, HttpEntity entity, String contentType, int connectionTimeout, int soTimeout) {
        List<HttpEntity> entities = new LinkedList<HttpEntity>();
        entities.add(entity);
        return postResponse(url, paras, handers, entities, contentType, connectionTimeout, soTimeout);
    }

    protected static HttpResponse postResponse(String url, Map<String, String> paras, Map<String, String> handers, List<HttpEntity> entities, String contentType, int connectionTimeout, int soTimeout) {
        // System.out.println("------------------Post---------------------------");
        String paraString = buildUrlParams(paras);
        // System.out.println("send_baseurl:" + url);
        // System.out.println("send_para:" + paraString);
        if (paraString.length() > 0)
            url = url + "?" + paraString;
        // System.out.println("send_url:" + url);
        HttpPost request = new HttpPost(url);
        if (entities != null) {
            for (HttpEntity entity : entities) {
                request.setEntity(entity);
            }

        }

        return httpResponse(request, handers, contentType, connectionTimeout, soTimeout);
    }

    // </editor-fold>

    // <editor-fold desc="put">
    public static HttpResponse putFormResponse(String url, Map<String, String> paras, Map<String, String> handers, Map<String, Object> datas, int connectionTimeout, int soTimeout) {
        List<NameValuePair> putdata = new LinkedList<NameValuePair>();
        if (datas != null) {
            for (Map.Entry<String, Object> entry : datas.entrySet()) {
                System.out.println("http put data  [key:" + entry.getKey() + "] [value:" + entry.getValue().toString() + "]");
                putdata.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        try {
            HttpEntity entity = new UrlEncodedFormEntity(putdata, HTTP.UTF_8);
            return putResponse(url, paras, handers, entity, "", connectionTimeout, soTimeout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("http get url error UnsupportedEncodingException" + e.getMessage(), e);
        }
    }

    public static HttpResponse putJsonResponse(String url, Map<String, String> paras, Map<String, String> handers, Map<String, Object> datas, int connectionTimeout, int soTimeout) {
        JSONObject json = JsonUtil.getObjectFromMap(datas);
        return putJsonResponse(url, paras, handers, json.toString(), connectionTimeout, soTimeout);
    }

    public static HttpResponse putJsonResponse(String url, Map<String, String> paras, Map<String, String> handers, String jsonContent, int connectionTimeout, int soTimeout) {
        return putContentResponse(url, paras, handers, jsonContent, CONTENTTYPE_JSON, connectionTimeout, soTimeout);
    }

    public static HttpResponse putXmlResponse(String url, Map<String, String> paras, Map<String, String> handers, String xmlContent, int connectionTimeout, int soTimeout) {
        return putContentResponse(url, paras, handers, xmlContent, CONTENTTYPE_XML, connectionTimeout, soTimeout);
    }

    public static HttpResponse putContentResponse(String url, Map<String, String> paras, Map<String, String> handers, String putContent, String contentType, int connectionTimeout, int soTimeout) {
        try {
            StringEntity entity = new StringEntity(putContent, HTTP.UTF_8);
            return putResponse(url, paras, handers, entity, contentType, connectionTimeout, soTimeout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("http get url error UnsupportedEncodingException" + e.getMessage(), e);
        }
    }

    public static HttpResponse putResponse(String url, Map<String, String> paras, Map<String, String> handers, HttpEntity entity, String contentType, int connectionTimeout, int soTimeout) {
        // System.out.println("------------------Put---------------------------");
        String paraString = buildUrlParams(paras);
        // System.out.println("send_baseurl:" + url);
        // System.out.println("send_para:" + paraString);
        if (paraString.length() > 0)
            url = url + "?" + paraString;
        // System.out.println("send_url:" + url);
        HttpPut request = new HttpPut(url);
        if (entity != null) {
            request.setEntity(entity);
        }

        return httpResponse(request, handers, contentType, connectionTimeout, soTimeout);
    }

    private static HashMap<String, String> sessionidMap = new HashMap<String, String>();//liyi 连接多个host时，sessionid不能认证
    // </editor-fold>
    // <editor-fold desc="http resoponse">
    protected static HttpResponse httpResponse(HttpRequestBase request, Map<String, String> handers, String contentType, int connectionTimeout, int soTimeout) {
        if (contentType != null && contentType.length() > 0) {
            request.addHeader(HTTP.CONTENT_TYPE, contentType);
        }
        if (handers != null) {
            for (Map.Entry<String, String> entry : handers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        HttpResponse response = null;
        
        String host = request.getURI().getHost();
        if (sessionidMap.containsKey(host)){
        	request.setHeader("Cookie", "JSESSIONID=" + sessionidMap.get(host)); 
        }

        try {
            response = client.execute(request);
            if (!sessionidMap.containsKey(host)){
                CookieStore mCookieStore = client.getCookieStore();  
                List<Cookie> cookies = mCookieStore.getCookies();  
                for (int i = 0; i < cookies.size(); i++) {  
                    if ("JSESSIONID".equalsIgnoreCase(cookies.get(i).getName())) { 
                    	sessionidMap.put(host, cookies.get(i).getValue());
                        break;  
                    }  
                }  
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new BarException("Http获取内容错误 " + e.getMessage(), e);
            // throw new IllegalArgumentException(
            // "http post url error ClientProtocolException"
            // + e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BarException("Http获取内容错误 " + e.getMessage(), e);
            // throw new IllegalArgumentException(
            // "http post url error IOException" + e.getMessage(), e);
        }
        return response;
    }

    // </editor-fold>

    // <editor-fold desc="common method">
    protected static String buildUrlParams(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
            }
        }
        return sb.toString();
    }
    // </editor-fold>

}
