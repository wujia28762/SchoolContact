package com.schoolcontact.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.school_contact.main.R;
import com.schoolcontact.view.HomeFragment;
import com.schoolcontact.view.NotificationActivity;

public class PicSelectUtils {

	public static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";

	public static Uri imageUri = buildUri();
	public static Uri imageUri44 = null;
	public static final int CROP_PICTURE = 2001;
	public static final int CHOOSE_PICTURE = 1999;
	public static final int TAKE_CROP_PICTURE = 2002;
	public static final int TAKE_PICTURE = 2000;

	public static void doPickPhotoAction(final Activity context) {
		ImageUtil.deleteTempFile(getRealFilePath(context,imageUri));
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(true);
		Window window = alertDialog.getWindow();
		// window.setWindowAnimations(R.style.Animation_Dialog_Bottom);
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		window.setContentView(R.layout.getpic);
		View view = window.getDecorView();

		view.findViewById(R.id.fromcamera).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
						doTakePhoto(context);// 用户点击了从照相机获取

					}

				});
		view.findViewById(R.id.fromggallery).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
						doPickPhotoFromGallery(context);// 从相册中去获取
					}

				});
		view.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
	}

	private static void doPickPhotoFromGallery(Activity context) {

		/*
		 * Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		 * intent.setType("image/*"); intent.putExtra("crop", "true");
		 * intent.putExtra("aspectX", 1); intent.putExtra("aspectY", 1);
		 * intent.putExtra("outputX", EventList.OUTPUTX);
		 * intent.putExtra("outputY", EventList.OUTPUTY);
		 * intent.putExtra("scale", true); intent.putExtra("return-data",
		 * false); intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		 * intent.putExtra("outputFormat",
		 * Bitmap.CompressFormat.JPEG.toString());
		 * intent.putExtra("noFaceDetection", true); // no face detection
		 * context.startActivityForResult(intent, CROP_PICTURE);
		 */

		if (context instanceof NotificationActivity) {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setType("image/*");
			context.startActivityForResult(intent, CROP_PICTURE);
			/*
			 * Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			 * intent.setType("image/*"); intent.putExtra("outputFormat",
			 * Bitmap.CompressFormat.JPEG.toString());
			 * intent.putExtra("noFaceDetection", true); // no face detection
			 * context.startActivityForResult(intent, CROP_PICTURE);
			 */
			// if (context instanceof HomeFragment)
		} else {
			// DocumentsContract.isDocumentUri(context, uri)
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			context.startActivityForResult(intent, CHOOSE_PICTURE);
			// Intent intent=new
			// Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
			// intent.addCategory(Intent.CATEGORY_OPENABLE);
			// intent.setType("image/jpeg");
			// if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
			// context.startActivityForResult(intent, CROP_PICTURE);
			// }else{
			// context.startActivityForResult(intent, CROP_PICTURE);
			// }
		}

	}

	private static void doTakePhoto(Activity context) {

	//	ImageUtil.deleteTempFile(getRealFilePath(context,imageUri));
		if (context instanceof NotificationActivity) {
			Intent getImageByCamera = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			context.startActivityForResult(getImageByCamera, TAKE_PICTURE);
			/*
			 * Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			 * intent.setType("image/*"); intent.putExtra("outputFormat",
			 * Bitmap.CompressFormat.JPEG.toString());
			 * intent.putExtra("noFaceDetection", true); // no face detection
			 * context.startActivityForResult(intent, CROP_PICTURE);
			 */
			// if (context instanceof HomeFragment)
		} else {
			System.out.println(imageUri);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action
																		// is
			intent.putExtra(MediaStore.EXTRA_OUTPUT, HomeFragment.imageUri);
			context.startActivityForResult(intent, TAKE_PICTURE);
		}
	}

	public static Uri buildUri() {
		System.out.println(Uri
				.fromFile(Environment.getExternalStorageDirectory())
				.buildUpon().appendPath(CROP_CACHE_FILE_NAME).build()
				.toString());
		return Uri.fromFile(Environment.getExternalStorageDirectory())
				.buildUpon().appendPath(CROP_CACHE_FILE_NAME).build();
	}

	public static void cropImageUri(Uri uri, int outputX, int outputY,
			int requestCode, Activity context) {

		Intent intent = new Intent("com.android.camera.action.CROP");

		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);

		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", outputX);

		intent.putExtra("outputY", outputY);

		intent.putExtra("scale", true);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		intent.putExtra("return-data", false);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("noFaceDetection", true); // no face detection

		context.startActivityForResult(intent, requestCode);

	}

	public static Bitmap decodeUriAsBitmap(Uri uri, Activity context) {

		Bitmap bitmap = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
	    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
	    if(bitmapref!=null){  
            bitmap = (Bitmap) bitmapref.get();  
        }
		

		System.out.println("转换URI！" + uri);
		try {
			BitmapFactory.Options options = new Options();
			options.inJustDecodeBounds = true;
			// BitmapFactory.decodeStream(is, null,options);
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			options.inSampleSize = ImageUtil.calculateInSampleSize(options,
					EventList.comPressHeight, EventList.comPressHeight);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri), null, options);
		//	bitmap = BitmapFactory.decodeStream();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

			return null;

		}

		return bitmap;

	}

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Uri uri, Activity context,int aspectX,int aspectY,int outputX,int outputY) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}

		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(context, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}

		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);

		context.startActivityForResult(intent, CROP_PICTURE);
	}

	public static File converBitmaptoFile(Bitmap mBitmap) {
		File f = new File(Environment.getExternalStorageDirectory(),
				CROP_CACHE_FILE_NAME);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			return f;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}
}
