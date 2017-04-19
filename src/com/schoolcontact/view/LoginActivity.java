package com.schoolcontact.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.IMService;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.LoadingDialog;

/**
 * @author Star
 * @description 用户登陆页面
 */
public class LoginActivity extends BaseActivity {

	private UserService cs = new UserService();
	private IMService is = new IMService();
	private EditText mUserNameEditText;
	private EditText mPasswordEditText;

	/**
	 * @param v
	 * @description 登录按钮点击事件
	 */
	public void onClicklogin() {

		String username = mUserNameEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
			if(mDialog!=null)
			mDialog.show();
			cs.getLogin(username, password, this);
			
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		onInit();
	}

	/**
	 * 
	 * @description Activity初始化
	 */
	public void onInit() {
		Button bt_login = (Button) findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);
		String username = this.getIntent().getStringExtra("username");
		String pwd = this.getIntent().getStringExtra("pwd");
		if (TextUtils.isEmpty(username)) {
			username = sccontext.getmPreferences()
					.getString("lastusername", "");
			pwd = sccontext.getmPreferences().getString("lastuserpwd", "");
		}
		mUserNameEditText = (EditText) findViewById(R.id.et_username);
		mUserNameEditText.setText(username);
		mPasswordEditText = (EditText) findViewById(R.id.et_password);
		mPasswordEditText.setText(pwd);
		mDialog = new LoadingDialog(this);

		mDialog.setText(R.string.logining);
		((TextView) findViewById(R.id.tv_forget)).setOnClickListener(this);

	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		
		switch (rm.getEvent()) {
		case EventList.SERVERLOGIN:
			if (rm.getCode().equals(EventList.SCUESS)) {
				
			} else {
				
				Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			}
			break;
		case EventList.GETGROUP:
//			if (rm.getCode().equals(EventList.SCUESS)) {
//				Intent it = new Intent();
//				it.setClass(this, HomeFragment.class);
//				startActivity(it);
//				finish();
//			} else {
//				Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
//			}
			break;

		case EventList.LOGIN:
			if (rm.getCode().equals(EventList.SCUESS)) {
				is.getGroup(this, true);
				if(ScContext.getInstance().getUi().getUser_group().equals("教师")){
					is.getNotificationList(this, true);
				}
				Intent it = new Intent();
				it.setClass(this, HomeFragment.class);
				if (mDialog.isShowing())
					mDialog.dismiss();
				startActivity(it);
				finish();
			} else {
				if (mDialog.isShowing())
					mDialog.dismiss();
				Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_login:
			onClicklogin();
			break;
		case R.id.tv_forget:
			onClickforget();
			break;
		default:
			break;
		}
	}

	private void onClickforget() {
		Intent it = new Intent();
		it.setClass(this, ForgetPwdActivity.class);
		startActivity(it);
		// finish();
	}

}
