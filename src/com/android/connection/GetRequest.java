package com.android.connection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

public class GetRequest {
	
	private static final int REQUEST_TIMEOUT = 5*1000;
	private static final int SO_TIMEOUT = 10*1000;
	
	
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	public String request(String urlStr) {
		String responseMsg = "";

		HttpPost request = new HttpPost(urlStr);
		try {


			HttpClient client = getHttpClient();

			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {

				responseMsg = EntityUtils
						.toString(response.getEntity());
			} else {
				responseMsg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (responseMsg.equals("null")) {
			return null;
		} else
			return responseMsg.trim();
	}

}
