package com.example.schedulemanager.activitys;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemanager.R;
import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.utils.StringUtil;
/**
 * 常用设置
 * @author smnan
 * 2015.4.26
 */
public class SettingsActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "SettingsActivity";
	private static final String FILENAME = "SCHEDULEMANAGER";
	private Context context;
	private CustomApplication application;
	
	private Button btnBack = null;
	private TextView tvTitle = null;
	
	private Button btnSetTime = null;
	private Button btnFeedBack = null;
	private Button btnClear = null;
	
	private Calendar calendar = null;
	private Map<String ,Object> map = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("设置");
		btnSetTime = (Button) this.findViewById(R.id.btnTimeSettings);
		btnFeedBack = (Button) this.findViewById(R.id.btnFeedBack);
		btnClear = (Button) this.findViewById(R.id.btnClearCache);
	}
	
	private void initData() {
		calendar = Calendar.getInstance();
		//从sharepreferenced获取默认提醒时间，并设置
		if(!StringUtil.isEmpty(application.getSettingTime())) {
			btnSetTime.setText("日程提醒时间\t\t\t" + application.getSettingTime());
		}
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
		btnSetTime.setOnClickListener(this);
		btnFeedBack.setOnClickListener(this);
		btnClear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == btnBack)
			finish();
		else if(v == btnSetTime) {
			//设置默认提醒时间
			timeSetting();
		}
		else if(v == btnFeedBack) {
			Intent intent = new Intent(SettingsActivity.this, FeedBackActivity.class);
			startActivity(intent);
		}
		else if(v == btnClear) {
			new AlertDialog.Builder(this).setTitle("温馨提示")
			.setMessage("是否清除所有数据？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//确定清除缓存
					new execClearTask().execute("");
					dialog.dismiss();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		}
	}
	/**
	 * 设置日程默认提醒时间
	 */
	private void timeSetting() {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		TimePickerDialog dialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				map = new HashMap<String, Object>();
				map.put("hour", hourOfDay);
				map.put("minute", minute);
				updateUI();
			}
		}, hour, minute, true);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		dialog.show();
	}
	/**
	 * 设置后立即刷新UI并更新配置文件
	 */
	private void updateUI() {
		btnSetTime.setText("日程提醒时间\t\t\t" + map.get("hour") + ":" + map.get("minute"));
		SharedPreferences share = getSharedPreferences(FILENAME, 0);
		SharedPreferences.Editor edit = share.edit();
		edit.putString("settingTime", map.get("hour") + ":" + map.get("minute"));
		edit.commit();
		application.setSettingTime(map.get("hour") + ":" + map.get("minute"));
	}
	/**
	 * 清除所有数据
	 */
	private void allClear() {
		//先清除配置文件，恢复出厂设置
		SharedPreferences share = getSharedPreferences(FILENAME, 0);
		SharedPreferences.Editor edit = share.edit();
		edit.putString("settingTime", "09:00");
		edit.commit();
		application.setSettingTime("09:00");
	}
	/**
	 * 异步清除缓存
	 */
	private class execClearTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			allClear();
			cleanDatabases(context);
			cleanInternalCache(context);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(SettingsActivity.this, "缓存已全部清除", 0).show();
			btnSetTime.setText("日程提醒时间\t\t\t" + application.getSettingTime());
			application.setClear(true);
			super.onPostExecute(result);
		}
	}
	/** 
	 * 
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * 
	 * @param directory 
	 */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

}
