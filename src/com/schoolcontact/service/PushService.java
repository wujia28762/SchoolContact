package com.schoolcontact.service;

import java.util.ArrayList;
import java.util.List;

import com.schoolcontact.model.CustomContent;
import com.schoolcontact.utils.ListUtils;

public class PushService extends BaseService {

	public static String[] messageType = { "MESSAGE", "COMMENT", "THUMBUP" };

	public static boolean aboutMe;
	public void savePushInfoToContext(String messageType,String messageBody) {

		CustomContent pi;
		
		try {
			
			
			pi = objectMapper.readValue(messageBody, CustomContent.class);
			
			if (!messageType.equals("MESSAGE")){
				//CustomContent cc = pi;
				if (!ListUtils.isEmpty(sccontext.getmCustomContent())) {
					sccontext.getmCustomContent().add(pi);
				} else {
					List<CustomContent> lcc = new ArrayList<CustomContent>();
					lcc.add(pi);
					sccontext.setmCustomContent(lcc);
				}
				sccontext.setNewAboutMe(true);
			}
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
