package com.android.publiccourse;

import com.android.svod.R;

import android.content.res.Resources;
import android.util.Log;

//配置网络调用常用的字�?
public final class Configuration {

	private static Resources resources; // can be improve: comment by renkf.

	public static Resources getResources() {
		return resources;
	}

	public static void setResources(Resources resources) {
		Configuration.resources = resources;
	}

	public static String getLoginUrl() {
		if (getProperty(R.string.learningbar_sdk_soap_url) != null) {
			if (getProperty(R.string.learningbar_sdk_soap_format_user) != null) {
				return getProperty(R.string.learningbar_sdk_soap_url)
						+ getProperty(R.string.learningbar_sdk_soap_format_user);
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public static String getCourseUrl() {

		if (getProperty(R.string.learningbar_sdk_soap_url) != null) {
			if (getProperty(R.string.learningbar_sdk_soap_format_course) != null) {
				return getProperty(R.string.learningbar_sdk_soap_url)
						+ getProperty(R.string.learningbar_sdk_soap_format_course);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public static String getCourseUrl_skyclass() {

		if (getProperty(R.string.skyclass_sdk_soap_url) != null) {
			if (getProperty(R.string.skyclass_sdk_soap_format_course) != null) {
				return getProperty(R.string.skyclass_sdk_soap_url)
						+ getProperty(R.string.skyclass_sdk_soap_format_course);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getGroupUrl() {

		if (getProperty(R.string.learningbar_sdk_soap_url) != null) {
			if (getProperty(R.string.learningbar_sdk_soap_format_group) != null) {
				return getProperty(R.string.learningbar_sdk_soap_url)
						+ getProperty(R.string.learningbar_sdk_soap_format_group);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getPersonalUrl() {

		if (getProperty(R.string.learningbar_sdk_soap_url) != null) {
			if (getProperty(R.string.learningbar_sdk_soap_format_friend) != null) {
				return getProperty(R.string.learningbar_sdk_soap_url)
						+ getProperty(R.string.learningbar_sdk_soap_format_friend);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getSoapNameSapce() {
		return getProperty(R.string.learningbar_sdk_soap_namespace);
	}

	public static String getSoapNameSapce_skyclass() {
		return getProperty(R.string.skyclass_sdk_soap_namespace);
	}
	public static String getSoapNameGroupSapce() {
		return getProperty(R.string.learningbar_sdk_soap_groupspace);
	}

	public static String getSoapNameFriendSapce() {
		return getProperty(R.string.learningbar_sdk_soap_personal);
	}

	public static String getDefaultSoapHeader() {
		return getProperty(R.string.learningbar_sdk_soap_soapheader);
	}

	public static boolean getBoolean(int key) {
		String value = getProperty(key);
		return Boolean.valueOf(value);
	}

	public static int getIntProperty(int key) {
		String value = getProperty(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static int getIntProperty(int key, int fallbackValue) {
		String value = getProperty(key, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static long getLongProperty(int key) {
		String value = getProperty(key);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static String getProperty(int key) {
		return getProperty(key, null);
	}

	public static String getProperty(int key, String defaultValue) {
		String value = null;
		try {
			if (resources == null)
				return defaultValue;
			value = resources.getString(key);
		} catch (Exception e) {
			Log.e("read resource is error", e.toString());
			value = "";
		}
		if ("".equals(value))
			return defaultValue;
		return value;
	}

	/**
	 * 学吧后台服务Http请求URL
	 * 
	 * @param urlRes
	 * @return
	 */
	public static String getHttpUrl(int urlRes) {
		String netconnction = resources
				.getString(R.string.learningbar_sdk_connection_environment);
		StringBuilder stringBuilder = new StringBuilder();
		if ("test".equals(netconnction)) {
			stringBuilder = new StringBuilder(
					resources.getString(R.string.learningbar_sdk_http_url));
		} else {
			stringBuilder = new StringBuilder(
					resources.getString(R.string.learningbar_sdk_http_url));
		}
		return stringBuilder.append(resources.getString(urlRes)).toString();
	}

	public static String getOpenCourseHttpUrl(int urlRes){
		//该网址die
		StringBuilder stringBuilder = new StringBuilder(
					resources.getString(R.string.PubCourseService));

		return stringBuilder.append(resources.getString(urlRes)).toString();		
	}
	
}
