package com.zyinfo.brj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtil {

	public final static String DATABASE = "Database";

	public static void putValue(Context context, String key, String value) {
		SharedPreferences sharedPreferences=context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);

		Editor editor = sharedPreferences.edit();

		editor.putString(key, value);

		editor.commit();

	}


	public static void deleteValue(Context context, String key) {
		SharedPreferences sharedPreferences=context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);

		Editor editor = sharedPreferences.edit();

		editor.remove(key);

		editor.commit();

	}

	public static String getValue(Context context, String key, String defValue) {

		SharedPreferences editor = context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);

		String value = editor.getString(key, defValue);

		return value;
	}
	public static void putIntValue(Context context, String key, int value) {
		SharedPreferences sharedPreferences=context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);

		Editor editor = sharedPreferences.edit();

		editor.putInt(key, value);

		editor.commit();

	}

	public static int getIntValue(Context context, String key, int defValue) {

		SharedPreferences editor = context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);

		int value = editor.getInt(key, defValue);

		return value;
	}

}
