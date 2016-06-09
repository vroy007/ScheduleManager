package com.example.schedulemanager.activitys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import okhttp3.Call;

import com.example.schedulemanager.R;
import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.components.MyTimePickerDialog;
import com.example.schedulemanager.models.ScheduleDateTag;
import com.example.schedulemanager.models.callbacks.ScheduleCallback;
import com.example.schedulemanager.models.Schedule;
import com.example.schedulemanager.services.Alarm;
import com.example.schedulemanager.services.Alarms;
import com.example.schedulemanager.utils.Properties;
import com.example.schedulemanager.utils.ScheduleDAO;
import com.example.schedulemanager.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
/**
 * 添加新日程
 * @author smnan
 * 2015.4.24
 */
public class AddScheduleActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "AddScheduleActivity";
	private static final int BEGINSET = 0;
	private static final int ENDSET = 1;
	private static final int TIPSET = 2;
	
	private Context context;
	private CustomApplication application;
	
	private Button btnBack = null;
	private TextView tvTitle = null;
	private Button btnDone = null;
	
	private EditText etContent = null;
	private Button btnBegin = null;
	private Button btnEnd = null;
	private Button btnTips = null;
	private CheckBox cbTips = null;
	
	private Calendar calendar = null;
	private Map<String, Object> dateMap = null;
	private Map<String, Object> timeMap = null;
	
	private Schedule dataModel = null;
	private ScheduleDAO dao = null;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	
	private String tempYear = null;
	private String tempMonth = null;
	private String tempDay = null;
	private String tipsTime = null;
	
	private String amYear = null;
	private String amMonth = null;
	private String amDay = null;
	private String amHour = null;
	private String amMinute = null;
	
	private int isGroup = 0; //区分是否为群组日历
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_schedule);
		
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		//添加群组日历
		Intent intent_ = getIntent();
		if(intent_ != null){
			if(intent_.getIntExtra("isGroup", 0) == 1)
				isGroup = 1;
			else
				isGroup = 0;
		}
		Log.v(TAG, "isGroup:::" + isGroup);
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("添加新日程");
		btnDone = (Button) this.findViewById(R.id.btnAnother);
		btnDone.setText("完成");
		btnDone.setVisibility(View.VISIBLE);
		
		etContent = (EditText) this.findViewById(R.id.etAddContent);
//		etContent.setCursorVisible(false);
		btnBegin = (Button) this.findViewById(R.id.btnAddBegin);
		btnEnd = (Button) this.findViewById(R.id.btnAddEnd);
		btnTips = (Button) this.findViewById(R.id.btnAddTips);
		cbTips = (CheckBox) this.findViewById(R.id.cbTips);
	}

	private void initData() {
		dao = new ScheduleDAO(this);
		calendar = Calendar.getInstance();
		dataModel = new Schedule();
		tipsTime = application.getSettingTime();
		int index = tipsTime.indexOf(":");
		amHour = tipsTime.substring(0, index);
		amMinute = tipsTime.substring(index);
//		btnTips.setText("开启提醒\t" + tipsTime);
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		btnBegin.setOnClickListener(this);
		btnEnd.setOnClickListener(this);
		btnTips.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack: {
				finish();
				break;
			}
			case R.id.btnAnother: {
				//完成添加
				application.setClear(false);
				updateSchedule();
				break;
			}
			case R.id.btnAddBegin: {
				//设置开始时间
				dateAlert(BEGINSET);
				break;
			}
			case R.id.btnAddEnd: {
				//设置结束时间
				dateAlert(ENDSET);
				break;
			}
			case R.id.btnAddTips: {
				//设置提醒时间
				dateAlert(TIPSET);
//				btnDone.setVisibility(View.VISIBLE);
				break;
			}
		}
	}
	/**
	 * 核对并添加日程到数据库
	 */
	private void updateSchedule() {
		if(!isTimeRight()) {
			// 时间设置出错
			Toast.makeText(AddScheduleActivity.this, "时间设置有误，请重置！", Toast.LENGTH_SHORT).show();
		}
		else if(StringUtil.isEmpty(etContent.getText().toString().trim())) {
			// 检查日程内容为空
			Toast.makeText(AddScheduleActivity.this, "请设置日程内容！", Toast.LENGTH_SHORT).show();
			etContent.requestFocus();
		}
		else {
			// 时间设置正确，将model导入数据库
			
			dataModel.setIsGroup(isGroup);  //设置是否为群组日历
			
			dataModel.setIsDone(0); // 初始化为“未完成”
			dataModel.setContents(etContent.getText().toString().trim());
			
			if(cbTips.isChecked()) {
				dataModel.setIsTips(1);
			}else
				dataModel.setIsTips(0);
			
			int scheduleID = dao.save(dataModel);
			String[] scheduleIDs = new String[]{String.valueOf(scheduleID)};
			
			//设置日程标记日期(将所有日程标记日期封装到list中)
			setScheduleDateTag(tempYear, tempMonth, tempDay, scheduleID);
			
			//添加闹钟
			if(cbTips.isChecked()) {
				addAlarm(scheduleID);
			}
			
			//备份到服务器
			OkHttpUtils.post().url(Properties.LOCALHOST_IP + "schedule/backup?")
					.addParams("scheduleID", scheduleID + "")
					.addParams("isTips", dataModel.isTips + "")
					.addParams("contents", dataModel.contents)
					.addParams("beginTime", dataModel.beginTime)
					.addParams("endTime", dataModel.endTime)
					.addParams("tipTime", dataModel.tipTime)
					.addParams("isDone", dataModel.isDone + "")
					.addParams("isGroup", dataModel.isGroup + "")
					.addParams("userId", application.getUser().id + "")
					.build().execute(new ScheduleCallback() {
						
						@Override
						public void onResponse(Schedule model) {
							if(model != null) {
								Toast.makeText(AddScheduleActivity.this, "日程同步成功！", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
						
						@Override
						public void onError(Call callback, Exception e) {
							Toast.makeText(AddScheduleActivity.this, "日程本地添加成功！", Toast.LENGTH_SHORT).show();
							finish();
						}
					});
			
		}
	}
	/**
	 * 添加闹钟
	 * @param scheduleID
	 */
	private void addAlarm(int scheduleID) {
		Alarm am = new Alarm();
		am.id = scheduleID;
		am.enabled = true;
		am.hour = Integer.parseInt(amHour);
		am.minutes = Integer.parseInt(amMinute);
		am.day = Integer.parseInt(amDay);
		am.month = Integer.parseInt(amMonth);
		am.year = Integer.parseInt(amYear);
		am.vibrate = true;
		am.label = dataModel.getContents();
		
		long time = Alarms.addAlarm(this, am);
		
//		Intent intent = new Intent(AddScheduleActivity.this, AlarmReceiver.class);
//		PendingIntent pIntent = PendingIntent.getBroadcast(AddScheduleActivity.this, scheduleID, intent, 0);
//		AlarmManager am_ = (AlarmManager) getSystemService(ALARM_SERVICE);
//		
//		Calendar cal = Calendar.getInstance();
//		cal.set(Integer.parseInt(amYear), Integer.parseInt(amMonth)-1, Integer.parseInt(amDay),
//				Integer.parseInt(amHour), Integer.parseInt(amMinute));
		
//		am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 120*1000, pIntent); //间隔两分钟的闹钟
//		am_.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
		Log.v(TAG, amYear + amMonth + amDay + amHour + amMinute + "…………" + time);
//		Log.e(TAG, "本来：" + cal.getTimeInMillis());
	}

	/**
	 * 设置日程标记日期
	 * @param remindID
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setScheduleDateTag(String year, String month, String day,int scheduleID){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = year+"/"+month+"/"+day;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(d));
		} catch (ParseException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		//封装要标记的日期
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(Integer.parseInt(year));
		dateTag.setMonth(Integer.parseInt(month));
		dateTag.setDay(Integer.parseInt(day));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
		//将标记日期存入数据库中
		dao.saveTagDate(dateTagList);
	}
	/**
	 * 检测日程时间设置对错
	 * @return
	 */
	private boolean isTimeRight() {
		boolean isRight_1 = false;
		boolean isRight_2 = false;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(dataModel.getBeginTime() != null && dataModel.getEndTime() != null && dataModel.getTipTime() != null) {
			try {
				Date d1 = df.parse(dataModel.getBeginTime());
				Date d2 = df.parse(dataModel.getEndTime());
				Date d3 = df.parse(dataModel.getTipTime());
				long diff_1 = d2.getTime() - d1.getTime(); // 结束时间 - 开始时间
				long diff_2 = d3.getTime() - d1.getTime(); // 提醒时间 - 开始时间
				
				if(diff_1 > 0)
					isRight_1 = true;
				else
					isRight_1 = false;
				
				if(diff_2 <= 0)
					isRight_2 = true;
				else
					isRight_2 = false;
				
			} catch (ParseException e) {
				Log.e(TAG, e.toString());
			}
			return isRight_1 && isRight_2;
		}
		else
			return false;
	}
	/**
	 * 日期设置
	 * @param type
	 */
	private void dateAlert(final int type) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", type);
				map.put("year", year);
				map.put("month", monthOfYear +1);
				map.put("day", dayOfMonth);
				dateMap = new HashMap<String, Object>();
				dateMap = map;
				timeAlert(type);
				Log.v(TAG, "datepicker");
			}
		}, year, month, day);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				return;
			}
		});
		dialog.show();
	}
	/**
	 * 时间设置
	 * @param type
	 * @param map 
	 */
	private void timeAlert(final int type) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		MyTimePickerDialog dialog = new MyTimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", type);
				map.put("hour", hourOfDay);
				map.put("minute", minute);
				timeMap = new HashMap<String, Object>();
				timeMap = map;
				updateUI();
				Log.v(TAG, "timepicker");
			}
		}, hour, minute, true);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				return;
			}
		});
		dialog.show();
	}
	/**
	 * 设置完刷新UI
	 */
	private void updateUI() {
		if(dateMap != null && timeMap != null) {
			if(dateMap.get("type") == Integer.valueOf(BEGINSET) && timeMap.get("type") == Integer.valueOf(BEGINSET)) {
				btnBegin.setText("开始时间\t" + dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + "\t\t"
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));
				
				tempYear = dateMap.get("year").toString();
				tempMonth = dateMap.get("month").toString();
				tempDay = dateMap.get("day").toString();
				
				dataModel.setBeginTime(dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + " "
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));
				
				btnTips.setText("开启提醒\t" + dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + "\t\t" + tipsTime);
				dataModel.setTipTime(dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + " "
						+ tipsTime);
				amYear = dateMap.get("year").toString();
				amMonth = dateMap.get("month").toString();
				amDay = dateMap.get("day").toString();
				
			}
			else if(dateMap.get("type") == Integer.valueOf(ENDSET) && timeMap.get("type") == Integer.valueOf(ENDSET)) {
				btnEnd.setText("结束时间\t" + dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + "\t\t"
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));

				dataModel.setEndTime(dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + " "
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));
			}
			else if(dateMap.get("type") == Integer.valueOf(TIPSET) && timeMap.get("type") == Integer.valueOf(TIPSET)) {
				btnTips.setText("开启提醒\t" + dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + "\t\t"
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));

				dataModel.setTipTime(dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + " "
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));
				amYear = dateMap.get("year").toString();
				amMonth = dateMap.get("month").toString();
				amDay = dateMap.get("day").toString();
				amHour = timeMap.get("hour").toString();
				amMinute = timeMap.get("minute").toString();
			}
		}
		else
			Toast.makeText(AddScheduleActivity.this, "设置失败，请重新设置时间！", 0).show();
	}
}
