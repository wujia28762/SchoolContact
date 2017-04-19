package com.schoolcontact.view;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.model.ImageInfo;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.PicSelectUtils;

public class UserInfoActivity extends BaseActivity {

	private UserService cs = new UserService();
	private AsyncImageLoader imageLoader;
	private ImageView iv_title;

	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		onInit();
	}

	public void onInit() {

		((Button) findViewById(R.id.bt_logout)).setOnClickListener(this);
		((TextView) findViewById(R.id.backtext)).setOnClickListener(this);

		((RelativeLayout) findViewById(R.id.info_layout))
				.setOnClickListener(this);
		TextView tv_user = (TextView) (findViewById(R.id.username));
		iv_title = (ImageView) findViewById(R.id.imagetitle);
		imageLoader = new AsyncImageLoader(this);

		// String url = sccontext.getUi().getIsportraituri();
		if (!(sccontext.getUi() == null)) {
			tv_user.setText(sccontext.getUi().getUsername());

			if (!TextUtils.isEmpty(sccontext.getUi().getIsportraituri())) {
				System.out.println(sccontext.getUi().getIsportraituri());
				Bitmap bitmap = imageLoader.loadImage(iv_title, sccontext
						.getUi().getIsportraituri());
				if (bitmap != null) {
					iv_title.setImageBitmap(bitmap);

				}
			}

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backtext:
			finish();
			break;

		case R.id.bt_logout:
			bt_logout();
			break;
		case R.id.info_layout:
			PicSelectUtils.doPickPhotoAction(UserInfoActivity.this);
			break;
		default:
			break;
		}
	}

	public void bt_logout() {
		cs.logOut(this);

	}

	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
		case EventList.LOGOUT:
			Toast.makeText(this, rm.getMess(), Toast.LENGTH_LONG).show();
			sccontext.clear();
			Intent mainIntent = new Intent(UserInfoActivity.this,
					LoginActivity.class);

			this.startActivity(mainIntent);

			MoreFragment.instance.getActivity().finish();
			this.finish();

			break;

		case EventList.UPLOADIMG:
			if (rm.getCode().equals(EventList.SCUESS)) {
				ImageInfo ii = (ImageInfo) rm.getData();
				sccontext.getUi().setIsportraituri(ii.getMediaid());
				// System.out.println("return :"+);
				// MoreFragment.instance.getIv_title().setImageBitmap(bitmap);
			}
			Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		System.out.println("收到回调");
		if (resultCode == RESULT_OK) {
			System.out.println(requestCode + "");
			switch (requestCode) {
			case PicSelectUtils.CROP_PICTURE: //
				if (PicSelectUtils.imageUri != null) {

					bitmap = PicSelectUtils.decodeUriAsBitmap(
							PicSelectUtils.imageUri, this);// decode
					iv_title.setImageBitmap(bitmap);

					try {
						cs.uploadImg(ImageUtil.compressImage(PicSelectUtils
								.getRealFilePath(this, PicSelectUtils.imageUri),
								"HeadTEMP.jpg", 60), this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			//
			case PicSelectUtils.TAKE_PICTURE:
				System.out.println("收到相机回调");
				PicSelectUtils.cropImageUri(PicSelectUtils.imageUri,
						EventList.OUTPUTX, EventList.OUTPUTY,
						PicSelectUtils.CROP_PICTURE, this);
				break;
			// 图册选择图片
			// case PicSelectUtils.CHOOSE_PICTURE:
			//
			// if (PicSelectUtils.imageUri != null) {
			//
			// Bitmap bitmap = PicSelectUtils.decodeUriAsBitmap(
			// PicSelectUtils.imageUri, this);// decode
			// iv_title.setImageBitmap(bitmap);
			//
			// cs.uploadImg(new
			// File(PicSelectUtils.getRealFilePath(this,PicSelectUtils.imageUri)),
			// this);
			// }

			}
		}
	}

}
