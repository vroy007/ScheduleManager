package com.example.schedulemanager.components;

import android.app.Application;

import com.example.schedulemanager.models.WeatherModel;
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
		//设置为debug模式
		/*Log.isDebug = true;
		Log.setContext(this);*/
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
	
}
