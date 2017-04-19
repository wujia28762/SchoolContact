package com.schoolcontact.view;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.school_contact.main.R;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.ZoomImageView;

public class ShowBigImageFragment extends BaseFragment {

	private String url = null;

	// private AsyncImageLoader imageLoader;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return onInit(inflater, container);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initFragment(getActivity().getIntent().getData());
	}

	@SuppressLint("NewApi")
	public View onInit(LayoutInflater inflater, ViewGroup container) {

		View tc = inflater.inflate(R.layout.fragment_showimage, container,
				false);
		ZoomImageView iv_bigimage = (ZoomImageView) tc
				.findViewById(R.id.iv_bigimage);

		TextView tv_Textview = (TextView) tc
				.findViewById(R.id.tv_bigimageempty);

		ShowBigImageActivity.imageLoader = new AsyncImageLoader(
				this.getActivity());
		// ShowBigImageActivity.imageLoader.setCompress(true);
		if (url.startsWith("http:")) {
			iv_bigimage.setTag(url);

			Bitmap bitmap = null;
			ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
			SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
					queue);
			if (bitmapref != null) {
				bitmap = (Bitmap) bitmapref.get();
			}
			bitmap = ShowBigImageActivity.imageLoader.loadImage(iv_bigimage,
					url);

			if (bitmap != null) {
				iv_bigimage.setVisibility(View.VISIBLE);
				tv_Textview.setVisibility(View.GONE);
				// Drawable bd = new BitmapDrawable(bitmap);
				iv_bigimage.setImageBitmap(bitmap);
				// iv_bigimage.setBackground(bd);
				iv_bigimage.setBackgroundColor(this.getResources().getColor(
						R.color.hei));

			}
		} else if (url.startsWith("file")) {
			String url1 = url.replace("file://", "");
			iv_bigimage.setTag(url1);

			Bitmap bitmap = null;
			ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
			SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
					queue);
			if (bitmapref != null) {
				bitmap = (Bitmap) bitmapref.get();
			}
			bitmap = BitmapFactory.decodeFile(url1);

			if (bitmap != null) {
				iv_bigimage.setVisibility(View.VISIBLE);
				tv_Textview.setVisibility(View.GONE);
				iv_bigimage.setBackgroundColor(this.getResources().getColor(
						R.color.hei));
				// Drawable.
				// Drawable bd = new BitmapDrawable(bitmap);
				// // iv_bigimage.setImageBitmap(bitmap);
				// iv_bigimage.setBackground(bd);
				// iv_bigimage.setAdjustViewBounds(true);
				// iv_bigimage.setImageBitmap(bitmap);

			}
		}
		return tc;
	}

	protected void initFragment(Uri uri) {
		if (uri == null) {
			return;
		}
		url = uri.getQueryParameter("bigimageurl");
		if (uri != null) {
			System.out.println("ªÒ»°µΩURL£∫" + url);
		}

	}

}
