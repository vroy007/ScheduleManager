package com.example.schedulemanager.components;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.ScheduleDetailActivity;
import com.example.schedulemanager.activitys.ScheduleListActivity;
import com.example.schedulemanager.models.ScheduleModel;

/**
 * 日程列表适配器
 * @author smnan
 * 2015.4.24
 */
public class ScheduleListAdapter extends BaseAdapter{

	private Context context;
	private Activity activity;
	private List<ScheduleModel> list;
	private LayoutInflater mInflater;
	private ViewHolder holder = null;
	
	public ScheduleListAdapter(Activity activity, Context context, List<ScheduleModel> list) {
		this.activity = activity;
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
			convertView = mInflater.inflate(R.layout.item_schedule_list, null);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvListDate);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvListContent);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvListTime);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		ScheduleModel model = list.get(position);
		int begin_line = model.getBeginTime().indexOf(" ");
		holder.tvDate.setText(model.getBeginTime().substring(0, begin_line));
		
		holder.tvContent.setText(model.getContents());
		
		holder.tvTime.setText(model.getBeginTime().substring(begin_line));
		
		convertView.setOnClickListener(new ItemClickListener(model, position));
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tvDate;
		TextView tvContent;
		TextView tvTime;
	}
	
	private class ItemClickListener implements OnClickListener {

		private ScheduleModel model = null;
		private int position = -1;
		
		public ItemClickListener(ScheduleModel model, int position) {
			super();
			this.model = model;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 点击某项日程，跳转到日程详情页
			Intent intent = new Intent(context, ScheduleDetailActivity.class);
			intent.putExtra("scheduleID", model.getScheduleID());
			activity.startActivity(intent);
		}
		
	}

}
