package com.zyinfo.brj.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */

public class GsonUtils {

	public static <T> T getPerson(String jsonString, Class<T> cls) {
		T t = null;
		try {

			JSONObject json = new JSONObject(jsonString);

			String message = json.getString("data");
			Gson gson = new Gson();
			t = gson.fromJson(message, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public static <T> List<T> getPersons(String jsonString, Class<T[]> cls) {

		T[] array = null;

		try {

			JSONObject json = new JSONObject(jsonString);

			String message = json.getString("data");

			array = new Gson().fromJson(message, cls);

		} catch (Exception e) {

		}
		return Arrays.asList(array);
	}

	@NonNull
	public static <T> List<T> getGSPersons(String jsonString, Class<T[]> cls) {

		T[] array = null;

		try {

			array = new Gson().fromJson(jsonString, cls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Arrays.asList(array);
	}

	@NonNull
	public static <T> List<T> getFriends(String jsonString, Class<T[]> cls) {

		T[] array = null;

		try {

			JSONObject json = new JSONObject(jsonString);

			String message = json.getString("Table");

			array = new Gson().fromJson(message, cls);

		} catch (Exception e) {

		}
		return Arrays.asList(array);
	}

	public static List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
