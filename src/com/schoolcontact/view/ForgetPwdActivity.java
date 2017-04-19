package com.schoolcontact.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.StringUtils;

public class ForgetPwdActivity extends BaseActivity {

	private UserService us = new UserService();
	private EditText et_phonenum;
	private EditText et_checknumber;
	private Button bt_getchecknum;
	private EditText et_newpwd;
	private EditText et_agpwd;
	// private Button bt_submit;
	private TimeCount time;
	private String phonenum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgetpwd);
		onInit();
	}

	@Override
	public void onInit() {

		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		et_checknumber = (EditText) findViewById(R.id.et_checknumber);

		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		bt_getchecknum = (Button) findViewById(R.id.bt_getchecknum);
		bt_getchecknum.setOnClickListener(this);
		et_newpwd = (EditText) findViewById(R.id.et_newpwd);
		et_agpwd = (EditText) findViewById(R.id.et_agpwd);
		findViewById(R.id.bt_submit).setOnClickListener(this);
		findViewById(R.id.backtext).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_getchecknum:
			time.start();
			getCheckNum();
			break;
		case R.id.bt_submit:
			saveNewPwd();
			break;
		case R.id.backtext:
			finish();
			break;
		default:
			break;
		}
	}

	private void saveNewPwd() {

		String checknumber = et_checknumber.getText().toString();
		String newpwd = et_newpwd.getText().toString();
		String agpwd = et_agpwd.getText().toString();
		if (TextUtils.isEmpty(phonenum) || TextUtils.isEmpty(checknumber)
				|| TextUtils.isEmpty(newpwd) || TextUtils.isEmpty(agpwd)) {
			Toast.makeText(this, "请填写完整信息", Toast.LENGTH_LONG).show();
			return;
		}
		if (newpwd.equals(agpwd)) {
			us.saveNewPwd(phonenum, checknumber, newpwd, this);
		} else {
			Toast.makeText(this, "请确认输入新密码是否相同", Toast.LENGTH_LONG).show();
		}

	}

	private void getCheckNum() {
		phonenum = et_phonenum.getText().toString();
		if (TextUtils.isEmpty(phonenum)) {
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtils.isMobile(phonenum)) {
			us.forgetCheckNum(phonenum, this);
		} else {
			Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.GETCHECKNUM:
			if (rm.getCode().equals(EventList.SCUESS)) {
				Toast.makeText(this, "获取验证码成功，请关注手机信息！", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "获取验证码失败！", Toast.LENGTH_LONG).show();
			}

			break;
		case EventList.SAVENEWPWD:
			if (rm.getCode().equals(EventList.SCUESS)) {
				Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			bt_getchecknum.setText("重新验证");
			bt_getchecknum.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			bt_getchecknum.setClickable(false);
			bt_getchecknum.setText(millisUntilFinished / 1000 + "秒");
		}
	}

}
