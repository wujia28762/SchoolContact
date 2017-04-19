package com.schoolcontact.adapter;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.PhotoAlbumSubItem;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.view.MessageDetailActivity;
import com.schoolcontact.view.MessageDetailWithImageActivity;
import com.schoolcontact.widget.GridView;

public class PhotoAlbumSunItemAdapter extends BaseAdapter {

	public static final String TAG = PhotoAlbumSunItemAdapter.class
			.getSimpleName();
	private List<FriendMessageInfo> mList;
	private Context mContext;
	private AsyncImageLoader imageLoader;
	private LayoutInflater mLayoutInflater;
	private List<String> urlList;
	
	public PhotoAlbumSunItemAdapter(Context context,
			List<FriendMessageInfo> list,AsyncImageLoader ail ) {
		mContext = context;
		mList = list;
		imageLoader = ail;
//		imageLoader.setCompress(true);
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	

	public void setDatas(List<FriendMessageInfo> list) {
		this.mList = list;
	}

	public void add(FriendMessageInfo data) {
		if (this.mList != null)
			this.mList.add(data);
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

	private static class ViewHolder {
		TextView content;
		ImageView Imagecontent;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_photoalbumsub,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.tv_content);
			
			viewHolder.Imagecontent = (ImageView) convertView
					.findViewById(R.id.iv_iamgecontent);
			//viewHolder.Imagecontent.getLayoutParams()
			viewHolder.Imagecontent.setLayoutParams(new LinearLayout.LayoutParams(120,120));//重点行

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		urlList = new ArrayList<String>();
		System.out.println("!!!!!!!!!!!@#@!#" + mList.size());
		final FriendMessageInfo pa = (FriendMessageInfo) mList.get(position);
		System.out.println(pa.getContent());

		if (pa != null) {
			viewHolder.content.setText(pa.getContent());
//			viewHolder.content.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					
//
//				}
//			});
		//	viewHolder.Imagecontent.setImageResource(R.drawable.ic_launcher);
			if (!TextUtils.isEmpty(pa.getFile_urls()))
				for (String url : pa.getFile_urls().split("\\,")) {
					String tempUrl = EventList.BASEURL;
					
					if (url.startsWith("/"))
						url = url.replaceFirst("/", "");
					tempUrl = tempUrl.concat(url);
					Log.i("临时拼接URL", tempUrl);
					urlList.add(tempUrl);
				}
			// final String imgUrl =
			// "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";
			if(urlList.size()>0)
			{
				viewHolder.Imagecontent.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.Imagecontent.setVisibility(View.GONE);
			}
			if (urlList.size()>0&&!TextUtils.isEmpty(urlList.get(0))&&!PhotoAlbumAdapter.isBusy) {
				viewHolder.Imagecontent.setTag(urlList.get(0));
				Bitmap bitmap = null;
				ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
			    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
			    if(bitmapref!=null){  
		            bitmap = (Bitmap) bitmapref.get();  
		        }
				bitmap = imageLoader.loadImage(viewHolder.Imagecontent,
						urlList.get(0));
				if (bitmap != null) {
					viewHolder.Imagecontent.setImageBitmap(bitmap);
				}
				
			}
			
			// viewHolder.Time.setText();

		}
		return convertView;
	}

}
