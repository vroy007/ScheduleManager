package com.example.schedulemanager.models.callbacks;

import com.example.schedulemanager.models.User;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * user json回调
 */
public abstract class UserCallback extends Callback<User>{
    @Override
    public User parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        User model = new Gson().fromJson(str, User.class);
        return model;
    }
}
