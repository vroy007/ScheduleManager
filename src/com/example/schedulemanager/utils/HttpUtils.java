package com.example.schedulemanager.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.schedulemanager.models.WeatherModel;

/**
 * http request
 * @author smnan
 *
 */
public class HttpUtils {

	public static WeatherModel getMessage(String id) {
		
		WeatherModel model = new WeatherModel();
		String json = doGet(id);
		
		try{
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject jsonObj = (JSONObject) jsonParser.nextValue();
			JSONObject info = jsonObj.getJSONObject("weatherinfo");
			if(info != null) {
				model.setCity(info.getString("city"));
				model.setCityId(info.getString("cityid"));
				model.setTemp1(info.getString("temp1"));
				model.setTemp2(info.getString("temp2"));
				model.setImg1(info.getString("img1"));
				model.setWeather1(info.getString("weather1"));
//				model.setPm(info.getString("pm"));
//				model.setPm_level(info.getString("pm-level"));
				model.setWarning("none");
			}
		} catch(JSONException e) {
			model.setWarning("网络故障，暂时无法获取天气信息");
		}
		return model;
	}
	
	public static String doGet(String id) {
		String result = null;
		String url = setParams(id);
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		
		try {
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlNet.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			is = conn.getInputStream();
			int len = -1;
			byte[] buffer = new byte[128];
			baos = new ByteArrayOutputStream();
			
			while((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			result = new String(baos.toByteArray());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private static String setParams (String id) {
		String url = "";
		url = Properties.SEVICE_URL + id + "&weatherType=0";
		return url;
	}
}
