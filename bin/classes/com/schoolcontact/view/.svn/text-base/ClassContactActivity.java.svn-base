package com.schoolcontact.view;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.Base.NotifyListener;
import com.schoolcontact.adapter.CustomAdapter;
import com.schoolcontact.model.ClassInfo;
import com.schoolcontact.model.ItemEntity;
import com.schoolcontact.model.RoleGropuInfo;
import com.schoolcontact.model.SchoolInfo;
import com.schoolcontact.model.UserListInfo;
import com.schoolcontact.widget.PinnedHeaderListView;

public class ClassContactActivity extends BaseActivity implements
		NotifyListener {

	private String classname;
	private List<String> useridlist;
	private List<String> usernamelist;
	private List<UserInfo> userinfo;
	private CustomAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_classcontact);
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

	public void onInit() {

		classname = this.getIntent().getStringExtra("classinfo");

		Log.v("获取班级数据", classname);
		TextView tv = (TextView) findViewById(R.id.classcontact_title);
		tv.setText(classname);

		PinnedHeaderListView listView = (PinnedHeaderListView) findViewById(R.id.pl_classcontact);

		List<ItemEntity> data = InitData();

		View HeaderView = getLayoutInflater().inflate(
				R.layout.listview_item_header, listView, false);
		listView.setPinnedHeader(HeaderView);

		customAdapter = new CustomAdapter(getApplication(), data);
		listView.setAdapter(customAdapter);

		listView.setOnScrollListener(customAdapter);

		listView.setOnItemClickListener(this);

		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);

	}

	private List<ItemEntity> InitData() {

		List<ItemEntity> data = new ArrayList<ItemEntity>();

		if (sccontext.getListinfo() != null
				&& sccontext.getListinfo().getResults() != null
				&& sccontext.getListinfo().getResults().size() != 0) {
			userinfo = new ArrayList<UserInfo>();
			useridlist = new ArrayList<String>();
			usernamelist = new ArrayList<String>();
			for (SchoolInfo schoolinfo : sccontext.getListinfo().getResults()) {
				for (ClassInfo classinfo : schoolinfo.getContents()) {
					if (classinfo.getName().equals(classname)) {
						for (RoleGropuInfo rolegropuinfo : classinfo
								.getContents()) {

							for (UserListInfo userlistinfo : rolegropuinfo
									.getContents()) {
								ItemEntity itemEntity1 = new ItemEntity(
										rolegropuinfo.getName(),
										userlistinfo.getUserName(),
										userlistinfo.getPortraitUri());

								userinfo.add(new UserInfo(userlistinfo
										.getUserId(), userlistinfo
										.getUserName(), Uri.parse(userlistinfo
										.getPortraitUri())));

								useridlist.add(userlistinfo.getUserId());
								usernamelist.add(userlistinfo.getUserName());
								data.add(itemEntity1);
							}

						}
					}
				}
			}
		} else {
			sccontext.registerDataListener(this);
		}

		return data;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RongIM.getInstance().startConversation(ClassContactActivity.this,
				Conversation.ConversationType.PRIVATE,
				useridlist.get(position), usernamelist.get(position));
	}

	@Override
	public void notifyDataChanged() {
		List<ItemEntity> data = InitData();
		customAdapter.setmData(data);
		customAdapter.notifyDataSetChanged();
	}

}
