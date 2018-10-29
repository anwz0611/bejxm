package com.zyinfo.brj.ui.more.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.addressBookInfoBeans;

import java.util.List;

/**
* @Description:
* @author:杨玉萍
* @date:2016年6月28日上午9:35:58
*/
public class ShowPhoneNumAdapter extends BaseAdapter {
	private Context mContext;
	private List<addressBookInfoBeans.WaterBookDetailListBean> list;

	public ShowPhoneNumAdapter(Context context, List<addressBookInfoBeans.WaterBookDetailListBean> list) {
		this.list = list;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.show_phone_num_adapter, null);
			holder.layout=(LinearLayout) convertView.findViewById(R.id.show_phone_item);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone_num = (TextView) convertView.findViewById(R.id.phone_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			// #00c0ff
		if(position % 2 ==0 ){
			holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.background));
		}else {
			holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}
			holder.name.setText(""+list.get(position).getDwlxr());
			holder.phone_num.setText(""+list.get(position).getDwlxrdh());
		return convertView;
	}

	class ViewHolder {
		private LinearLayout layout;
		
		private TextView name;//政区名称
		private TextView phone_num;//县区名称

	}
}
