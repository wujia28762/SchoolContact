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
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.LoadingDialog;

public class ModifyPwdActivity extends BaseActivity {

	private EditText oldpwd;
	private EditText newpwd;
	private EditText agpwd;
	private UserService cs = new UserService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypwd);
		onInit();
	}

	@Override
	public void onInit() {

		oldpwd = (EditText) findViewById(R.id.et_oldpwd);
		newpwd = (EditText) findViewById(R.id.et_newpwd);
		agpwd = (EditText) findViewById(R.id.et_agpwd);
		mDialog = new LoadingDialog(this);
		mDialog.setText(R.string.doing);
		((Button) findViewById(R.id.bt_submit)).setOnClickListener(this);

		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_submit:
			String old = oldpwd.getText().toString();
			String newp = newpwd.getText().toString();
			String apwd = agpwd.getText().toString();
			if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newp)
					|| TextUtils.isEmpty(apwd)) {
				Toast.makeText(this, "请完整填写修改信息", Toast.LENGTH_LONG).show();
				return;
			} else {
				if (apwd.equals(newp)) {
					onClickSubmit(old, apwd);
				} else {
					Toast.makeText(this, "请确认输入新密码是否相同", Toast.LENGTH_LONG)
							.show();
				}
			}

			break;
		case R.id.backtext:
			finish();
		default:
			break;
		}
	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.MODIFYPASSWORD:
			if(mDialog.isShowing())
			{
				mDialog.dismiss();
			}
			Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			// cs.logOut(this);
			break;
		case EventList.LOGOUT:
			Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			if (rm.getCode().equals(EventList.SCUESS)) {
				sccontext.clear();
				Intent mainIntent = new Intent(this, LoginActivity.class);

				this.startActivity(mainIntent);

				MoreFragment.instance.getActivity().finish();
				this.finish();
			}

			break;
		default:
			break;
		}
	}

	public void onClickSubmit(String old, String newp) {
		String storepwd = sccontext.getmPreferences().getString("lastuserpwd",
				"");
		if (storepwd.equals(old)) {
			mDialog.show();
			cs.modifyPassword(old, newp, this);
		} else {
			Toast.makeText(this, "原密码不正确？", Toast.LENGTH_LONG).show();
		}
	}

}
