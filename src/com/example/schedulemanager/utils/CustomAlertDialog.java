package com.example.schedulemanager.utils;

import com.example.schedulemanager.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAlertDialog extends AlertDialog {
	private LinearLayout mainLayout = null;
	private TextView titleText = null;
	private LinearLayout topLayout = null;
	private LinearLayout bottomLayout = null;
	private TextView msgText = null;
	
	private Context mContext = null;
	
	private int btnCount = 0;
	
	public CustomAlertDialog(Context context) {
		super(context);
		mContext = context;
		initViews();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mainLayout);
	}
	
	private void initViews() {
		// 主布局
		mainLayout = new LinearLayout(mContext);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 55);
		titleText = new TextView(mContext);
		titleText.setGravity(Gravity.CENTER);
		titleText.setTextSize(20);
		titleText.setTextColor(Color.BLACK);
		mainLayout.addView(titleText, titleParams);
		
		// messge布局
		LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		topParams.weight = 1;
		topParams.topMargin = 10;
		topLayout = new LinearLayout(mContext);
		topLayout.setLayoutParams(topParams);
		// message
		msgText = new TextView(mContext);
		msgText.setGravity(Gravity.CENTER);
		msgText.setTextSize(18);
		msgText.setTextColor(Color.parseColor("#525252"));
		msgText.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 140));
		topLayout.addView(msgText);
		
		mainLayout.addView(topLayout);
		
		// 底部布局
		LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//		bottomParams.bottomMargin = 15;
		bottomLayout = new LinearLayout(mContext);
		bottomLayout.setGravity(Gravity.CENTER);
		bottomLayout.setLayoutParams(bottomParams);
		mainLayout.addView(bottomLayout);
	}
	
	public void setBackgroundResource(int resid) {
		mainLayout.setBackgroundResource(resid);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		titleText.setText(title);
	}

	@Override
	public void setMessage(CharSequence message) {
		msgText.setText(message);
	}
	
	public void setButton(int resid, CustomAlertDialog.OnClickListener listener) {
		Button button = new Button(mContext);
		button.setBackgroundResource(resid);
		Tag tag = new Tag();
		tag.listener = listener;
		tag.whichButton = btnCount;
		button.setTag(tag);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tag tag = (Tag) v.getTag();
				tag.listener.onClick(CustomAlertDialog.this, tag.whichButton);
			}
		});
		bottomLayout.addView(button);
		
		btnCount++;
	}
	
	public void setButton(int resid,String text, CustomAlertDialog.OnClickListener listener) {
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		btnParams.weight=1;
		Button button = new Button(mContext);
		button.setBackgroundResource(resid);
		button.setText(text);
		button.setTextSize(18);
		button.setLayoutParams(btnParams);
		button.setTextColor(mContext.getResources().getColor(R.color.blue));
		Tag tag = new Tag();
		tag.listener = listener;
		tag.whichButton = btnCount;
		button.setTag(tag);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tag tag = (Tag) v.getTag();
				tag.listener.onClick(CustomAlertDialog.this, tag.whichButton);
			}
		});
		bottomLayout.addView(button);
		
		btnCount++;
	}
	
	public interface OnClickListener {
		void onClick(DialogInterface dialog, int whichButton);
	}
	
	private class Tag {
		CustomAlertDialog.OnClickListener listener;
		int whichButton;
	}
	
	public DialogInterface showView(View view) {
		show();
		setContentView(view);
		
		return this;
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		return false;
//	}
	
}
