package com.schoolcontact.view;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.adapter.MessageDetailAdapter;
import com.schoolcontact.adapter.PhotoAlbumSunItemAdapter;
import com.schoolcontact.listener.AsyncImageLoaderListener;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.ItemEntity;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.widget.MyJazzyViewPager;

public class MessageDetailWithImageActivity extends BaseActivity implements
		OnPageChangeListener, AsyncImageLoaderListener {

	protected static final String TAG = "MessageDetailWithImageActivity";
	private MyJazzyViewPager mViewPager;
	private List<String> it = new ArrayList<String>();
	private AsyncImageLoader imageLoader;
	private TextView tvst;
	private ProgressBar mGrogressBar;
	private FriendMessageInfo fi;
	// private Handler mHandler;
	private List<String> urlList = new ArrayList<String>();
	private TextView mTv_comment;
	private TextView mTv_pinglunright;
	private TextView mTv_zanright;
	private RelativeLayout rl_forward;
	private MessageDetailWithImageActivity instance = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_messagedetailwithmess);
		onInit();
	}

	@Override
	public void onInit() {
		Intent it1 = this.getIntent();

		fi = (FriendMessageInfo) it1
				.getSerializableExtra(PhotoAlbumSunItemAdapter.TAG);
		imageLoader = new AsyncImageLoader(this);
		tvst = (TextView) findViewById(R.id.showimage_title);
		findViewById(R.id.backtext).setOnClickListener(this);
		findViewById(R.id.tv_savetext).setOnClickListener(this);
		mGrogressBar = (ProgressBar) findViewById(R.id.loadingGrogressBar);
		// mHandler = new Handler();
		rl_forward = (RelativeLayout)findViewById(R.id.rl_forward);
		imageLoader.setAsyncimageloaderfinshlistener(this);
		mViewPager = (MyJazzyViewPager) findViewById(R.id.id_viewPager);
		mTv_comment = (TextView) findViewById(R.id.tv_comment);
		mTv_comment.setText(fi.getContent());
		mTv_pinglunright = (TextView) findViewById(R.id.tv_pinglunright);
		mTv_zanright = (TextView) findViewById(R.id.tv_zanright);
		mTv_pinglunright.setText(fi.getComments().size()+"");
		mTv_zanright.setText(fi.getThumbs().size()+"");
		rl_forward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(instance,
						MessageDetailActivity.class);
				Bundle bl = new Bundle();
				bl.putSerializable(PhotoAlbumSunItemAdapter.TAG, fi);
				it.putExtras(bl);
				instance.startActivity(it);
				
			}
		});
		
		if (!TextUtils.isEmpty(fi.getFile_urls()))
			for (String url : fi.getFile_urls().split("\\,")) {
				String tempUrl = EventList.BASEURL;
				if (url.startsWith("/"))
					url = url.replaceFirst("/", "");
				tempUrl = tempUrl.concat(url);
				Log.i("临时拼接URL", tempUrl);
				urlList.add(tempUrl);
			}
		for (String i : urlList) {
			if(i.contains("thumbnail_"))
			{
				i = i.replaceFirst("thumbnail_", "");
			}
			it.add(i);
		}
		MessageDetailAdapter mMessageDetailAdapter = new MessageDetailAdapter(
				this, mViewPager, imageLoader);
		mMessageDetailAdapter.setmData(it);

		tvst.setText("1/" + it.size());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(mMessageDetailAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_savetext:
			saveBigImage();
			break;
		case R.id.backtext:
			finish();
			break;

		}

	}

	private void saveBigImage() {
		//
		String cui = it.get(mViewPager.getCurrentItem());
		Bitmap bitmap = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
	    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
	    if(bitmapref!=null){  
            bitmap = (Bitmap) bitmapref.get();  
        }
		bitmap = imageLoader.getBitmapFromDisk(cui);

		if (bitmap == null) {
			Toast.makeText(this, "图片加载中，请稍后保存！", Toast.LENGTH_SHORT).show();
		} else {
			ImageUtil.saveImageToGallery(this, bitmap);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		System.out.println("==========>" + arg0);
		tvst.setText((arg0 + 1) + "/" + it.size());
	}

	@Override
	public void FinshLoader() {
		mGrogressBar.setVisibility(View.GONE);
	}

	@Override
	public void StartLoader() {
		mGrogressBar.setVisibility(View.VISIBLE);
	}
}
