package com.example.schedulemanager.activitys.groups;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.AddScheduleActivity;
import com.example.schedulemanager.activitys.BaseActivity;
import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.components.FoodListAdapter;
import com.example.schedulemanager.models.Schedule;
import com.example.schedulemanager.utils.AlertDialogUtil;
import com.example.schedulemanager.utils.ScheduleDAO;
/**
 * 美食群组
 * @author smnan
 * 2015.4.25
 */
public class GroupFoodActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "GroupFoodActivity";
	private Context context;
	private CustomApplication application;
	
	private FrameLayout layout = null;
	private Button btnBack = null;
	private TextView tvTitle = null;
	private Button btnAdd = null;
	
	private ListView rlv = null;
	private List<Schedule> list = null;
	private FoodListAdapter mAdapter = null;
	
	private ScheduleDAO dao = null;
	private Schedule model = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_food);
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		layout = (FrameLayout) this.findViewById(R.id.layout_top);
		layout.setBackgroundColor(getResources().getColor(R.color.foodbac));
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("美食");
		btnAdd = (Button) this.findViewById(R.id.btnAnother);
		btnAdd.setBackgroundResource(R.drawable.addschedule_11);
		btnAdd.setVisibility(View.VISIBLE);
		
		rlv = (ListView) this.findViewById(R.id.lvFood);
	}

	private void initData() {
		dao = new ScheduleDAO(this);
		list = dao.getGroupSchedule();
		if(list != null) {
			mAdapter = new FoodListAdapter(GroupFoodActivity.this, context, list);
			rlv.setAdapter(mAdapter);
		}
		else {
			AlertDialogUtil.showDialog(this, "温馨提示", "暂无群组日程");
			rlv.setAdapter(null);
		}
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == btnBack)
			finish();
		else if(v == btnAdd) {
			Intent intent = new Intent(GroupFoodActivity.this, AddScheduleActivity.class);
			intent.putExtra("isGroup", 1);
			startActivity(intent);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initData();
	}
	
}
