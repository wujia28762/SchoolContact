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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.ImageViewWithUrl;
import com.schoolcontact.model.PhotoAlbumItem;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.view.ImageScanActivity;
import com.schoolcontact.view.MessageDetailWithImageActivity;
import com.schoolcontact.widget.GridView;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private AsyncImageLoader imageLoader;
	private int arg;
	private List<String> urlList;
	private int wh;

	public GridViewAdapter(Context context, int arg) {
		this.arg = arg;
		this.context = context;
		inflater = LayoutInflater.from(context);

	}

	@SuppressWarnings("deprecation")
	public GridViewAdapter(Context context, List<String> p,
			AsyncImageLoader imageLoader) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.urlList = p;
		this.imageLoader = imageLoader;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
	//	wh = wm.getDefaultDisplay().getWidth();
		wh = ((wm.getDefaultDisplay().getWidth()-170-ImageUtil.Dp2Px(context, 75))-(4*ImageUtil.Dp2Px(context, 2)))/3;
		System.out.println("!!屏幕宽度！！！SP"+wh+"");
	}

	@Override
	public int getCount() {
		if (urlList != null)

			return urlList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (urlList != null)

			return urlList.get(arg0);
		else
			return null;

	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public static class ViewHolder {

		private ImageViewWithUrl imagewithurl;

	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.img_gridview_item, null);
		//	int wh1 = ImageUtil.Dp2Px(context, wh);
		//	System.out.println("!!屏幕宽度！！！DP"+wh1+"");
			convertView.setLayoutParams(new GridView.LayoutParams(wh,wh));// 重点行
			viewHolder = new ViewHolder();
			viewHolder.imagewithurl = new ImageViewWithUrl();
			viewHolder.imagewithurl.setmImageView((ImageView) convertView
					.findViewById(R.id.iv_gridview_img));
			convertView.setTag(viewHolder);
		} else {
			// System.out.println("@@@@@@@@@@!!!!!!!!"+convertView.getTag().toString());
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (urlList.size() > 0) {

			viewHolder.imagewithurl.setmUrl(urlList.get(arg0));
			if (!TextUtils.isEmpty(viewHolder.imagewithurl.getmUrl())
					&& !MainListViewAdapter.isBusy) {
				// System.out.println(urlList.get(arg0));
				// viewHolder.mImageView.setTag(viewHolder.mUrl);
				imageLoader.setCompress(true);
				Bitmap bitmap = null;
				ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
				SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(
						bitmap, queue);
				if (bitmapref != null) {
					bitmap = (Bitmap) bitmapref.get();
				}
				bitmap = imageLoader.loadImage(viewHolder.imagewithurl,
						urlList.get(arg0));
				if (bitmap != null) {
					viewHolder.imagewithurl.getmImageView().setImageBitmap(
							bitmap);
				}
			}
			viewHolder.imagewithurl.getmImageView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							/*
							 * Toast.makeText(context, "点击了图片！！",
							 * Toast.LENGTH_SHORT) .show();
							 */
							Intent intent = new Intent(context,
									ImageScanActivity.class);
							Bundle bl = new Bundle();
							bl.putInt("position", arg0);
							bl.putStringArrayList("urlList",
									(ArrayList<String>) urlList);
							intent.putExtras(bl);
							context.startActivity(intent);
						}
					});
		}
		return convertView;
	}

}
