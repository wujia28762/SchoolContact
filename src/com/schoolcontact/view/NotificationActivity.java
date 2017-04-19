package com.schoolcontact.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.adapter.AttachmentAdapter;
import com.schoolcontact.model.AttachmentInfo;
import com.schoolcontact.model.ClassInfo;
import com.schoolcontact.model.ItemAttachment;
import com.schoolcontact.model.ItemNotification;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.model.SchoolInfo;
import com.schoolcontact.service.IMService;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.LoadingDialog;
import com.schoolcontact.utils.PicSelectUtils;

public class NotificationActivity extends BaseActivity {

	// private LayoutInflater layoutInflater;
	// private LinearLayout linearLayout;
	private LinearLayout sortLayout;
	private LinearLayout groupLayout;
	private LinearLayout addAttachmentLayout;
	private TextView sortContent;
	private TextView groupContent;
	private String notificationSort = "";
	// private String notificationName = "";
	// private Bitmap bitmap;
	private UserService cs = new UserService();
	private IMService is = new IMService();
	private ListView attachmentListView;
	private AttachmentAdapter attachmentAdapter;
	private List<ItemAttachment> data;
	private TextView notificationSendButton;
	private TextView backText;
	private EditText contentView;
	private String notificationClassName, notificationClassId,
			notificationGroupName, notificationGroupId;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notification_send_button:
			if(!mDialog.isShowing())
			{
				mDialog.setText(R.string.publishing);
				mDialog.show();
			}
			is.notificationSend(this, sortContent.getText().toString().trim(),
					contentView.getText().toString().trim(),
					new ItemNotification(notificationClassName,
							notificationClassId, notificationGroupName,
							notificationGroupId), data);
			break;
		case R.id.backtext:
			finish();
			break;
		case R.id.sort_layout:
			final AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
			alertDialog.show();
			alertDialog.setCanceledOnTouchOutside(true);
			Window window = alertDialog.getWindow();
			window.setGravity(Gravity.BOTTOM);
			window.setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			window.setContentView(R.layout.get_notification_sort);
			View view = window.getDecorView();

			view.findViewById(R.id.announcement_bt).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							sortContent.setText(R.string.announcement_string);
							sccontext
									.getmPreferences()
									.edit()
									.putString(
											"notification_sort_"
													+ sccontext.getCurr_user(),
											sortContent.getText().toString()
													.trim()).commit();
							alertDialog.dismiss();
						}

					});
			view.findViewById(R.id.task_bt).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							sortContent.setText(R.string.task_string);
							sccontext
									.getmPreferences()
									.edit()
									.putString(
											"notification_sort_"
													+ sccontext.getCurr_user(),
											sortContent.getText().toString()
													.trim()).commit();
							alertDialog.dismiss();
						}

					});
			view.findViewById(R.id.cancel_bt).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});

			break;

		case R.id.group_layout:
			Intent intent = new Intent(this, NotificationListActivity.class);
			startActivity(intent);
			break;

		case R.id.add_attachment_layout:
			PicSelectUtils.doPickPhotoAction(this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		notificationSort = sccontext.getmPreferences().getString(
				"notification_sort_" + sccontext.getCurr_user(), "");
		if (!notificationSort.equals("")) {
			sortContent.setText(notificationSort);
		} else {
//			sortContent.setText(R.string.announcement_string);
//			sccontext
//					.getmPreferences()
//					.edit()
//					.putString("notification_sort_" + sccontext.getCurr_user(),
//							sortContent.getText().toString().trim()).commit();
		}
		notificationClassName = sccontext.getmPreferences().getString(
				"notification_class_name_" + sccontext.getCurr_user(), "");
		notificationClassId = sccontext.getmPreferences().getString(
				"notification_class_id_" + sccontext.getCurr_user(), "");
		notificationGroupName = sccontext.getmPreferences().getString(
				"notification_group_name_" + sccontext.getCurr_user(), "");
		notificationGroupId = sccontext.getmPreferences().getString(
				"notification_group_id_" + sccontext.getCurr_user(), "");
		if (!notificationClassName.equals("")) {
			String string = notificationClassName;
			if (!notificationGroupName.equals("")) {
				string += "-" + notificationGroupName;
			}
			groupContent.setText(string);
		} else if (!getFirstOfNotificationList().equals("")) {
		//	groupContent.setText(getFirstOfNotificationList());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notification);
		onInit();
	}

	private String getFirstOfNotificationList() {
		if (sccontext.getNotificationListinfo() != null
				&& sccontext.getNotificationListinfo().getResults() != null
				&& sccontext.getNotificationListinfo().getResults().size() != 0) {
			for (SchoolInfo subgroupinfo : sccontext.getNotificationListinfo()
					.getResults()) {
				for (ClassInfo cf : subgroupinfo.getContents()) {
					return cf.getName();
				}
			}
		}
		return "";
	}

	public void onInit() {

		sortLayout = (LinearLayout) findViewById(R.id.sort_layout);
		groupLayout = (LinearLayout) findViewById(R.id.group_layout);
		addAttachmentLayout = (LinearLayout) findViewById(R.id.add_attachment_layout);
		mDialog = new LoadingDialog(this);
		sortLayout.setOnClickListener(this);
		groupLayout.setOnClickListener(this);
		addAttachmentLayout.setOnClickListener(this);

		sortContent = (TextView) findViewById(R.id.sort_content);
		groupContent = (TextView) findViewById(R.id.group_content);

		attachmentListView = (ListView) findViewById(R.id.attachment_list);
		data = new ArrayList<ItemAttachment>();
		attachmentAdapter = new AttachmentAdapter(this, data);
		attachmentListView.setAdapter(attachmentAdapter);

		contentView = (EditText) findViewById(R.id.notification_content);
		notificationSendButton = (TextView) findViewById(R.id.notification_send_button);
		notificationSendButton.setOnClickListener(this);
		backText = (TextView) findViewById(R.id.backtext);
		backText.setOnClickListener(this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		System.out.println("收到回调");
		if (resultCode == RESULT_OK) {
			System.out.println(requestCode + "");
			switch (requestCode) {
			case PicSelectUtils.CROP_PICTURE: //
				Uri uri = data.getData();
				if (uri != null) {
					try {
						cs.uploadNotificationImg(
								ImageUtil.compressImage((PicSelectUtils.getRealFilePath(this,
										uri)),"通知图片.jpg", 60),
								this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case PicSelectUtils.TAKE_PICTURE:
				Uri takeUri = data.getData();
				if (takeUri != null) {
					cs.uploadNotificationImg(
							new File(PicSelectUtils.getRealFilePath(this,
									takeUri)), this);
				} else {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						Bitmap bitmap = null;
						ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
					    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
					    if(bitmapref!=null){  
				            bitmap = (Bitmap) bitmapref.get();  
				        }
						
						bitmap = (Bitmap) bundle.get("data");
						takeUri = Uri.parse(MediaStore.Images.Media
								.insertImage(getContentResolver(), bitmap, null,
										null));
					}
					try {
						cs.uploadNotificationImg(
								ImageUtil.compressImage((PicSelectUtils.getRealFilePath(this,takeUri)),"通知图片.jpg", 60), this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case PicSelectUtils.CHOOSE_PICTURE: //

				if(data.getData()!=null){
					Uri uri44 = null;
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
						String url = PicSelectUtils.getPath(this, data.getData());
						uri44 = Uri.parse(url);
					}else{
						uri44 = data.getData();
					}
					try {
						cs.uploadNotificationImg(
								ImageUtil.compressImage((PicSelectUtils.getRealFilePath(this,uri44)),"通知图片.jpg", 60), this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
			}
		}
	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.ADDATTACHMENT:
			if (rm.getCode().equals(EventList.SCUESS)) {
				Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
				if (rm.getData() instanceof AttachmentInfo) {
					AttachmentInfo attachmentInfo = (AttachmentInfo) rm
							.getData();
					ItemAttachment itemAttachment = new ItemAttachment(
							attachmentInfo.getFile_url(),
							attachmentInfo.getFile_name(),
							attachmentInfo.getFile_type(),
							attachmentInfo.getFile_size());
					data.add(itemAttachment);
					attachmentAdapter.notifyDataSetChanged();
				}
			} else {
				Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
			}
			break;
		case EventList.NOTIFICATIONSEND:
			if (rm.getCode().equals(EventList.SCUESS)) {
				Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
				if(mDialog.isShowing())
				{
					//mDialog.setText(R.string.publishing);
					mDialog.dismiss();
				}
				finish();
			} else {
				String errorTip = "";
				if (rm.getMess().equals("4070011")) {
					errorTip = "发送失败,服务端保存失败";
				} else if (rm.getMess().equals("4070012")) {
					errorTip = "发送失败，融云错误";
				}
				Toast.makeText(this, "错误:" + errorTip, Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			break;
		}
	}
}
