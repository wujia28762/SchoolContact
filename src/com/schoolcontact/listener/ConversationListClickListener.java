package com.schoolcontact.listener;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.MessageContent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.schoolcontact.view.NotificationListDisplayActivity;

public class ConversationListClickListener implements ConversationListBehaviorListener{

	@Override
	public boolean onConversationClick(Context arg0, View arg1,
			UIConversation arg2) {
		String targetId = arg2.getConversationTargetId();
		ConversationType conversationType = arg2.getConversationType();
		int latestMessageId = arg2.getLatestMessageId();
		Conversation conversation = RongIMClient.getInstance().getConversation(conversationType, targetId);
		
		MessageContent message = conversation.getLatestMessage();//RongIMClient.getInstance().getLatestMessages(conversationType, targetId, latestMessageId).get(0).getContent();
		String messageName = conversation.getLatestMessage().getClass().getSimpleName();
		
		if(messageName.equals("NotificationMessage")){
			
			Log.i("此会话是", "通知");
			Intent intent = new Intent(arg0, NotificationListDisplayActivity.class);
			intent.putExtra("conversationTypeValue", conversationType.getValue());
			intent.putExtra("targetId", targetId);
			intent.putExtra("latestMessageId", latestMessageId);
			arg0.startActivity(intent);
			arg2.setUnReadMessageCount(0);
		//	arg1.postInvalidate();
			 RongContext.getInstance().getEventBus().post(new Event.ConversationUnreadEvent(conversationType,targetId));
			return true;
		}
		return false;
	}

	@Override
	public boolean onConversationLongClick(Context arg0, View arg1,
			UIConversation arg2) {
		return false;
	}

}
