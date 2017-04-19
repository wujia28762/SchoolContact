package com.schoolcontact.utils;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.school_contact.main.R;
import com.schoolcontact.Base.NotifyListener;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.GroupInfo;

public class DialogUtils {

	private static Dialog dialog;
	private static Button mButton;
	private static EditText mEdittext;
	public final static int CUSTOMDIALOGRETURN = 2999;

	public static EditText getmEdittext() {
		return mEdittext;
	}

	public static Dialog getDialog() {
		return dialog;
	}

	public static Button getmButton() {
		return mButton;
	}

	public static void getAlertDialog(String text, final Activity context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(text);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				context.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	

	public static void getcustomDialog(final Context context, int layout) {
		dialog = new Dialog(context);
		// dialog.show();
		// dialog.setCanceledOnTouchOutside(true);

		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		// window.setWindowAnimations(R.style.Animation_Dialog_Bottom);
		window.setBackgroundDrawable(new ColorDrawable(0));
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		window.setContentView(layout);
		// View view = window.getDecorView();
		mEdittext = (EditText) window.findViewById(R.id.et_inputcomments);
		mButton = (Button) window.findViewById(R.id.bt_submitcomments);
		// return true;

	}

	
}
