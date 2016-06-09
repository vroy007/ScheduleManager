package com.example.schedulemanager.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.models.Schedule;
import com.example.schedulemanager.utils.ScheduleDAO;

public class AlarmService extends Service{

	private static final String TAG = "AlarmService";
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Context context;
	private CustomApplication application;
	private ScheduleDAO dao = null;
	private List<Schedule> dataList = null;
	private AlarmManager am = null;
	private PendingIntent pIntent = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		initData();
	}
	/**
	 * 初始化数据库闹钟
	 */
	private void initData() {
		dao = new ScheduleDAO(context);
		dataList = new ArrayList<Schedule>();
		dataList = dao.getAllSchedule();
		for(int i = 0; i < dataList.size(); i++) {
			Schedule model = dataList.get(i);
			if(model.getIsTips() == 1 && model.getIsDone() == 0) {
				int _id = model.getScheduleID();
				String content = model.getContents();
				try {
					Date date = df.parse(model.getTipTime());
					long amTime = date.getTime() - System.currentTimeMillis();
					if(amTime > 0) {
						Intent intent_ = new Intent(context, AlarmReceiver.class);
						intent_.putExtra("content", content);
						pIntent = PendingIntent.getBroadcast(context, _id, intent_, 0);
						am.set(AlarmManager.RTC_WAKEUP, amTime, pIntent);
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
