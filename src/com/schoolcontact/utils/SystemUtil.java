package com.schoolcontact.utils;

import com.schoolcontact.Base.ScContext;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;

public class SystemUtil {
	// ������Ҫ����һ��Notification�Ĳ���
	@SuppressWarnings("deprecation")
	public static void setAlarmParams(Notification notification,
			Context mAppContext) {
		// AudioManager provides access to volume and ringer mode control.
		AudioManager volMgr = (AudioManager) mAppContext
				.getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()) {// ��ȡϵͳ���õ�����ģʽ
		case AudioManager.RINGER_MODE_SILENT:// ����ģʽ��ֵΪ0����ʱ���𶯣�������
			notification.sound = null;
			notification.vibrate = null;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:// ��ģʽ��ֵΪ1����ʱ���𶯣�������
			notification.sound = null;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			break;
		case AudioManager.RINGER_MODE_NORMAL:// ����ģʽ��ֵΪ2�������������1_���嵫���𶯣�2_����+��
			Uri ringTone = null;
			// ��ȡ���������
			SharedPreferences sp = ScContext.getInstance().getmPreferences();
			if (!sp.contains(EventList.KEY_RING_TONE)) {// ���û�����������ļ�����ô��������������
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.defaults |= Notification.DEFAULT_SOUND;
			} else {
				String ringFile = sp.getString(EventList.KEY_RING_TONE, null);
				if (ringFile == null) {// ��ֵ��Ϊ�գ�����������
					ringTone = null;
				} else if (!TextUtils.isEmpty(ringFile)) {// ��������1��Ĭ��2�Զ��壬������һ��uri
					ringTone = Uri.parse(ringFile);
				}
				notification.sound = ringTone;

				boolean vibrate = sp.getBoolean(EventList.KEY_NEW_MAIL_VIBRATE,
						true);
				if (vibrate == false) {// ���������ò��𶯣���ô�Ͳ�����
					notification.vibrate = null;
				} else {// ���������Ҫ�𶯣���ʱ��Ҫ��ϵͳ����ô���õģ�����=0;��=1�����ھ���ģʽ����=2��
					if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
						// ����
						notification.vibrate = null;
					} else if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
						// ֻ�ھ���ʱ��
						notification.vibrate = null;
					} else {
						// ��
						notification.defaults |= Notification.DEFAULT_VIBRATE;
					}
				}
			}
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;// ��������
			break;
		default:
			break;
		}
	}
}
