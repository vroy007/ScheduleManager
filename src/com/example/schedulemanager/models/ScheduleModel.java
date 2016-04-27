package com.example.schedulemanager.models;

/**
 * 每个日程的数据model
 * @author smnan
 * 2015.5.1
 */
public class ScheduleModel{

	private int scheduleID;
	private int isTips = -1;
	private String contents = null;
	private String beginTime = null;
	private String endTime = null;
	private String tipTime = null;
	private int isDone = -1;
	private int isGroup = -1;

	public ScheduleModel() {
	}

	public ScheduleModel(int scheduleID, int isTips, String contents,
			String beginTime, String endTime, String tipTime, int isDone, int isGroup) {
		super();
		this.scheduleID = scheduleID;
		this.isTips = isTips;
		this.contents = contents;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.tipTime = tipTime;
		this.isDone = isDone;
		this.isGroup = isGroup;
	}

	public int getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}

	public int getIsTips() {
		return isTips;
	}

	public void setIsTips(int isTips) {
		this.isTips = isTips;
	}

	public int getIsDone() {
		return isDone;
	}

	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTipTime() {
		return tipTime;
	}

	public void setTipTime(String tipTime) {
		this.tipTime = tipTime;
	}

	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}
	
}
