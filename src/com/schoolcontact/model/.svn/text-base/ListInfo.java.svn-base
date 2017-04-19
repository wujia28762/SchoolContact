package com.schoolcontact.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ListInfo extends BaseModel {
	private List<SchoolInfo> results;
	private Map<String,UserListInfo> notices;



	public Map<String,UserListInfo> getNotices() {
		return notices;
	}

	public void setNotices(Map<String,UserListInfo> notices) {
		this.notices = notices;
	}

	public List<SchoolInfo> getResults() {
		return results;
	}

	public void setResults(List<SchoolInfo> results) {
		this.results = results;
	}

}



