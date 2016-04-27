package com.example.schedulemanager.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 手机重启广播
 * @author smnan
 * 2015.5.3
 */
public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			//手机重启
			Alarms.saveSnoozeAlert(context, -1, -1);
//			Intent startServiceIntent = new Intent(context, AlarmService.class);
//			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startService(startServiceIntent);
		}
		Alarms.disableExpiredAlarms(context);
        Alarms.setNextAlert(context);
	}

}
