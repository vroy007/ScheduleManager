package com.example.schedulemanager.models.callbacks;

import com.example.schedulemanager.models.Schedule;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class ScheduleCallback extends Callback<Schedule> {

	@Override
	public Schedule parseNetworkResponse(Response response) throws Exception {
		 String str = response.body().string();
		 Schedule model = new Gson().fromJson(str, Schedule.class);
         return model;
	}

}
