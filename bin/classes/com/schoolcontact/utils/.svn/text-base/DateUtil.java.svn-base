package com.schoolcontact.utils;

import java.text.SimpleDateFormat;

import android.text.format.Time;

public class DateUtil {

	private static String[] month = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
	/**
	 * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
	 * 
	 * @param context
	 * @param when
	 * @return String
	 */
	public static String formatTimeString(long when) {
		Time then = new Time();
		then.set(when);
		Time now = new Time();
		now.setToNow();

		String formatStr;
		if (then.year != now.year) {
			formatStr = "yyyy/MM/dd";
		} else if (then.yearDay != now.yearDay) {
			// If it is from a different day than today, show only the date.
			formatStr = "MM/dd";
		} else {
			// Otherwise, if the message is from today, show the time.
			formatStr = "HH:MM";
		}

		// if (then.year == now.year && then.yearDay == now.yearDay) {
		// return context.getString(R.string.date_today);
		// } else if ((then.year == now.year) && ((now.yearDay - then.yearDay)
		// == 1)) {
		// return context.getString(R.string.date_yesterday);
		// } else {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String temp = sdf.format(when);
		if (temp != null && temp.length() == 5
				&& temp.substring(0, 1).equals("0")) {
			temp = temp.substring(1);
		}
		return temp;
		// }
	}
	public static String formatTimeStringToData(long when) {
		Time then = new Time();
		then.set(when);
//		Time now = new Time();
//		now.setToNow();
//
//		String formatStr;
//		if (then.year != now.year) {
//			formatStr = "yyyy/MM/dd";
//		} else if (then.yearDay != now.yearDay) {
//			// If it is from a different day than today, show only the date.
//			formatStr = "MM/dd";
//		} else {
//			// Otherwise, if the message is from today, show the time.
//			formatStr = "HH:MM";
//		}
//
//		// if (then.year == now.year && then.yearDay == now.yearDay) {
//		// return context.getString(R.string.date_today);
//		// } else if ((then.year == now.year) && ((now.yearDay - then.yearDay)
//		// == 1)) {
//		// return context.getString(R.string.date_yesterday);
//		// } else {
//		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
//		String temp = sdf.format(when);
//		if (temp != null && temp.length() == 5
//				&& temp.substring(0, 1).equals("0")) {
//			temp = temp.substring(1);
//		}
		
		if(then.monthDay<10)
		{
			return "0"+then.monthDay+month[then.month];
		}
		return then.monthDay+month[then.month];
		// }
	}
}
