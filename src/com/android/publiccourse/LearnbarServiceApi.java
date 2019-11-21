package com.android.publiccourse;

import java.io.IOException;
import java.util.Map;



public final class LearnbarServiceApi {

    private static final String TAG = "LearnbarServiceApi";

    /**
     * @param classz
     * @param soapUrl
     * @param soapMethod
     * @param soapNameSapce
     * @param soapHeaderName
     * @param soapHeaderItem
     * @param paras
     * @return <RESPONSE>
     * @throws IOException
     */
    public <RESPONSE extends BusinessResponse> RESPONSE getBusinessResponse(Class<RESPONSE> classz, String soapUrl, String soapMethod, String soapNameSapce, String soapHeaderName, Map<String, String> soapHeaderItem, Map<String, String> paras) {
        RESPONSE response = null;

        String content = HttpSoap.getContent(soapUrl, soapMethod, soapNameSapce, soapHeaderName, soapHeaderItem, paras);
        try {
            response = classz.newInstance();
        } catch (InstantiationException e) {
            throw new BarException("实例化对象错�?." + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new BarException("实例化对象错�?." + e.getMessage(), e);
        }
        if (content == "") {
            response.setStatus(false);
            response.setMessage("返回数据为NULL");
        } else {
            response.StringToObject(content);
        }

        return response;
    }

    /**
     * @param classz
     * @param httpUrl
     * @param headerParas
     * @param paras
     * @param requestMethod
     * @return <RESPONSE>
     * @throws IOException
     */
    public <RESPONSE extends BusinessResponse> RESPONSE getHttpBusinessResponse(Class<RESPONSE> classz, String httpUrl, Map<String, String> headerParas, Map<String, String> paras, int requestMethod) {
        RESPONSE response = null;

        String content = HttpMethod.getContent(httpUrl, paras, headerParas, requestMethod);
        try {
            response = classz.newInstance();
        } catch (InstantiationException e) {
            throw new BarException("实例化对象错�?." + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new BarException("实例化对象错�?." + e.getMessage(), e);
        }
        if (content.isEmpty()) {
            response.setStatus(false);
            response.setMessage("返回数据为NULL");
        } else {
            response.StringToObject(content);
        }

        return response;
    }
}
