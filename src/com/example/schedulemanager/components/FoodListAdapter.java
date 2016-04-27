package com.example.schedulemanager.components;

import java.util.List;
import java.util.Map;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.ScheduleDetailActivity;
import com.example.schedulemanager.models.ScheduleModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
/**
 * 美食日程列表adapter
 * @author smnan
 * 2015.4.25
 */
public class FoodListAdapter extends BaseAdapter{

	private Context context;
	private Activity activity;
	private List<ScheduleModel> list;
	private LayoutInflater mInflater;
	private ViewHolder holder = null;
	
	public FoodListAdapter(Activity activity, Context context, List<ScheduleModel> list) {
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
			convertView = mInflater.inflate(R.layout.item_groups_food, null);
			holder.btnDetail = (Button) convertView.findViewById(R.id.btnFoodDetail);
			holder.btnComment = (Button) convertView.findViewById(R.id.btnComment);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		//设置图片
		if(position % 2 == 0) {
			Drawable dw = context.getResources().getDrawable(R.drawable.qunzuye_04);
			dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
			holder.btnDetail.setCompoundDrawables(dw, null, null, null);
		}
		else {
			Drawable dw = context.getResources().getDrawable(R.drawable.qunzuye_05);
			dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
			holder.btnDetail.setCompoundDrawables(dw, null, null, null);
		}
		
		ScheduleModel model = list.get(position);
		int index = model.getBeginTime().indexOf(" ");
		holder.btnDetail.setText(model.getBeginTime().substring(0, index) + "\t\t" + model.getContents() + "\n"
								+ model.getBeginTime() + " -- " + model.getEndTime());
		
		holder.btnDetail.setOnClickListener(new ItemClickListener(position, model));
		holder.btnComment.setOnClickListener(new CommentClickListener(position));
		
		return convertView;
	}

	class ViewHolder {
		Button btnDetail;
		Button btnComment;
	}
	/**
	 * 跳转到详情页
	 * 监听器
	 */
	private class ItemClickListener implements OnClickListener {

		private int position = -1;
		private ScheduleModel model = null;
		
		public ItemClickListener(int position, ScheduleModel model) {
			super();
			this.position = position;
			this.model = model;
		}

		@Override
		public void onClick(View v) {
			// 点击某项日程，跳转到日程详情页
			Intent intent = new Intent(context, ScheduleDetailActivity.class);
			intent.putExtra("scheduleID", model.getScheduleID());
			activity.startActivity(intent);
		}
	}
	/**
	 * 评论页
	 * 监听器
	 */
	private class CommentClickListener implements OnClickListener {
		
		private int position = -1;
		
		public CommentClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			CommentView commentWindow = new CommentView(context);
			commentWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		}
		
	}
}
