package com.schoolcontact.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class LoginInfo extends BaseModel{

	private String user_group;
	private String lastlocation;
	private String lastip;
	private String permission;
	private String usersetting;
	private String istoken;
	private String sid;
	private String lasttime;
	private String isportraituri;
	private String lang;
	private String usertradeid;
	private String username;
	private String user_id;
	private String domain;
	public String getLastlocation() {
		return lastlocation;
	}
	public void setLastlocation(String lastlocation) {
		this.lastlocation = lastlocation;
	}
	public String getLastip() {
		return lastip;
	}
	public void setLastip(String lastip) {
		this.lastip = lastip;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getUsersetting() {
		return usersetting;
	}
	public void setUsersetting(String usersetting) {
		this.usersetting = usersetting;
	}
	public String getIstoken() {
		return istoken;
	}
	public void setIstoken(String istoken) {
		this.istoken = istoken;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public String getIsportraituri() {
		return isportraituri;
	}
	public void setIsportraituri(String isportraituri) {
		this.isportraituri = isportraituri;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getUsertradeid() {
		return usertradeid;
	}
	public void setUsertradeid(String usertradeid) {
		this.usertradeid = usertradeid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_group() {
		return user_group;
	}
	public void setUser_group(String user_group) {
		this.user_group = user_group;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
