package com.schoolcontact.adapter;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.model.ItemEntity;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.ZoomImageView;
import com.schoolcontact.widget.MyJazzyViewPager;

public class MessageDetailAdapter extends PagerAdapter {
	private Context mContext;

	private MyJazzyViewPager mViewPager;

	private List<String> mData;

	// private TextView mTitle;

	public List<String> getmData() {
		return mData;
	}

	public void setmData(List<String> mData) {
		this.mData = mData;
	}

	private AsyncImageLoader mimageLoader;

	private LayoutInflater mLayoutInflater;

	public MessageDetailAdapter(Context context, MyJazzyViewPager viewpager,
			AsyncImageLoader imageLoader) {
		mViewPager = viewpager;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mimageLoader = imageLoader;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView((View) object);

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View convertView = mLayoutInflater.inflate(R.layout.fragment_showimage,
				null);
		ZoomImageView zoomimageview = (ZoomImageView) convertView
				.findViewById(R.id.iv_bigimage);
		TextView textview = (TextView) convertView
				.findViewById(R.id.tv_bigimageempty);

		// ImageView imageView
		// zoomimageview.setScaleType(ScaleType.CENTER_CROP);
		// Bitmap mBitmap =
		String itementity = mData.get(position);
		zoomimageview.setTag(itementity);
		Bitmap bitmap = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
	    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
	    if(bitmapref!=null){  
            bitmap = (Bitmap) bitmapref.get();  
        }
		bitmap = mimageLoader.loadImage(zoomimageview, itementity);

		if (bitmap != null) {
			zoomimageview.setVisibility(View.VISIBLE);
			// textview.setVisibility(View.GONE);
			zoomimageview.setImageBitmap(bitmap);
			zoomimageview.setBackgroundColor(mContext.getResources().getColor(R.color.hei));

		} else {
			zoomimageview.setVisibility(View.VISIBLE);
			textview.setVisibility(View.GONE);

//			Resources r = mContext.getResources();
//			Bitmap bmp = null;
//			ReferenceQueue<Bitmap> queue1 = new ReferenceQueue<Bitmap>();  
//		    SoftReference<Bitmap> bitmapref1 = new SoftReference<Bitmap>(bmp, queue1);
//		    if(bitmapref1!=null){  
//		    	bmp = (Bitmap) bitmapref.get();  
//	        }
//			bmp = BitmapFactory.decodeResource(r, R.drawable.yt);
//			zoomimageview.setImageBitmap(bmp);
//			zoomimageview.setba
		}
		ViewGroup parent = (ViewGroup) zoomimageview.getParent();
		if (parent != null) {
			parent.removeAllViews();
		}

		container.addView(zoomimageview);
		mViewPager.setObjectForPosition(zoomimageview, position);

		return zoomimageview;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

}