package com.example.schedulemanager.utils;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.schedulemanager.models.ScheduleDateTag;
import com.example.schedulemanager.models.ScheduleModel;

/**
 * 对日程DAO操作
 * @author smnan
 * 2015.5.1
 */
public class ScheduleDAO {

	private Context context = null;
	private DBOpenHelper dbOpenHelper = null;
	
	public ScheduleDAO(Context context) {
		this.context = context;
		dbOpenHelper = new DBOpenHelper(context, "schedules.db");
	}
	/**
	 * 保存日程信息
	 * @param scheduleVO
	 */
	public int save(ScheduleModel model){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", model.getContents());
		values.put("begin_time", model.getBeginTime());
		values.put("end_time", model.getEndTime());
		values.put("tip_time", model.getTipTime());
		values.put("isTip", model.getIsTips());
		values.put("isDone", model.getIsDone());
		values.put("isGroup", model.getIsGroup());
		db.beginTransaction();
		int scheduleID = -1;
		try{
			db.insert("schedule", null, values);
		    Cursor cursor = db.rawQuery("select max(scheduleID) from schedule", null);
		    if(cursor.moveToFirst()){
		    	scheduleID = (int) cursor.getLong(0);
		    }
		    cursor.close();
		    db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	    return scheduleID;
	}
	
	/**
	 * 查询某一条日程信息
	 * @param scheduleID
	 * @return
	 */
	public ScheduleModel getScheduleByID(int scheduleID){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("schedule", new String[]{"scheduleID","content","begin_time","end_time","tip_time","isTip","isDone","isGroup"}, "scheduleID=?", new String[]{String.valueOf(scheduleID)}, null, null, null);
		if(cursor.moveToFirst()){
			int schID = cursor.getInt(cursor.getColumnIndex("scheduleID"));
			int isTip = cursor.getInt(cursor.getColumnIndex("isTip"));
			int isDone = cursor.getInt(cursor.getColumnIndex("isDone"));
			int isGroup = cursor.getInt(cursor.getColumnIndex("isGroup"));
			String scheduleContent = cursor.getString(cursor.getColumnIndex("content"));
			String beginTime = cursor.getString(cursor.getColumnIndex("begin_time"));
			String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
			String tipTime = cursor.getString(cursor.getColumnIndex("tip_time"));
			cursor.close();
			return new ScheduleModel(schID, isTip, scheduleContent, beginTime, endTime, tipTime, isDone, isGroup);
		}
		cursor.close();
		return null;
		
	}
	
	/**
	 * 查询所有的日程信息
	 * @return
	 */
	public ArrayList<ScheduleModel> getAllSchedule(){
		ArrayList<ScheduleModel> list = new ArrayList<ScheduleModel>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("schedule", new String[]{"scheduleID","content","begin_time","end_time","tip_time","isTip","isDone","isGroup"}, null, null, null, null, "scheduleID desc");
		while(cursor.moveToNext()){
			int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID")); 
			int isTip = cursor.getInt(cursor.getColumnIndex("isTip"));
			int isDone = cursor.getInt(cursor.getColumnIndex("isDone"));
			int isGroup = cursor.getInt(cursor.getColumnIndex("isGroup"));
			String scheduleContent = cursor.getString(cursor.getColumnIndex("content"));
			String beginTime = cursor.getString(cursor.getColumnIndex("begin_time"));
			String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
			String tipTime = cursor.getString(cursor.getColumnIndex("tip_time"));
			ScheduleModel vo = new ScheduleModel(scheduleID, isTip, scheduleContent, beginTime, endTime, tipTime, isDone, isGroup);
			list.add(vo);
		}
		cursor.close();
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
		
	}
	
	/**
	 * 查询群组日程信息
	 * @return
	 */
	public ArrayList<ScheduleModel> getGroupSchedule(){
		ArrayList<ScheduleModel> list = new ArrayList<ScheduleModel>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("schedule", new String[]{"scheduleID","content","begin_time","end_time","tip_time","isTip","isDone","isGroup"}, null, null, null, null, "scheduleID desc");
		while(cursor.moveToNext()){
			int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID")); 
			int isTip = cursor.getInt(cursor.getColumnIndex("isTip"));
			int isDone = cursor.getInt(cursor.getColumnIndex("isDone"));
			int isGroup = cursor.getInt(cursor.getColumnIndex("isGroup"));
			String scheduleContent = cursor.getString(cursor.getColumnIndex("content"));
			String beginTime = cursor.getString(cursor.getColumnIndex("begin_time"));
			String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
			String tipTime = cursor.getString(cursor.getColumnIndex("tip_time"));
			ScheduleModel vo = new ScheduleModel(scheduleID, isTip, scheduleContent, beginTime, endTime, tipTime, isDone, isGroup);
			if(isGroup == 1)
				list.add(vo);
		}
		cursor.close();
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
		
	}
	
	/**
	 * 删除日程
	 * @param scheduleID
	 */
	public void delete(int scheduleID){
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			db.delete("schedule", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
			db.delete("scheduletagdate", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	/**
	 * 更新日程
	 * @param vo
	 */
	public void update(ScheduleModel model){
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", model.getContents());
		values.put("begin_time", model.getBeginTime());
		values.put("end_time", model.getEndTime());
		values.put("tip_time", model.getTipTime());
		values.put("isTip", model.getIsTips());
		values.put("isDone", model.getIsDone());
		values.put("isGroup", model.getIsGroup());
		db.update("schedule", values, "scheduleID=?", new String[]{String.valueOf(model.getScheduleID())});
	}
	
	/**
	 * 将日程标志日期保存到数据库中
	 * @param dateTagList
	 */
	public void saveTagDate(ArrayList<ScheduleDateTag> dateTagList){
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ScheduleDateTag dateTag = new ScheduleDateTag();
		for(int i = 0; i < dateTagList.size(); i++){
			dateTag = dateTagList.get(i);
			ContentValues values = new ContentValues();
			values.put("year", dateTag.getYear());
			values.put("month", dateTag.getMonth());
			values.put("day", dateTag.getDay());
			values.put("scheduleID", dateTag.getScheduleID());
			db.insert("scheduletagdate", null, values);
		}
	}
	
	/**
	 * 只查询出当前月的日程日期
	 * @param currentYear
	 * @param currentMonth
	 * @return
	 */
	public ArrayList<ScheduleDateTag> getTagDate(int currentYear, int currentMonth){
		ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("scheduletagdate", new String[]{"tagID","year","month","day","scheduleID"}, "year=? and month=?", new String[]{String.valueOf(currentYear),String.valueOf(currentMonth)}, null, null, null);
		while(cursor.moveToNext()){
			int tagID = cursor.getInt(cursor.getColumnIndex("tagID"));
			int year = cursor.getInt(cursor.getColumnIndex("year"));
			int month = cursor.getInt(cursor.getColumnIndex("month"));
			int day = cursor.getInt(cursor.getColumnIndex("day"));
			int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID"));
			ScheduleDateTag dateTag = new ScheduleDateTag(tagID,year,month,day,scheduleID);
			dateTagList.add(dateTag);
			}
		cursor.close();
		if(dateTagList != null && dateTagList.size() > 0){
			return dateTagList;
		}
		return null;
	}
	
	/**
	 * 当点击每一个gridview中item时,查询出此日期上所有的日程标记(scheduleID)
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String[] getScheduleByTagDate(int year, int month, int day){
		ArrayList<ScheduleModel> scheduleList = new ArrayList<ScheduleModel>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		//根据时间查询出日程ID（scheduleID），一个日期可能对应多个日程ID
		Cursor cursor = db.query("scheduletagdate", new String[]{"scheduleID"}, "year=? and month=? and day=?", new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
		String scheduleIDs[] = null;
		scheduleIDs = new String[cursor.getCount()];
		int i = 0;
		while(cursor.moveToNext()){
			String scheduleID = cursor.getString(cursor.getColumnIndex("scheduleID"));
			scheduleIDs[i] = scheduleID;
			i++;
		}
		cursor.close();
		return scheduleIDs;
	}
	
	/**
	 *关闭DB
	 */
	public void destoryDB(){
		if(dbOpenHelper != null){
			dbOpenHelper.close();
		}
	}
}
