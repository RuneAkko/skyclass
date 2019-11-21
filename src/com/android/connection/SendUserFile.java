package com.android.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.domain.StringUtility;
import com.android.domain.User;
import com.android.svod.Login;
import com.android.svod.Page4;


public class SendUserFile {

    private static final String TAG = "SendUserFile";
    private static final int REQUEST_TIMEOUT = 5 * 1000;
    private static final int SO_TIMEOUT = 10 * 1000;
    private Boolean isSend = false;

    public SendUserFile() {
        super();
        // TODO Auto-generated constructor stub
    }

    public boolean Send(List<User> usrs) {

//		 String path="https://xueli.xjtudlc.com/MobileLearning/learningLog.aspx?p=3";
        String path = "http://118.190.245.63:5000/user_log_post/";

        try {
            Map<String, Object> params1 = new HashMap<String, Object>();

            for (User usr : usrs) {

                String fileName = URLEncoder.encode(usr.getpath(), "utf-8");
                if (usr.getOperationCode() == "1") {
                    params1.put("o", "1");
                    params1.put("t", usr.getTime());
                    params1.put("s", usr.getstudentcode());
                    params1.put("c", usr.getcourseid());
                    params1.put("i", "androidphone-" + fileName);
                }
                //

                params1.put("o", usr.getOperationCode());
                //

                params1.put("t", usr.getTime());
                //

                params1.put("s", usr.getstudentcode());
                //courseid

                params1.put("c", usr.getcourseid());
                //
                params1.put("l", usr.getlen());
                //
                params1.put("i", "androidphone-" + fileName);
                isSend = post(params1, path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSend;
    }

    //HttpClient已在6.0后被废弃
//    public HttpClient getHttpClient() {
//        BasicHttpParams httpParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
//        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
//        HttpClient client = new DefaultHttpClient(httpParams);
//        return client;
//    }

    /**
     * @param map
     * @param path
     * @return
     * @throws Exception
     */
    public boolean post(Map<String, Object> map, String path) throws Exception {


        //
        StringBuilder sb = new StringBuilder(path);

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            sb.append("&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());

        }
//		Log.i(TAG, "1 :" + sb);
        String str = sb.toString();


        str = str.replace(" ", "%20");

        Log.i(TAG,
                "what posted : " + str);


        URL url = new URL(str);

        Log.i(TAG,
                "post url:" + url);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-agent", "Mozilla/4.0");
//		conn.setRequestProperty("Content-Type", String.valueOf(str.getBytes().length));
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
//		conn.setDoOutput(true);
//		conn.setDoInput(true);
//		conn.setUseCaches(false);
		conn.connect();

        Log.i(TAG,
                "responseCode:" + conn.getResponseCode());


//		OutputStream stream = conn.getOutputStream();
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
//
//		writer.write(str);
//		writer.flush();
//		stream.close();
//		writer.close();


        if (conn.getConnectTimeout() <= 10000 && conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = null;
            InputStream in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder res = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            Log.i(TAG, "user log response : " + res);
            conn.disconnect();
            return true;
        } else {
            conn.disconnect();
            return false;
        }

    }
}
