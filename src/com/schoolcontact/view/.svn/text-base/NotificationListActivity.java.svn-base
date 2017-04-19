package com.schoolcontact.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.NotifyListener;
import com.schoolcontact.model.ClassInfo;
import com.schoolcontact.model.ItemNotification;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.model.RoleGropuInfo;
import com.schoolcontact.model.SchoolInfo;
import com.schoolcontact.service.IMService;
import com.schoolcontact.utils.EventList;

public class NotificationListActivity extends BaseActivity implements
		NotifyListener, SwipeRefreshLayout.OnRefreshListener {

	private ListView listView;
//	private List<String> classitems;
	private SwipeRefreshLayout mSwipeLayout;
	private IMService is = new IMService();
	private ArrayAdapter notificationListAdapter;
	private List<ItemNotification> data;
//	private Map<String, String> noticedata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notification_list);
		List<ItemNotification> data = createData();
		listView = (ListView) findViewById(R.id.notification_list);
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.notification_list_swipe);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo,
				R.color.abc_search_url_text_holo);
		// * 创建新的HeaderView，即置顶的HeaderView
//		View HeaderView = View.inflate(this, R.layout.listview_item_header, null);
//		listView.setPinnedHeader(HeaderView);

		listView.setEmptyView((TextView) findViewById(R.id.notification_list_empty));
		listView.setOnItemClickListener(this);
		notificationListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
		listView.setAdapter(notificationListAdapter);

//		listView.setOnScrollListener(notificationListAdapter);

		sccontext.setNl(this);
	}
	
	@Override
	public void onInit() {
		
	}

	private List<ItemNotification> createData() {

		data = new ArrayList<ItemNotification>();
		if (sccontext.getNotificationListinfo() != null
				&& sccontext.getNotificationListinfo().getResults() != null
				&& sccontext.getNotificationListinfo().getResults().size() != 0) {
			for (SchoolInfo subgroupinfo : sccontext.getNotificationListinfo().getResults()) {
				for (ClassInfo cf : subgroupinfo.getContents()) {
					ItemNotification itemNotificationClass = new ItemNotification(cf.getName(), cf.getId(), "", "");
					data.add(itemNotificationClass);
					for(RoleGropuInfo roleGropuInfo : cf.getContents()){
						ItemNotification itemNotificationGroup = new ItemNotification(cf.getName(), cf.getId(), roleGropuInfo.getName(), roleGropuInfo.getId());
						data.add(itemNotificationGroup);
					}
				}
			}
		}
		Log.v("填充data", data.toString());
		return data;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String notificationClassName = data.get(position).getClassName();
		String notificationClassId = data.get(position).getClassId();
		String notificationGroupName = data.get(position).getGroupName();
		String notificationGroupId = data.get(position).getGroupId();
		sccontext.getmPreferences().edit().putString("notification_class_name_"+sccontext.getCurr_user(), notificationClassName).commit();
		sccontext.getmPreferences().edit().putString("notification_class_id_"+sccontext.getCurr_user(), notificationClassId).commit();
		sccontext.getmPreferences().edit().putString("notification_group_name_"+sccontext.getCurr_user(), notificationGroupName).commit();
		sccontext.getmPreferences().edit().putString("notification_group_id_"+sccontext.getCurr_user(), notificationGroupId).commit();
		finish();
		/*if (TextUtils.isEmpty(noticedata.get(data.get(position).getmContent()))) {

			Intent it = new Intent(getActivity(), ClassContactActivity.class);

			Bundle bundle = new Bundle();
//			bundle.putString("classinfo", classitems.get(position));
			it.putExtras(bundle);
			startActivity(it);

		} else {
			RongIM.getInstance().startConversation(this.getActivity(),
					Conversation.ConversationType.PRIVATE,
					noticedata.get(data.get(position).getmContent()),
					data.get(position).getmContent());
		}*/

	}

	@Override
	public void notifyDataChanged() {
		System.out.println("数据集改变");
		data = createData();
		notificationListAdapter.notifyDataSetChanged();

	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.GETNOTIFICATIONLIST:
			Toast.makeText(this, "更新列表成功", Toast.LENGTH_SHORT)
					.show();
			// notifyDataChanged();
			mSwipeLayout.setRefreshing(false);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		is.getNotificationList(this, true);
	}

}
