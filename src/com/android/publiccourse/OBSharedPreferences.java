package com.android.publiccourse;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @ClassName: OBSharedPreferences
 * @Description: TODO
 * @author zhangchunzhe
 * @date 2013-6-27 下午3:50:24
 * 
 */

public class OBSharedPreferences {

	private static OBSharedPreferences mPreferences;
	private SharedPreferences mSharedPreferences;
	private static final String FILE_NAME = "OBSharedPreferences";

	/**
	 * 
	 * @param context
	 */
	private OBSharedPreferences(Context context) {
		mSharedPreferences = context.getSharedPreferences(FILE_NAME, 0);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static OBSharedPreferences getInstance(Context context) {
		if (mPreferences == null) {
			mPreferences = new OBSharedPreferences(context);
		}
		return mPreferences;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Long getLongValue(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getSettingBooleanValue(String key) {
		return mSharedPreferences.getBoolean(key, true);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		return mSharedPreferences.getInt(key, 0);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getIntDefaultValue(String key) {
		return mSharedPreferences.getInt(key, -1);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getStringDefaultValue(String key) {
		return mSharedPreferences.getString(key, "");
	}

	/**
	 * 
	 * @param key
	 */
	public void putIntValue(String key, int value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(key);
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 */
	public void putLongValue(String key, long value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 */
	public void putBooleanValue(String key, boolean bool) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, bool);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 */
	public void putStringValue(String key, String value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 
	 */
	public void clearSharedPreferences() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
