package com.example.schedulemanager.models;

/**
 * 天气model
 * @author smnan
 * 2015.4.23
 */
public class WeatherModel {

	private String warning;
	private String city;
	private String cityId;
	private String temp1;
	private String temp2;
	private String weather1;
	private String img1;
//	private String pm;  // pm2.5
//	private String pm_level; //空气质量
	
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public String getWeather1() {
		return weather1;
	}
	public void setWeather1(String weather1) {
		this.weather1 = weather1;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
//	public String getPm() {
//		return pm;
//	}
//	public void setPm(String pm) {
//		this.pm = pm;
//	}
//	public String getPm_level() {
//		return pm_level;
//	}
//	public void setPm_level(String pm_level) {
//		this.pm_level = pm_level;
//	}
}
