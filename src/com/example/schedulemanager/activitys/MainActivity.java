package com.example.schedulemanager.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.schedulemanager.R;
import com.example.schedulemanager.components.CalendarAdapter;
import com.example.schedulemanager.components.CustomApplication;
import com.example.schedulemanager.components.ScheduleTodayAdapter;
import com.example.schedulemanager.models.ScheduleModel;
import com.example.schedulemanager.models.WeatherModel;
import com.example.schedulemanager.utils.DeviceUtil;
import com.example.schedulemanager.utils.HttpUtils;
import com.example.schedulemanager.utils.Properties;
import com.example.schedulemanager.utils.ScheduleDAO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
/**
 * 主页
 * @author smnan
 * 2015.4.23
 */
public class MainActivity extends BaseActivity implements OnClickListener{
	
	private static final String TAG = "MainActivity";
	private static long exitTime = 0;
	private static final String FILENAME = "SCHEDULEMANAGER";
	private static final String[] MonthName = {"January","February","March","April","May","June",
											"July","August","September","October","November","December"};
	
	private Context context;
	private CustomApplication application;
	
	private Button btnPerson = null;
	private Button btnAdd = null;
	
	//日历显示控件
	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private ViewFlipper flipper = null;
	private GridView gridView = null;
	private static int jumpMonth = 0; // 每次滑动，增加或减去丄1�7个月,默认丄1�7�即显示当前月）
	private static int jumpYear = 0; // 滑动跨越丄1�7年，则增加或者减去一幄1�7,默认丄1�7即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	/** 每次添加gridview到viewflipper中时给的标记 */
	private int gvFlag = 0;
	/** 当前日期，显示在顶端*/
	private TextView tvCalDate = null;
	/** 当前的年朄1�7 */
	private TextView currentMonth;
	/** 上个朄1�7 */
	private ImageView prevMonth;
	/** 下个朄1�7 */
	private ImageView nextMonth;
	
	private ListView rlvSchedule = null;
	private ScheduleTodayAdapter tAdapter = null;
//	private Button btnSchedule = null;
	private ImageView imgWeather = null;
	private TextView tvWeather = null;
	
	private ScheduleDAO dao = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = getApplicationContext();
		application = (CustomApplication) getApplication();
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		btnPerson = (Button) this.findViewById(R.id.btnLeft);
		btnPerson.setBackgroundResource(R.drawable.gerenye_03);
		btnPerson.setVisibility(View.VISIBLE);
		btnAdd = (Button) this.findViewById(R.id.btnRight);
		btnAdd.setBackgroundResource(R.drawable.homepage_03);
		btnAdd.setVisibility(View.VISIBLE);
		
		rlvSchedule = (ListView) this.findViewById(R.id.lv_btnScheduel);
//		btnSchedule = (Button) this.findViewById(R.id.btnSchedule);
		imgWeather = (ImageView) this.findViewById(R.id.imgWeather);
		tvWeather = (TextView) this.findViewById(R.id.tvWeather);
		
		tvCalDate = (TextView) this.findViewById(R.id.tvCalDate);
		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		flipper = (ViewFlipper) this.findViewById(R.id.flipper);
		flipper.removeAllViews();
	}

	private void initData() {
		
		dao = new ScheduleDAO(this);
		
		if(DeviceUtil.isNetworkAvailable(MainActivity.this))
			new execWeatherTask().execute("101280101"); //传�1�7�广州的城市id
		else {
			imgWeather.setVisibility(View.GONE);
			tvWeather.setText("当前网络不可甄1�7");
			Toast.makeText(MainActivity.this, "请检查网组1�7", Toast.LENGTH_SHORT).show();
		}
		//获取默认设置文件信息
		SharedPreferences share = getSharedPreferences(FILENAME, 0);
		if(share.getString("settingTime", "").equals("")) {
			SharedPreferences.Editor editor = share.edit();
			editor.putString("settingTime", "09:00");
			editor.commit();
		}
		application.setSettingTime(share.getString("settingTime", "09:00"));
		/**日历数据初始匄1�7*/
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		
		gestureDetector = new GestureDetector(this, new MyGestureListener());
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);
		flipper.addView(gridView, 0);
		addTextToTopTextView(currentMonth);
		/**当前日期日程初始匄1�7*/
		String[] todayIDs = dao.getScheduleByTagDate(year_c, month_c, day_c);
		if(todayIDs != null && todayIDs.length > 0) {
			rlvSchedule.setVisibility(View.VISIBLE);
			showTodaySchedule(todayIDs);
		}
		else
        	rlvSchedule.setVisibility(View.GONE);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		/*int position = application.getPosition();
		String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
		// //这一天的阴历
		String scheduleYear = calV.getShowYear();
		String scheduleMonth = calV.getShowMonth();
		if(position != 0) {
			calV.setSelection(position);
		}
		
		//通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信恄1�7
        String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
        if(scheduleIDs != null && scheduleIDs.length > 0) {
        	rlvSchedule.setVisibility(View.VISIBLE);
        	showTodaySchedule(scheduleIDs);
        }
        else
        	rlvSchedule.setVisibility(View.GONE);*/
		
		/**日历数据初始匄1�7*/
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);
		flipper.removeAllViews();
		flipper.addView(gridView, 0);
		addTextToTopTextView(currentMonth);
		/**当前日期日程初始匄1�7*/
		String[] todayIDs = dao.getScheduleByTagDate(year_c, month_c, day_c);
		if(todayIDs != null && todayIDs.length > 0 && !application.isClear()) {
			rlvSchedule.setVisibility(View.VISIBLE);
			showTodaySchedule(todayIDs);
		}
		else
        	rlvSchedule.setVisibility(View.GONE);
	}

	/**
	 * 异步加载天气信息
	 */
	private class execWeatherTask extends AsyncTask<String, Void, String> {

		private WeatherModel model = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			model = HttpUtils.getMessage(params[0]);
			application.setWeatherModel(model);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(model.getWarning().equals("none")) {
				tvWeather.setText(model.getWeather1() + "\t" + model.getTemp1() + "\n" + model.getCity());
				OkHttpUtils.get()
					.url(Properties.PHOTO_URL + "b" +model.getImg1() + ".gif")
					.build().execute(new BitmapCallback() {
						public void onResponse(android.graphics.Bitmap bitmap) {
							imgWeather.setImageBitmap(bitmap);
						}

						@Override
						public void onError(Call arg0, Exception arg1) {
							Log.e(TAG, arg1.toString());
						};
					});
			}
			else
				tvWeather.setText(model.getWarning());
			super.onPostExecute(result);
		}
	}
	
	private void initEvent() {
		btnPerson.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
//		btnSchedule.setOnClickListener(this);
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
	}

	public void intent(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(MainActivity.this, cls);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v == btnPerson) {
			//跳转到个人页
			Intent intent_ = new Intent(MainActivity.this, UserActivity.class);
			startActivity(intent_);
		}
		else if(v == btnAdd) {
			//跳转到添加日程页
			Intent intent_ = new Intent(MainActivity.this, AddScheduleActivity.class);
			startActivity(intent_);
		}
		/*else if(v == btnSchedule) {
			//跳转到相应日程页
			Intent intent_ = new Intent(MainActivity.this, ScheduleListActivity.class);
			startActivity(intent_);
		}*/
		else if(v == prevMonth) {
			enterPrevMonth(gvFlag);
		}
		else if(v == nextMonth) {
			enterNextMonth(gvFlag);
		}
	}
	/**
	 * 逄1�7出程庄1�7
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN) {
			if(System.currentTimeMillis() - exitTime > 2000) {
				exitTime = System.currentTimeMillis();
				Toast.makeText(MainActivity.this, "再按丄1�7次�1�7�1�7出程庄1�7", Toast.LENGTH_SHORT).show();
			}
			else {
				finish();
				System.exit(0);
			}
			return true; //不能缺少
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 手势操作
	 */
	@SuppressLint("NewApi")
	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
			if(e1.getY() <= flipper.getY() + flipper.getHeight()) {
				if (e1.getX() - e2.getX() > 120) {
					// 像左滑动
					enterNextMonth(gvFlag);
					return true;
				} else if (e1.getX() - e2.getX() < -120) {
					// 向右滑动
					enterPrevMonth(gvFlag);
					return true;
				}
				return false;
			}
			else
				return false;
		}
	}
	/**
	 * 移动到下丄1�7个月
	 * 
	 * @param gvFlag
	 */
	private void enterNextMonth(int gvFlag) {
		addGridView(); // 添加丄1�7个gridView
		jumpMonth++; // 下一个月

		calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		addTextToTopTextView(currentMonth); // 移动到下丄1�7月后，将当月显示在头标题丄1�7
		gvFlag++;
		flipper.addView(gridView, gvFlag);
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		flipper.showNext();
		flipper.removeViewAt(0);
	}

	/**
	 * 移动到上丄1�7个月
	 * 
	 * @param gvFlag
	 */
	private void enterPrevMonth(int gvFlag) {
		addGridView(); // 添加丄1�7个gridView
		jumpMonth--; // 上一个月

		calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		gvFlag++;
		addTextToTopTextView(currentMonth); // 移动到上丄1�7月后，将当月显示在头标题丄1�7
		flipper.addView(gridView, gvFlag);

		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
		flipper.showPrevious();
		flipper.removeViewAt(0);
	}

	/**
	 * 添加头部的年仄1�7 闰哪月等信息
	 * 
	 * @param view
	 */
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(MonthName[Integer.valueOf(calV.getShowMonth()) - 1]).append("  /  ").append(calV.getShowYear());
		view.setText(textDate);
		tvCalDate.setText(day_c + "");
		/**当前日期日程初始匄1�7*/
		String[] todayIDs = dao.getScheduleByTagDate(Integer.parseInt(calV.getShowYear()), Integer.parseInt(calV.getShowMonth()), day_c);
		if(todayIDs != null && todayIDs.length > 0) {
			rlvSchedule.setVisibility(View.VISIBLE);
			showTodaySchedule(todayIDs);
		}
		else
        	rlvSchedule.setVisibility(View.GONE);
	}

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();

		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(40);
		// gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		if (Width == 720 && Height == 1280) {
			gridView.setColumnWidth(40);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 去除gridView边框
		gridView.setPadding(10, 0, 10, 0);
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(10);
		gridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return MainActivity.this.gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				// 点击任何丄1�7个item，得到这个item的日朄1�7(排除点击的是周日到周兄1�7(点击不响庄1�7))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7 && position <= endPosition - 7) {
					application.setPosition(position);
					String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
					// String scheduleLunarDay =
					// calV.getDateByClickItem(position).split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
//					Toast.makeText(MainActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, 0).show();
					calV.setSelection(position);
					
					//通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信恄1�7
	                String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                if(scheduleIDs != null && scheduleIDs.length > 0) {
	                	rlvSchedule.setVisibility(View.VISIBLE);
	                	showTodaySchedule(scheduleIDs);
	                }
	                else
	                	rlvSchedule.setVisibility(View.GONE);
				}
			}
		});
		gridView.setLayoutParams(params);
	}
	/**
	 * 点击日历某一天后，显示当天日稄1�7
	 * @param scheduleIDs
	 */
	private void showTodaySchedule(String[] scheduleIDs) {
		List<ScheduleModel> list = new ArrayList<ScheduleModel>();
		ScheduleModel model = null;
		for(int i = 0; i < scheduleIDs.length; i++) {
			model = dao.getScheduleByID(Integer.parseInt(scheduleIDs[i]));
			list.add(model);
		}
		tAdapter = new ScheduleTodayAdapter(context, this, list);
		rlvSchedule.setAdapter(tAdapter);
		setListViewHeight(rlvSchedule);
	}
	/**
	 * scrollview里嵌套listview
	 * @param list
	 */
	private void setListViewHeight(ListView list){
		ListAdapter listAdapter = list.getAdapter();
		if(listAdapter == null)
			return;
		
		int totalHeight = 0;
		int len = listAdapter.getCount();
		for(int i=0; i<len; i++){
			View listItem = listAdapter.getView(i, null, list);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = list.getLayoutParams();
		params.height = totalHeight + list.getDividerHeight() * (listAdapter.getCount()-1);
		list.setLayoutParams(params);
	}
}
