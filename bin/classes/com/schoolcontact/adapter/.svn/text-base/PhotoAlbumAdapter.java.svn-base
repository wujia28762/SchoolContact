package com.schoolcontact.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.PhotoAlbumItem;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.view.MessageDetailActivity;
import com.schoolcontact.view.MessageDetailWithImageActivity;
import com.schoolcontact.widget.CommentListView;

public class PhotoAlbumAdapter extends BaseAdapter {

	private static final String TAG = PhotoAlbumAdapter.class.getSimpleName();
	private List<PhotoAlbumItem> mList;
	private Context mContext;
	private AsyncImageLoader imageLoader;
	private LayoutInflater mLayoutInflater;

	private final Object _LOCK = new Object();
	public static boolean isBusy = false;

	public PhotoAlbumAdapter(Context context, List<PhotoAlbumItem> list) {
		mContext = context;
		mList = list;
		imageLoader = new AsyncImageLoader(mContext);
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void setDatas(List<PhotoAlbumItem> list) {
		this.mList = list;
	}

	public void loadDrawable(boolean flag) {

		synchronized (_LOCK) {

			isBusy = flag;
		}
	}

	@Override
	public int getCount() {

		if (mList != null)
			return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		TextView Time;
		CommentListView contentList;
		PhotoAlbumSunItemAdapter mPhotoAlbumSunItemAdapter;

		public PhotoAlbumSunItemAdapter getmPhotoAlbumSunItemAdapter() {
			return mPhotoAlbumSunItemAdapter;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_photoalbum,
					parent, false);
			viewHolder = new ViewHolder();
			convertView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));// 重点行
			viewHolder.Time = (TextView) convertView
					.findViewById(R.id.tv_ptime);
			viewHolder.contentList = (CommentListView) convertView
					.findViewById(R.id.lv_plist);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final PhotoAlbumItem pa = (PhotoAlbumItem) mList.get(position);

		viewHolder.mPhotoAlbumSunItemAdapter = new PhotoAlbumSunItemAdapter(
				mContext, pa.getmSubList(), imageLoader);
		SpannableString builder = new SpannableString(pa.getTimeStr());

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(50);
		builder.setSpan(sizeSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
		builder.setSpan(styleSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		viewHolder.Time.setText(builder);
		// viewHolder.Time.setText();

		viewHolder.contentList.setAdapter(viewHolder.mPhotoAlbumSunItemAdapter);
		viewHolder.contentList
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						List<FriendMessageInfo> fi = pa.getmSubList();
						imageLoader.setCompress(false);
						if (fi.get(position).getFile_urls().equals("")) {
							Intent it = new Intent(mContext,
									MessageDetailActivity.class);
							Bundle bl = new Bundle();
							bl.putSerializable(PhotoAlbumSunItemAdapter.TAG,
									fi.get(position));
							it.putExtras(bl);
							mContext.startActivity(it);
						} else {

							Intent it = new Intent(mContext,
									MessageDetailWithImageActivity.class);
							Bundle bl = new Bundle();
							bl.putSerializable(PhotoAlbumSunItemAdapter.TAG,
									fi.get(position));
							it.putExtras(bl);
							mContext.startActivity(it);
						}

					}
				});
		// ListViewUtil.setListViewHeightBasedOnChildren(viewHolder.contentList);

		return convertView;
	}
}
