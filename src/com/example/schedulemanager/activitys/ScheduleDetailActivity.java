package com.example.schedulemanager.activitys;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.schedulemanager.models.Schedule;
import com.example.schedulemanager.models.callbacks.ScheduleCallback;
import com.example.schedulemanager.services.Alarm;
import com.example.schedulemanager.services.Alarms;
import com.example.schedulemanager.utils.Properties;
import com.example.schedulemanager.utils.ScheduleDAO;
import com.example.schedulemanager.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
/**
 * 日程详情页
 * @author smnan
 * 2015.4.30
 */
public class ScheduleDetailActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "ScheduleDetailActivity";
	private static final int BEGINSET = 0;
	private static final int ENDSET = 1;
	private static final int TIPSET = 2;
	
	private boolean isEditing = false; //标记是否处于编辑状态
	
	private Context context;
	private CustomApplication application;
	
	private Button btnBack = null;
	private TextView tvTitle = null;
	private Button btnEdit = null;
	private Button btnDel = null;
	
	private EditText etContent = null;
	private Button btnBegin = null;
	private Button btnEnd = null;
	private Button btnTips = null;
	private CheckBox cbTips = null;
	private Button btnMark = null;
	
	private Calendar calendar = null;
	private Map<String, Object> dateMap = null;
	private Map<String, Object> timeMap = null;
	
	private ScheduleDAO dao = null;
	private Schedule dataModel = null;
	private int scheduleID = -1;
	
	private int tmpYear = 0;
	private int tmpMonth = 0;
	private int tmpDay = 0;
	private int tmpHour = 0;
	private int tmpMinute = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_detail);
		
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		
		scheduleID = getIntent().getIntExtra("scheduleID", 0);
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("我的日程");
		btnEdit = (Button) this.findViewById(R.id.btnOther);
		btnEdit.setText("编辑");
		btnEdit.setVisibility(View.VISIBLE);
		btnDel = (Button) this.findViewById(R.id.btnAnother);
		btnDel.setText("删除");
		btnDel.setVisibility(View.VISIBLE);
		
		etContent = (EditText) this.findViewById(R.id.etDetailContent);
		etContent.setEnabled(false);
		etContent.setText("日程内容");
		btnBegin = (Button) this.findViewById(R.id.btnDetailBegin);
		btnBegin.setEnabled(false);
		btnEnd = (Button) this.findViewById(R.id.btnDetailEnd);
		btnEnd.setEnabled(false);
		btnTips = (Button) this.findViewById(R.id.btnDetailTips);
		btnTips.setEnabled(false);
		cbTips = (CheckBox) this.findViewById(R.id.cbDetailTips);
		cbTips.setEnabled(false);
		btnMark = (Button) this.findViewById(R.id.btnMark);
		btnMark.setPressed(false);
		btnMark.setEnabled(false);
	}

	private void initData() {
		calendar = Calendar.getInstance();
		
		dao = new ScheduleDAO(this);
		dataModel = new Schedule();
		dataModel = dao.getScheduleByID(scheduleID);
		etContent.setText(dataModel.getContents());
		btnBegin.setText("开始时间\t" + dataModel.getBeginTime());
		btnEnd.setText("结束时间\t" + dataModel.getEndTime());
		btnTips.setText("开启提醒\t" + dataModel.getTipTime());
		
		if(dataModel.getIsTips() == 0) {
			cbTips.setChecked(false);
		}else
			cbTips.setChecked(true);
		
		if(dataModel.getIsDone() == 1) {
			//已完成
			btnEdit.setVisibility(View.GONE);
			btnMark.setBackgroundResource(R.drawable.richengye_hover);
			btnMark.setText("已完成");
		}else {
			//未完成
			btnEdit.setVisibility(View.VISIBLE);
			btnMark.setBackgroundResource(R.drawable.richengye_normal);
			btnMark.setText("点击标记完成√");
		}
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
		btnEdit.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		etContent.setOnClickListener(this);
		btnBegin.setOnClickListener(this);
		btnEnd.setOnClickListener(this);
		btnTips.setOnClickListener(this);
		btnMark.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack: {
				finish();
				break;
			}
			case R.id.btnOther: {
				if(!isEditing) {
					// 编辑
					etContent.setEnabled(true);
					btnBegin.setEnabled(true);
					btnEnd.setEnabled(true);
					btnTips.setEnabled(true);
					btnMark.setEnabled(true);
					cbTips.setEnabled(true);
					etContent.requestFocus();
					
					btnEdit.setText("完成");
					isEditing = true;
				}else {
					// 完成，保存新日程设置
					etContent.setEnabled(false);
					btnBegin.setEnabled(false);
					btnEnd.setEnabled(false);
					btnTips.setEnabled(false);
					btnMark.setEnabled(false);
					cbTips.setEnabled(false);
					
					updateSchedule();
					
					btnEdit.setText("编辑");
					isEditing = false;
				}
				break;
			}
			case R.id.btnAnother: {
				// 删除
				new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage("是否删除此日程？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//删除该日程，并finish()
						//服务器
						OkHttpUtils.get().url(Properties.LOCALHOST_IP + "schedule/delete?scheduleID=" + scheduleID)
								.build().execute(new StringCallback() {
									
									@Override
									public void onResponse(String arg0) {
										//本地
										dao.delete(scheduleID);
										Alarms.deleteAlarm(context, scheduleID);//删除闹钟
										finish();
									}
									
									@Override
									public void onError(Call arg0, Exception arg1) {
										
									}
							});
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				break;
			}
			case R.id.btnDetailBegin: {
				//开始时间
				dateAlert(BEGINSET);
				break;
			}
			case R.id.btnDetailEnd: {
				//结束时间
				dateAlert(ENDSET);
				break;
			}
			case R.id.btnDetailTips: {
				//提醒时间
				dateAlert(TIPSET);
				break;
			}
			case R.id.btnMark: {
				//标记完成
				//异步改写数据库该日程完成进度
				if(dataModel.getIsDone() == 0) {
					btnMark.setBackgroundResource(R.drawable.richengye_hover);
					btnMark.setText("已完成");
				}
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
			Toast.makeText(ScheduleDetailActivity.this, "时间设置有误，请重置！", Toast.LENGTH_SHORT).show();
		}
		else if(StringUtil.isEmpty(etContent.getText().toString().trim())) {
			// 检查日程内容为空
			Toast.makeText(ScheduleDetailActivity.this, "请设置日程内容！", Toast.LENGTH_SHORT).show();
			etContent.requestFocus();
		}
		else {
			// 时间设置正确，将model导入数据库
			if(cbTips.isChecked()) {
				dataModel.setIsTips(1);
			}else
				dataModel.setIsTips(0);
			
			if(btnMark.getText().toString().equals("已完成")) {
				dataModel.setIsDone(1);
			}else
				dataModel.setIsDone(0);
			
			dataModel.setContents(etContent.getText().toString().trim());
			
			dao.update(dataModel); //更新数据库
			
			if(dataModel.getIsDone() == 0 && dataModel.getIsTips() == 1) {
				int _id = dataModel.getScheduleID();
				//先删除闹钟
				Alarms.deleteAlarm(context, _id);
				//再添加新闹钟
				Alarm am = new Alarm();
				am.id = _id;
				am.enabled = true;
				am.year = tmpYear;
				am.month = tmpMonth;
				am.day = tmpDay;
				am.hour = tmpHour;
				am.minutes = tmpMinute;
				am.vibrate = true;
				am.label = dataModel.getContents();
				long time = Alarms.addAlarm(context, am);
				Log.v(TAG, "…………" + time);
			}
			
			//备份到服务器
			OkHttpUtils.post().url(Properties.LOCALHOST_IP + "schedule/backup?")
					.addParams("scheduleID", dataModel.scheduleID + "")
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
								Toast.makeText(context, "日程修改同步成功！", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
						
						@Override
						public void onError(Call callback, Exception e) {
							Toast.makeText(context, "日程本地修改成功！", Toast.LENGTH_SHORT).show();
							finish();
						}
					});
		}
	}
	/**
	 * 检测日程时间设置对错
	 * @return
	 */
	private boolean isTimeRight() {
		boolean isRight_1 = false;
		boolean isRight_2 = false;
//		boolean isRight_3 = false;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d1 = df.parse(dataModel.getBeginTime());
			Date d2 = df.parse(dataModel.getEndTime());
			Date d3 = df.parse(dataModel.getTipTime());
			long diff_1 = d2.getTime() - d1.getTime(); // 结束时间 - 开始时间
			long diff_2 = d3.getTime() - d1.getTime(); // 提醒时间 - 开始时间
//			long diff_3 = d2.getTime() - d3.getTime(); // 结束时间 - 提醒时间
			
			if(diff_1 > 0)
				isRight_1 = true;
			else
				isRight_1 = false;
			
			if(diff_2 <= 0)
				isRight_2 = true;
			else
				isRight_2 = false;
			
//			if(diff_3 >= 0)
//				isRight_3 = true;
//			else
//				isRight_3 = false;
			
		} catch (ParseException e) {
			Log.e(TAG, e.toString());
		}
		return isRight_1 && isRight_2; // && isRight_3;
	}
	/**
	 * 日期设置
	 * @param type
	 */
	private void dateAlert(final int type) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(ScheduleDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
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
			}
		}, year, month, day);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
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
		MyTimePickerDialog dialog = new MyTimePickerDialog(ScheduleDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", type);
				map.put("hour", hourOfDay);
				map.put("minute", minute);
				timeMap = new HashMap<String, Object>();
				timeMap = map;
				updateUI();
			}
		}, hour, minute, true);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
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
				
				dataModel.setBeginTime(dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day") + " "
						+ timeMap.get("hour") + ":" + timeMap.get("minute"));
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
				
				tmpYear = Integer.parseInt(dateMap.get("year").toString());
				tmpMonth = Integer.parseInt(dateMap.get("month").toString());
				tmpDay = Integer.parseInt(dateMap.get("day").toString());
				tmpHour = Integer.parseInt(timeMap.get("hour").toString());
				tmpMinute = Integer.parseInt(timeMap.get("minute").toString());
			}
		}
		else
			Toast.makeText(ScheduleDetailActivity.this, "设置失败，请重新设置时间！", 0).show();
	}
}
