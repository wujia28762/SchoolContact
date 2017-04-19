package com.schoolcontact.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ImageInfo extends BaseModel{

	private String mediaid ;

	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}
	
}
