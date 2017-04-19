package com.schoolcontact.view;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIMClientWrapper;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Conversation;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.IMService;
import com.schoolcontact.service.PushService;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.LoadingDialog;
import com.schoolcontact.utils.PicSelectUtils;
import com.schoolcontact.utils.UpdateManager;
import com.schoolcontact.widget.BadgeView;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.umeng.analytics.MobclickAgent;

public class HomeFragment extends FragmentActivity implements
		OnCheckedChangeListener, MessageCallback {
	// private FragmentTabHost mTabHost;
	private RadioGroup radioGroup;
	String[] tabs = { "��ҳ", "��ϵ��", "��","�༶Ȧ" };
	private IMService is = new IMService();
	private UserService cs = new UserService();
	private TextView tv;
	private ProgressBar mProgressBar;
	private TextView loading;
	public static HomeFragment instance;
	private AlertDialog logOutDialog;
	private AlertDialog.Builder logOutbuilder;
	private BroadcastReceiver mBroadcastReceiver;
	private ReceiveXgMessageReceiver receiveXgMessageReceiver;
	private String username;
	private String pwd;
	private Fragment fragment;
	private AlertDialog dialog;
	private AlertDialog.Builder dialogbuilder;
	private TextView notificationButton;
	private Button notifyButton;
	private BadgeView badge1;
	private TranslateAnimation enterAnima;
	private UpdateManager um;
	private TranslateAnimation exitAnima;

	public static Uri imageUri =  buildUri();
	private static String CROP_CACHE_FILE_NAME = "crop_cache_file1.jpg";
	private PushService ps;
	private Button more;
	private LoadingDialog mDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		onInit();

	}

	private static Uri buildUri() {
		return Uri.fromFile(Environment.getExternalStorageDirectory())
				.buildUpon().appendPath(CROP_CACHE_FILE_NAME ).build();
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("ִ����HomeONRESUME");
		MobclickAgent.onResume(HomeFragment.this);

		if (!ScContext.getInstance().isNewMessage()&&badge1.isShown()) {
			badge1.hide(exitAnima);
		}
//		if(fragment instanceof ConversationListFragment)
//		{
//			changeFragment(tabs[0]);
//		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(HomeFragment.this);
		mProgressBar.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
		tv.setVisibility(View.VISIBLE);
		
		

	}

	private void onInit() {

		// logOutDialog = new AlertDialog.Builder(HomeFragment.this)
		// .setTitle("�ǳ���ʾ").setMessage("����һ̨�豸���ô��˺ŵ�¼�����豸�����˳���")
		// .setPositiveButton("ȷ��", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		//
		// logOutDialog.dismiss();
		// cs.logOut(HomeFragment.this);
		//
		// }
		//
		// }).create();
		
		if (ScContext.getInstance().getUi() != null) {
			XGPushManager.registerPush(getApplicationContext(), ScContext
					.getInstance().getUi().getUsertradeid());
		}

		um = new UpdateManager(this);
		um.checkUpdate();
		ps = new PushService();
		mDialog = new LoadingDialog(this);
		instance = this;
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);

		more = (Button) findViewById(R.id.query_button);
		badge1 = new BadgeView(this, more);
		// badge1.
		if (ScContext.getInstance().getUi().getUser_group()
				.equals("��ʦ")) {
			notifyButton = (Button) findViewById(R.id.notify_button);
			notifyButton.setOnClickListener(new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HomeFragment.this,
							NotificationActivity.class);
					startActivity(intent);
				}
			});
			notifyButton.setVisibility(View.VISIBLE);
			
		}
		else
		{
			notifyButton = (Button) findViewById(R.id.notify_button);
			notifyButton.setVisibility(View.GONE);
		}
		
		tv = (TextView) findViewById(R.id.title);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingGrogressBar);

		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.setPressed(true);
		loading = (TextView) findViewById(R.id.tv_loading);
		loading.setVisibility(View.VISIBLE);

		username = this.getIntent().getStringExtra("username");
		pwd = this.getIntent().getStringExtra("pwd");

		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectivityManager != null) {
				
					if ((connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || connectivityManager
							.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
							&& !TextUtils.isEmpty(username)) {// ������wifi
						System.out.println("BEFORE HomeActivity   "
								+ Thread.currentThread().getName());
						
						cs.getLogin(username, pwd, HomeFragment.this);
					} else if (TextUtils.isEmpty(username)) {
						dealMessage(new ReturnMessage("0", "",
								EventList.SERVERLOGIN, null));

					} else {

						if (ScContext.getInstance().getUi() != null) {
							cs.connectRC(null, null, ScContext.getInstance()
									.getUi().getIstoken());
//							RongIMClient
//							.setConnectionStatusListener(new ConnectionStatusListener() {
//								
//								@Override
//								public void onChanged(ConnectionStatus connectionStatus) {
//
//									switch (connectionStatus) {
//
//									case CONNECTED:// ���ӳɹ���
//
//										instance.runOnUiThread(new Runnable() {
//											public void run() {
//												mProgressBar.setVisibility(View.GONE);
//												loading.setVisibility(View.GONE);
//												tv.setVisibility(View.VISIBLE);
//											}
//										});
//										break;
//									case DISCONNECTED:// �Ͽ����ӡ�
//										Toast.makeText(instance, "�Ͽ����ӣ�", Toast.LENGTH_LONG).show();
//										break;
//									case CONNECTING:// �����С�
//										Toast.makeText(instance, "�����У�", Toast.LENGTH_LONG).show();
//										break;
//									case NETWORK_UNAVAILABLE:// ���粻���á�
//
//										
//										break;
//									case KICKED_OFFLINE_BY_OTHER_CLIENT:// �û��˻��������豸��¼�������ᱻ�ߵ���
//										Toast.makeText(instance, "������¼", Toast.LENGTH_LONG).show();
//										dealMessage(new ReturnMessage("0", "", EventList.LOGOUT, null));
//
//									}
//
//								}
//							});
						}
						is.getGroup(instance, false);
						RadioButton btn = (RadioButton) radioGroup
								.getChildAt(0);
						btn.toggle();
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mBroadcastReceiver, filter);

		notificationButton = (TextView) findViewById(R.id.notification_send);
		notificationButton.setVisibility(View.GONE);
		notificationButton
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(HomeFragment.this,
								NotificationActivity.class);
						startActivity(intent);
						
					}
				});
		enterAnima = new TranslateAnimation(0, 0, 100, 0);
		enterAnima.setInterpolator(new BounceInterpolator());
		enterAnima.setDuration(1000);
		exitAnima = new TranslateAnimation(0, 0, 0, -100);
		exitAnima.setInterpolator(new BounceInterpolator());
		exitAnima.setDuration(1000);
		receiveXgMessageReceiver = new ReceiveXgMessageReceiver();
		IntentFilter xgFilter = new IntentFilter();
		xgFilter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
		xgFilter.addAction("com.tencent.android.tpush.action.FEEDBACK");
		registerReceiver(receiveXgMessageReceiver, xgFilter);

	}

	public void changeFragment(String tag) {

		FragmentManager manager = getSupportFragmentManager();

		fragment = manager.findFragmentByTag(tag);

		if (fragment == null || fragment instanceof EmptyFragment) {

			if (tag.equals(tabs[0])) {
				notificationButton.setVisibility(View.GONE);
				if (RongIM.getInstance() != null
						&& RongIM.getInstance().getRongIMClient() != null) {
					ConversationListFragment listFragment = ConversationListFragment
							.getInstance();
					Uri uri = Uri
							.parse("rong://" + getApplicationInfo().packageName)
							.buildUpon()
							.appendPath("conversationlist")
							.appendQueryParameter(
									Conversation.ConversationType.PRIVATE
											.getName(),
									"false")
							// ����˽�ĻỰ�Ƿ�ۺ���ʾ
							.appendQueryParameter(
									Conversation.ConversationType.GROUP
											.getName(),
									"true")
							.appendQueryParameter(
									Conversation.ConversationType.DISCUSSION
											.getName(),
									"false")
							.appendQueryParameter(
									Conversation.ConversationType.SYSTEM
											.getName(),
									"true")
							.appendQueryParameter(
									Conversation.ConversationType.PUBLIC_SERVICE
											.getName(), "false")
							.appendQueryParameter(
									Conversation.ConversationType.APP_PUBLIC_SERVICE
											.getName(), "false").build();
					listFragment.setUri(uri);
					fragment = listFragment;
					FragmentTransaction ft = manager.beginTransaction();
					ft.replace(R.id.tabcontent, fragment, "conversationlist");
					ft.commitAllowingStateLoss();
					return;
				} else {
					notificationButton.setVisibility(View.GONE);
					fragment = new EmptyFragment();
				}
				// fragment = new ConversationListFragment();
			} else if (tag.equals(tabs[1])) {
//				if (ScContext.getInstance().getUi().getUser_group()
//						.equals("��ʦ")) {
//					notificationButton.setVisibility(View.VISIBLE);
//				} else 
				{
					notificationButton.setVisibility(View.GONE);
				}
				fragment = new ContactFragment();

			} else if (tag.equals(tabs[2])) {
				notificationButton.setVisibility(View.GONE);
				
				fragment = new MoreFragment();
			}
			else if(tag.equals(tabs[3]))
			{
				notificationButton.setVisibility(View.GONE);
				if (badge1.isShown()) {
					badge1.hide(exitAnima);
				}
				fragment = new RealTimeFragment();
			}
		}

		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.tabcontent, fragment);
		ft.commitAllowingStateLoss();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		XGPushManager.registerPush(getApplicationContext(), "*");
		// XGPushManager.unregisterPush(getApplicationContext());
	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.GETGROUP:
			break;

		case EventList.SERVERLOGIN:
			mProgressBar.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			tv.setVisibility(View.VISIBLE);
			RadioButton btn = (RadioButton) radioGroup.getChildAt(0);
			btn.toggle();
			if (RongIM.getInstance() != null
					&& RongIM.getInstance().getRongIMClient() != null) {
				RongIMClientWrapper
				.setConnectionStatusListener(new ConnectionStatusListener() {
					
					@Override
					public void onChanged(ConnectionStatus connectionStatus) {

						switch (connectionStatus) {

						case CONNECTED:// ���ӳɹ���
							instance.runOnUiThread(new Runnable() {
								public void run() {
									mProgressBar.setVisibility(View.GONE);
									loading.setVisibility(View.GONE);
									tv.setVisibility(View.VISIBLE);
								}
							});
							
							break;
						case DISCONNECTED:// �Ͽ����ӡ�
						//	Toast.makeText(instance, "�Ͽ����ӣ�", Toast.LENGTH_LONG).show();
							break;
						case CONNECTING:// �����С�
						//	Toast.makeText(instance, "�����У�", Toast.LENGTH_LONG).show();
							break;
						case NETWORK_UNAVAILABLE:// ���粻���á�

							
							break;
						case KICKED_OFFLINE_BY_OTHER_CLIENT:// �û��˻��������豸��¼�������ᱻ�ߵ���
						//	Toast.makeText(instance, "������¼", Toast.LENGTH_LONG).show();
							instance.dealMessage(new ReturnMessage("0", "", EventList.LOGOUT, null));

						}

					}
				});
				
			}
			break;
		case EventList.LOGIN:
			
			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			if (rm.getCode().equals(EventList.SCUESS)) {
			
				System.out.println("AFTER HomeActivity   "
						+ Thread.currentThread().getName());
				
				is.getGroup(this, false);
				if (ScContext.getInstance().getUi().getUser_group()
						.equals("��ʦ")) {
					is.getNotificationList(this, false);
				}
			} else if (rm.getCode().equals("error")) {
				Toast.makeText(this, "�����������ʧ�ܣ�", Toast.LENGTH_LONG).show();
			} else {
				dialogbuilder = new AlertDialog.Builder(HomeFragment.this)
						.setTitle("�ǳ���ʾ").setMessage("��¼������ȷ���������Ϣ�Ƿ���ȷ��")
						.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								Intent mainIntent = new Intent(
										HomeFragment.this, LoginActivity.class);
								instance.startActivity(mainIntent);
								instance.finish();
								dialog.dismiss();
							}

						});
				this.runOnUiThread(new Runnable() {
					public void run() {
						System.out.println("In UITHREAD   "
								+ Thread.currentThread().getName());
						dialog = dialogbuilder.create();
						dialog.show();
					}
				});

				// dialog.show();

			}
			break;
		case EventList.LOGOUT:
			// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
			// if (rm.getCode().equals(EventList.SCUESS)) {

			logOutbuilder = new AlertDialog.Builder(HomeFragment.this)
					.setTitle("�ǳ���ʾ").setMessage("����һ̨�豸���ô��˺ŵ�¼�����豸�����˳���")
					.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							if (ScContext.getInstance().getUi() != null) {
								XGPushManager.registerPush(
										getApplicationContext(), ScContext
												.getInstance().getUi()
												.getUsertradeid());
							}
							Intent mainIntent = new Intent(HomeFragment.this,
									LoginActivity.class);

							instance.startActivity(mainIntent);

							instance.finish();
							logOutDialog.dismiss();

							cs.clear();

						}
					});
			this.runOnUiThread(new Runnable() {
				public void run() {
					logOutDialog = logOutbuilder.create();
					logOutDialog.show();
				}
			});

			// }

			break;
		case EventList.UPLOADIMG:
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			if (rm.getCode().equals(EventList.SCUESS)) {

				// ((ImageView) findViewById(R.id.imagetitle))
				// .setImageBitmap(bitmap);
				Toast.makeText(this, "�����ɹ�!", Toast.LENGTH_LONG).show();
			} else {
			//	Toast.makeText(this, "����ʧ��!", Toast.LENGTH_LONG).show();
			}

			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		System.out.println("�յ��ص�");
		if (resultCode == RESULT_OK) {
			System.out.println(requestCode + "");
			switch (requestCode) {

			case PicSelectUtils.CROP_PICTURE: //

				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bitmap = null;
					ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
				    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
				    if(bitmapref!=null){  
			            bitmap = (Bitmap) bitmapref.get();  
			        }
					bitmap = extras.getParcelable("data");
					((ImageView) findViewById(R.id.imagetitle))
							.setImageBitmap(bitmap);

					mDialog.setText(R.string.uploading);
					mDialog.show();

					cs.uploadImg(PicSelectUtils.converBitmaptoFile(bitmap),
							this);
				}

				break;
			//
			case PicSelectUtils.TAKE_CROP_PICTURE: //

			//	ImageUtil.deleteTempFile(PicSelectUtils.getRealFilePath(this,imageUri));
				if (PicSelectUtils.imageUri != null) {

				//	PicSelectUtils.imageUri = data.getData();
					System.out.println("��ȡ��������У�" + data.getData());
					
					Bitmap bitmap = null;
					ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
				    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
				    if(bitmapref!=null){  
			            bitmap = (Bitmap) bitmapref.get();  
			        }
					bitmap = PicSelectUtils.decodeUriAsBitmap(
							imageUri, this);// decode
					((ImageView) findViewById(R.id.imagetitle))
							.setImageBitmap(bitmap);

					mDialog.setText(R.string.uploading);
					mDialog.show();
					try {
						cs.uploadImg(
								ImageUtil.compressImage((PicSelectUtils.getRealFilePath(this,
										imageUri)),"TEMP1.jpg", 60), this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
			case PicSelectUtils.CHOOSE_PICTURE: //

				PicSelectUtils.startPhotoZoom(data.getData(), this, 1, 1, 200,
						200);

				break;
			case PicSelectUtils.TAKE_PICTURE:
				System.out.println("�յ�����ص�");
				PicSelectUtils.cropImageUri(imageUri,
						EventList.OUTPUTX, EventList.OUTPUTY,
						PicSelectUtils.TAKE_CROP_PICTURE, this);
				break;
			}
		}
	}

	@Override
	public void finish() {
		// RongIMClientWrapper.setConnectionStatusListener(null);
		unregisterReceiver(mBroadcastReceiver);
		unregisterReceiver(receiveXgMessageReceiver);
		super.finish();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_index:
			tv.setText(tabs[0]);
			changeFragment(tabs[0]);
			break;
		case R.id.radio_contact:
			tv.setText(tabs[1]);
			changeFragment(tabs[1]);
			break;
		case R.id.radio_more:
			tv.setText(tabs[2]);
			changeFragment(tabs[2]);
			break;
		case R.id.radio_query:
			tv.setText(tabs[3]);
			changeFragment(tabs[3]);
			break;
		default:
			break;
		}
	}

	class ReceiveXgMessageReceiver extends XGPushBaseReceiver {

		@Override
		public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNotifactionClickedResult(Context arg0,
				XGPushClickedResult arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNotifactionShowedResult(Context arg0,
				XGPushShowedResult arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRegisterResult(Context arg0, int arg1,
				XGPushRegisterResult arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSetTagResult(Context arg0, int arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		// ��д�˷���
		@Override
		public void onTextMessage(Context context, XGPushTextMessage message) {
			if (context == null || message == null) {
				return;
			}
			String messageType = message.getContent().toString();
			if (!messageType.equals("MESSAGE")) {
				String messageBody = message.getCustomContent().toString();
				// String contentString = message.getContent();
				ps.savePushInfoToContext(messageType, messageBody);
			}
			// Log.i("���͹�������Ϣ��messageString", messageString);
			// Log.i("���͹�������Ϣ��contentString", contentString);
			if (ScContext.getInstance().isNewAboutMe()) {
				badge1.setText(""
						+ ScContext.getInstance().getmCustomContent().size());
				badge1.show(enterAnima);
			} else {
				badge1.setText(R.string.newmessgae);
				badge1.show(enterAnima);
				ScContext.getInstance().setNewMessage(true);

			}

		}

		@Override
		public void onUnregisterResult(Context arg0, int arg1) {

		}

	}

}
