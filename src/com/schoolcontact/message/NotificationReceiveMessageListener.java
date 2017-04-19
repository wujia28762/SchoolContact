package com.schoolcontact.message;

import android.util.Log;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public class NotificationReceiveMessageListener implements OnReceiveMessageListener {

	/**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成。
     */
    @Override
    public boolean onReceived(Message message, int left) {
    	
    	MessageContent messageContent = message.getContent();

        if (messageContent instanceof NotificationMessage) {//文本消息
        	NotificationMessage notificationMessage = (NotificationMessage) messageContent;
            Log.d("NotificationReceiveMessageListener", "onReceived-NotificationMessage:" + notificationMessage.getContent());
        } else {
            Log.d("NotificationReceiveMessageListener", "onReceived-其他消息，自己来判断处理");
        }
        //开发者根据自己需求自行处理
        return false;
    }
}
