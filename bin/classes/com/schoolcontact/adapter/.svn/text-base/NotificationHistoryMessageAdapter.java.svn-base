package com.schoolcontact.adapter;

import io.rong.imlib.model.Message;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.message.NotificationMessage;
import com.schoolcontact.view.NotificationDisplayActivity;

public class NotificationHistoryMessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Message> data;

	public NotificationHistoryMessageAdapter(Context context,
			List<Message> resData) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.data = resData;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.notification_message_item, null);
			holder.notificationType = (TextView) convertView
					.findViewById(R.id.notification_type);
			holder.notificationTime = (TextView) convertView
					.findViewById(R.id.notification_time);
			holder.notificationSender = (TextView) convertView
					.findViewById(R.id.notification_sender);
			holder.notificationReceiver = (TextView) convertView
					.findViewById(R.id.notification_receiver);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Date date = new Date(); 
		date.setTime(data.get(position).getReceivedTime());
		java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    final String timeString = format.format(date);
	    
		holder.notificationTime.setText(timeString);
		if(data.get(position).getContent() instanceof NotificationMessage){
			final NotificationMessage notificationMessage = (NotificationMessage) data.get(position).getContent();
			holder.notificationSender.setText("发送人：" + notificationMessage.getFromname());
			holder.notificationType.setText(notificationMessage.getMessage_type());
			holder.notificationReceiver.setText("接收人：" + notificationMessage.getTonames());
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), NotificationDisplayActivity.class);
					intent.putExtra("time", timeString);
					intent.putExtra("notice_message", notificationMessage);
					v.getContext().startActivity(intent);
				}
			});
		}
		
		return convertView;
	}

	class ViewHolder {
		TextView notificationType;
		TextView notificationTime;
		TextView notificationSender;
		TextView notificationReceiver;
	}

}
