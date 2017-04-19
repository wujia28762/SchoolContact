package com.schoolcontact.view;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.NotifyListener;
import com.schoolcontact.adapter.CustomAdapter;
import com.schoolcontact.model.ClassInfo;
import com.schoolcontact.model.ItemEntity;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.model.SchoolInfo;
import com.schoolcontact.service.IMService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.widget.PinnedHeaderListView;

public class ContactFragment extends BaseFragment implements NotifyListener,
		SwipeRefreshLayout.OnRefreshListener {

	private PinnedHeaderListView listView;
	private List<String> classitems;
	private SwipeRefreshLayout mSwipeLayout;
	private IMService is = new IMService();
	private CustomAdapter customAdapter;
	private List<ItemEntity> data;
	private Map<String, String> noticedata;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onPause() {
		super.onPause();
		customAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return onInit(inflater, container);
	}

	public View onInit(LayoutInflater inflater, ViewGroup container) {

		List<ItemEntity> data = createData();
		View tc = inflater.inflate(R.layout.tab_contact, container, false);
		listView = (PinnedHeaderListView) tc.findViewById(R.id.group_list);
		mSwipeLayout = (SwipeRefreshLayout) tc.findViewById(R.id.id_swipe_ly);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo);
		// * 创建新的HeaderView，即置顶的HeaderView
		View HeaderView = inflater.inflate(R.layout.listview_item_header,
				listView, false);
		listView.setPinnedHeader(HeaderView);

		listView.setEmptyView((TextView) tc.findViewById(R.id.empty));
		listView.setOnItemClickListener(this);
		customAdapter = new CustomAdapter(this.getActivity(), data);
		listView.setAdapter(customAdapter);

		listView.setOnScrollListener(customAdapter);

		sccontext.setNl(this);

		return tc;
	}

	private List<ItemEntity> createData() {

		noticedata = new HashMap<String, String>();
		classitems = new ArrayList<String>();
		data = new ArrayList<ItemEntity>();
		if (sccontext.getListinfo() != null
				&& sccontext.getListinfo().getResults() != null
				&& sccontext.getListinfo().getResults().size() != 0) {
			for (SchoolInfo subgroupinfo : sccontext.getListinfo().getResults()) {

				ItemEntity itemEntity2 = new ItemEntity(subgroupinfo.getName(),
						sccontext.getListinfo().getNotices()
								.get(subgroupinfo.getId()).getUserName(),
						sccontext.getListinfo().getNotices()
								.get(subgroupinfo.getId()).getPortraitUri());

				data.add(itemEntity2);
				noticedata.put(
						sccontext.getListinfo().getNotices()
								.get(subgroupinfo.getId()).getUserName(),
						sccontext.getListinfo().getNotices()
								.get(subgroupinfo.getId()).getUserId());
				classitems.add(sccontext.getListinfo().getNotices()
						.get(subgroupinfo.getId()).getUserName());
				for (ClassInfo cf : subgroupinfo.getContents()) {
					classitems.add(cf.getName());
					ItemEntity itemEntity1 = new ItemEntity(
							subgroupinfo.getName(), cf.getName(),
							cf.getPortraitUri());
					data.add(itemEntity1);
				}
			}
		}
		Log.v("填充data", data.toString());
		return data;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (TextUtils.isEmpty(noticedata.get(data.get(position).getmContent()))) {

			Intent it = new Intent(getActivity(), ClassContactActivity.class);

			Bundle bundle = new Bundle();
			bundle.putString("classinfo", classitems.get(position));
			it.putExtras(bundle);
			startActivity(it);

		} else {
			String targetId = noticedata.get(data.get(position).getmContent());
			ConversationType conversationType = Conversation.ConversationType.PRIVATE;
			Conversation conversation = RongIMClient.getInstance()
					.getConversation(conversationType, targetId);
			int latestMessageId = 0;
			if (conversation != null) {
				latestMessageId = conversation.getLatestMessageId();

			} else {
				Toast.makeText(getActivity(), "没有消息", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			Intent intent = new Intent(getActivity(),
					NotificationListDisplayActivity.class);
			intent.putExtra("conversationTypeValue",
					conversationType.getValue());
			intent.putExtra("targetId", targetId);
			intent.putExtra("latestMessageId", latestMessageId);
			getActivity().startActivity(intent);

			/*
			 * RongIM.getInstance().startConversation(this.getActivity(),
			 * Conversation.ConversationType.PRIVATE,
			 * noticedata.get(data.get(position).getmContent()),
			 * data.get(position).getmContent());
			 */
		}

	}

	@Override
	public void notifyDataChanged() {
		System.out.println("数据集改变");
		List<ItemEntity> data = createData();
		customAdapter.setmData(data);
		customAdapter.notifyDataSetChanged();

	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.GETGROUP:
			if (rm.getCode().equals(EventList.SCUESS)) {
				mSwipeLayout.setRefreshing(false);
//			} else {
//				Toast.makeText(this.getActivity(), "刷新失败！", Toast.LENGTH_SHORT)
//						.show();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		is.getGroup(this, true);
	}

}
