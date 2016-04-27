package com.example.schedulemanager.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class DeviceUtil {
	public final static String IMSI = "IMSI";
	public final static String IMEI = "IMEI";

	public static boolean isWiFiActive(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext()  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if (info != null) {  
                for (int i = 0; i < info.length; i++) {  
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }
	
	/**
	 * sdcard是否可用
	 * @return
	 */
	public static boolean isSdcardEnable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 判断设备是否连接了网络
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
        boolean netSataus = false;
        try {
			 ConnectivityManager cwjManager =
        		(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
        }
		} catch (Exception e) {
			Log.e("DeviceUtil", e.getMessage());
		}
        return netSataus;
    }
	
//	public static boolean isNetworkAvailable(Context context) {
//	    ConnectivityManager connectivity = (ConnectivityManager) context  
//	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
//	    if (connectivity == null) {  
//	        boitealerte(this.getString(R.string.alert),  
//	                "getSystemService rend null");  
//	    } else {//获取所有网络连接信息  
//	        NetworkInfo[] info = connectivity.getAllNetworkInfo();  
//	        if (info != null) {//逐一查找状态为已连接的网络  
//	            for (int i = 0; i < info.length; i++) {  
//	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
//	                    return true;  
//	                }  
//	            }  
//	        }  
//	    }  
//	    return false;  
//	}
	
	/**
	 * 获取设备IMSI和IMEI号
	 * @param context
	 * @return
	 */
	public static Map<String, String> getIMSIAndIMEI(Context context) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		TelephonyManager mTelephonyMgr = 
				(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
		String imei = mTelephonyMgr.getDeviceId();
		
		map.put(IMSI, imsi);
		map.put(IMEI, imei);
		 
		return map;
	}
	
	/**
	 * 获取设备分辨率
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getScreenPixels(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
	
	/**
	 * 获取程序版本号
	 * @throws NameNotFoundException
	 * 
	 */
	public static float getAppVesionCode(Context context) throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE).versionCode;
	}
	
	/**
	 * 获取程序版本名称
	 * @throws NameNotFoundException
	 * 
	 */
	public static String getAppVesionName(Context context) throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE).versionName;
	}
	
	/**
	 * 获取系统版本号
	 * @throws NameNotFoundException
	 * 
	 */
	public static String getSystemVesionCode(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceSoftwareVersion();
	}
	
	/**
	 * 获取设备类型(phone、pad)
	 * 
	 */
	public static String deviceStyle(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
        	return "phone";
        }

        return "pad";
    }
}
