package com.schoolcontact.model;

import java.io.Serializable;
import java.util.List;

public class ClassInfo implements Serializable {
	/**
	 * @description 
	 */
	private static final long serialVersionUID = 3417072940280738522L;
	private String id;
	private String name;
	private String isGroup;
	private String groupId;
	private String portraitUri;
	private List<RoleGropuInfo> contents;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<RoleGropuInfo> getContents() {
		return contents;
	}

	public void setContents(List<RoleGropuInfo> contents) {
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPortraitUri() {
		return portraitUri;
	}

	public void setPortraitUri(String portraitUri) {
		this.portraitUri = portraitUri;
	}

}
