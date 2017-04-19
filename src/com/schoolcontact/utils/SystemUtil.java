package com.schoolcontact.utils;

import com.schoolcontact.Base.ScContext;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;

public class SystemUtil {
	// 首先需要接收一个Notification的参数
	@SuppressWarnings("deprecation")
	public static void setAlarmParams(Notification notification,
			Context mAppContext) {
		// AudioManager provides access to volume and ringer mode control.
		AudioManager volMgr = (AudioManager) mAppContext
				.getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
		case AudioManager.RINGER_MODE_SILENT:// 静音模式，值为0，这时候不震动，不响铃
			notification.sound = null;
			notification.vibrate = null;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:// 震动模式，值为1，这时候震动，不响铃
			notification.sound = null;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			break;
		case AudioManager.RINGER_MODE_NORMAL:// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
			Uri ringTone = null;
			// 获取软件的设置
			SharedPreferences sp = ScContext.getInstance().getmPreferences();
			if (!sp.contains(EventList.KEY_RING_TONE)) {// 如果没有生成配置文件，那么既有铃声又有震动
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.defaults |= Notification.DEFAULT_SOUND;
			} else {
				String ringFile = sp.getString(EventList.KEY_RING_TONE, null);
				if (ringFile == null) {// 无值，为空，不播放铃声
					ringTone = null;
				} else if (!TextUtils.isEmpty(ringFile)) {// 有铃声：1，默认2自定义，都返回一个uri
					ringTone = Uri.parse(ringFile);
				}
				notification.sound = ringTone;

				boolean vibrate = sp.getBoolean(EventList.KEY_NEW_MAIL_VIBRATE,
						true);
				if (vibrate == false) {// 如果软件设置不震动，那么就不震动了
					notification.vibrate = null;
				} else {// 否则就是需要震动，这时候要看系统是怎么设置的：不震动=0;震动=1；仅在静音模式下震动=2；
					if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
						// 不震动
						notification.vibrate = null;
					} else if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
						// 只在静音时震动
						notification.vibrate = null;
					} else {
						// 震动
						notification.defaults |= Notification.DEFAULT_VIBRATE;
					}
				}
			}
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;// 都给开灯
			break;
		default:
			break;
		}
	}
}
