package com.schoolcontact.service;

import net.tsz.afinal.FinalHttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.utils.EventList;

/**
 * @author Star
 * @description  服务基类（包括通用的URL以及上下文引用等）
 */
public abstract class BaseService {

	public final String baseUrl = EventList.BASEURL;
	public final ScContext sccontext = ScContext.getInstance();
	public final FinalHttp fh = new FinalHttp();
	public final ObjectMapper objectMapper = new ObjectMapper();
}
