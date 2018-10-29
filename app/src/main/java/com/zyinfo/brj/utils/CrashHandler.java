/**
 * 
 */
package com.zyinfo.brj.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.zyinfo.brj.api.AppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
*
*/

public class CrashHandler extends Exception implements UncaughtExceptionHandler {
	
	 /** Debug Log Tag */  
	public static final String TAG = "CrashHandler";
  /** CrashHandler实例 */  
  private static CrashHandler INSTANCE = new CrashHandler();  
  /** 程序的Context对象 */  
  private Context mContext;
  /** 系统默认的UncaughtException处理类 */  
  private Thread.UncaughtExceptionHandler mDefaultHandler;
  private Activity activity;
  private AlertDialog appCrashDialog;
////用来存储设备信息和异常信息
		private Map<String, String> infos = new HashMap<String, String>();
	//	// 用于格式化日期,作为日志文件名的一部分
		private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		private String errorMassge;

    
  private CrashHandler() { 

  }  

  /** 获取CrashHandler实例 ,单例模式 */  
  public static CrashHandler getInstance() {  
      return INSTANCE;  
  }  
    
  /** 
   * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器 
   * @param ctx 
   */  
  public void init(Context ctx) {
      mContext = ctx;  
      mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler(this);
  }  
    
  /** 
   * 当UncaughtException发生时会转入该函数来处理 
   */  
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
      if (!handleException(ex) && mDefaultHandler != null) {  
          // 如果用户没有处理则让系统默认的异常处理器来处理  
      	Log.e("进入uncaughtException", "如果用户没有处理则让系统默认的异常处理器来处理 ");
          mDefaultHandler.uncaughtException(thread, ex);  
      } else {  
          // Sleep一会后结束程序  
          // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序  
          try {  
              Thread.sleep(5000);
          } catch (InterruptedException e) {
              Log.e(TAG, "Error : ", e);
          }  
          android.os.Process.killProcess(android.os.Process.myPid());  
          AppManager.getAppManager().finishAllActivity();
          System.exit(10);
      }  
  }  

  /** 
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑 
   * @param ex 
   * @return true:如果处理了该异常信息;否则返回false 
   */  
  private boolean handleException(Throwable ex) {
      if (ex == null) {  
          return false;  
      } 
//    // 收集设备信息  
    collectDeviceInfo(mContext);
    // 保存日志文件
 		saveCrashInfo2File(ex);
//      new Thread() {
//          @Override
//          public void run() {
//              // Toast 显示需要出现在一个线程的消息队列中
//              Looper.prepare();
//              PromptDialog dialog=new PromptDialog(mContext);
//              dialog.loading(errorMassge);
////              Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", 0).show();
//              Looper.loop();
//          }
//      }.start();

      return true;  
  }  
  
  /**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		Log.e("保存错误信息到文件中", "***************");
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				Log.e("123", "exception=" + sb.toString());
				errorMassge=sb.toString();
				// 发送到邮件
				sendMail(sb.toString());
				Log.e("123", sb.toString());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
		}
		return null;
	}

	
	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		Log.e("收集资料！", "***************");
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}
	

  private void sendMail(final String string) {
		new Thread(){
			public void run() {
				Log.e("发送邮件！", "***************");
//				MailUtils mail = new MailUtils();
//				mail.sendEmail("303438914@qq.com", 
//						"app崩溃日志（"+Utils.TimeUtils(System.currentTimeMillis())+"）", string);
//				 
//				mail.sendEmail("462495133@qq.com", 
//						"app崩溃日志（"+Utils.TimeUtils(System.currentTimeMillis())+"）", string);
//				
//				mail.sendEmail("630862108@qq.com",
//						"app崩溃日志（"+Utils.TimeUtils(System.currentTimeMillis())+"）", string);
//				
			};
		}.start();
	}
	  
    
}
