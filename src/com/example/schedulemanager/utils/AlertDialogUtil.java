package com.example.schedulemanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class AlertDialogUtil {
	
	public static void showDialog(Context context, CharSequence title,
			CharSequence msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if(title==null||title.equals(""))
		{
			builder.setTitle("温馨提示");
		}
		else
		{
			builder.setTitle(title);
		}
		
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	/**
	 * 
	 * @param context
	 * @param title
	 *            // 提示标题
	 * @param msg
	 *            // 提示信息
	 * @param okListener
	 *            // 确定按钮事件
	 */
	public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
			DialogInterface.OnClickListener okListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton("确定", okListener);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		return builder.show();
	}
	
	/**
	 * 
	 * @param context
	 * @param msg				// 提示信息
	 * @param backgroundResid	// 对话框背景
	 * @param btnOkResid		// 确定按钮样式
	 */
	public static AlertDialog showNotCancelDialog(Context context, CharSequence title, CharSequence msg, 
			DialogInterface.OnClickListener okListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton("确定", okListener);
		return builder.show();
	}
	
	/**
	 * 
	 * @param context
	 * @param msg				// 提示信息
	 * @param backgroundResid	// 对话框背景
	 * @param btnOkResid		// 确定按钮样式
	 */
	public static void showCustomDialog(Context context, CharSequence msg,
			int backgroundResid, int btnOkResid) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		if (backgroundResid > 0)
			dialog.setBackgroundResource(backgroundResid);
		
		dialog.setMessage(msg);
		dialog.setButton(btnOkResid, new CustomAlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	/**
	 * 
	 * @param context
	 * @param msg				// 提示信息
	 * @param backgroundResid	// 对话框背景
	 * @param btnOkResid		// 确定按钮样式
	 * @param okListener		// 确定按钮事件
	 */
	public static void showCustomDialog(Context context, CharSequence msg,
			int backgroundResid, int btnOkResid, 
			CustomAlertDialog.OnClickListener okListener) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		if (backgroundResid > 0)
			dialog.setBackgroundResource(backgroundResid);
		
		dialog.setMessage(msg);
		dialog.setButton(btnOkResid, okListener);
		dialog.show();
	}
	/**
	 * 
	 * @param context
	 * @param msg				// 提示信息
	 * @param backgroundResid	// 对话框背景
	 * @param btnOkResid		// 确定按钮样式
	 * @param btnCancelResid	// 取消按钮样式
	 * @param okListener		// 确定按钮事件
	 */
	public static void showCustomDialog(Context context, CharSequence msg,
			int backgroundResid, int btnOkResid, int btnCancelResid,
			CustomAlertDialog.OnClickListener okListener) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		if (backgroundResid > 0)
			dialog.setBackgroundResource(backgroundResid);
		
		dialog.setMessage(msg);
		dialog.setButton(btnOkResid, okListener);
		
		dialog.setButton(btnCancelResid,
				new CustomAlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}
	/**
	 * 
	 * @param context
	 * @param msg				// 提示信息
	 * @param backgroundResid	// 对话框背景
	 * @param btnOkResid		// 确定按钮样式
	 * @param btnCancelResid	// 取消按钮样式
	 * @param okListener		// 确定按钮事件
	 */
	public static void showCustomDialog(Context context, CharSequence msg,
			int backgroundResid, int btnOkResid, int btnCancelResid,
			CustomAlertDialog.OnClickListener okListener,CustomAlertDialog.OnClickListener cancelListener) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		if (backgroundResid > 0)
			dialog.setBackgroundResource(backgroundResid);
		
		dialog.setMessage(msg);
		dialog.setButton(btnOkResid, okListener);
		
		dialog.setButton(btnCancelResid,cancelListener);
		dialog.show();
	}
	
	public static void showCustomDialogReverseButton(Context context,
			CharSequence msg, int backgroundResid, int btnCancelResid,
			int btnOkResid, CustomAlertDialog.OnClickListener okListener) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		if (backgroundResid > 0)
			dialog.setBackgroundResource(backgroundResid);
		
		dialog.setMessage(msg);
		dialog.setButton(btnCancelResid,
				new CustomAlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		dialog.setButton(btnOkResid, okListener);
		
		dialog.show();
	}
	
	public static DialogInterface showCustomDialog(Context context, View view) {
		CustomAlertDialog dialog = new CustomAlertDialog(context);
		dialog.showView(view);
		
		return dialog;
	}
	
}
