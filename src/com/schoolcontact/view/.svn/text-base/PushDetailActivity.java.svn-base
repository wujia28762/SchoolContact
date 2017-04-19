package com.schoolcontact.view;

import java.util.List;

import com.school_contact.main.R;
import com.schoolcontact.adapter.PushDtailAdapter;
import com.schoolcontact.model.CustomContent;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.utils.ListUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PushDetailActivity extends BaseActivity {

	private PushDtailAdapter mPushDtailAdapter;
	private ListView mList;
	private PushDetailActivity instance;
	private List<CustomContent> cc;

	@Override
	public void dealMessage(ReturnMessage rm) {
		// TODO Auto-generated method stub
		super.dealMessage(rm);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pushdetail);
		onInit();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		if (sccontext.isNewAboutMe()) {
			sccontext.setNewAboutMe(false);
			sccontext.setmCustomContent(null);
		}
	}

	@Override
	public void onInit() {

		instance = this;
		cc = sccontext.getmCustomContent();
		mPushDtailAdapter = new PushDtailAdapter(this, cc);
		mList = (ListView) findViewById(R.id.lv_pushdetail);
		mList.setAdapter(mPushDtailAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!ListUtils.isEmpty(cc)) {
					Intent intent = new Intent(instance,
							MessageDetailActivity.class);
					intent.putExtra("message_id", cc.get(position)
							.getMessage_id());
					instance.startActivity(intent);
					instance.finish();

				}
			}
		});
		findViewById(R.id.backtext).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				instance.finish();
			}
		});

	}

}
