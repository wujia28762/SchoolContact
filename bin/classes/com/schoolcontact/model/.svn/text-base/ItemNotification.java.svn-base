package com.schoolcontact.model;

public class ItemNotification {
	private String className;
	private String classId;
	private String groupName;
	private String groupId;

	public ItemNotification() {
	}
	public ItemNotification(String className, String classId, String groupName,
			String groupId) {
		this.className = className;
		this.classId = classId;
		this.groupName = groupName;
		this.groupId = groupId;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Override
	public String toString() {
		String result = className;
		if(!groupName.equals("")){
			result += "-"+groupName;
		}
		return result;
	}
	
}
