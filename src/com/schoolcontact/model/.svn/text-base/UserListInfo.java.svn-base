package com.schoolcontact.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Star
 * @description 用户实体类
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserListInfo implements Serializable{
	/**
	 * @description 
	 */
	private static final long serialVersionUID = -423124386288825544L;
	private String userId;
	private String userName;
	private String userType;
	private String portraitUri;
	private String comments;

	public UserListInfo(String userId, String userName, String userType,
			String portraitUri, String comments) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
		this.portraitUri = portraitUri;
		this.comments = comments;
	}

	public UserListInfo()
	{
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPortraitUri() {
		return portraitUri;
	}

	public void setPortraitUri(String portraitUri) {
		this.portraitUri = portraitUri;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}