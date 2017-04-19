package com.schoolcontact.service;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.text.TextUtils;
import android.util.Log;

import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.model.AttachmentInfo;
import com.schoolcontact.model.CheckNum;
import com.schoolcontact.model.ImageInfo;
import com.schoolcontact.model.LogOut;
import com.schoolcontact.model.LoginBefore;
import com.schoolcontact.model.LoginInfo;
import com.schoolcontact.model.ModifyPassWord;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.model.SaveNewPwd;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.StringUtils;

/**
 * @author Star
 * @description 用户服务类（含与业务无关的用户操作）
 */
public class UserService extends BaseService {

	private String url = baseUrl + EventList.userAjax;

	private String imgurl = baseUrl + "common/upload/avatarHandler.jsp";

	private String attachmenturl = baseUrl + "common/upload/uploadAttachmentHandler.jsp";
	
	/**
	 * @throws Exception
	 * @description 获取SID 以及SALT
	 */
	public void getLogin(final String username, final String password,
			final MessageCallback mc) {
		System.out.println(url);
		Log.i("登录账号密码", username + "       " + password);
		//HTTP请求参数封装
		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_USER);
		params.put("event", EventList.EVENT_BEFORELOGIN);
		System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.BEFORELOGIN, null));
			}

			@Override
			public void onSuccess(String t) {
				try {
					Log.v("登录前请求", t);
					LoginBefore lf = objectMapper.readValue(t,
							LoginBefore.class);

					login(username, password, mc, lf.getSid());

				} catch (Exception e) {
					System.out.println("登录前请求解析异常");
					e.printStackTrace();
				}
			}

		});

	}

	public void uploadImg(final File img, final MessageCallback mc) {
		if(sccontext.getUi()!=null)
		{
		FinalHttp fl = new FinalHttp();
		AjaxParams params = new AjaxParams();
		try {
			params.put("uid", sccontext.getUi().getUsertradeid());
			params.put("media", img);
		} catch (FileNotFoundException e1) {
			System.out.println("头像图片文件未找到");
			e1.printStackTrace();
		}

		Log.i("上传图片URL", FinalHttp.getUrlWithQueryString(imgurl, params));
		// fl.addHeader("Content-Type", "");
		fl.post(imgurl, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.UPLOADIMG, null));
			}

			@Override
			public void onSuccess(String t) {
				Log.i("上传头像请求", t);
				try {
					ImageInfo li = objectMapper.readValue(t, ImageInfo.class);
					if (li.isScuess(li)) {
						String url = baseUrl + "static/avatar/"
								+ sccontext.getUi().getUsertradeid()
								+ "/middle/" + li.getMediaid();
					//	System.out.println(url);
						// li.setMediaid(url);
						sccontext.getUi().setIsportraituri(url);

					}
					mc.dealMessage(new ReturnMessage(li.getCode(),
							li.getMess(), EventList.UPLOADIMG, null));
					// System.out.println();
				} catch (Exception e) {
					// System.out.println("上传头像请求解析出现异常");
					e.printStackTrace();
				}
			}

		});
		}
	}

	// 上传通知的图片
		public void uploadMoodImg(final File file,final Map<String, Object> map,final MessageCallback mc ) {
			FinalHttp fl = new FinalHttp();
			AjaxParams params = new AjaxParams();

			if(sccontext.getUi()!=null)
			{
			params.put("domain", sccontext.getUi().getUser_id());
			params.put("app_id", "easm30");
			params.put("attach_id", "k12notice");
			try {
				params.put("file", file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			fl.post(attachmenturl, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					map.put("FinshText", "上传失败");
					mc.dealMessage(new ReturnMessage("" + errorNo, strMsg,
							EventList.ADDATTACHMENT, null));
				}

				@Override
				public void onSuccess(String t) {
					try {
						AttachmentInfo ai = objectMapper.readValue(t,
								AttachmentInfo.class);
						if (ai.isScuess(ai)) {
							map.put("itemText", ai.getFile_url());
							map.put("FinshText", "上传完毕");
							mc.dealMessage(new ReturnMessage(ai.getCode(), ai
									.getMess(), EventList.ADDATTACHMENT, ai));
						}
						
						// System.out.println();
					} catch (Exception e) {
						// System.out.println("上传头像请求解析出现异常");
						e.printStackTrace();
					}
				}

			});
			}
		}
	// 上传通知的图片
	public void uploadNotificationImg(final File file, final MessageCallback mc) {
		FinalHttp fl = new FinalHttp();
		AjaxParams params = new AjaxParams();

		if(sccontext.getUi()!=null)
		{
		params.put("domain", sccontext.getUi().getUser_id());
		params.put("app_id", "easm30");
		params.put("attach_id", "k12notice");
		try {
			params.put("file", file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		fl.post(attachmenturl, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("" + errorNo, strMsg,
						EventList.ADDATTACHMENT, null));
			}

			@Override
			public void onSuccess(String t) {
				try {
					AttachmentInfo ai = objectMapper.readValue(t,
							AttachmentInfo.class);
					if (ai.isScuess(ai)) {
						mc.dealMessage(new ReturnMessage(ai.getCode(), ai
								.getMess(), EventList.ADDATTACHMENT, ai));
					}
					/*
					 * ImageInfo li = objectMapper.readValue(t,
					 * ImageInfo.class); if (li.isScuess(li)) { String url =
					 * baseUrl + "static/avatar/" +
					 * sccontext.getUi().getUsertradeid() + "/middle/" +
					 * li.getMediaid(); System.out.println(url);
					 * li.setMediaid(url); mc.dealMessage(new
					 * ReturnMessage(li.getCode(), li .getMess(),
					 * EventList.UPLOADIMG, li)); } else { mc.dealMessage(new
					 * ReturnMessage(li.getCode(), li .getMess(),
					 * EventList.UPLOADIMG, null)); }
					 */
					// System.out.println();
				} catch (Exception e) {
					// System.out.println("上传头像请求解析出现异常");
					e.printStackTrace();
				}
			}

		});
		}
	}

	/**
	 * @param phonenum
	 *            手机号码
	 * @param mc
	 *            回调接口
	 * @description 获取验证码
	 */
	public void forgetCheckNum(final String phonenum, final MessageCallback mc) {
		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_USER);
		params.put("event", EventList.EVENT_GETCHECKNUM);
		params.put("mobile", phonenum);
		System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.GETCHECKNUM, null));
			}

			@Override
			public void onSuccess(String t) {
				Log.i("获取验证码请求", t);
				try {
					final CheckNum li = objectMapper.readValue(t,
							CheckNum.class);
					mc.dealMessage(new ReturnMessage(li.getCode(),
							li.getMess(), EventList.GETCHECKNUM, null));
					// System.out.println();
				} catch (Exception e) {
					System.out.println("获取验证码请求解析出现异常");
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * @param phonenum
	 *            手机号码
	 * @param vcode
	 *            验证码
	 * @param newpassword
	 *            新密码
	 * @param mc
	 *            回调接口
	 * @description 手机重置密码
	 */
	public void saveNewPwd(final String phonenum, final String vcode,
			final String newpassword, final MessageCallback mc) {
		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_USER);
		params.put("event", EventList.EVENT_SAVENEWPWD);
		params.put("mobile", phonenum);
		params.put("vcode", vcode);
		params.put("password", newpassword);
		System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.SAVENEWPWD, null));
			}

			@Override
			public void onSuccess(String t) {
				Log.i("保存新密码请求", t);
				try {
					final SaveNewPwd li = objectMapper.readValue(t,
							SaveNewPwd.class);
					mc.dealMessage(new ReturnMessage(li.getCode(),
							li.getMess(), EventList.SAVENEWPWD, null));
				} catch (Exception e) {
					System.out.println("保存新密码请求解析出现异常");
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * @param username
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @param mc
	 *            回调主线程方法
	 * @param sid
	 * @description 完成登录操作 返回数据给主线程
	 */
	public void login(final String username, final String password,
			final MessageCallback mc, String sid) {

		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_USER);
		params.put("event", EventList.EVENT_LOGIN);
		params.put("sid", sid);
		params.put("username", username);
		params.put("password", password);
		params.put("device", EventList.ANDROID);
	//	System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.LOGIN, null));
			}

			@Override
			public void onSuccess(String t) {
				Log.i("登录请求", t);
				try {
					final LoginInfo li = objectMapper.readValue(t,
							LoginInfo.class);
					if (li.isScuess(li)) {
						if (!TextUtils.isEmpty(li.getIstoken())) {

							saveUser(username, password, t, li);
						//	System.out.println("In UserService  "+Thread.currentThread().getName());
							
							
							mc.dealMessage(new ReturnMessage(li.getCode(), li
									.getMess(), EventList.LOGIN, null));
							connectRC(li, mc, li.getIstoken());
							
						}
					} else {
						mc.dealMessage(new ReturnMessage(li.getCode(), li
								.getMess(), EventList.LOGIN, null));
					}
				} catch (Exception e) {
					System.out.println("登录请求解析出现异常");
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * @param oldPassword
	 *            用户输入旧密码
	 * @param newPassword
	 *            用户输入新密码
	 * @param mc
	 *            回调主线程方法
	 * @description 修改密码 （返回 正确或者错误信息）
	 */
	public void modifyPassword(String oldPassword, final String newPassword,
			final MessageCallback mc) {
		if(sccontext.getUi()!=null)
		{
		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_USER);
		params.put("event", EventList.EVENT_MODIFYPASSWORD);
		params.put("sid", sccontext.getUi().getSid());
		params.put("oldpwd", oldPassword);
		params.put("newpwd", newPassword);
		params.put("userid", sccontext.getUi().getUser_id());
		System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// super.onFailure(t, errorNo, strMsg);
				mc.dealMessage(new ReturnMessage("error", "请求超时！",
						EventList.MODIFYPASSWORD, null));
			}

			@Override
			public void onSuccess(String t) {
				Log.i("修改密码请求", t);
				try {
					final ModifyPassWord mp = objectMapper.readValue(t,
							ModifyPassWord.class);
					if (mp.isScuess(mp)) {
						sccontext.getmPreferences().edit()
								.putString("lastuserpwd", newPassword);
					}
					mc.dealMessage(new ReturnMessage(mp.getCode(),
							mp.getMess(), EventList.MODIFYPASSWORD, null));
				} catch (Exception e) {
					System.out.println("修改密码解析出现异常");
					e.printStackTrace();
				}
			}

		});
		}
	}

	/**
	 * @param mc
	 *            回调主程序方法
	 * @description 登出请求 （返回 正确或者错误信息）
	 */
	public void logOut(final MessageCallback mc) {
		if (sccontext.getUi() != null) {
			AjaxParams params = new AjaxParams();
			params.put("actor", EventList.ACTOR_USER);
			params.put("userid", sccontext.getUi().getUser_id());
			params.put("event", EventList.EVENT_LOGOUT);
			params.put("sid", sccontext.getUi().getSid());
			params.put("device", EventList.ANDROID);
			System.out.println(FinalHttp.getUrlWithQueryString(url, params));
			fh.get(url, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					mc.dealMessage(new ReturnMessage("error", "请求超时!",
							EventList.LOGOUT, null));
				}

				@Override
				public void onSuccess(String t) {
					Log.i("登出请求", t);
					try {
						final LogOut lo = objectMapper.readValue(t,
								LogOut.class);
						/*
						 * if (lo.isScuess(lo)) { clear(); }
						 */
						clear();
						mc.dealMessage(new ReturnMessage(lo.getCode(), lo
								.getMess(), EventList.LOGOUT, null));

					} catch (Exception e) {
						System.out.println("登出请求解析出现异常");
						e.printStackTrace();
					}
				}

			});
		}
	}

	/**
	 * @param username
	 * @param pwd
	 * @param t
	 * @param li
	 * @description 保存用户信息到运行内存和手机内存
	 */
	public void saveUser(String username, String pwd, String t, LoginInfo li) {
		System.out.println("开始保存账号");

		sccontext.setUi(li);
		// 保存最后登录用户信息
		sccontext.getmPreferences().edit().putString("lastusername", username)
				.commit();
		sccontext.getmPreferences().edit().putString("lastuserpwd", pwd)
				.commit();

		sccontext.getmPreferences().edit().putBoolean("isauto", true).commit();

		sccontext.setCurr_user(username);
		// 保存当前用户
		sccontext.getmPreferences().edit().putString("userinfo_" + username, t)
				.commit();
		sccontext.getmPreferences().edit()
				.putString("username_" + username, username).commit();
		sccontext.getmPreferences().edit()
				.putString("pwd_" + username, StringUtils.enCorder(pwd))
				.commit();
	}

	public void connectRC(final LoginInfo li, final MessageCallback mc,
			String token) {
		 String token1 =
		 "hH4vOxtHo3c/Y9rkRfOm1hdjky/cxuZUwYrlmooJjGqI5+kaLd18rPv6V0YnrAq7FAeGWkKQMrieJqymfMsjpg==";

		if (RongIM.getInstance() == null) {
		}
		// 连接融云服务器。
		try {
			RongIM.connect(token1, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {

					System.out.println("Login onTokenIncorrect.");
				}

				@Override
				public void onError(ErrorCode arg0) {

					if (mc != null) {
						mc.dealMessage(new ReturnMessage("error", "请求超时！",
								EventList.SERVERLOGIN, null));
					}
					System.out.println("Login onError.");
				}

				@Override
				public void onSuccess(String arg0) {

					if (mc != null) {
					mc.dealMessage(new ReturnMessage(li.getCode(),
							li.getMess(), EventList.SERVERLOGIN, null));

					}
					Log.v("Connect:", "Login successfully.");

				}

			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clear() {
		sccontext.clear();
		sccontext.getmPreferences().edit().putBoolean("isauto", false).commit();
		RongIM.getInstance().logout();

	}

}
