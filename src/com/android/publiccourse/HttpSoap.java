package com.android.publiccourse;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class HttpSoap {
	private static final String TAG = "HttpSoap";

	public static String getContent(String soapUrl, String soapMethod,
			String soapNameSapce, String soapHeaderName,
			Map<String, String> soapHeaderItem, Map<String, String> paras) {
		String SOAP_ACTION = soapNameSapce + soapMethod;
		String rs = "";

//		Log.v("SHAN", "soap url: " + soapUrl);
//		Log.v("SHAN", "soap method: " + soapMethod);
//		Log.v("SHAN", "soap ns: " + soapNameSapce);
//		Log.v("SHAN", "soap headerName: " + soapHeaderName);
//		Log.v("SHAN", "soap action: " + SOAP_ACTION);
//		Log.d("cn.com.open.learningbarapp", "the method="+soapMethod);
		SoapObject request = new SoapObject(soapNameSapce, soapMethod);
		// soap body 请求参数�?		
		if (paras != null && !paras.isEmpty()) {
			for (Iterator it = paras.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Entry) it.next();
//				Log.d("cn.com.open.learningbarapp", "the paras key="+e.getKey());
//				Log.d("cn.com.open.learningbarapp","the paras value="+e.getValue());
				request.addProperty(e.getKey().toString(), e.getValue());

			}
		}
		// soapheader请求参数�?	
		Element[] header = new Element[1];
		header[0] = new Element().createElement(soapNameSapce, soapHeaderName);
		if (soapHeaderItem != null && !soapHeaderItem.isEmpty()) {
			for (Iterator it = soapHeaderItem.entrySet().iterator(); it
					.hasNext();) {
				Map.Entry e = (Entry) it.next();
				Element subItem = new Element().createElement(soapNameSapce, e
						.getKey().toString());
				subItem.addChild(Node.TEXT, e.getValue());
				header[0].addChild(Node.ELEMENT, subItem);

			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.headerOut = header;
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(soapUrl);
		SoapObject result = null;
		try {
			// web service调用
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
			rs = result1.toString();
//			Log.d(TAG, "The show message" + rs);
		} catch (IOException e) {
			e.printStackTrace();
			// Log.d(TAG, "The current IOException is =="+e +e.getMessage());
			throw new BarException("http get url error IOException"
					+ e.getMessage(), e);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// Log.d(TAG, "The current XmlPullParserException is =="+e
			// +e.getMessage());
			throw new BarException("Xml parse error XmlPullParserException"
					+ e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			// Log.d(TAG, "The current Exception is =="+e +e.getMessage());
			throw new BarException("other not catch Exception  exist"
					+ e.getMessage(), e);
		}
		return rs;

	}
}
