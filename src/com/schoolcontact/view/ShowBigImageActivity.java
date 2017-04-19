package com.schoolcontact.view;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.ImageUtil;

public class ShowBigImageActivity extends FragmentActivity implements
		OnClickListener {

	private View iv_bigImage;
	public static AsyncImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_showimage);
		onInit();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;
		case R.id.tv_savetext:
			saveBigImage();
			break;
		default:
			break;
		}
	}

	private void saveBigImage() {
		if (iv_bigImage.getVisibility() == View.VISIBLE) {
			String url = iv_bigImage.getTag().toString();

			if(iv_bigImage.getTag().toString().startsWith("http:"))
			{
				Bitmap bitmap = null;
				ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
			    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
			    if(bitmapref!=null){  
		            bitmap = (Bitmap) bitmapref.get();  
		        }
				bitmap = imageLoader.getBitmapFromDisk(url);
			ImageUtil.saveImageToGallery(this, bitmap);
			}
			else
			{
				Toast.makeText(this, "本地已存在此图片！", Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(this, "图片加载中，请稍后保存！", Toast.LENGTH_SHORT).show();
		}

	}

	public void onInit() {
		iv_bigImage = (View) findViewById(R.id.iv_bigimage);
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_savetext)).setOnClickListener(this);

	}
}
