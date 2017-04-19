package com.schoolcontact.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.school_contact.main.R;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.StringUtils;
import com.tencent.android.tpush.XGPushManager;

/**
 * @author Star
 * @description 欢迎动画页面。 对应页面--
 */
public class StartAnim extends BaseActivity {

	private static final int LOAD_DISPLAY_TIME = 1000;
	private boolean islogin = false;
	private String fl_username;
	private String fl_pwd;
	private boolean isAuto = false;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.startanim);
		onInit();
	}

	@Override
	protected void onResume() {

		super.onResume();
		fl_username = sccontext.getmPreferences().getString("lastusername", "");
		fl_pwd = StringUtils.deCorder(sccontext.getmPreferences().getString(
				"lastuserpwd", ""));
		isAuto = sccontext.getmPreferences().getBoolean("isauto", false);
		if ((!(fl_username.equals("")) && !(fl_pwd.equals("")) && isAuto)) {
			dealMessage(new ReturnMessage("0", "", EventList.LOGIN, null));
		} else {
			dealMessage(new ReturnMessage("1", "", EventList.LOGIN, null));
		}
		

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.LOGIN:
			if (rm.getCode().equals(EventList.SCUESS)) {
				islogin = true;
			}
			new Handler().postDelayed(new Runnable() {
				public void run() {

					Intent mainIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("username", fl_username);
					bundle.putString("pwd", fl_pwd);
					mainIntent.putExtras(bundle);
					if (islogin) {
						mainIntent.setClass(StartAnim.this, HomeFragment.class);
					} else {
						mainIntent.setClass(StartAnim.this, LoginActivity.class);
					}
					startActivity(mainIntent);
					finish();
					overridePendingTransition(R.anim.push_in, R.anim.push_out);
				}
			}, LOAD_DISPLAY_TIME);

			break;

		default:
			break;
		}
	}

	@Override
	public void onInit() {
		// 开启logcat输出，方便debug，发布时请关闭
				// XGPushConfig.enableDebug(this, true);
				// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
				// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
				// 具体可参考详细的开发指南
				// 传递的参数为ApplicationContext
//				Context context = getApplicationContext();
//				XGPushManager.registerPush(context);	

				// 2.36（不包括）之前的版本需要调用以下2行代码
//				Intent service = new Intent(context, XGPushService.class);
//				context.startService(service);


				// 其它常用的API：
				// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
				// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
				// 反注册（不再接收消息）：unregisterPush(context)
				// 设置标签：setTag(context, tagName)
				// 删除标签：deleteTag(context, tagName)
	}
}
