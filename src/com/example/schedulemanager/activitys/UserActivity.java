package com.example.schedulemanager.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.groups.MyGroupsActivity;
/**
 * 个人页
 * @author smnan
 * 2015.4.23
 */
public class UserActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "UserActivity";
	private Context context;
	
	private Button btnList = null;
	private Button btnTeam = null;
	private Button btnSettings = null;
	private Button btnBack = null;
	private TextView tvVersion = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		context = getApplicationContext();
		
		initView();
		initEvent();
	}
	
	private void initView() {
		btnList = (Button) this.findViewById(R.id.btnList);
		btnTeam = (Button) this.findViewById(R.id.btnTeam);
		btnSettings = (Button) this.findViewById(R.id.btnSettings);
		tvVersion = (TextView) this.findViewById(R.id.tvVersion);
		btnBack = (Button) this.findViewById(R.id.btnUserBack);
		
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String version = info.versionName;
			tvVersion.setText(version + "版本");
			
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	private void initEvent() {
		btnList.setOnClickListener(this);
		btnTeam.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v == btnList) {
			//日程列表
			Intent intent_ = new Intent(UserActivity.this, ScheduleListActivity.class);
			startActivity(intent_);
		}
		else if(v == btnTeam) {
			//群组日历
			Intent intent_ = new Intent(UserActivity.this, MyGroupsActivity.class);
			startActivity(intent_);
		}
		else if(v == btnSettings) {
			//常用设置
			Intent intent_ = new Intent(UserActivity.this, SettingsActivity.class);
			startActivity(intent_);
		}
		else if(v == btnBack) {
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
	
}
