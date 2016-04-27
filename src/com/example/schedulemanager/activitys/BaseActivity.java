package com.example.schedulemanager.activitys;

import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.example.schedulemanager.utils.DeviceUtil;

/**
 * Activity基类
 * 
 */
public class BaseActivity extends Activity implements Serializable{
	//逄1�7出程序标礄1�7
	public boolean isExitApp = false;
	//注销程序标示
	public boolean isLogout = false;
	//注销程序标示
	public Class<?> loginActivityClass = null;
	
	protected int pageIndex = 0;
	protected int PAGE_SIZE = 10;
	
	protected int RELOAD = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
//		LogUtil.e("System.out", "Activity Name:" + getClass().getName());
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		pageIndex = 0;
	}

	@Override
	protected void onStop() {
		super.onStop();
		//BitmapManage.isRemoveLast = false;
	}
	
	protected void onPause() {
		super.onPause();
		//BitmapManage.isRemoveLast = false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	public void finishOther() {
	}
		
	public void finishAll() {
	}
	
	public void execTask() {
		
	}
	
	protected void onStartIntent(Class<Activity> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isExitApp) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				exitAppAlert();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void exitAppAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("温馨提示");
		if (isLogout) {
			builder.setMessage("确定要注锄1�7吗？");
		}
		
		if (isExitApp) {
			builder.setMessage("确定要�1�7�1�7出吗＄1�7");
		}
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (isExitApp) {
					closeApp();
				}

				if (isLogout && loginActivityClass != null) {
					backLogin(loginActivityClass);
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void closeApp() {
		finish();
		Process.killProcess(Process.myPid());
	}
	
	private void backLogin(Class<?> loginClass) {
		SharedPreferences sp = getSharedPreferences("Phnix", MODE_PRIVATE);
		sp.edit().putBoolean("isLogout", true).commit();
		finishOther();
		Intent intent = new Intent(this, loginClass);
		startActivity(intent);
		finish();
	}
	
	protected void backHome(Class<?> homeClass) {
	}
	
	protected void showToastShort(String text){
		Toast.makeText(this, text,Toast.LENGTH_SHORT).show();
	}
	protected void showToastLong(String text){
		Toast.makeText(this, text,Toast.LENGTH_LONG).show();
	}
	
	protected String getCallString(int pageNo, int pageSize) {
//		"&call_info={drive:phone,platform:Android,sys_version:2.3.3,app_version:1.0}&call_common={page_no:1, page_size:20, order_by:[]}";
		StringBuffer sb = new StringBuffer("&call_info={\"drive\":\"" + DeviceUtil.deviceStyle(this) + "\"");
		sb.append(",\"os_type\":\"Android\"");
		sb.append(",\"os_version\":\"" + DeviceUtil.getSystemVesionCode(this) + "\"");
		try {
			sb.append(",\"app_version\":\"" + DeviceUtil.getAppVesionCode(this) + "\"");
		} catch (NameNotFoundException e) {
			Log.e("BaseActivity", e.getMessage());
		}
		sb.append("}");
		sb.append("&call_common={\"page_no\":\"" + pageNo + "\",\"page_size\":\"" + pageSize + "\"}");
//		sb.append(",\"order_by\":[{\"name\":\"\",\"seq\":\"asc\"},{\"name\":\"\",\"seq\":\"desc\"}]");
		
		return sb.toString();
	}
}
