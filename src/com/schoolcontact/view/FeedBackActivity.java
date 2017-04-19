package com.schoolcontact.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.CommonService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.LoadingDialog;

public class FeedBackActivity extends BaseActivity implements TextWatcher {

	private CommonService cs = new CommonService();
	private final int total = EventList.TOTALNUM;
	private int res_length = total;
	private EditText add_content;
	private TextView restext;

	// private TextView submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback);
		onInit();
	}

	@Override
	public void onInit() {

		mDialog = new LoadingDialog(this);
		mDialog.setText(R.string.doing);
		add_content = (EditText) findViewById(R.id.et_add_content);
		add_content.addTextChangedListener(this);
		restext = (TextView) findViewById(R.id.tv_restext);
		findViewById(R.id.tv_submit).setOnClickListener(this);
		findViewById(R.id.backtext).setOnClickListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

		restext.setText("还可以输入" + res_length + "个字：");
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (res_length > 0) {
			res_length = total - add_content.getText().length();
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		restext.setText("还可以输入" + res_length + "个字：");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			if (add_content.getText().length() != 0 && res_length >= 0) {
				mDialog.show();
				//System.out.println("!!!!!!!!@#!@#"+add_content.getText().toString());
				cs.sendFeedback(add_content.getText().toString(), this);
			} else {
				Toast.makeText(this,
						"请确认字数是否在" + EventList.TOTALNUM + "字以内,或内容不为空",
						Toast.LENGTH_SHORT).show();
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
		case EventList.FEEDBACK:
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
			finish();
			break;

		default:
			break;
		}
	}

}
