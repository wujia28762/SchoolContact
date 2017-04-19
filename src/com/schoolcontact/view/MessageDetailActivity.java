package com.schoolcontact.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.adapter.MainListViewAdapter;
import com.schoolcontact.adapter.PhotoAlbumSunItemAdapter;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.GroupService;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.widget.XListView;
import com.schoolcontact.widget.XListView.IXListViewListener;

public class MessageDetailActivity extends BaseActivity implements
		IXListViewListener, OnClickListener {

	private XListView mListView;

	private AsyncImageLoader imageLoader;

	private MainListViewAdapter mv;
	private List<FriendMessageInfo> fmi;
	private FriendMessageInfo fi;
	private GroupService gs = new GroupService();
	private String message_id;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends);
		onInit();

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;

		}

	}

	@Override
	public void onInit() {
		Intent it = this.getIntent();

		imageLoader = new AsyncImageLoader(this);
		fi = (FriendMessageInfo) it
				.getSerializableExtra(PhotoAlbumSunItemAdapter.TAG);
		fmi = new ArrayList<FriendMessageInfo>();
		mv = new MainListViewAdapter(this,imageLoader);
		if(fi==null)
		{
			message_id = it.getStringExtra("message_id");
			gs.getOneMessage(message_id, this);
			mv.setData(fmi);
		}
		else
		{
			fmi = new ArrayList<FriendMessageInfo>();
			fmi.add(fi);
			mv.setData(fmi);
			gs.getOneMessage(fi.getKey_id(), this);
			
		}
		imageLoader = new AsyncImageLoader(this);
		findViewById(R.id.tv_submit).setVisibility(View.GONE);
		findViewById(R.id.iv_refresh).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_choosegroup)).setText("ฯ๊ว้");
		findViewById(R.id.backtext).setOnClickListener(this);
		
		// ((ScrollView) findViewById(R.id.act_solution_1_sv))
		// .smoothScrollTo(0, 0);
		mListView = (XListView) findViewById(R.id.ll_main_listView);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View addHeaderView = inflater.inflate(R.layout.xlistviewblankhader,
				null);

		// View v = findViewById(R.layout.xlistviewblankhader);
		// mListView.addHeaderView(v);
		mListView.addHeaderView(addHeaderView);
		mListView.setNeedFoot(false);
		
		
		mListView.setAdapter(mv);
		mListView.setXListViewListener(this);

	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.PUBLISHCOMMENT: {

			if (rm.getCode().equals("y")) {
				// sccontext.setMgroupFriendMessageInfo(mv.getData());
				// gs.saveFriendMessage("usersbmessagelist_" + uid,
				// sccontext.getMgroupFriendMessageInfo());
			//	mv.notifyDataSetChanged();
			}
		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
		case EventList.PUBLISHAGREE: {
			if (rm.getCode().equals("y")) {
			//	mv.notifyDataSetChanged();
			}

		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;
		case EventList.DELETEMESSAGE: {
			if (rm.getCode().equals("y")) {
				fi = null;
			//	mv.setData(null);
				mv.notifyDataSetChanged();
			}

		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;
		case EventList.GETONEMESSAGE: {
			if (rm.getCode().equals(EventList.SCUESS)) {
				FriendMessageInfo fm = (FriendMessageInfo) rm.getData();
				//List<FriendMessageInfo> lm = new ArrayList<FriendMessageInfo>();
			//	fi = fm;
			//	lm.add(fm);
			//	mv.setData(lm);
				fmi.clear();
				fmi.add(fm);
			//	mv.setData(fmi);
				mv.notifyDataSetChanged();
			}

		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
		default:
			break;
		}
	}

}
