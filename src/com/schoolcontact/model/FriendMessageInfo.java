package com.schoolcontact.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendMessageInfo implements Serializable {

	/**
	 * @description
	 */
	private static final long serialVersionUID = 6945809395400547569L;
	private String key_id;
	private String content;
	private String uname;
	private String file_urls;
	private String send_time;
	private String uid;
	private String portraitUri;
	private List<CommentInfo> comments;
	private List<String> thumbs;
	private String group_id;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getKey_id() {
		return key_id;
	}

	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFile_urls() {
		return file_urls;
	}

	public void setFile_urls(String file_urls) {
		this.file_urls = file_urls;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public List<CommentInfo> getComments() {
		return comments;
	}

	public void setComments(List<CommentInfo> comments) {
		this.comments = comments;
	}

	public String getPortraitUri() {
		return portraitUri;
	}

	public void setPortraitUri(String portraitUri) {
		this.portraitUri = portraitUri;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<String> getThumbs() {
		return thumbs;
	}

	public void setThumbs(List<String> thumbs) {
		this.thumbs = thumbs;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	@Override
	public boolean equals(Object o) {
		FriendMessageInfo p = (FriendMessageInfo) o;
		if (this.getKey_id() == p.getKey_id()) {
			return true;
		} else {
			return false;
		}

	}

}
