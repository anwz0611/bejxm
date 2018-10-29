package com.zyinfo.brj.utils;

import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 访问WebService的工具类,
 * 
 * @see http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 * 
 */
public class WebServiceUtils {

	// 含有3个线程的线程池
	private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
	// 命名空间
	private static final String NAMESPACE = "MobileApps";

	/**
	 * 
	 * @param url
	 *            WebService服务器地址
	 * @param methodName
	 *            WebService的调用方法名
	 * @param properties
	 *            WebService的参数
	 * @param webServiceCallBack
	 *            回调接口
	 */
	public static void callWebService(String url, final String methodName, HashMap<String, Object> properties,
                                      final WebServiceCallBack webServiceCallBack)  {

		// 创建HttpTransportSE对象，传递WebService服务器地址
		final HttpTransportSE httpTransportSE = new HttpTransportSE(url, 8 * 1000);
		// 创建SoapObject对象
		SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

		// SoapObject添加参数
		if (properties != null) {
			for (Iterator<Map.Entry<String, Object>> it = properties.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Object> entry = it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
		}

		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		// 设置是否调用的是.Net开发的WebService
		soapEnvelope.setOutputSoapObject(soapObject);
		soapEnvelope.dotNet = true;
		httpTransportSE.debug = true;

		// 用于子线程与主线程通信的Handler
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 将返回值回调到callBack的参数中
				webServiceCallBack.callBack((String) msg.obj);
			}

		};

		// 开启线程去访问WebService
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				SoapObject resultSoapObject = null;
				String result = null;
				try {
					httpTransportSE.call(NAMESPACE +"/"+ methodName, soapEnvelope);
					if (soapEnvelope.getResponse() != null) {
						// 获取服务器响应返回的SoapObject
						resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
						result = resultSoapObject.getProperty(0).toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 将获取的消息利用Handler发送到主线程
					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface WebServiceCallBack {
		public void callBack(String result);
	}
}
