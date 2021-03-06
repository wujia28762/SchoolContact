package com.schoolcontact.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.model.CommentInfo;

public class CommentListViewAdapter extends BaseAdapter {

	public static final String TAG = "CommentListViewAdapter";
	private LayoutInflater inflater;
	private Context context;

	private List<CommentInfo> mList;

	public List<CommentInfo> getmList() {
		return mList;
	}

	public void setmList(List<CommentInfo> mList) {
		this.mList = mList;
	}

	public CommentListViewAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public CommentListViewAdapter(Context context, List<CommentInfo> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mList = list;
	}

	@Override
	public int getCount() {

		if(mList!=null)
		return mList.size();
		else
			return 0;
	}

	public void add(CommentInfo p) {
		if (this.mList != null)
			this.mList.add(p);
	}

	@Override
	public Object getItem(int arg0) {

		if(mList!=null)
		return this.mList.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	public class ViewHolder {

		private TextView mTextView;
		private TextView bemTextView;
		private TextView huifu;
		private TextView content;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.listview_comment_item, null);
			viewHolder = new ViewHolder();

			
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.tv_comment_text);
			viewHolder.bemTextView = (TextView) convertView
					.findViewById(R.id.tv_be_comment_text);
			viewHolder.huifu = (TextView) convertView
					.findViewById(R.id.re_comment_text);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.comment_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// String text = mList.get(arg0).
		if (mList.size() > 0) {
			// viewHolder.mll_commentlist.setVisibility(View.VISIBLE);
			System.out.println("!!!!!!!!!!!!!" + mList.get(arg0).getUname());
			if (TextUtils.isEmpty(mList.get(arg0).getTo_uname())
					&& !TextUtils.isEmpty(mList.get(arg0).getUname())) {

				SpannableStringBuilder builder = new SpannableStringBuilder(
						mList.get(arg0).getUname()+":");

				viewHolder.huifu.setVisibility(View.GONE);
				viewHolder.bemTextView.setVisibility(View.GONE);
				// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
				ForegroundColorSpan redSpan = new ForegroundColorSpan(
						Color.parseColor("#336699"));
				builder.setSpan(redSpan, 0,
						mList.get(arg0).getUname().length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.content.setVisibility(View.VISIBLE);
				viewHolder.content.setText(mList.get(arg0).getContent());
				viewHolder.mTextView.setText(builder);
			} else if (!TextUtils.isEmpty(mList.get(arg0).getTo_uname())
					&& !TextUtils.isEmpty(mList.get(arg0).getUname())) {

				SpannableStringBuilder builder = new SpannableStringBuilder(
						mList.get(arg0).getUname());
				SpannableStringBuilder builder1 = new SpannableStringBuilder(
						mList.get(arg0).getTo_uname()+":");
				// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
				ForegroundColorSpan redSpan = new ForegroundColorSpan(
						Color.parseColor("#336699"));
				builder.setSpan(redSpan, 0,
						mList.get(arg0).getUname().length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder1.setSpan(redSpan, 0, mList.get(arg0).getTo_uname()
						.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.mTextView.setText(builder);
				viewHolder.bemTextView.setText(builder1);
				viewHolder.huifu.setVisibility(View.VISIBLE);
				viewHolder.bemTextView.setVisibility(View.VISIBLE);
				viewHolder.content.setText(mList.get(arg0).getContent());
				viewHolder.content.setVisibility(View.VISIBLE);
				viewHolder.bemTextView
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
//								Toast.makeText(context, "点击了姓名！2！",
//										Toast.LENGTH_SHORT).show();

							}
						});
			}

			viewHolder.mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Toast.makeText(context, "点击了姓名！！", Toast.LENGTH_SHORT)
//							.show();
				}
			});
		}
		return convertView;
	}
}
