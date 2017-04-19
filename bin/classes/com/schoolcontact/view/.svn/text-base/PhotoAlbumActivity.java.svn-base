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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.school_contact.main.R;
import com.schoolcontact.adapter.PhotoAlbumAdapter;
import com.schoolcontact.adapter.PhotoAlbumAdapter.ViewHolder;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.ImageViewWithUrl;
import com.schoolcontact.model.PhotoAlbumItem;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.GroupService;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.DateUtil;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.ListUtils;
import com.schoolcontact.utils.LoadingDialog;
import com.schoolcontact.utils.PicSelectUtils;
import com.schoolcontact.widget.XListView;
import com.schoolcontact.widget.XListView.IXListViewListener;

public class PhotoAlbumActivity extends BaseActivity implements
		IXListViewListener, OnClickListener {

	// private RelativeLayout mReplaceBackground;// 点击更换背景

	private XListView mListView;
	private TextView mTitlename;
	private PhotoAlbumAdapter mPhotoAlbumAdapter;
	private List<PhotoAlbumItem> mData;
	private GroupService gs = new GroupService();
	private String curr_uid;
	private String curr_iconUrl;
	private String curr_name;
	private ImageViewWithUrl mTitleImage;
	private AsyncImageLoader imageLoader;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photoalbum);
		onInit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_click_replace_background:
			PicSelectUtils.doPickPhotoAction(this);
			break;
		case R.id.backtext:
			finish();
			break;

		case R.id.tv_submit:
			Intent mainIntent = new Intent(PhotoAlbumActivity.this,
					SendMoodActivity.class);
			this.startActivity(mainIntent);
			// instance.finish();
			break;

		}

	}

	@Override
	public void onInit() {
		imageLoader = new AsyncImageLoader(this);
		mDialog = new LoadingDialog(this);
		mTitleImage = new ImageViewWithUrl();
		curr_uid = getIntent().getStringExtra("uid");
		curr_name = getIntent().getStringExtra("name");
		curr_iconUrl = getIntent().getStringExtra("url");
		// ((ScrollView) findViewById(R.id.sl_pitem)).smoothScrollTo(0, 0);
		findViewById(R.id.tv_submit).setOnClickListener(this);
		findViewById(R.id.backtext).setOnClickListener(this);
		mListView = (XListView) findViewById(R.id.ll_photo_listView);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View addHeaderView = inflater.inflate(R.layout.head_listview, null);
		mTitleImage.setmImageView((ImageView) addHeaderView
				.findViewById(R.id.iv_head_img));
		mTitlename = (TextView) addHeaderView.findViewById(R.id.tv_pyqusername);
		if (sccontext.getUi() != null) {
			mTitlename.setText(curr_name);
			ImageUtil.disAsyIamge(mTitleImage, curr_iconUrl, imageLoader);
		}

		mListView.addHeaderView(addHeaderView);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		List<FriendMessageInfo> ls = gs.findMyFriendMessageFromFile(curr_uid);
		/*if (!ListUtils.isEmpty(ls)) {
			sccontext.setMyFriendMessageInfo(ls);
		}*/
		initData(ls);
		mPhotoAlbumAdapter = new PhotoAlbumAdapter(this, mData);

		mListView.setAdapter(mPhotoAlbumAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:

					System.out.println("SCROLL_STATE_FLING");//
					mPhotoAlbumAdapter.loadDrawable(true);
					break;

				case OnScrollListener.SCROLL_STATE_IDLE:// up

					System.out.println("SCROLL_STATE_IDLE");
					mPhotoAlbumAdapter.loadDrawable(false);
					updateSingleRow();
					// mMainListViewAdapter.notifyDataSetChanged();// 刷新最后一屏数据.
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

					System.out.println("SCROLL_STATE_TOUCH_SCROLL");//
					mPhotoAlbumAdapter.loadDrawable(true);
					// mMainListViewAdapter.getView(arg0, convertView, arg2)
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// _start_index = firstVisibleItem;
				// _end_index = firstVisibleItem + visibleItemCount;
				// if (_end_index >= totalItemCount) {
				// _end_index = totalItemCount - 1;
				// }

			}
		});

	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.LOOKSBMESSAGE:
		//	Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
			onLoad();
			initData(sccontext.getMyFriendMessageInfo());
			mPhotoAlbumAdapter.setDatas(mData);
			mPhotoAlbumAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

	public void initData(List<FriendMessageInfo> fmi)

	{
		if (fmi != null) {
			mData = new ArrayList<PhotoAlbumItem>();
			for (int i = 0; i < fmi.size(); i++) {
				boolean finded = false;
				String tmp = DateUtil.formatTimeStringToData(Long.valueOf(fmi
						.get(i).getSend_time()));
				for (int j = 0; j < mData.size(); j++) {
					// String tmp =
					// DateUtil.formatTimeStringToData(Long.valueOf(fmi.get(i).getSend_time()));
					if (tmp.equals(mData.get(j).getTimeStr())) {
						mData.get(j).getmSubList().add(fmi.get(i));
						finded = true;
						break;
					}

				}
				if (!finded) {
					List<FriendMessageInfo> mSubList = new ArrayList<FriendMessageInfo>();
					mSubList.add(fmi.get(i));
					PhotoAlbumItem pi = new PhotoAlbumItem(tmp, mSubList);
					mData.add(pi);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		gs.getSBMessageList(this, curr_uid, true, true);

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sccontext.setMyFriendMessageInfo(null);
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		gs.getSBMessageList(this, curr_uid, false, false);

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	private void updateSingleRow() {

		if (mListView != null) {
			int start = mListView.getFirstVisiblePosition();

			for (int i = start, j = mListView.getLastVisiblePosition(); i <= j; i++) {
				View view = mListView.getChildAt(i - start);
				if (view.getTag() instanceof ViewHolder) {
					ViewHolder view1 = (ViewHolder) (mListView.getChildAt(i
							- start).getTag());
					view1.getmPhotoAlbumSunItemAdapter().notifyDataSetChanged();
				}

			}
		}

	}
}
