package com.schoolcontact.view;


import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.utils.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity implements MessageCallback,
		OnClickListener, OnCheckedChangeListener, OnItemClickListener {

	protected LoadingDialog mDialog;
	protected final ScContext sccontext = ScContext.getInstance();

	public abstract void onInit();

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void dealMessage(ReturnMessage rm) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
