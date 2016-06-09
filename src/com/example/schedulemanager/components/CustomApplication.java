package com.example.schedulemanager.components;

import java.util.Date;

import com.example.schedulemanager.models.User;
import com.example.schedulemanager.models.WeatherModel;
import com.example.schedulemanager.models.callbacks.UserCallback;
import com.example.schedulemanager.utils.Properties;
import com.example.schedulemanager.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import okhttp3.Call;
/**
 * 全局应用类
 * @author smnan
 * 2015.4.23
 */
public class CustomApplication extends Application{

	private static CustomApplication instance;
	private WeatherModel weatherModel;
	
	private String settingTime;  //日程默认提醒时间
	
	private int position = 0; //记录日历点击的位置
	
	private boolean isClear = false; //记录是否清除缓存
	
	private User user = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initConfig();
	}

	public static CustomApplication getInstance() {
		return instance;
	}
	/**
	 * 初始化系统参数
	 */
	private void initConfig() {
		TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		user = new User(StringUtil.getMD5(tm.getDeviceId()), StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		OkHttpUtils.post().url(Properties.LOCALHOST_IP + "user/login?")	
					.addParams("userId", user.userId)
					.addParams("lastTime", user.lastTime)
					.build().execute(new UserCallback() {
						
						@Override
						public void onResponse(User model) {
							if(model != null) {
								user = model;
								Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
							}
						}
						
						@Override
						public void onError(Call arg0, Exception e) {
							Log.e("login", e.toString());
							Toast.makeText(getApplicationContext(), "连不上服务器", Toast.LENGTH_SHORT).show();
						}
					});
	}
	
	public WeatherModel getWeatherModel() {
		return weatherModel;
	}

	public void setWeatherModel(WeatherModel weatherModel) {
		this.weatherModel = weatherModel;
	}

	public String getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(String settingTime) {
		this.settingTime = settingTime;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isClear() {
		return isClear;
	}

	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}

	public User getUser() {
		if(user == null)
			initConfig();
		return user;
	}
	
}
