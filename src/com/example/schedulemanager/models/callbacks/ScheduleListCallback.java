package com.example.schedulemanager.models.callbacks;

import java.util.List;

import com.example.schedulemanager.models.Schedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * message list 回调
 */
public abstract class ScheduleListCallback extends Callback<List<Schedule>>{
    @Override
    public List<Schedule> parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        List<Schedule> lists = new Gson().fromJson(str, new TypeToken<List<Schedule>>() {}.getType());
        return lists;
    }
}
