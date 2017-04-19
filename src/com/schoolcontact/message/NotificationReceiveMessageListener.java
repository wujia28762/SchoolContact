package com.schoolcontact.message;

import android.util.Log;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public class NotificationReceiveMessageListener implements OnReceiveMessageListener {

	/**
     * �յ���Ϣ�Ĵ���
     *
     * @param message �յ�����Ϣʵ�塣
     * @param left    ʣ��δ��ȡ��Ϣ��Ŀ��
     * @return �յ���Ϣ�Ƿ�����ɡ�
     */
    @Override
    public boolean onReceived(Message message, int left) {
    	
    	MessageContent messageContent = message.getContent();

        if (messageContent instanceof NotificationMessage) {//�ı���Ϣ
        	NotificationMessage notificationMessage = (NotificationMessage) messageContent;
            Log.d("NotificationReceiveMessageListener", "onReceived-NotificationMessage:" + notificationMessage.getContent());
        } else {
            Log.d("NotificationReceiveMessageListener", "onReceived-������Ϣ���Լ����жϴ���");
        }
        //�����߸����Լ��������д���
        return false;
    }
}
