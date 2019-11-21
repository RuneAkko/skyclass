package com.android.publiccourse;

import java.util.HashMap;

import com.android.svod.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;


public class HttpTask implements Runnable {
	private static final String TAG = "HttpSoapTask";
	// 参数名称定义
	private BindDataService mService; // 中转的Service
	private Intent mIntent; // 广播发�?的Intent，在构�?方法中初始化
	private TaskType taskType; // 任务类型，由service传入
	private Class<? extends BusinessResponse> responseClass; // 响应结果
	private Class<? extends Context> intentActionClass; // 取类名声称action，在IntentFilter中过�?
	private String httpUrl; // soap方法请求地址
	private HashMap<String, String> paramsMap; // 请求参数，由service传入
	private HashMap<String, String> headerMap; // http的header头参�?
	private int httpRequest;// http请求方式

	/**
	 * 构�?方法
	 * 
	 * @param mService
	 *            : MeosDataService引用
	 * @param intentAction
	 * @param taskType
	 * @param url
	 * @param responseClass
	 * @param paramsMap
	 *            : 参数�?	 */
	public HttpTask(BindDataService mService,
			Class<? extends Context> intentAction, TaskType taskType,
			Class<? extends BusinessResponse> responseClass, String httpUrl,
			HashMap<String, String> headerParam,
			HashMap<String, String> paramsMap, int httpRequest) {
		this.mService = mService;
		intentActionClass = intentAction;
		mIntent = new Intent(intentAction.getName());// Action取传入的Activity类名
		this.taskType = taskType;
		this.responseClass = responseClass;
		this.httpUrl = httpUrl;
		this.headerMap = headerParam;
		this.paramsMap = paramsMap;
		this.httpRequest = httpRequest;
	}

	/**
	 * 执行方法
	 */
	public void run() {

		BusinessResponse rep = null;
		mIntent.putExtra(Constants.DEFAULT_INTENT_EXTRA_ACTION,
				intentActionClass.getName());
		try {
			rep = getBusinessResponse();
		} catch (BarException ex) {// 发生异常，广播异常，UI做后续处�?			
			Log.e(Constants.TASK_LOG_TAG, ex.getMessage());
			mIntent.putExtra(Constants.DEFAULT_INTENT_EXTRA_TYPE,
					TaskType.Meos_Ex.name());// 广播�?��异常

			rep = new BusinessResponse();
			rep.setData(null);
			rep.setMessage(ex.getMessage());
			rep.setStatus(false);
			// Log.d(TAG, "the current message is only sole"+ex.getMessage());
			if (ex.getMessage().contains(
					mService.getResources().getString(
							R.string.net_conncet_error))) {
				// Log.d(TAG, "the current message"+ex.getMessage());
				rep.setCode(String.valueOf(Constants.NET_CONNECT_ERROR_TAG));

			}

			mIntent.putExtra(Constants.DEFAULT_INTENT_EXTRA_RESPONSE, rep);
			mService.sendBroadcast(mIntent);
			return;
		}
		if (rep != null) {
			// Log.d(Constants.TASK_LOG_TAG,
			// "http response return sucess! put into intent start");
			mIntent.putExtra(Constants.DEFAULT_INTENT_EXTRA_TYPE,
					taskType.name());
		} else {
			Log.w(Constants.TASK_LOG_TAG,
					">>>>http response exception: response is null!!!");
		}
		mIntent.putExtra(Constants.DEFAULT_INTENT_EXTRA_RESPONSE, rep);
		mService.sendBroadcast(mIntent);
		return;
	}

	/**
	 * HTTP请求响应 应为private�?	 * 
	 * @return
	 */
	public final BusinessResponse getBusinessResponse() {
		BusinessResponse response = mService.sdkClient.getHttpBusinessResponse(
				responseClass, httpUrl, headerMap, paramsMap, httpRequest);

		if (!response.getStatus()) {
			// Log.w(TAG, "The current return status value is error" +
			// response);
			String code = response.getCode();
			if (!"".equals(code)) {
				String key = "Meos_Server_Error_" + code.replace('.', '_');
				Resources resources = Configuration.getResources();
				int id = resources.getIdentifier(key, "string",
						"cn.com.open.learningbarapp");
				if (id == 0)
					id = R.string.Learningbar_Server_Error_UnKonow;
				String message = resources.getString(id);
				// Log.d("open.meos.sdk", "response code:" + code + " Message:"
				// + message);
				response.setMessage(message);
				if (code.contains(Constants.SER_SYS_ERR)) {
					throw new BarException("response status:"
							+ response.getStatus() + " Message:" + message);// 服务端异�?			
					}
			}
		}
		return response;
	}
}
