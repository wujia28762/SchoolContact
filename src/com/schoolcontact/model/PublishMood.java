package com.schoolcontact.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishMood extends BaseFriendModel {

	private String send_time;
	private String key_id;
	
	public String getKey_id() {
		return key_id;
	}
	public void setKeyid(String key_id) {
		this.key_id = key_id;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
}
