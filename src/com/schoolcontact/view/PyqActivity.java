package com.schoolcontact.view;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.NotifyListener;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.adapter.MainListViewAdapter;
import com.schoolcontact.adapter.MainListViewAdapter.ViewHolder;
import com.schoolcontact.model.CustomContent;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.GroupInfo;
import com.schoolcontact.model.ImageViewWithUrl;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.GroupService;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.DialogUtils;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.LoadingDialog;
import com.schoolcontact.utils.PicSelectUtils;
import com.schoolcontact.utils.StringUtils;
import com.schoolcontact.widget.XListView;
import com.schoolcontact.widget.XListView.IXListViewListener;

public class PyqActivity extends BaseActivity implements IXListViewListener,
		OnClickListener {

	private int num;
	private String techGroupFlag = "n";
	private boolean hasNew;
	private XListView mListView;
	private Handler mHandler;
	private ProgressBar pb_head;// 等待提示
	// private RelativeLayout mReplaceBackground;// 点击更换背景
	// private TextView mBackgroundText;
	private ImageViewWithUrl mTitleImage;
	private TextView mTitlename;
	private AsyncImageLoader imageLoader;
	private TextView mTv_choosegroup;
	private ImageView iv_refresh;
	private ProgressBar mloadingGrogressBar;// 等待提示
	private GroupService gs = new GroupService();
	private AlertDialog techGroupDialog;
	private MainListViewAdapter mMainListViewAdapter;
	private RelativeLayout rl_newunread;
	private ImageView push_image;
	private TextView tv_new_num;
	private PyqActivity instance;
	private int _start_index;
	private int _end_index;
	private int wh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends);
		onInit();

	}

	@Override
	protected void onResume() {

		super.onResume();
		if (ScContext.getInstance().getUi().getUser_group().equals("教师")) {
			gs.getGroup(this, true, techGroupFlag);// 教师的话先要当前朋友圈列表
		} else {
			gs.getGroup(this, true, "a");// 学生的话直接要所有朋友圈列表
		}
		/*
		 * if (!TextUtils.isEmpty(sccontext.getCurr_groupName())) {
		 * mTv_choosegroup.setText(sccontext.getCurr_groupName());
		 * 
		 * dealMessage(new ReturnMessage(EventList.SCUESS, "分组选择成功！",
		 * EventList.CHOOSEGROUPFINISH, null)); }
		 */
		if (!sccontext.isNewAboutMe()) {
			rl_newunread.setVisibility(View.GONE);
		}
		// if(mMainListViewAdapter!=null)
		// {
		// mMainListViewAdapter.notifyDataSetChanged();
		// }
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		sccontext.setMgroupFriendMessageInfo(null);
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {

		pb_head.setVisibility(View.VISIBLE);
		gs.getAllMessageList(this, false, true);

		// mHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// pb_head.setVisibility(View.GONE);
		// onLoad();
		// }
		// }, 10000);
	}

	@Override
	public void onLoadMore() {

		gs.getAllMessageList(this, false, false);
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
			Intent mainIntent = new Intent(PyqActivity.this,
					SendMoodActivity.class);
			this.startActivity(mainIntent);
			// instance.finish();
			break;
		case R.id.tv_choosegroup:
			if (sccontext.getmGroupInfos() != null
					|| sccontext.getTechCurrGroupInfos() != null
					|| sccontext.getTechHisGroupInfos() != null) {
				techGroupDialog.show();
				Button PositiveButton = ((AlertDialog) techGroupDialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				PositiveButton.setBackgroundColor(Color.rgb(0x32, 0x99, 0xCC));
				Button negativeButton = ((AlertDialog) techGroupDialog)
						.getButton(AlertDialog.BUTTON_NEGATIVE);
				negativeButton.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
			}
			break;
		case R.id.iv_refresh:
			// 只有老师点击刷新列表，学生不显示刷新按钮
			techGroupFlag = "n";
			gs.getGroup(this, true, techGroupFlag);
			if (!mDialog.isShowing()) {

				mDialog.show();
			}
			break;

		}

	}

	@Override
	public void finish() {
		super.finish();
		sccontext.setCurrentGroupNo(0);
		// sccontext.setMgroupFriendMessageInfo(null);
		unregisterReceiver(refreshGroupNameReceiver);
	}

	@Override
	public void dealMessage(ReturnMessage rm) {
		switch (rm.getEvent()) {
		case EventList.GETGROUPINFO:
			if (rm.getCode().equals(EventList.SCUESS)) {
				if (mDialog.isShowing()) {
					mDialog.dismiss();
				}

				// 如果是学生的话不设置班级选择
				if (ScContext.getInstance().getUi().getUser_group()
						.equals("教师")) {
					if (techGroupFlag.equals("n")) {
						techGroupFlag = "y";
						gs.getGroup(this, true, techGroupFlag);
					} else if (techGroupFlag.equals("y")) {
						initGroupDialog();
						mTv_choosegroup.setOnClickListener(this);
						// Toast.makeText(this, rm.getMess(),
						// Toast.LENGTH_SHORT)
						// .show();
						if (ScContext
								.getInstance()
								.getmPreferences()
								.getString(
										"userdisplaygroupid_"
												+ ScContext.getInstance()
														.getCurr_user(), "")
								.equals("")) {
							techGroupDialog.show();
							techGroupDialog.setCanceledOnTouchOutside(false);
							Button PositiveButton = ((AlertDialog) techGroupDialog)
									.getButton(AlertDialog.BUTTON_POSITIVE);
							PositiveButton.setBackgroundColor(Color.rgb(0x32,
									0x99, 0xCC));
							Button negativeButton = ((AlertDialog) techGroupDialog)
									.getButton(AlertDialog.BUTTON_NEGATIVE);
							negativeButton.setBackgroundColor(Color.rgb(0xFF,
									0xFF, 0xFF));
							gs.getAllMessageList(this, false, true);
						} else {
							String groupName = ScContext
									.getInstance()
									.getmPreferences()
									.getString(
											"userdisplaygroupname_"
													+ ScContext.getInstance()
															.getCurr_user(), "");
							String groupId = ScContext
									.getInstance()
									.getmPreferences()
									.getString(
											"userdisplaygroupid_"
													+ ScContext.getInstance()
															.getCurr_user(), "");
							mTv_choosegroup.setText(groupName);
							sccontext.setDisplayGroupName(groupName);
							sccontext.setDisplayGroupId(groupId);
							changeTechPublishGroupId(groupId);
							dealMessage(new ReturnMessage(EventList.SCUESS,
									"分组选择成功！", EventList.CHOOSEGROUPFINISH,
									null));
						}
					}
				} else {
					sccontext.setDisplayGroupId(sccontext.getmGroupInfos()
							.get(0).getGroup_id());
					sccontext.setDisplayGroupName(sccontext.getmGroupInfos()
							.get(0).getGroup_name());
					mTv_choosegroup.setText(sccontext.getmGroupInfos().get(0)
							.getGroup_name());
					sccontext.setCurr_group(sccontext.getmGroupInfos().get(0)
							.getGroup_id());
					sccontext.setCurr_groupName(sccontext.getmGroupInfos()
							.get(0).getGroup_name());
					sccontext.setCurrentGroupNo(0);
					dealMessage(new ReturnMessage(EventList.SCUESS, "分组选择成功！",
							EventList.CHOOSEGROUPFINISH, null));
					// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT)
					// .show();
				}

			} else {
				// Toast.makeText(this, rm.getMess(),
				// Toast.LENGTH_SHORT).show();
			}
			break;
		case EventList.LOOKMESSAGE: {
			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

			if (mloadingGrogressBar.getVisibility() == View.VISIBLE)
				mloadingGrogressBar.setVisibility(View.GONE);

			if (pb_head.getVisibility() == View.VISIBLE) {
				pb_head.setVisibility(View.GONE);
			}
			onLoad();
			if (rm.getCode().equals(EventList.SCUESS)) {
				if(sccontext
						.getMgroupFriendMessageInfo()!=null)
				{
				mMainListViewAdapter.setData(sccontext
						.getMgroupFriendMessageInfo());
				mMainListViewAdapter.notifyDataSetChanged();
				}
				if (ScContext.getInstance().getUi().getUser_group()
						.equals("教师")) {

				} else {
					if (rm.getMess().equals("没有消息")) {
						// 当前朋友圈没有数据的话，切换到下一个班级继续查
						int currentGroupNo = sccontext.getCurrentGroupNo() + 1;
						if (currentGroupNo > sccontext.getmGroupInfos().size() - 1) {
						} else {
							sccontext.setCurrentGroupNo(currentGroupNo);
							mTv_choosegroup.setText(sccontext.getmGroupInfos()
									.get(currentGroupNo).getGroup_name());
							sccontext.setDisplayGroupName(sccontext
									.getmGroupInfos().get(currentGroupNo)
									.getGroup_name());
							sccontext.setDisplayGroupId(sccontext
									.getmGroupInfos().get(currentGroupNo)
									.getGroup_id());
							gs.getAllMessageList(this, false, false);
						}
					}
				}
			}
		}
			break;
		case EventList.CHOOSEGROUPFINISH: {

			if (mloadingGrogressBar.getVisibility() == View.GONE)
				mloadingGrogressBar.setVisibility(View.VISIBLE);
			gs.getAllMessageList(this, true, true);
		}
			break;
		case EventList.PUBLISHCOMMENT: {

			if (rm.getCode().equals("y")) {
				// mMainListViewAdapter
				// .setData(sccontext.getMgroupFriendMessageInfo());
				mMainListViewAdapter.notifyDataSetChanged();
			}
			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;
		case EventList.PUBLISHAGREE: {
			if (rm.getCode().equals("y")) {
				// mMainListViewAdapter
				// .setData(sccontext.getMgroupFriendMessageInfo());
				mMainListViewAdapter.notifyDataSetChanged();
			}

			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;
		case EventList.DELETEMESSAGE: {
			if (rm.getCode().equals("y")) {

				mMainListViewAdapter.notifyDataSetChanged();
			}

			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;

		case EventList.DELETECOMMENT: {
			if (rm.getCode().equals("y")) {
				// mMainListViewAdapter.setData(sccontext.getMgroupFriendMessageInfo());
				mMainListViewAdapter.notifyDataSetChanged();
			}

			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
			break;
		case EventList.GETONEMESSAGE: {
			if (rm.getCode().equals("y")) {
				// mMainListViewAdapter.setData(sccontext.getMgroupFriendMessageInfo());
				mMainListViewAdapter.notifyDataSetChanged();
			}

			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

		}
		}

	}

	// 如果groupId是当前列表中的班级，改变老师要发布的班级圈
	private void changeTechPublishGroupId(String groupId) {
		List<GroupInfo> list = sccontext.getTechCurrGroupInfos();
		boolean hasGroupId = false;
		for (GroupInfo gi : list) {
			if (gi.getGroup_id().equals(groupId)) {
				sccontext.setCurr_group(gi.getGroup_id());
				sccontext.setCurr_groupName(gi.getGroup_name());
				hasGroupId = true;

				ScContext
						.getInstance()
						.getmPreferences()
						.edit()
						.putString(
								"usercurrgroupid_"
										+ ScContext.getInstance()
												.getCurr_user(),
								gi.getGroup_id()).commit();

				ScContext
						.getInstance()
						.getmPreferences()
						.edit()
						.putString(
								"usercurrgroupname_"
										+ ScContext.getInstance()
												.getCurr_user(),
								gi.getGroup_name()).commit();

				break;
			}
		}
		if (!hasGroupId) {
			sccontext.setCurr_group(null);
		}
	}

	@Override
	public void onInit() {
		registerReceiver(refreshGroupNameReceiver, new IntentFilter(
				"com.school_contact.main.refreshgroupname"));
		imageLoader = new AsyncImageLoader(this);
		mDialog = new LoadingDialog(this);
		mTitleImage = new ImageViewWithUrl();
		mHandler = new Handler();
		instance = this;
		mTv_choosegroup = (TextView) findViewById(R.id.tv_choosegroup);
		iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
		if (ScContext.getInstance().getUi().getUser_group().equals("教师")) {
			iv_refresh.setOnClickListener(this);
		} else {
			iv_refresh.setVisibility(View.INVISIBLE);
		}
		findViewById(R.id.tv_submit).setOnClickListener(this);
		findViewById(R.id.backtext).setOnClickListener(this);
		// ((ScrollView) findViewById(R.id.act_solution_1_sv))
		// .smoothScrollTo(0, 0);mloadingGrogressBar
		mloadingGrogressBar = (ProgressBar) findViewById(R.id.loadingGrogressBar);
		mListView = (XListView) findViewById(R.id.ll_main_listView);
		pb_head = (ProgressBar) findViewById(R.id.pb_head);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View addHeaderView = inflater.inflate(R.layout.head_listview, null);
		// mReplaceBackground = (RelativeLayout) addHeaderView
		// .findViewById(R.id.rl_click_replace_background);
		// mBackgroundText = (TextView)
		// addHeaderView.findViewById(R.id.textView1);
		// mReplaceBackground.setOnClickListener(this);
		mTitleImage.setmImageView((ImageView) addHeaderView
				.findViewById(R.id.iv_head_img));
		rl_newunread = (RelativeLayout) addHeaderView
				.findViewById(R.id.rl_newunread);
		push_image = (ImageView) addHeaderView.findViewById(R.id.push_image);
		tv_new_num = (TextView) addHeaderView.findViewById(R.id.tv_new_num);
		if (sccontext.isNewAboutMe()) {

			rl_newunread.setVisibility(View.VISIBLE);
			List<CustomContent> lcc = sccontext.getmCustomContent();
			tv_new_num.setText("有" + lcc.size() + "条新信息");
			if (!TextUtils.isEmpty(lcc.get(lcc.size() - 1)
					.getComment_portraitUri())) {
				// System.out.println(urlList.get(arg0));
				// viewHolder.mImageView.setTag(viewHolder.mUrl);
				Bitmap bitmap = imageLoader.loadImage(push_image,
						lcc.get(lcc.size() - 1).getComment_portraitUri());
				if (bitmap != null) {
					push_image.setImageBitmap(bitmap);
				}
			}
			rl_newunread.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent mainIntent = new Intent(PyqActivity.this,
							PushDetailActivity.class);
					instance.startActivity(mainIntent);
				}
			});
		}
		mTitlename = (TextView) addHeaderView.findViewById(R.id.tv_pyqusername);
		if (sccontext.getUi() != null) {
			mTitlename.setText(sccontext.getUi().getUsername());
			ImageUtil.disAsyIamge(mTitleImage, sccontext.getUi()
					.getIsportraituri(), imageLoader);
		}
		mTitleImage.getmImageView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PyqActivity.this,
						PhotoAlbumActivity.class);
				intent.putExtra("uid", sccontext.getUi().getUsertradeid());
				intent.putExtra("name", sccontext.getUi().getUsername());
				intent.putExtra("url", sccontext.getUi().getIsportraituri());
				PyqActivity.this.startActivity(intent);

			}
		});

		sccontext.setMgroupFriendMessageInfo(null);
		mMainListViewAdapter = new MainListViewAdapter(this, imageLoader);
		List<FriendMessageInfo> list = gs
				.findMyGroupFriendMessageFromFile(sccontext.getCurr_user());
		mMainListViewAdapter.setData(list);
		mListView.setPullLoadEnable(true);
		mListView.addHeaderView(addHeaderView);
		mListView.setAdapter(mMainListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:

					System.out.println("SCROLL_STATE_FLING");//
					mMainListViewAdapter.loadDrawable(true);
					break;

				case OnScrollListener.SCROLL_STATE_IDLE:// up

					System.out.println("SCROLL_STATE_IDLE");
					mMainListViewAdapter.loadDrawable(false);
					updateSingleRow();
					// mMainListViewAdapter.notifyDataSetChanged();// 刷新最后一屏数据.
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

					System.out.println("SCROLL_STATE_TOUCH_SCROLL");//
					mMainListViewAdapter.loadDrawable(true);
					// mMainListViewAdapter.getView(arg0, convertView, arg2)
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				num = visibleItemCount;
				// _start_index = firstVisibleItem;
				// _end_index = firstVisibleItem + visibleItemCount;
				// if (_end_index >= totalItemCount) {
				// _end_index = totalItemCount - 1;
				// }

			}
		});
		mDialog.setText(R.string.getgroup);
		mDialog.show();
		mloadingGrogressBar.setVisibility(View.VISIBLE);
		/*
		 * if (ScContext.getInstance().getUi().getUser_group().equals("教师")) {
		 * gs.getGroup(this, true, techGroupFlag);// 教师的话先要当前朋友圈列表 } else {
		 * gs.getGroup(this, true, "a");// 学生的话直接要所有朋友圈列表 }
		 */

	}

	private void groupLv(LinearLayout layout, List<GroupInfo> giList) {
		layout.removeAllViews();
		if (giList == null || giList.size() == 0) {
			return;
		}
		for (GroupInfo groupInfo : giList) {
			Button btn1 = new Button(this);
			btn1.setBackgroundColor(Color.WHITE);
			btn1.setText(groupInfo.getGroup_name());
			btn1.setTag(groupInfo.getGroup_id());

			layout.addView(btn1);
			// window.addContentView(btn1, params);
			btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					techGroupDialog.dismiss();
					// Intent it = new Intent();

					if (ScContext.getInstance().getUi().getUser_group()
							.equals("教师")) {
						sccontext.setMgroupFriendMessageInfo(null);
					}
					if (ScContext.getInstance().getCurr_user() != null) {
						ScContext
								.getInstance()
								.getmPreferences()
								.edit()
								.putString(
										"userdisplaygroupid_"
												+ ScContext.getInstance()
														.getCurr_user(),
										StringUtils.enCorder((((Button) v)
												.getTag().toString())))
								.commit();

						ScContext
								.getInstance()
								.getmPreferences()
								.edit()
								.putString(
										"userdisplaygroupname_"
												+ ScContext.getInstance()
														.getCurr_user(),
										StringUtils.enCorder((((Button) v)
												.getText().toString())))
								.commit();
						/*
						 * ScContext .getInstance() .getmPreferences() .edit()
						 * .putString( "usercurrgroupid_" +
						 * ScContext.getInstance() .getCurr_user(),
						 * StringUtils.enCorder((((Button) v)
						 * .getTag().toString()))) .commit(); ScContext
						 * .getInstance() .getmPreferences() .edit() .putString(
						 * "usercurrgroupname_" + ScContext.getInstance()
						 * .getCurr_user(), StringUtils.enCorder((((Button) v)
						 * .getText().toString()))) .commit();
						 */
						/*
						 * ScContext.getInstance().setCurr_group( ((Button)
						 * v).getTag().toString());
						 * ScContext.getInstance().setCurr_groupName( ((Button)
						 * v).getText().toString());
						 */
						ScContext.getInstance().setDisplayGroupId(
								((Button) v).getTag().toString());
						ScContext.getInstance().setDisplayGroupName(
								((Button) v).getText().toString());

						changeTechPublishGroupId(((Button) v).getTag()
								.toString());
						mTv_choosegroup.setText(sccontext.getDisplayGroupName());
						dealMessage(new ReturnMessage(EventList.SCUESS,
								"分组选择成功！", EventList.CHOOSEGROUPFINISH, null));

					}

				}

			});

		}
	}

	private void initGroupDialog() {

		final LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		listLayout.setLayoutParams(params);

		ScrollView scroller = new ScrollView(this);

		LinearLayout outerLayout = new LinearLayout(this);
		outerLayout.setOrientation(LinearLayout.VERTICAL);

		Builder builder = new AlertDialog.Builder(this);
		// builder.setCancelable(true);

		builder.setView(outerLayout);

		builder.setTitle(R.string.chooseGroup)
				.setPositiveButton("当前圈",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								groupLv(listLayout,
										sccontext.getTechCurrGroupInfos());
								Button PositiveButton = ((AlertDialog) techGroupDialog)
										.getButton(AlertDialog.BUTTON_POSITIVE);
								PositiveButton.setBackgroundColor(Color.rgb(
										0x32, 0x99, 0xCC));
								Button negativeButton = ((AlertDialog) techGroupDialog)
										.getButton(AlertDialog.BUTTON_NEGATIVE);
								negativeButton.setBackgroundColor(Color.rgb(
										0xFF, 0xFF, 0xFF));
							}

						})
				.setNegativeButton("历史圈",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								groupLv(listLayout,
										sccontext.getTechHisGroupInfos());
								Button PositiveButton = ((AlertDialog) techGroupDialog)
										.getButton(AlertDialog.BUTTON_POSITIVE);
								PositiveButton.setBackgroundColor(Color.rgb(
										0xFF, 0xFF, 0xFF));
								Button negativeButton = ((AlertDialog) techGroupDialog)
										.getButton(AlertDialog.BUTTON_NEGATIVE);
								negativeButton.setBackgroundColor(Color.rgb(
										0x32, 0x99, 0xCC));
							}

						});

		/*
		 * ArrayList<GroupInfo> al = new ArrayList<GroupInfo>(); for(int i=
		 * 0;i<20;i++){ GroupInfo gi = new GroupInfo();
		 * gi.setGroup_id(String.valueOf(i));
		 * gi.setGroup_name("name"+String.valueOf(i)); al.add(gi); }
		 * groupLv(listLayout, al);
		 */
		groupLv(listLayout, sccontext.getTechCurrGroupInfos());
		scroller.addView(listLayout);
		outerLayout.addView(scroller);
		
		techGroupDialog = builder.create();
		techGroupDialog.setCanceledOnTouchOutside(true);
		//techGroupDialog.setCanceleable();
		try {
			Field field = techGroupDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			Object obj = field.get(techGroupDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			field.set(obj, new GroupDialogButtonHandler(techGroupDialog));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class GroupDialogButtonHandler extends Handler {
		private WeakReference<DialogInterface> weakDialog;

		public GroupDialogButtonHandler(DialogInterface di) {
			weakDialog = new WeakReference<DialogInterface>(di);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						weakDialog.get(), msg.what);
				break;
			}
		}
	}

	private void updateSingleRow() {

		if (mListView != null) {
			int start = mListView.getFirstVisiblePosition();

			for (int i = start, j = mListView.getLastVisiblePosition(); i <= j; i++) {
				View view = mListView.getChildAt(i - start);
				if (view.getTag() instanceof ViewHolder) {
					ViewHolder view1 = (ViewHolder) (mListView.getChildAt(i
							- start).getTag());
					view1.getmGridViewAdapter().notifyDataSetChanged();
				}

			}
		}
	}

	private BroadcastReceiver refreshGroupNameReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			mTv_choosegroup.setText(sccontext.getDisplayGroupName());
		}
	};

}