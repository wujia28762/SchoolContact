package com.schoolcontact.Base;

import io.rong.imkit.RongIM;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolcontact.listener.ConversationListClickListener;
import com.schoolcontact.listener.MyConversationBehaviorListener;
import com.schoolcontact.message.NotificationMessage;
import com.schoolcontact.message.NotificationReceiveMessageListener;
import com.schoolcontact.model.ListInfo;
import com.schoolcontact.model.LoginInfo;
import com.schoolcontact.provider.ImageMessageItemProvider;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.UpdateManager;

public class App extends Application {

	private SharedPreferences appPreferences;
	public ObjectMapper objectMapper = new ObjectMapper();

	public UserService us = new UserService();

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化。
//		RongIM.init(this);

		providerInit();
	}

	private void providerInit() {
		appPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
		ScContext.getInstance().setmPreferences(appPreferences);

		// 获取当前用户
		String curr_username = appPreferences.getString("lastusername", "");
		System.out.println("curr_username" + curr_username);
		ScContext.getInstance().setCurr_user(curr_username);
		if (getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);
			RongIM.registerMessageTemplate(new ImageMessageItemProvider());
			RongIM.setConversationListBehaviorListener(new ConversationListClickListener());
			RongIM.registerMessageType(NotificationMessage.class);
		//	RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
			// RongIM.registerMessageTemplate(new
			// NotificationMessageProvider());
			RongIM.setOnReceiveMessageListener(new NotificationReceiveMessageListener());
			String t = appPreferences.getString("userlist_" + curr_username, "");
			if (!t.equals("")) {
				try {

					ListInfo li = objectMapper.readValue(t, ListInfo.class);
					ScContext.getInstance().setUserProvider(li);

				} catch (Exception e) {
					System.out.println("解析好友关系缓存失败!");
					e.printStackTrace();
				}
			}
		}
		
		// }
		// 初始化公共上下文XML文件引用

		String t1 = appPreferences.getString("usercurrgroupname_"
				+ curr_username, "");
		if (!t1.equals("")) {
			ScContext.getInstance().setCurr_groupName(t1);
		}
		String t2 = appPreferences.getString(
				"usercurrgroupid_" + curr_username, "");
		if (!t2.equals("")) {
			ScContext.getInstance().setCurr_group(t2);
		}
		// 从缓存中获取个人信息。
		String userinfo = appPreferences.getString("userinfo_" + curr_username,
				"");
		if (!(userinfo.equals(""))) {
			try {
				LoginInfo li = objectMapper
						.readValue(userinfo, LoginInfo.class);
				ScContext.getInstance().setUi(li);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

}
