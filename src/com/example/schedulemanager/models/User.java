package com.example.schedulemanager.models;

/**
 * 用户model
 * @author shimn
 *
 */
public class User {
	//服务器生成唯一uuid
	public String id;
	//本机生成加密的deviceId
	public String userId;
	//用户最后一次登录时间
	public String lastTime;
	
	public User(String userId, String lastTime) {
		this.userId = userId;
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", lastTime=" + lastTime + "]";
	}
}
