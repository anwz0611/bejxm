package com.zyinfo.brj.ui.more.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.entity.PhoneDialog;
import com.zyinfo.brj.ui.more.activity.EditoeSmsActivity;
import com.zyinfo.brj.ui.more.activity.PhoneListActivity;
import com.zyinfo.brj.utils.SharedUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:危险区列表——适配器
 * @author:杨玉萍
 * @date:2016年8月3日上午9:35:58
 */
public class PhoneAdapter extends BaseExpandableListAdapter {

	private Context context;
	private int isshow;
	public static String ChooseNum="0";
	private List<addressBookInfoBeans> list;
	private List<String> checkedChildrenString = new ArrayList<String>();
	// 以选中的子列表项
	private List<addressBookInfoBeans.WaterBookDetailListBean> checkedChildren = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();
	// 父列表项的选中状态：value值为1（选中）、2（部分选中）、3（未选中）
	private Map<String, Boolean> groupCheckedStateMap = new HashMap<String, Boolean>();
	private List<Map<String, Boolean>> groupCheckBox = new ArrayList<Map<String, Boolean>>();
	private List<List<Map<String, Boolean>>> childCheckBox = new ArrayList<List<Map<String, Boolean>>>();
	private static final String G_CB = "G_CB";
	private static final String C_CB = "C_CB";

	public PhoneAdapter(Context context, List<addressBookInfoBeans> list, int isshow,
                        List<List<Map<String, Boolean>>> childCheckBox, List<Map<String, Boolean>> groupCheckBox,
                        List<String> checkedChildrenString, List<addressBookInfoBeans.WaterBookDetailListBean> checkedChildren) {
		SharedUtil.putIntValue(context, "ChooseNum", checkedChildrenString.size());
		this.context = context;
		this.list = list;
		this.isshow = isshow;
		this.checkedChildrenString = checkedChildrenString;
		this.checkedChildren = checkedChildren;
		this.groupCheckBox = groupCheckBox;
		this.childCheckBox = childCheckBox;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).getWaterBookDetailList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).getWaterBookDetailList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.phone_group, null);
		}
		TextView DeptNM = (TextView) view.findViewById(R.id.deptnm);
		TextView Number = (TextView) view.findViewById(R.id.number);
		LinearLayout ll_layout = (LinearLayout) view.findViewById(R.id.cb_layout);
		final CheckBox gCheckBox = (CheckBox) view.findViewById(R.id.select1);

		if (isshow == 1) {
			gCheckBox.setVisibility(View.GONE);
		} else if (isshow == 2) {
			gCheckBox.setVisibility(View.VISIBLE);
		}
		DeptNM.setText(list.get(groupPosition).getDwmc());
		Number.setText("(" + list.get(groupPosition).getWaterBookDetailList().size() + ")");
		boolean isgOk = (Boolean) groupCheckBox.get(groupPosition).get(G_CB);
		gCheckBox.setChecked(isgOk);
		gCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean isChecked = gCheckBox.isChecked();
				groupCheckBox.get(groupPosition).put("G_CB", isChecked);
				changChildStates(groupPosition, isChecked);
				
				PhoneAdapter.this.notifyDataSetChanged();
			}
		});

		return view;
	}

	private void changChildStates(int groupPosition, Boolean isChecked) {
		for (int i = 0; i < childCheckBox.get(groupPosition).size(); i++) {
			childCheckBox.get(groupPosition).get(i).put(C_CB, isChecked);
			if (isChecked) {
				if (!checkedChildrenString.contains(list.get(groupPosition).getWaterBookDetailList().get(i).getDwlxrdh())) {
					checkedChildren.add(list.get(groupPosition).getWaterBookDetailList().get(i));
					checkedChildrenString.add(list.get(groupPosition).getWaterBookDetailList().get(i).getDwlxrdh());
					PhoneListActivity.getmList(0, list.get(groupPosition).getWaterBookDetailList().get(i));
				}
			} else {
				if (checkedChildrenString.contains(list.get(groupPosition).getWaterBookDetailList().get(i).getDwlxrdh())) {
					checkedChildren.remove(list.get(groupPosition).getWaterBookDetailList().get(i));
					checkedChildrenString.remove(list.get(groupPosition).getWaterBookDetailList().get(i).getDwlxrdh());
					PhoneListActivity.getmList(1, list.get(groupPosition).getWaterBookDetailList().get(i));
				}
			}
		}
		Log.e("1111111111111111111111", checkedChildrenString.size()+"");
		SharedUtil.putIntValue(context, "ChooseNum", checkedChildrenString.size());
		Intent intent = new Intent();
		intent.setAction("Num");
		context.sendBroadcast(intent);
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		View v = null;

		if (v == null) {

			v = LayoutInflater.from(context).inflate(R.layout.phone_child, null);
			holder = new ViewHolder();
			holder.select = (CheckBox) v.findViewById(R.id.select);

			holder.Name = (TextView) v.findViewById(R.id.name);

			holder.Mobile = (TextView) v.findViewById(R.id.mobile);

			// holder.OfficeTel = (TextView) v.findViewById(R.id.officetel);

			holder.sex = (ImageView) v.findViewById(R.id.sex_rel);

			holder.call = (ImageView) v.findViewById(R.id.call_rel);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (isshow == 1) {
			holder.select.setVisibility(View.GONE);
		} else if (isshow == 2) {
			holder.select.setVisibility(View.VISIBLE);
		}
		final String childrenId = list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxrdh();
		holder.Mobile.setText(list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxrdh());
		holder.Name.setText(list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxr());
		// holder.OfficeTel.setText(list.get(groupPosition).getResultlist().get(childPosition).getOfficeTel());

		Boolean iscOk = (Boolean) childCheckBox.get(groupPosition).get(childPosition).get(C_CB);
		holder.select.setChecked(iscOk);
		holder.select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (!checkedChildrenString.contains(childrenId)) {
						checkedChildren.add(list.get(groupPosition).getWaterBookDetailList().get(childPosition));
						checkedChildrenString
								.add(list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxrdh());
						PhoneListActivity.getmList(0, list.get(groupPosition).getWaterBookDetailList().get(childPosition));

					}

					int[] count = new int[list.size()];
					count[groupPosition] = list.get(groupPosition).getWaterBookDetailList().size();
					for (int i = 0; i < list.get(groupPosition).getWaterBookDetailList().size(); i++) {
						String mon = list.get(groupPosition).getWaterBookDetailList().get(i).getDwlxrdh();
						if (checkedChildrenString.contains(mon)) {
							count[groupPosition] = count[groupPosition] - 1;
						}
					}
					if (count[groupPosition] == 0) {
						groupCheckBox.get(groupPosition).put(G_CB, true);
						count[groupPosition] = list.get(groupPosition).getWaterBookDetailList().size();
					} else {
						groupCheckBox.get(groupPosition).put(G_CB, false);
						count[groupPosition] = list.get(groupPosition).getWaterBookDetailList().size();
					}
				} else {
					if (checkedChildrenString.contains(childrenId)) {
						checkedChildren.remove(list.get(groupPosition).getWaterBookDetailList().get(childPosition));
						checkedChildrenString
								.remove(list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxrdh());
						PhoneListActivity.getmList(1, list.get(groupPosition).getWaterBookDetailList().get(childPosition));
					}
					groupCheckBox.get(groupPosition).put(G_CB, isChecked);
				}
				
				Log.e("222222222222222222222", checkedChildrenString.size()+"");
				SharedUtil.putIntValue(context, "ChooseNum", checkedChildrenString.size());
				Intent intent = new Intent();
				intent.setAction("Num");
				context.sendBroadcast(intent);
				
				childCheckBox.get(groupPosition).get(childPosition).put(C_CB, isChecked);
				PhoneAdapter.this.notifyDataSetChanged();
			}
			
		});

		// 发送短信
		holder.sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, EditoeSmsActivity.class);
				Bundle bd = new Bundle();
				bd.putString("type", "phone");
				bd.putSerializable("phoneNum", list.get(groupPosition).getWaterBookDetailList().get(childPosition));
				intent.putExtras(bd);
				context.startActivity(intent);
				Toast.makeText(context, list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxr(), 0).show();
			}
		});

		// 打电话
		holder.call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context,
				// list.get(groupPosition).getResultlist().get(childPosition).getName(),
				// 0).show();
				PhoneDialog dialog = new PhoneDialog(context);
				dialog.loading(list.get(groupPosition).getWaterBookDetailList().get(childPosition).getDwlxrdh(),
						list.get(groupPosition).getWaterBookDetailList().get(childPosition).getOffice());
			}
		});

		return v;
	}

	
	/*
	 * 获取选中的个数
	 */
	public String getChooseNum() {
		return checkedChildrenString.size()+"";
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public List<addressBookInfoBeans.WaterBookDetailListBean> getCheckedChildren() {

		return checkedChildren;
	}

	public void setCheckedChildren(List<addressBookInfoBeans.WaterBookDetailListBean> checkedChildren) {
		this.checkedChildren = checkedChildren;
	}

	public List<String> getcheckedChildrenString() {

		return checkedChildrenString;
	}

	public void setcheckedChildrenString(List<String> checkedChildren) {
		this.checkedChildrenString = checkedChildrenString;
	}

	private class ViewHolder {
		private TextView DeptNM, Number, Mobile, OfficeTel, Name;
		private CheckBox select, select1;
		private ImageView sex, call;
		private LinearLayout ll_layout;
		private ImageView cb_box;
	}

}
