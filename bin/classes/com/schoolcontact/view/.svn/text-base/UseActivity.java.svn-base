package com.schoolcontact.view;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.school_contact.main.R;

public class UseActivity extends BaseActivity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use);
		onInit();
	}

	@Override
	public void onInit() {
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);
		WebView webView = (WebView) findViewById(R.id.use_webview);
		webView.loadUrl("http://jxt.syhcinfo.com/public/html/terms.html");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backtext:
			finish();

		default:
			break;
		}
	}

}
