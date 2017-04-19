package com.schoolcontact.view;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.school_contact.main.R;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.LoadingDialog;

public class UserSettingActivity extends BaseActivity {

	private UserService cs = new UserService();
	private AudioManager audioManager;

	private ToggleButton tbvoice;
	private ToggleButton tbshack;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;
		case R.id.bt_logOut:
			bt_logout();
			break;
		default:
			break;
		}
	}

	public void bt_logout() {
		mDialog.setText(R.string.logouting);
		mDialog.show();
		cs.logOut(this);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		onInit();
	}

	@Override
	public void onInit() {
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mDialog = new LoadingDialog(this);
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);
		tbvoice = (ToggleButton) findViewById(R.id.tb_voice);
		tbvoice.setOnCheckedChangeListener(this);
		tbshack = (ToggleButton) findViewById(R.id.tb_shack);
		tbshack.setOnCheckedChangeListener(this);
		((ToggleButton) findViewById(R.id.tb_listenvoice))
				.setOnCheckedChangeListener(this);
		((Button) findViewById(R.id.bt_logOut)).setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.tb_voice:

			if (isChecked) {
				if (tbshack.isChecked()) {
					tbshack.setChecked(false);
				}
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

			} else {

				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

			}
			break;
		case R.id.tb_shack:

			if (isChecked) {

				if (tbvoice.isChecked()) {
					tbvoice.setChecked(false);
				}
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

			} else {

				// tbvoice.setChecked(false);
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
			break;
		case R.id.tb_listenvoice:

			break;
		default:
			break;
		}
	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.LOGOUT:
		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
		//	sccontext.clear();
			mDialog.dismiss();
			Intent mainIntent = new Intent(UserSettingActivity.this,
					LoginActivity.class);
			this.startActivity(mainIntent);
			MoreFragment.instance.getActivity().finish();
			this.finish();
			// System.exit(0);
			// restartApplication();
			break;

		default:
			break;
		}
	}

	// private void restartApplication() {
	// // Intent k = getBaseContext().getPackageManager()
	// // .getLaunchIntentForPackage(getBaseContext().getPackageName());
	// // k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// // startActivity(k);
	// }
}
