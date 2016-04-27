package com.example.schedulemanager.activitys.groups;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.BaseActivity;
/**
 * 加入群组
 * @author smnan
 * 2015.4.25
 */
public class JoinGroupActivity extends BaseActivity implements OnClickListener{

	private Button btnBack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_group);
		
		btnBack = (Button) this.findViewById(R.id.btnJoinGroupBack);
		btnBack.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v == btnBack)
			finish();
	}
	
}
