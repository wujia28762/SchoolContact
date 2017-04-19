package com.schoolcontact.Base;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.schoolcontact.model.ClassInfo;
import com.schoolcontact.model.CustomContent;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.GroupInfo;
import com.schoolcontact.model.ListInfo;
import com.schoolcontact.model.LoginInfo;
import com.schoolcontact.model.RoleGropuInfo;
import com.schoolcontact.model.SchoolInfo;
import com.schoolcontact.model.UserListInfo;

public class ScContext {
	// private static final String TAG = Sc_Context.class.getSimpleName();
	// 保存全局sid
	public void clear() {
		nl = null;
		listinfo = new ListInfo();
		ui = null;
		mCustomContent=null;
		mGroupInfos = null;
		Curr_group = null;
		Curr_groupName=null;
		mgroupFriendMessageInfo = null;
		myFriendMessageInfo = null;
		Curr_user = null;
		// userinfos = null;
	}
	private boolean newAboutMe;
	private boolean newMessage;
	private NotifyListener nl;
	private List<CustomContent> mCustomContent;

	private List<GroupInfo> mGroupInfos;
	private List<FriendMessageInfo> mgroupFriendMessageInfo;
	private List<FriendMessageInfo> myFriendMessageInfo;
	
	//教师登录时，当前圈和历史圈分别向服务器要
	private List<GroupInfo> techCurrGroupInfos;
	private List<GroupInfo> techHisGroupInfos;
	private int currentGroupNo;//当前班级在朋友圈的编号
	
	private String Curr_group;
	private String Curr_groupName;
	
	
	private String Curr_user;
	private SharedPreferences mPreferences;

	private ListInfo listinfo;
	private ListInfo notificationListinfo;

	// private List<UserInfo> userinfos;

	private LoginInfo ui;
	//要显示的朋友圈groupid,groupname
	private String displayGroupId;
	private String displayGroupName;

	public String getDisplayGroupName() {
		return displayGroupName;
	}

	public void setDisplayGroupName(String displayGroupName) {
		this.displayGroupName = displayGroupName;
	}

	public String getDisplayGroupId() {
		return displayGroupId;
	}

	public void setDisplayGroupId(String displayGroupId) {
		this.displayGroupId = displayGroupId;
	}

	public List<GroupInfo> getTechCurrGroupInfos() {
		return techCurrGroupInfos;
	}

	public void setTechCurrGroupInfos(List<GroupInfo> techCurrGroupInfos) {
		this.techCurrGroupInfos = techCurrGroupInfos;
	}

	public List<GroupInfo> getTechHisGroupInfos() {
		return techHisGroupInfos;
	}

	public void setTechHisGroupInfos(List<GroupInfo> techHisGroupInfos) {
		this.techHisGroupInfos = techHisGroupInfos;
	}

	public int getCurrentGroupNo() {
		return currentGroupNo;
	}

	public void setCurrentGroupNo(int currentGroupNo) {
		this.currentGroupNo = currentGroupNo;
	}

	public List<GroupInfo> getmGroupInfos() {
		return mGroupInfos;
	}

	public void setmGroupInfos(List<GroupInfo> mGroupInfos) {
		this.mGroupInfos = mGroupInfos;
	}



	public String getCurr_group() {
		return Curr_group;
	}

	public void setCurr_group(String curr_group) {
		Curr_group = curr_group;
	}



	public ListInfo getNotificationListinfo() {
		return notificationListinfo;
	}

	public void setNotificationListinfo(ListInfo notificationListinfo) {
		this.notificationListinfo = notificationListinfo;
	}

	public NotifyListener getNl() {
		return nl;
	}

	public void setNl(NotifyListener nl) {
		this.nl = nl;
	}

	public ListInfo getListinfo() {
		return listinfo;
	}

	// public List<UserInfo> getUserinfos() {
	// return userinfos;
	// }
	//
	// public void setUserinfos(List<UserInfo> userinfos) {
	// this.userinfos = userinfos;
	// }

	public void setListinfo(ListInfo listinfo) {
		this.listinfo = listinfo;
	}

	public LoginInfo getUi() {
		return ui;
	}

	public void setUi(LoginInfo ui) {
		this.ui = ui;
	}

	// 单例模式，私有构造方法
	private ScContext() {

	}

	// 内部类生成实例，线程安全
	private static class ScContextFactory {
		private static ScContext instance = new ScContext();
	}

	public SharedPreferences getmPreferences() {
		return mPreferences;
	}

	public void setmPreferences(SharedPreferences mPreferences) {
		this.mPreferences = mPreferences;
	}

	// 返回单例方法
	public static ScContext getInstance() {
		return ScContextFactory.instance;
	}

	public void noifyView() {
		// Log.v("进入通知更改数据集", nl.toString());
		if (!(nl == null)) {

			Log.v("网络获取好友关系完毕", nl.toString());
			nl.notifyDataChanged();

		}

	}

	public void noifyNotificationView() {
		// Log.v("进入通知更改通知列表集", nl.toString());
		if (!(nl == null)) {

			Log.v("网络获取通知列表完毕", nl.toString());
			nl.notifyDataChanged();

		}

	}

	public void registerDataListener(NotifyListener lis) {
		nl = lis;
	}



	public String getCurr_user() {
		return Curr_user;
	}

	public void setCurr_user(String curr_user) {
		Curr_user = curr_user;
	}

	public void setUserProvider(ListInfo li) {
		final List<UserInfo> userlistinfoApp = new ArrayList<UserInfo>();
		try {

			for (SchoolInfo schoolinfo : li.getResults()) {

				userlistinfoApp
						.add(new UserInfo(li.getNotices()
								.get(schoolinfo.getId()).getUserId(), li
								.getNotices().get(schoolinfo.getId())
								.getUserName(), Uri.parse(li.getNotices()
								.get(schoolinfo.getId()).getPortraitUri())));
				for (ClassInfo classinfo : schoolinfo.getContents()) {
					for (RoleGropuInfo rolegropuinfo : classinfo.getContents()) {
						for (UserListInfo userlistinfo : rolegropuinfo
								.getContents()) {
							userlistinfoApp.add(new UserInfo(userlistinfo
									.getUserId(), userlistinfo.getUserName(),
									Uri.parse(userlistinfo.getPortraitUri())));
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("获取缓存失败");
			e.printStackTrace();
		}

		// 将好友信息载入缓存以供通讯录显示
		// ScContext.getInstance().setUserinfos(userlistinfoApp);

		// 融云好友列表关系提供。
		RongIM.setUserInfoProvider(new UserInfoProvider() {

			@Override
			public UserInfo getUserInfo(String arg0) {
				for (UserInfo userinfo : userlistinfoApp) {
					if (userinfo.getUserId().equals(arg0)) {
						return userinfo;
					}
				}
				return null;
			}
		}, true);

	}

	public String getCurr_groupName() {
		return Curr_groupName;
	}

	public void setCurr_groupName(String curr_groupName) {
		Curr_groupName = curr_groupName;
	}

	public List<FriendMessageInfo> getMgroupFriendMessageInfo() {
		return mgroupFriendMessageInfo;
	}

	public void setMgroupFriendMessageInfo(List<FriendMessageInfo> mgroupFriendMessageInfo) {
		this.mgroupFriendMessageInfo = mgroupFriendMessageInfo;
	}

	public List<FriendMessageInfo> getMyFriendMessageInfo() {
		return myFriendMessageInfo;
	}

	public void setMyFriendMessageInfo(List<FriendMessageInfo> myFriendMessageInfo) {
		this.myFriendMessageInfo = myFriendMessageInfo;
	}

	public List<CustomContent> getmCustomContent() {
		return mCustomContent;
	}

	public void setmCustomContent(List<CustomContent> mCustomContent) {
		this.mCustomContent = mCustomContent;
	}

	public  boolean isNewAboutMe() {
		return newAboutMe;
	}

	public synchronized void setNewAboutMe(boolean newAboutMe) {
		this.newAboutMe = newAboutMe;
	}

	public boolean isNewMessage() {
		return newMessage;
	}

	public synchronized void setNewMessage(boolean newMessage) {
		this.newMessage = newMessage;
	}



}
