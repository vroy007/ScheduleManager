package com.example.schedulemanager.components;

import java.util.List;
import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.ScheduleDetailActivity;
import com.example.schedulemanager.models.Schedule;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ScheduleTodayAdapter extends BaseAdapter{

	private Context context;
	private Activity activity;
	private List<Schedule> list;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	
	public ScheduleTodayAdapter(Context context, Activity activity, List<Schedule> list) {
		super();
		this.context = context;
		this.activity = activity;
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
			convertView = mInflater.inflate(R.layout.item_schedule_today, null);
			holder.btnSchedule = (Button) convertView.findViewById(R.id.btnSchedule);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		Schedule model = list.get(position);
		
		String contents = model.getContents();
		String beginTime = model.getBeginTime();
		String endTime = model.getEndTime();

		holder.btnSchedule.setText("待办事项： " + contents + "\n" 
									+ "开始时间： " + beginTime + "\n" 
									+ "结束时间： " + endTime);
		
		holder.btnSchedule.setOnClickListener(new ItemClickListener(model, position));
		
		return convertView;
	}

	class ViewHolder {
		Button btnSchedule;
	}
	
	private class ItemClickListener implements OnClickListener {

		private Schedule model = null;
		private int position = -1;
		
		public ItemClickListener(Schedule model, int position) {
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
