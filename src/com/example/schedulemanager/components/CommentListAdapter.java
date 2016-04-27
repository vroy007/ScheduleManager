package com.example.schedulemanager.components;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.utils.StringUtil;

public class CommentListAdapter extends BaseAdapter{

	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	
	public CommentListAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_pop_comment, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvPopName);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvPopTime);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvPopContent);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		Map<String ,Object> map = list.get(position);
		holder.tvTime.setText(StringUtil.friendlyTime("2015-04-26 15:55"));
		holder.tvContent.setText(map.get("comment").toString());
		
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvTime;
		TextView tvContent;
	}
}
