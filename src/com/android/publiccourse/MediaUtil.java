package com.android.publiccourse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaUtil {

	public static final boolean METADATA_ALL = false;
	public static final boolean BYPASS_METADATA_FILTER = false;

	// Playback capabilities.
	public static final int PAUSE_AVAILABLE = 29; // Boolean
	public static final int SEEK_BACKWARD_AVAILABLE = 30; // Boolean
	public static final int SEEK_FORWARD_AVAILABLE = 31; // Boolean
	public static final int SEEK_AVAILABLE = 32; // Boolean

	public static Object getMetadata(MediaPlayer mediaPlayer) {
		Class<?> clazz;
		try {
			clazz = Class.forName("android.media.MediaPlayer");
			Method method = clazz.getMethod("getMetadata", boolean.class,
					boolean.class);
			return method.invoke(mediaPlayer, METADATA_ALL,
					BYPASS_METADATA_FILTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setDataSource(MediaPlayer mediaPlayer, Context context,
			Uri uri, Map<String, String> headers)
			throws IllegalArgumentException, IOException {
		Class<?> clazz;
		try {
			clazz = Class.forName("android.media.MediaPlayer");
			Method method = clazz.getMethod("setDataSource", Context.class,
					Uri.class, Map.class);
			method.invoke(mediaPlayer, context, uri, headers);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean suspend(MediaPlayer mediaPlayer) {
		Class<?> clazz;
		try {
			clazz = Class.forName("android.media.MediaPlayer");
			Method method = clazz.getMethod("suspend");
			return (Boolean) method.invoke(mediaPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean resume(MediaPlayer mediaPlayer) {
		Class<?> clazz;
		try {
			clazz = Class.forName("android.media.MediaPlayer");
			Method method = clazz.getMethod("resume");
			return (Boolean) method.invoke(mediaPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Metadata has method
	 */
	public static boolean has(Object metadata, int OPERATER_AVAILABLE) {
		try {
			Class<?> clazz = Class.forName("android.media.Metadata");
			Method method = clazz.getMethod("has", int.class);
			return (Boolean) method.invoke(metadata, OPERATER_AVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Metadata getBoolean method
	 */
	public static boolean getBoolean(Object metadata, int OPERATER_AVAILABLE) {
		try {
			Class<?> clazz = Class.forName("android.media.Metadata");
			Method method = clazz.getMethod("getBoolean", int.class);
			return (Boolean) method.invoke(metadata, OPERATER_AVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * getTime
	 */
	public static boolean getString(Object metadata, int OPERATER_AVAILABLE) {
		try {
			Class<?> clazz = Class.forName("android.media.Metadata");
			Method method = clazz.getMethod("getTimedText", int.class);
			return (Boolean) method.invoke(metadata, OPERATER_AVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
