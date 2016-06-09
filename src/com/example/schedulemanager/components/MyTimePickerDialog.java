package com.example.schedulemanager.components;
import android.app.TimePickerDialog;
import android.content.Context;

public class MyTimePickerDialog extends TimePickerDialog {

    public MyTimePickerDialog(Context context, OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
        // TODO Auto-generated constructor stub
    }

    public MyTimePickerDialog(Context context, int theme,
            OnTimeSetListener callBack, int hourOfDay, int minute,
            boolean is24HourView) {
        super(context, theme, callBack, hourOfDay, minute, is24HourView);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub

        // 注释掉，防止onTimeSet()执行两次
        // super.onStop();
    }

}