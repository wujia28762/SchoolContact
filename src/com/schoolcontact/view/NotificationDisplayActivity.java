package com.schoolcontact.view;

import java.util.List;

import net.tsz.afinal.core.Arrays;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school_contact.main.R;
import com.schoolcontact.adapter.NotificationAttachmentAdapter;
import com.schoolcontact.message.NotificationMessage;
import com.schoolcontact.model.AttachmentInfo;
import com.schoolcontact.service.UserService;

public class NotificationDisplayActivity extends BaseActivity{
	
	private List<AttachmentInfo> attachments; 
	private ObjectMapper objectMapperPrivate = new ObjectMapper();
	private TextView sendTime;
	private TextView senderName, receiverName, notificationTypeTextView;
	private TextView notificationContent;
	private ListView attachmentListView;
	private NotificationAttachmentAdapter notificationAttachmentAdapter;
	private LinearLayout layout;
	private UserService us = new UserService();
	private TextView backTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notification_display);
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

	
	@Override
	public void onInit() {
		Intent intent = getIntent();
		String timeString = intent.getStringExtra("time");
		NotificationMessage notificationMessage = intent.getParcelableExtra("notice_message");
		String notifcationType = notificationMessage.getMessage_type();
		String fromName = notificationMessage.getFromname();
		String toName = notificationMessage.getTonames();
		String contentString = notificationMessage.getContent();
		String attachmentString = notificationMessage.getAttachments();
		AttachmentInfo[] attachmentArray = null;
		try {
			attachmentArray = objectMapperPrivate.readValue(attachmentString, AttachmentInfo[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(attachmentArray!=null&&attachmentArray.length>0){
			attachments = Arrays.asList(attachmentArray);
		}
		notificationTypeTextView = (TextView) findViewById(R.id.notification_display_type);
		sendTime = (TextView) findViewById(R.id.notification_display_time);
		senderName = (TextView) findViewById(R.id.notification_display_sender);
		receiverName = (TextView) findViewById(R.id.notification_display_receiver);
		backTextView = (TextView) findViewById(R.id.backtext);
		backTextView.setOnClickListener(this);
		notificationContent = (TextView) findViewById(R.id.notification_display_content);
		attachmentListView = (ListView) findViewById(R.id.notification_display_attachment);
		notificationAttachmentAdapter = new NotificationAttachmentAdapter(this, attachments);
		attachmentListView.setAdapter(notificationAttachmentAdapter);
		
		notificationTypeTextView.setText(notifcationType);
		senderName.setText(fromName);
		receiverName.setText(toName);
		sendTime.setText(timeString);
		notificationContent.setText(contentString);
		
	}
	
}
