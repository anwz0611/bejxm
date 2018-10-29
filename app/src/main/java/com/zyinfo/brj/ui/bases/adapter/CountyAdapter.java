package com.zyinfo.brj.ui.bases.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.waterChannelBeans;


import java.util.List;

/**
 * @Description:县区统计列表——适配器
 * @author:杨玉萍
 * @date:2016年6月28日上午9:35:58
 */
public class CountyAdapter extends BaseAdapter {
	private Context mContext;
	private List<waterChannelBeans> list;

	public CountyAdapter(Context context, List<waterChannelBeans> list) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.county_item, null);
			holder.county_name = (TextView) convertView.findViewById(R.id.county_name);
			holder.county_num = (TextView) convertView.findViewById(R.id.county_num);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.county_name.setText("" + list.get(position).getName());
		holder.county_num.setText("" + "(" + list.size() + ")");
		return convertView;
	}

	class ViewHolder {
		private TextView county_name;//政区名称
		private TextView county_num;//个数

	}

}
