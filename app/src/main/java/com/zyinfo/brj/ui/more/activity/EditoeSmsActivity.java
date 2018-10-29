package com.zyinfo.brj.ui.more.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyinfo.brj.R;
import com.zyinfo.brj.api.ApiConstants;
import com.zyinfo.brj.bean.SendMessageBeans;
import com.zyinfo.brj.bean.SendSmsBean;
import com.zyinfo.brj.bean.SortModel;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.more.adapter.ShowPhoneNumAdapter;
import com.zyinfo.brj.ui.more.contract.SendMessageContract;
import com.zyinfo.brj.ui.more.model.SendMessageModel;
import com.zyinfo.brj.ui.more.presenter.SendMessagePresenter;
import com.zyinfo.brj.utils.GsonUtils;
import com.zyinfo.brj.utils.WebServiceUtils;
import com.zyinfo.common.base.BaseActivity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description:编辑短信
 *
 *
 */

public class EditoeSmsActivity  extends BaseActivity<SendMessagePresenter, SendMessageModel> implements SendMessageContract.View{
	/**
	 * 头部处理
	 */
	@Bind(R.id.textView1)
	 TextView tv_title;

	@Bind(R.id.fanhui)
	 ImageView fanhui;

	@Bind(R.id.show_phone_all_num)
	 ListView showPhoneListView;

	@Bind(R.id.image_text)
	 TextView image_text;

	@Bind(R.id.send)
	 Button send_button;

	@Bind(R.id.send_sms)
	 EditText send_editText;

	@Bind(R.id.no_date)
	 RelativeLayout no_date;

	private ShowPhoneNumAdapter adapter;
	private List<addressBookInfoBeans.WaterBookDetailListBean> list = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	private addressBookInfoBeans.WaterBookDetailListBean phone;
	private ShapeLoadingDialog dialog;
	private List<SendSmsBean> eror = new ArrayList<SendSmsBean>();

	private String message = "呜呜。。。我还不知道要发送给谁？";
	private String type;

	private Context context;
	private List<SortModel> listS=new ArrayList<SortModel>();

//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_editoe_sms);
//		ButterKnife.bind(this);
//		context = this;
//
//
//	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_editoe_sms;
	}

	@Override
	public void initPresenter() {
		mPresenter.setVM(this, mModel);
	}

	@Override
	public void initView() {
		context=mContext;
		initData();
	}


	/*
	 * view的初始
	 */
	private void initData() {
		// TODO Auto-generated method stub
		List<addressBookInfoBeans.WaterBookDetailListBean> list1 = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
		type = getIntent().getExtras().getString("type");
		if (type.equals("phone")) {
			phone = (addressBookInfoBeans.WaterBookDetailListBean) getIntent().getExtras().getSerializable("phoneNum");
			if (!phone.getDwlxrdh().isEmpty() && !phone.getDwlxrdh().equals("")) {
				list.add(phone);
			}
		} else if (type.equals("phoneList")) {
			list1 = (List<addressBookInfoBeans.WaterBookDetailListBean>) getIntent().getSerializableExtra("phoneList");
		}else if (type.equals("phoneListS")) {
			listS = (List<SortModel>) getIntent().getSerializableExtra("phoneListS");
			for (int i = 0; i < listS.size(); i++) {
				addressBookInfoBeans.WaterBookDetailListBean phoneItemInfo = new addressBookInfoBeans.WaterBookDetailListBean();
				phoneItemInfo.setDwlxrdh(listS.get(i).getMobile());
				phoneItemInfo.setDwlxr(listS.get(i).getName());
				list.add(phoneItemInfo);
			}
		}
		for (int i = 0; i < list1.size(); i++) {
			addressBookInfoBeans.WaterBookDetailListBean phoneItemInfo = new addressBookInfoBeans.WaterBookDetailListBean();
			if (!list1.get(i).getDwlxrdh().isEmpty() && !list1.get(i).getDwlxrdh().equals("")) {
				phoneItemInfo = list1.get(i);
				list.add(phoneItemInfo);
			}
		}
		tv_title.setText("短信编辑");
		dialog = new ShapeLoadingDialog(context);
		image_text.setText(message);
		fanhui.setVisibility(View.VISIBLE);
		if (list.size() == 0) {
			image_text.setText(message);
			no_date.setVisibility(View.VISIBLE);
		} else {
			image_text.setText(message);
			no_date.setVisibility(View.GONE);
		}
		adapter = new ShowPhoneNumAdapter(context, list);
		showPhoneListView.setAdapter(adapter);
	}

	@OnClick({ R.id.fanhui, R.id.send })
	 void OnClick(View view) {
		switch (view.getId()) {

		// actionBar的返回上一个页面
		case R.id.fanhui:
			// 隐藏软键盘
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					EditoeSmsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			Intent intent = new Intent();
			intent.setAction("showOld");
			EditoeSmsActivity.this.sendBroadcast(intent);
			finish();
			break;

		// 发送短信
		case R.id.send:
			String contextString = send_editText.getText().toString().intern();
			if (!contextString.isEmpty() && !contextString.equals("")) {
				// if (list.size() != 0) {
				SendSms1(contextString);
//				try {
////					dialog.loading("正在发送！");
////					SendSms(contextString);
//
//					send_editText.setText("");
//				} catch (UnsupportedEncodingException e) {
//
//					e.printStackTrace();
//				}
//				// } else {
//				// Toast.makeText(context, "你还没有选择发送人员！", 0).show();
//				// }
			} else {
				Toast.makeText(context, "你还没有编辑发送内容！", 0).show();
			}
			break;
		default:
			break;
		}
	}

	private void SendSms1(String contextString) {
		// 隐藏软键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				EditoeSmsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		String phoneNums = "";
		if (list.size()>1){
		for(int i = 0; i < list.size(); i++)
		{
			phoneNums+=list.get(i).getDwlxrdh()+",";

		}
		}else {
			phoneNums=list.get(0).getDwlxrdh();
		}
		String content=URLDecoder.decode(contextString.toString());
		mPresenter.SendMessageRequest(phoneNums,content);

	}

	/*
	 * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
	 */
	public static StringBuffer getRequestData(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode))
						.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	// 发送短信请求
	private void SendSms(String contextString) throws UnsupportedEncodingException {
		// 隐藏软键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				EditoeSmsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		// TODO Auto-generated method stub
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String phoneNums = "";
		for(int i = 0; i < list.size(); i++)
		{
			phoneNums+=list.get(i).getDwlxrdh()+",";
		}
		Log.e("phoneNums", phoneNums);
		map.put("userId", "admin");
		map.put("phoneNums", phoneNums);
		map.put("content", URLDecoder.decode(contextString.toString(), "utf-8"));
		WebServiceUtils.callWebService(ApiConstants.send_msm, "SendMsg", map, new WebServiceUtils.WebServiceCallBack() {

			@Override
			public void callBack(String result) {
				// TODO Auto-generated method stub

				dialog.dismiss();
				if(result!=null)
				{
					List<SendSmsBean> send_nums = GsonUtils.getPersons(result, SendSmsBean[].class);
					int count = 0;
					for (int i = 0; i < send_nums.size(); i++) {
						String isSus = send_nums.get(i).getStatus();
						if (isSus.equals("True")) {
							count++;
						} else {
							eror.add(send_nums.get(i));
						}
					}
					if (count == send_nums.size()) {
						list.clear();
						message = "我已经全部发送成功！";
						adapter.notifyDataSetChanged();
						Toast.makeText(EditoeSmsActivity.this, "发送成功！", 0).show();
						Intent intent = new Intent();
						intent.setAction("updata");
						EditoeSmsActivity.this.sendBroadcast(intent);
					} else {
						// 没有发送成功
						showErrorSms();
					}
					if (list.size() == 0) {
						image_text.setText(message);
						no_date.setVisibility(View.VISIBLE);
					} else {
						image_text.setText(message);
						no_date.setVisibility(View.GONE);
					}
				}
			
			}

		});
	}

	// 显示没有发送的人员
	private void showErrorSms() {
		// TODO Auto-generated method stub
		String erroeSms = "";
		for (int i = 0; i < eror.size(); i++) {
			erroeSms = eror.get(i).getPhoneNum() + ",";
		}
		Toast.makeText(EditoeSmsActivity.this, erroeSms + "没有发送成功！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showLoading(String title) {
		dialog.loading("正在发送！");
	}

	@Override
	public void stopLoading() {
		dialog.dismiss();
	}

	@Override
	public void showErrorTip(String msg) {

	}

	@Override
	public void returnSendMessage(SendMessageBeans sendMessageBeans) {
	if (sendMessageBeans.isSMSstate()){
		Toast.makeText(EditoeSmsActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
		finish();
	}else {
		Toast.makeText(EditoeSmsActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
	}
	}
}
