package com.schoolcontact.model;

import java.io.Serializable;


public class GroupInfo implements Serializable {

	/**
	 * @description 
	 */
	private static final long serialVersionUID = -9184526245749189720L;
	private String group_id;
	private String group_name;
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
