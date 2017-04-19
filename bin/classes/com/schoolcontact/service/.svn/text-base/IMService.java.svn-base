package com.schoolcontact.service;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.ItemAttachment;
import com.schoolcontact.model.ItemNotification;
import com.schoolcontact.model.ListInfo;
import com.schoolcontact.model.NotificationBack;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.model.TokenInfo;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.StringUtils;

/**
 * @author Star
 * @description IM通信接口服务类
 */
public class IMService extends BaseService {

	private String url = baseUrl + EventList.imAjax;

	/**
	 * @description 刷新TOKEN
	 */
	public void refreshToken(final MessageCallback mc) {
		System.out.println(url);
		if (sccontext.getUi() != null) {
			AjaxParams params = new AjaxParams();
			params.put("actor", EventList.ACTOR_REFRESHTOKEN);
			params.put("event", EventList.EVENT_TOKEN);
			params.put("sid", sccontext.getUi().getSid());
			params.put("data", EventList.DATA_REFRESHTOKEN);
			System.out.println(FinalHttp.getUrlWithQueryString(url, params));

			fh.get(url, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {

					mc.dealMessage(new ReturnMessage("error", "请求超时！",
							EventList.REFRESHTOKEN, null));
				}

				@Override
				public void onSuccess(String t) {
					try {
						Log.v("刷新TOKEN请求", t);
						TokenInfo lf = objectMapper.readValue(t,
								TokenInfo.class);
						if (TextUtils.isEmpty(lf.getToken()))
							;
						{
							mc.dealMessage(new ReturnMessage("0", "刷新成功",
									EventList.REFRESHTOKEN, lf));
						}
						mc.dealMessage(new ReturnMessage("1", "刷新失败",
								EventList.REFRESHTOKEN, null));

					} catch (Exception e) {
						System.out.println("登录前请求解析异常");
						e.printStackTrace();
					}
				}

			});
		}

	}

	/**
	 * @param mc
	 *            主线程回调接口
	 * @param isAuto
	 *            是否手动请求（下拉刷新）
	 * @description 获取好友关系
	 */
	public void getGroup(final MessageCallback mc, boolean isAuto) {
		String cache = sccontext.getmPreferences().getString(
				"userlist_" + sccontext.getCurr_user(), "");
		if (cache.equals("") || isAuto) {
			if (sccontext.getUi() != null) {
				System.out.println(url);
				AjaxParams params = new AjaxParams();
				params.put("actor", EventList.ACTOR_IM);
				params.put("event", EventList.EVENT_GROUP);
				params.put("sid", sccontext.getUi().getSid());
				params.put("data", EventList.DATA_GETCONTACTS);
				params.put("userid", sccontext.getUi().getUsertradeid());
				System.out
						.println(FinalHttp.getUrlWithQueryString(url, params));
				fh.get(url, params, new AjaxCallBack<String>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						mc.dealMessage(new ReturnMessage("error", "请求超时！",
								EventList.GETGROUP, null));
					}

					@Override
					public void onSuccess(String t) {
						try {
							Log.v("网络获取好友信息", t);
							ListInfo li = objectMapper.readValue(t,
									ListInfo.class);
							System.out.println(li.getResults());
							if (li.isScuess(li) && li.getResults() != null
									&& li.getResults().size() != 0) {
								sccontext
										.getmPreferences()
										.edit()
										.putString(
												"userlist_"
														+ sccontext
																.getCurr_user(),
												StringUtils.enCorder(t))
										.commit();
								sccontext.setUserProvider(li);
							}

							ScContext.getInstance().setListinfo(li);
							ScContext.getInstance().noifyView();
							mc.dealMessage(new ReturnMessage(li.getCode(), li
									.getMess(), EventList.GETGROUP, li));
						} catch (Exception e) {
							System.out.println("解析好友信息出现异常");
							e.printStackTrace();
						}
					}

				});
			}
		} else {
			String t = StringUtils.deCorder(cache);
			ListInfo li;
			System.out.println("获取好友缓存成功\n" + t);
			try {
				li = objectMapper.readValue(t, ListInfo.class);
				ScContext.getInstance().setListinfo(li);
				ScContext.getInstance().noifyView();
				mc.dealMessage(new ReturnMessage(EventList.SCUESS, "获取缓存成功",
						EventList.GETGROUP, li));
			} catch (Exception e) {
				System.out.println("获取缓存解析错误");
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param mc
	 *            主线程回调接口
	 * @description 获取消息通知校班组目录
	 */
	public void getNotificationList(final MessageCallback mc, boolean isAuto) {
		String cache = sccontext.getmPreferences().getString(
				"notification_list_" + sccontext.getCurr_user(), "");
		if (cache.equals("") || isAuto) {
			System.out.println(url);
			if(sccontext.getUi()!=null)
			{
			AjaxParams params = new AjaxParams();
			params.put("actor", EventList.ACTOR_IM);
			params.put("event", EventList.EVENT_NOTIFICATIONLIST);
			params.put("sid", sccontext.getUi().getSid());
			params.put("data", EventList.DATA_NOTIFICATIONLIST);
			params.put("userid", sccontext.getUi().getUsertradeid());
			System.out.println(FinalHttp.getUrlWithQueryString(url, params));
			fh.get(url, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					mc.dealMessage(new ReturnMessage(strMsg, strMsg,
							EventList.GETNOTIFICATIONLIST, null));
				}

				@Override
				public void onSuccess(String t) {
					try {
						Log.v("网络获取好友信息", t);
						ListInfo li = objectMapper.readValue(t, ListInfo.class);
						// System.out.println(li.getResults());
						if (li.isScuess(li) && li.getResults() != null
								&& li.getResults().size() != 0) {
							sccontext
									.getmPreferences()
									.edit()
									.putString(
											"notification_list_"
													+ sccontext.getCurr_user(),
											StringUtils.enCorder(t)).commit();
							// sccontext.setUserProvider(li);
						}

						ScContext.getInstance().setNotificationListinfo(li);
						ScContext.getInstance().noifyNotificationView();
						mc.dealMessage(new ReturnMessage(li.getCode(), li
								.getMess(), EventList.GETNOTIFICATIONLIST, li));
					} catch (Exception e) {
						System.out.println("解析通知列表出现异常");
						e.printStackTrace();
					}
				}

			});
			}
		} else {
			String t = StringUtils.deCorder(cache);
			ListInfo li;
			System.out.println("获取通知列表缓存成功\n" + t);
			try {
				li = objectMapper.readValue(t, ListInfo.class);
				ScContext.getInstance().setNotificationListinfo(li);
				ScContext.getInstance().noifyNotificationView();
				mc.dealMessage(new ReturnMessage(EventList.SCUESS, "获取缓存成功",
						EventList.GETNOTIFICATIONLIST, li));
			} catch (Exception e) {
				System.out.println("获取通知列表缓存解析错误");
				e.printStackTrace();
			}

		}
	}

	/**
	 * @description 发送通知
	 */
	public void notificationSend(final MessageCallback mc, String sort,
			String content, ItemNotification itemNotification,
			List<ItemAttachment> attachmentList) {
		System.out.println(url);
		AjaxParams params = new AjaxParams();
		if(sccontext.getUi()!=null)
		{
		params.put("actor", EventList.ACTOR_IM);
		params.put("event", EventList.EVENT_NOTIFICATIONSEND);
		params.put("sid", sccontext.getUi().getSid());
		params.put("data", EventList.DATA_NOTIFICATIONSEND);
		params.put("userid", sccontext.getUi().getUsertradeid());
		params.put("message_type", sort);
		params.put("content", content);
		params.put("class_id", itemNotification.getClassId());
		params.put("group_id", itemNotification.getGroupId());
		params.put("tonames", itemNotification.toString());
		String attachmentListJson = "";
		try {
			attachmentListJson = objectMapper
					.writeValueAsString(attachmentList);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		params.put("attachments", attachmentListJson);

		fh.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				System.out.println("通知发送失败");
			}

			@Override
			public void onSuccess(String t) {
				try {
					NotificationBack nb = objectMapper.readValue(t,
							NotificationBack.class);
					mc.dealMessage(new ReturnMessage(nb.getCode(), nb
							.getResults(), EventList.NOTIFICATIONSEND, null));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		}
	}

}
