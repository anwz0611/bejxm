package com.zyinfo.brj.ui.more.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;


import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.more.adapter.SortAdapter;
import com.zyinfo.brj.utils.CharacterParser;
import com.zyinfo.brj.utils.PinyinComparator;
import com.zyinfo.brj.view.ClearEditText;
import com.zyinfo.brj.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Description:通讯录（按照姓名查询）
 *
 *
 */

public class CompanyStationFragment extends Activity {
	private static final String TAG = "TestFragment";
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter sortAdapter;
	private ClearEditText mClearEditText;
	private Context context;
	private CharacterParser characterParser;
	private List<addressBookInfoBeans.WaterBookDetailListBean> SourceDateList = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	private PinyinComparator pinyinComparator;
	private List<addressBookInfoBeans.WaterBookDetailListBean> lists = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	private ShapeLoadingDialog sDialog;
	private String loading_text;
	private List<Map<String, Boolean>> groupCheckBox = new ArrayList<Map<String, Boolean>>();
	private static final String G_CB = "G_CB";
	private List<String> listChoose = new ArrayList<String>();
	private int show = 1;

	// 这两个List用于暂时存储联系人的名字和号码
	List<String> myContactName = new ArrayList<String>();
	List<String> myContactNumber = new ArrayList<String>();

	private static final String[] CONTACTOR_ION = new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };

	// 开启广播监听是否发送成功！
	private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			switch (action) {
			// 短信编辑页面返回取值
			case "updata":
				show = 1;
				listChoose.clear();
				sortAdapter = new SortAdapter(context, SourceDateList, show, groupCheckBox);
				sortListView.setAdapter(sortAdapter);
				sortAdapter.notifyDataSetChanged();
				break;
			case "allSend":
				show = 2;
				listChoose = sortAdapter.getlistChoose();
				sortAdapter = new SortAdapter(context, SourceDateList, show, groupCheckBox);
				sortListView.setAdapter(sortAdapter);
				sortAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_name);
		context = this;
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("updata");// 更新
		mFilter.addAction("allSend");// 群发
		mFilter.addAction("allSendMes");// 群发
		mFilter.addAction("Numpage");// 选中数量
		context.registerReceiver(mBroadcast, mFilter);

		initViews();
	}

	private void getPhoneList() {
//		 getSimPhoneList();
		Cursor phones = null;
		ContentResolver cr = getContentResolver();
		try {
			phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, CONTACTOR_ION, null, null,
					"sort_key");
			if (phones != null) {
				final int displayNameIndex = phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				final int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				String phoneString, displayNameString;
				while (phones.moveToNext()) {
					phoneString = phones.getString(phoneIndex);
					displayNameString = phones.getString(displayNameIndex);
					if (phoneString.substring(0, 1).equals("0")) {
						continue;
					}
					myContactName.add(displayNameString);
					myContactNumber.add(phoneString);
					addressBookInfoBeans.WaterBookDetailListBean sortModel = new addressBookInfoBeans.WaterBookDetailListBean();
					sortModel.setDwlxr(displayNameString);
					sortModel.setDwlxrdh(phoneString);
					lists.add(sortModel);
				}
			}
		} catch (Exception e) {
		} finally {
			if (phones != null)
				phones.close();
		}

		SourceDateList = filledData(lists);
		// 根据a-z进行排序源数据

		Collections.sort(SourceDateList, pinyinComparator);
		getListData();
		sortAdapter = new SortAdapter(context, SourceDateList, show, groupCheckBox);
		sortListView.setAdapter(sortAdapter);
		sDialog.dismiss();
	}

	/*
	 * 初始化Adapter数据
	 */
	private void getListData() {
		// TODO Auto-generated method stub
		if (groupCheckBox.size() != 0) {
			groupCheckBox.clear();
		}
		for (int i = 0; i < SourceDateList.size(); i++) {
			Map<String, Boolean> curGB = new HashMap<String, Boolean>();
			curGB.put(G_CB, false);
			groupCheckBox.add(curGB);
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		loading_text = context.getString(R.string.loading_text);
		sDialog = new ShapeLoadingDialog(context);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {

				int position = sortAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sDialog.loading(loading_text);
		getPhoneList();
	}

	/**
	 * ListView
	 *
	 */
	private List<addressBookInfoBeans.WaterBookDetailListBean> filledData(List<addressBookInfoBeans.WaterBookDetailListBean> list) {
		List<addressBookInfoBeans.WaterBookDetailListBean> mSortList = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
		for (int i = 0; i < list.size(); i++) {
			addressBookInfoBeans.WaterBookDetailListBean sortModel = new addressBookInfoBeans.WaterBookDetailListBean();
			sortModel.setDwlxr(list.get(i).getDwlxr().toString());
			sortModel.setDwlxrdh(list.get(i).getDwlxrdh().toString());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(list.get(i).getDwlxr());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<addressBookInfoBeans.WaterBookDetailListBean> filterDateList = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (addressBookInfoBeans.WaterBookDetailListBean sortModel : SourceDateList) {
				String name = sortModel.getDwlxr();// 姓名
				String phone = sortModel.getDwlxrdh();// 电话号码
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(filterStr.toString())
						|| phone.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(phone).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		sortAdapter.updateListView(filterDateList);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		context.unregisterReceiver(mBroadcast);
	}

}
