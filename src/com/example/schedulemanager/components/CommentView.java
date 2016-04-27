package com.example.schedulemanager.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.schedulemanager.R;
import com.example.schedulemanager.utils.StringUtil;
/**
 * 评论窗
 * @author smnan
 * 2015.4.25
 */
public class CommentView extends PopupWindow implements OnClickListener{

	private static final String TAG = "CommentView";
	private Context context;
	private CustomApplication application;
	private View view = null;
	
	private EditText etComment = null;
	private Button btnCommit = null;
	private ListView rlv = null;
	private CommentListAdapter mAdapter = null;
	private List<Map<String, Object>> list = null;
	
	public CommentView(Context context) {
		this.context = context;
//		application = (CustomApplication) ((Activity)context).getApplication();
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.comment_popupwindow, null);
		
		etComment = (EditText) view.findViewById(R.id.etComment);
		btnCommit = (Button) view.findViewById(R.id.btnCommit);
		rlv = (ListView) view.findViewById(R.id.lvComment);
		
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = v.findViewById(R.id.pop_layout_comment).getHeight();
				int y = (int) event.getY();
				if(event.getAction() == MotionEvent.ACTION_UP)
					if(y < height)
						dismiss();
				return true;
			}
		});
		btnCommit.setOnClickListener(this);
		
		list = new ArrayList<Map<String,Object>>();
		mAdapter = new CommentListAdapter(context, list);
		rlv.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if(v == btnCommit) {
			//add a new comment into listview(rlv)
			if(!StringUtil.isEmpty(etComment.getText().toString().trim())) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comment", etComment.getText().toString().trim());
				list.add(map);
				
				mAdapter.notifyDataSetChanged();
				etComment.setText("");
				Toast.makeText(context, "评论提交成功", 0).show();
			}
			else {
				Toast.makeText(context, "评论内容不能为空", 0).show();
				etComment.requestFocus();
			}
		}
	}

}
