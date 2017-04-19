package com.schoolcontact.view;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.ListUtils;
import com.schoolcontact.utils.PicSelectUtils;
import com.schoolcontact.widget.BadgeView;

public class MoreFragment extends BaseFragment {

	private AsyncImageLoader imageLoader;
	public static MoreFragment instance;
	private String url;
	
	
	private ImageView iv_title;
//	private TextView tv_pyq;
	
//
//	public ImageView getIv_title() {
//		return iv_title;
//	}
//
//	public void setIv_title(ImageView iv_title) {
//		this.iv_title = iv_title;
//	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onResume() {

		super.onResume();

	}

	public View onInit(LayoutInflater inflater, ViewGroup container) {
		instance = this;
		View v = inflater.inflate(R.layout.tab_more, container, false);
		RelativeLayout info_layout = (RelativeLayout) (v
				.findViewById(R.id.info_layout));
		info_layout.setOnClickListener(this);

		
		
		RelativeLayout pyq_layout = (RelativeLayout) (v
				.findViewById(R.id.pyq_layout));
		pyq_layout.setOnClickListener(this);
		
		RelativeLayout modifypwd_layout = (RelativeLayout) (v
				.findViewById(R.id.modifypwd_layout));
		modifypwd_layout.setOnClickListener(this);

		RelativeLayout SettingLayout = (RelativeLayout) (v
				.findViewById(R.id.SettingLayout));
		SettingLayout.setOnClickListener(this);
		
		
		RelativeLayout RL_about = (RelativeLayout) (v
				.findViewById(R.id.RL_about));
		RL_about.setOnClickListener(this);

		TextView tv_user = (TextView) (v.findViewById(R.id.username));
		TextView tv_acc = (TextView) (v.findViewById(R.id.account));

		iv_title = (ImageView) (v.findViewById(R.id.imagetitle));

		imageLoader = new AsyncImageLoader(this.getActivity());


		// sccontext.setImageLoader(imageLoader);
		if (!(sccontext.getUi() == null)) {
			tv_user.setText(sccontext.getUi().getUsername());
			tv_acc.setText("ÓÃ»§×é£º" + sccontext.getUi().getUser_group());
			if (!TextUtils.isEmpty(sccontext.getUi().getIsportraituri())) {
				url = sccontext.getUi().getIsportraituri();
				iv_title.setTag(url);
				Bitmap bitmap = null;
				ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
			    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
			    if(bitmapref!=null){  
		            bitmap = (Bitmap) bitmapref.get();  
		        }
				bitmap = imageLoader.loadImage(iv_title, url);
				if (bitmap != null) {
					iv_title.setImageBitmap(bitmap);
				}
			}
		}

		return v;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return onInit(inflater, container);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_layout:
			PicSelectUtils.doPickPhotoAction(MoreFragment.this.getActivity());
			break;
		case R.id.modifypwd_layout:

			Intent ml = new Intent(this.getActivity(), ModifyPwdActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("classinfo", "asd");
			ml.putExtras(bundle);
			this.startActivity(ml);
			break;
		case R.id.SettingLayout:
			Intent sl = new Intent(this.getActivity(),
					UserSettingActivity.class);
			this.startActivity(sl);
			break;
		case R.id.RL_about:
			Intent rl = new Intent(this.getActivity(), UserAbout.class);
			this.startActivity(rl);
			break;
			
		case R.id.pyq_layout:
			
			break;

		}
	}

}
