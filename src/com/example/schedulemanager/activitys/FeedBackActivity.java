package com.example.schedulemanager.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.schedulemanager.R;
/**
 * 意见反馈
 * @author smnan
 * 2015.4.26
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "FeedBackActivity";
	private Button btnBack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		
		btnBack = (Button) this.findViewById(R.id.btnFeedBackBack);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == btnBack)
			finish();
	}
}
