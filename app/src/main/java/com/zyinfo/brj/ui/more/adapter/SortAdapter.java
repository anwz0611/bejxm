package com.zyinfo.brj.ui.more.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.entity.PhoneDialog;
import com.zyinfo.brj.ui.more.activity.EditoeSmsActivity;
import com.zyinfo.brj.ui.more.activity.PhoneListActivity;
import com.zyinfo.brj.utils.SharedUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Description:按照名称查询（通讯录)--适配器
 * 
 * @author:杨玉萍
 * @date:2016年7月5日下午5:36:05
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private  List<addressBookInfoBeans.WaterBookDetailListBean> list = null;
	private Context mContext;
	private int isshow;
	private List<addressBookInfoBeans.WaterBookDetailListBean> list1 = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	private List<String> listChoose=new ArrayList<String>();
	private addressBookInfoBeans.WaterBookDetailListBean phoneItemInfo=null;
	private List<Map<String, Boolean>> groupCheckBox = new ArrayList<Map<String, Boolean>>();
	private static final String G_CB = "G_CB";
	public SortAdapter(Context mContext, List<addressBookInfoBeans.WaterBookDetailListBean> list, int isshow
			, List<Map<String, Boolean>> groupCheckBox
//			,List<String> listChoose
			) {
		//初始化选中个数
		list1.clear();
		SharedUtil.putIntValue(mContext, "ChooseNum1", list1.size());
		this.mContext = mContext;
		this.list = list;
		this.isshow = isshow;
		this.groupCheckBox = groupCheckBox;
//		this.listChoose = listChoose;
	}

	/**
	 * @param list
	 */
	public void updateListView(List<addressBookInfoBeans.WaterBookDetailListBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final addressBookInfoBeans.WaterBookDetailListBean mContent = list.get(position);
		View viewA=null;
		if (viewA == null) {
			viewHolder = new ViewHolder();
			viewA = LayoutInflater.from(mContext).inflate(R.layout.item, null);
			viewHolder.tvTitle = (TextView) viewA.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) viewA.findViewById(R.id.catalog);
			viewHolder.Mobile = (TextView) viewA.findViewById(R.id.mobile);
			viewHolder.sex = (ImageView) viewA.findViewById(R.id.sex_rel);
			viewHolder.call = (ImageView) viewA.findViewById(R.id.call_rel);
			viewHolder.selecte = (CheckBox) viewA.findViewById(R.id.select_name);
			viewA.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewA.getTag();
		}
		viewHolder.Mobile.setText(list.get(position).getDwlxrdh());
		viewHolder.tvTitle.setText(this.list.get(position).getDwlxr());
		viewHolder.sex.setTag(list.get(position));
		viewHolder.call.setTag(list.get(position));
		if (isshow == 1) {
			viewHolder.selecte.setVisibility(View.GONE);
		} else if (isshow == 2) {
			viewHolder.selecte.setVisibility(View.VISIBLE);
		}
		Log.e("checks[pos]", groupCheckBox.get(position).get(G_CB)+"");
		Boolean iscOk = (Boolean)groupCheckBox.get(position).get(G_CB);
		viewHolder.selecte.setChecked(iscOk);
		/*
		 * 选择通讯录人员
		 */
		viewHolder.selecte.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				int num=SharedUtil.getIntValue(mContext, "ChooseNum", 0);
//				checks[position] = isChecked; 
//				Log.e("点击了谁", list.get(position).getName());
				if (isChecked) {
					if (!listChoose.contains(list.get(position).getDwlxrdh())) {
						list1.add(list.get(position));
						listChoose.add(list.get(position).getDwlxrdh());
						PhoneListActivity.getmList(0, list.get(position));
					}
					
					Log.e("集合的个数：", ""+list1.size()+isChecked);
					Log.e("true", "点击到："+list.get(position).getDwlxr());
				}else{
					if (listChoose.contains(list.get(position).getDwlxrdh())) {
						list1.remove(list.get(position));
						listChoose.remove(list.get(position).getDwlxrdh());
						PhoneListActivity.getmList(1, list.get(position));
						Log.e("false", "取消掉："+list.get(position).getDwlxr());
					}
					
					Log.e("集合的个数：", ""+list1.size()+isChecked);
					Log.e("false", "取消掉："+list.get(position).getDwlxr());
				}
				groupCheckBox.get(position).put(G_CB, isChecked);
				SharedUtil.putIntValue(mContext, "ChooseNum1", list1.size());
				Intent intent = new Intent();
				intent.setAction("Num");
				mContext.sendBroadcast(intent);
				SortAdapter.this.notifyDataSetChanged();
			}
		});
		// 发送短信
		viewHolder.sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub 
				Intent intent = new Intent();
				intent.setClass(mContext, EditoeSmsActivity.class);
				Bundle bd = new Bundle();
				bd.putString("type", "phone");
				bd.putSerializable("phoneNum", list.get(position));
				intent.putExtras(bd);
				mContext.startActivity(intent);
			}
		});

		// 打电话
		viewHolder.call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhoneDialog dialog = new PhoneDialog(mContext);
				dialog.loading(list.get(position).getDwlxrdh(), list.get(position).getOffice());
			}
		});
		return viewA;
	}

	private class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView Mobile;
		CheckBox selecte;
		private ImageView sex, call;
	}


	/*
	 * 获取选中的人员
	 */
	public List<String> getlistChoose() {
		return listChoose;
	}
//
//	public int getSectionForPosition(int position) {
//		return list.get(position).getSortLetters().charAt(0);
//	}
//
//	public int getPositionForSection(int section) {
//		for (int i = 0; i < getCount(); i++) {
//			String sortStr = list.get(i).getSortLetters();
//			char firstChar = sortStr.toUpperCase().charAt(0);
//			if (firstChar == section) {
//				return i;
//			}
//		}
//
//		return -1;
//	}

	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();

		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int i) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int i) {
		return 0;
	}
}