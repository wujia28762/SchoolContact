package com.schoolcontact.view;

import io.rong.imkit.fragment.ConversationFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.school_contact.main.R;

public class ConversationActivity extends FragmentActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.conversation);
		onInit();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;

		default:
			break;
		}
	}

	private void onInit() {
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);

		String title = null;
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.talk_window);
		if (fragment instanceof ConversationFragment) {
			title = ((ConversationFragment) fragment).getUri()
					.getQueryParameter("title");
		}

		((TextView) findViewById(R.id.name_title)).setText(title);
	}

}
