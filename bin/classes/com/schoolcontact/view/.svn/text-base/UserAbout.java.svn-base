package com.schoolcontact.view;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.service.CommonService;

public class UserAbout extends BaseActivity {

	
	
	
	CommonService cs = new CommonService();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;

		case R.id.rl_feedback:
			Intent il = new Intent(this,FeedBackActivity.class);
			this.startActivity(il);
			break;
			
		case R.id.rl_use:
			Intent useIntent = new Intent(this,UseActivity.class);
			this.startActivity(useIntent);
			break;
			
		case R.id.rl_help:
			Intent helpIntent = new Intent(this,HelpActivity.class);
			this.startActivity(helpIntent);
			break;
		case R.id.rl_update:
			Intent helpIntent1 = new Intent(this,HelpActivity.class);
			this.startActivity(helpIntent1);
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		onInit();
	}

	@Override
	public void onInit() {
		
		((RelativeLayout) findViewById(R.id.rl_feedback)).setOnClickListener(this);
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.rl_use)).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.rl_help)).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.rl_update)).setOnClickListener(this);
	}
	
	
 

}
