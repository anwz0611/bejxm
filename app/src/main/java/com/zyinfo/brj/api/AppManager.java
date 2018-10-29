package com.zyinfo.brj.api;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AppManager {

	private static final String TAG = AppManager.class.getSimpleName();
	private List<Activity> activitys = new ArrayList<Activity>();
	private static AppManager instance;

	private AppManager() {

	}

	/**
	 * @Title: getAppManager
	 * @Description: TODO(单一实例)
	 * @param @return
	 * @return AppManager
	 * @throws
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * @Title: addActivity
	 * @Description: TODO(添加Activity到堆栈)
	 * @param @param activity
	 * @return void
	 * @throws
	 */
	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	/**
	 * @Title: currentActivity
	 * @Description: TODO(获取当前Activity（堆栈中最后一个压入的）)
	 * @param @return
	 * @return Activity
	 * @throws
	 */
	public Activity currentActivity() {
		Activity activity = activitys.get(activitys.size() - 1);
		return activity;
	}

	/**
	 * @Title: finishAllActivity
	 * @Description: TODO(结束所有activity)
	 * @param
	 * @return void
	 * @throws
	 */
	public void finishAllActivity() {
		for (Activity activity : activitys) {
			activity.finish();
		}
		activitys.clear();
	}

	/**
	 * @Title: AppExit
	 * @Description: TODO(退出应用)
	 * @param @param context
	 * @return void
	 * @throws
	 */
	public void AppExit(Context context) {
		finishAllActivity();
		//System.exit(0);
	}
}