package com.schoolcontact.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishComment extends BaseFriendModel {

	private String comment_time;
	private String key_id;
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}
	public String getKey_id() {
		return key_id;
	}
	public void setKeyid(String key_id) {
		this.key_id = key_id;
	}
}
