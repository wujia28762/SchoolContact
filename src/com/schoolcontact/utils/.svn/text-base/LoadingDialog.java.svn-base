package com.schoolcontact.utils;


import com.school_contact.main.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class LoadingDialog extends Dialog
{
  private TextView mTextView;

  public LoadingDialog(Context context)
  {
    super(context, R.style.RcDialog);

    setContentView(R.layout.dialog_loading);
    this.mTextView = ((TextView)findViewById(R.id.loadding_message));
  }

  public void show()
  {
    super.show();
  }

  public void dismiss()
  {
    super.dismiss();
  }

  public void setText(String s) {
    if (this.mTextView != null) {
      this.mTextView.setText(s);
      this.mTextView.setVisibility(0);
    }
  }

  public void setText(int res) {
    if (this.mTextView != null) {
      this.mTextView.setText(res);
      this.mTextView.setVisibility(0);
    }
  }

  public void setTextColor(int color) {
    this.mTextView.setTextColor(color);
  }

  public boolean onTouchEvent(MotionEvent event)
  {
    if (event.getAction() == 0) {
      Log.d("LoadingDialog", "onTouchEvent");
      return false;
    }
    return super.onTouchEvent(event);
  }
}