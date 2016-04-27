package com.example.schedulemanager.activitys.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.activitys.BaseActivity;
/**
 * 群组日历
 * @author smnan
 * 2015.4.25
 */
public class MyGroupsActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "MyGroupsActivity";
	private Context context;
	
	private Button btnBack = null;
	private TextView tvTitle = null;
	
	private Button btnNew = null; //创建群组
	private Button btnJoin = null; //加入群组
	private Button btnLove = null; //爱
	private Button btnDream = null; //梦想
	private Button btnFood = null; //美食
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_groups);
		context = getApplicationContext();
		
		initView();
		initEvent();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText("群组日历");
		btnNew = (Button) this.findViewById(R.id.btnNewGroup);
		btnJoin = (Button) this.findViewById(R.id.btnJoinGroup);
		btnLove = (Button) this.findViewById(R.id.btnGroupLove);
		btnDream = (Button) this.findViewById(R.id.btnGroupDream);
		btnFood = (Button) this.findViewById(R.id.btnGroupFood);
	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
		btnNew.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		btnLove.setOnClickListener(this);
		btnDream.setOnClickListener(this);
		btnFood.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack: {
				finish();
				break;
			}
			case R.id.btnNewGroup: { //创建群组
				Intent intent = new Intent(MyGroupsActivity.this, NewGroupActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.btnJoinGroup: { //加入群组
				Intent intent = new Intent(MyGroupsActivity.this, JoinGroupActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.btnGroupLove: { //爱
				break;
			}
			case R.id.btnGroupDream: { //梦想
				break;
			}
			case R.id.btnGroupFood: { //美食
				Intent intent = new Intent(MyGroupsActivity.this, GroupFoodActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
}
