package com.schoolcontact.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

public class MyConversationBehaviorListener implements ConversationBehaviorListener{

	@Override
	public boolean onMessageClick(Context arg0, View arg1, Message arg2) {
		if(arg2.getContent() instanceof ImageMessage)
		{
			ImageMessage mImageMessage = (ImageMessage) arg2.getContent();
		//	Intent it = new Intent(arg0,PhotoActivity.class);
			
		}
		return false;
	}

	@Override
	public boolean onMessageLongClick(Context arg0, View arg1, Message arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUserPortraitClick(Context arg0, ConversationType arg1,
			UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context arg0, ConversationType arg1,
			UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
