package com.schoolcontact.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.model.CustomContent;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.DateUtil;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ListUtils;

public class PushDtailAdapter extends BaseAdapter {

	private List<CustomContent> mData;

	private Context context;

	private AsyncImageLoader imageLoader;

	private LayoutInflater mLayoutInflater;

	public PushDtailAdapter(Context pContext, List<CustomContent> pData) {
		context = pContext;
		mData = pData;
		imageLoader = new AsyncImageLoader(pContext);
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (mData != null)
			return mData.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mData != null)
			return mData.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mLayoutInflater.inflate(
					R.layout.listview_item_pushdetail, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.messageContent = (TextView) convertView
					.findViewById(R.id.tv_messagecontent);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.commentContent = (TextView) convertView
					.findViewById(R.id.tv_commentcontent);
			viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.iconUrl = (ImageView) convertView
					.findViewById(R.id.iv_post_head);
			viewHolder.ImageContentUrl = (ImageView) convertView
					.findViewById(R.id.iv_content_img);
			viewHolder.AgreeContentUrl = (ImageView) convertView
					.findViewById(R.id.iv_head_img);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!ListUtils.isEmpty(mData)) {
			CustomContent cc = mData.get(position);

			if (!TextUtils.isEmpty(cc.getMessage_file())) {
				String tempUrl = EventList.BASEURL;
				if (cc.getMessage_file().startsWith("/")) {
					String url = cc.getMessage_file().replaceFirst("/", "");
					tempUrl = tempUrl.concat(url);
					cc.setMessage_file(tempUrl);
				}

				viewHolder.ImageContentUrl.setVisibility(View.VISIBLE);
				viewHolder.messageContent.setVisibility(View.GONE);
				if (!TextUtils.isEmpty(cc.getMessage_file())) {
					viewHolder.ImageContentUrl.setTag(cc.getMessage_file());
					Bitmap bitmap = imageLoader.loadImage(
							viewHolder.ImageContentUrl, cc.getMessage_file());
					if (bitmap != null) {
						viewHolder.ImageContentUrl.setImageBitmap(bitmap);
					}

				}
			} else {
				viewHolder.ImageContentUrl.setVisibility(View.GONE);
				viewHolder.messageContent.setVisibility(View.VISIBLE);
				viewHolder.messageContent.setText(cc.getMessage_content());
			}
			if (cc.getComment_content()!=null) {
				viewHolder.commentContent.setVisibility(View.VISIBLE);
				viewHolder.commentContent.setText(cc.getComment_content());
			} else {
				viewHolder.commentContent.setVisibility(View.GONE);
				viewHolder.AgreeContentUrl.setVisibility(View.VISIBLE);
			}
			viewHolder.time.setText(DateUtil.formatTimeString(Long.valueOf(cc
					.getComment_time())));
			viewHolder.name.setText(cc.getComment_uname());
			if (!TextUtils.isEmpty(cc.getComment_portraitUri())) {
				viewHolder.iconUrl.setTag(cc.getComment_portraitUri());
				Bitmap bitmap = imageLoader.loadImage(viewHolder.iconUrl,
						cc.getComment_portraitUri());
				if (bitmap != null) {
					viewHolder.iconUrl.setImageBitmap(bitmap);
				}

			}
		}
		return convertView;
	}

	public void setmData(List<CustomContent> mData) {
		this.mData = mData;
	}

	private class ViewHolder {
		ImageView AgreeContentUrl;
		TextView name;
		TextView messageContent;
		TextView time;
		TextView commentContent;
		ImageView iconUrl;
		ImageView ImageContentUrl;

	}

}
