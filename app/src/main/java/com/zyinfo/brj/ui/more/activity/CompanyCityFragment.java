package com.zyinfo.brj.ui.more.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.more.adapter.PhoneAdapter;
import com.zyinfo.brj.ui.more.contract.AddressBookInfoContract;
import com.zyinfo.brj.ui.more.model.AddressBookInfoModel;
import com.zyinfo.brj.ui.more.presenter.AddressBookInfoPresenter;
import com.zyinfo.common.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Description:通讯录（单位）
 *
 *测试
 *
 */

public class CompanyCityFragment extends BaseActivity<AddressBookInfoPresenter, AddressBookInfoModel> implements AddressBookInfoContract.View{

	private Context context;
	/**
	 * listview
	 */
	private ExpandableListView lv_list;

	private PhoneAdapter mAdapter;

//	private List<PhoneInfo> list = new ArrayList<PhoneInfo>();
private List<addressBookInfoBeans> list = new ArrayList<addressBookInfoBeans>();
	private ShapeLoadingDialog dialog;
	private String Loading_text;
	private RelativeLayout sendrel;
	private TextView editor_msm;
	private int show = 1;
	private boolean show_send_sms = false;
	private List<String> checkedChildrenString = new ArrayList<String>();
	private List<addressBookInfoBeans.WaterBookDetailListBean> checkedChildren = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	private List<Map<String, Boolean>> groupCheckBox = new ArrayList<Map<String, Boolean>>();
	private List<List<Map<String, Boolean>>> childCheckBox = new ArrayList<List<Map<String, Boolean>>>();
	private List<String> groupArray = new ArrayList<String>();
	private List<List<String>> childArray = new ArrayList<List<String>>();
	private static final String G_CB = "G_CB";
	private static final String C_CB = "C_CB";
	private FragmentManager manager;
	private FragmentTransaction transaction;

	// 开启广播监听是否发送成功！
	private BroadcastReceiver mBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			switch (action) {
			//短信编辑页面返回取值
			case "updata":
//				getListData(list);
//				mAdapter.getcheckedChildrenString().clear();
//				mAdapter.getCheckedChildren().clear();
//				mAdapter.notifyDataSetChanged();
				show = 1;
				getListData(list);
				checkedChildrenString.clear();
				checkedChildren.clear();
				mAdapter = new PhoneAdapter(context, list, show, childCheckBox, groupCheckBox
				// , childArray, groupArray
				, checkedChildrenString, checkedChildren);
				lv_list.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				editor_msm.setVisibility(View.INVISIBLE);
				show_send_sms = false;
				break;
			//群发短信
			case "allSend":
					// 显示编辑按钮
					show = 2;
					mAdapter = new PhoneAdapter(context, list, show, childCheckBox, groupCheckBox,
							checkedChildrenString, checkedChildren);
					lv_list.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					editor_msm.setVisibility(View.VISIBLE);
					show_send_sms = true;

				break;
			default:
				break;
			}
		}
	};

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.company_danwei);
//		context = this;
//		Loading_text = context.getString(R.string.loading_text);
//		IntentFilter mFilter = new IntentFilter();
//		mFilter.addAction("updata");//更新
//		mFilter.addAction("allSend");//群发
////		mFilter.addAction("allSendMes");//群发
////		mFilter.addAction("Numpage");//选中数量
//		CrashHandler.getInstance().init(this);
//		context.registerReceiver(mBroadcast, mFilter);
//		dialog = new ShapeLoadingDialog(context);
//		dialog.loading(Loading_text);
//		initView();
//		getData();
//	}

	@Override
	public int getLayoutId() {
		return R.layout.company_danwei;
	}

	@Override
	public void initPresenter() {
		mPresenter.setVM(this, mModel);
	}

	@Override
	public void initView() {
		context=this;
        Loading_text = context.getString(R.string.loading_text);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("updata");//更新
		mFilter.addAction("allSend");//群发
//		mFilter.addAction("allSendMes");//群发
//		mFilter.addAction("Numpage");//选中数量
//		CrashHandler.getInstance().init(this);
		context.registerReceiver(mBroadcast, mFilter);
		dialog = new ShapeLoadingDialog(context);
		dialog.loading(Loading_text);
        initViews();
        if (list.size()==0){
            mPresenter.getAddressBookInfoDataRequest();
        }
	}


//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		getData();
//		mAdapter = new PhoneAdapter1(context, list, show, childCheckBox, groupCheckBox, checkedChildrenString,
//				checkedChildren);
//		mAdapter.notifyDataSetChanged();
//		super.onResume();
//	}

	public void initViews() {
		// TODO Auto-generated method stub
		/**
		 * listview
		 */
		lv_list = (ExpandableListView) findViewById(R.id.test_city_listview);
		mAdapter = new PhoneAdapter(context, list, show, childCheckBox, groupCheckBox, checkedChildrenString,
				checkedChildren);
		lv_list.setAdapter(mAdapter);
		sendrel = (RelativeLayout) findViewById(R.id.sendrel);
		editor_msm = (TextView) findViewById(R.id.r_msm_message);
		// 编辑短信
		editor_msm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<addressBookInfoBeans.WaterBookDetailListBean> list1 = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
				// 获取adapter里面的选中数据
				list1 = mAdapter.getCheckedChildren();
				Intent intent = new Intent(context, EditoeSmsActivity.class);
				Bundle bd = new Bundle();
				bd.putString("type", "phoneList");
				bd.putSerializable("phoneList", (Serializable) list1);
				intent.putExtras(bd);
				context.startActivity(intent);
			}
		});

		lv_list.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition, int childPosition,
                                        long arg4) {
				return true;
			}
		});

		// 群发短信按钮
		sendrel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!show_send_sms) {
					// 显示编辑按钮
					show = 2;
					mAdapter = new PhoneAdapter(context, list, show, childCheckBox, groupCheckBox,
							checkedChildrenString, checkedChildren);
					lv_list.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					editor_msm.setVisibility(View.VISIBLE);
					show_send_sms = true;
				} else {
					// 隐藏编辑按钮并清空选中数据
					show = 1;
					getListData(list);
					checkedChildrenString.clear();
					checkedChildren.clear();
					mAdapter = new PhoneAdapter(context, list, show, childCheckBox, groupCheckBox
					// , childArray, groupArray
					, checkedChildrenString, checkedChildren);
					lv_list.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					editor_msm.setVisibility(View.INVISIBLE);
					show_send_sms = false;
				}
			}
		});

	}

	/*
	 * 初始化Adapter数据
	 */
	private void getListData(List<addressBookInfoBeans> list2) {
		// TODO Auto-generated method stub
		if (groupCheckBox.size() != 0) {
			groupCheckBox.clear();
			childCheckBox.clear();
		}
		for (int i = 0; i < list2.size(); i++) {
			groupArray.add(list2.get(i).getDwmc());
			Map<String, Boolean> curGB = new HashMap<String, Boolean>();
			curGB.put(G_CB, false);
			groupCheckBox.add(curGB);
			List<String> t = new ArrayList<String>();
			List<Map<String, Boolean>> childCB = new ArrayList<Map<String, Boolean>>();
			for (int j = 0; j < list2.get(i).getWaterBookDetailList().size(); j++) {
				Map<String, Boolean> curCB = new HashMap<String, Boolean>();
				curCB.put(C_CB, false);
				t.add(list2.get(i).getWaterBookDetailList().get(j).getDwdh());
				childCB.add(curCB);
			}
			childArray.add(t);
			childCheckBox.add(childCB);
		}
	}

	// 网络请求
	private void getData() {

		List<addressBookInfoBeans> list1;



//		// TODO Auto-generated method stub
//		XUtil.Get(Constant.addressBookInfo, null, new MyCallBack<String>() {
//
//			@Override
//			public void onSuccess(String xmlString) {
//				super.onSuccess(xmlString);
//				List<addressBookInfoBeans> list1;
//				list1 = GsonUtils.getGSPersons(xmlString, addressBookInfoBeans[].class);
//				list.addAll(list1);
//				getListData(list);
//				mAdapter.notifyDataSetChanged();
//				dialog.dismiss();
//			}
//
//			@Override
//			public void onError(Throwable ex, boolean isOnCallback) {
//				super.onError(ex, isOnCallback);
//				dialog.dismiss();
//			}
//		});
	}

	private void intentTo(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra("home", "home");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		return;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		context.unregisterReceiver(mBroadcast);
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void showLoading(String title) {

	}

	@Override
	public void stopLoading() {

	}

	@Override
	public void showErrorTip(String msg) {

	}

	@Override
	public void returnAddressBookInfoData(List<addressBookInfoBeans> addressBookInfoBeans) {
        list.addAll(addressBookInfoBeans);

        getListData(list);
        mAdapter.notifyDataSetChanged();
        dialog.dismiss();
	}
}
