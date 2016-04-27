package com.example.schedulemanager.activitys;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.components.ScheduleListAdapter;
import com.example.schedulemanager.models.ScheduleModel;
import com.example.schedulemanager.utils.AlertDialogUtil;
import com.example.schedulemanager.utils.ScheduleDAO;
/**
 * 日程列表
 * @author smnan
 * 2015.4.23
 */
public class ScheduleListActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "ScheduleListActivity";
	private Context context;
	private CustomApplication application;
	
	private Button btnBack = null;
	private TextView tvTitle = null;
	private ListView rlv = null;
	
	private ScheduleListAdapter mAdapter = null;
	
	private ScheduleDAO dao = null;
	private ScheduleModel model = null;
	private List<ScheduleModel> dataList = new ArrayList<ScheduleModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_list);
		
		context = getApplicationContext();
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("日程列表");
		tvTitle.setVisibility(View.VISIBLE);
		rlv = (ListView) this.findViewById(R.id.rlvAllSchedule);
	}

	private void initData() {
		dao = new ScheduleDAO(this);
		dataList = dao.getAllSchedule();
		if(dataList != null) {
			mAdapter = new ScheduleListAdapter(ScheduleListActivity.this, context, dataList);
			rlv.setAdapter(mAdapter);
		}
		else {
			AlertDialogUtil.showDialog(this, "温馨提示", "暂无日程");
			rlv.setAdapter(null);
		}
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == btnBack) {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initData();
	}
	
}
