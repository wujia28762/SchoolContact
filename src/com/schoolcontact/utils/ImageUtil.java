package com.schoolcontact.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.schoolcontact.model.ImageViewWithUrl;

public class ImageUtil {
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 960, 960);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
		SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
				queue);
		if (bitmapref != null) {
			bitmap = (Bitmap) bitmapref.get();
		}
		bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static File compressImage(String filePath, String fileName, int q)
			throws FileNotFoundException {

		Bitmap bm = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
		SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bm, queue);
		if (bitmapref != null) {
			bm = (Bitmap) bitmapref.get();
		}
		bm = getSmallBitmap(filePath);

		int degree = readPictureDegree(filePath);

		if (degree != 0) {// 旋转照片角度
			bm = rotateBitmap(bm, degree);
		}

		File imageDir = getAlbumDir();

		File outputFile = new File(imageDir, fileName);

		FileOutputStream out = new FileOutputStream(outputFile);

		bm.compress(Bitmap.CompressFormat.JPEG, q, out);

		return outputFile;
	}

	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"JXTSaveImage");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			Toast.makeText(context, "图片已保存到：JXTSaveImage文件夹下.",
					Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// // 其次把文件插入到系统图库
		// try {
		// MediaStore.Images.Media.insertImage(context.getContentResolver(),
		// file.getAbsolutePath(), fileName, null);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// // 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + appDir.getAbsolutePath())));
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
		SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(output,
				queue);
		if (bitmapref != null) {
			output = (Bitmap) bitmapref.get();
		}
		output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static void disAsyIamge(ImageView iv, String url,
			AsyncImageLoader imageLoader) {
		if (!TextUtils.isEmpty(url)) {
			iv.setTag(url);
			Bitmap bitmap = null;
			ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
			SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
					queue);
			if (bitmapref != null) {
				bitmap = (Bitmap) bitmapref.get();
			}
			bitmap = imageLoader.loadImage(iv, url);
			if (bitmap != null) {
				iv.setImageBitmap(bitmap);
			}
			// if (bitmap.isRecycled()) {
			// bitmap.recycle();
			// }
		}
	}

	public static void disAsyIamge(ImageViewWithUrl iv, String url,
			AsyncImageLoader imageLoader) {
		if (!TextUtils.isEmpty(url)) {
			iv.setmUrl(url);
			Bitmap bitmap = null;
			ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
			SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
					queue);
			if (bitmapref != null) {
				bitmap = (Bitmap) bitmapref.get();
			}
			bitmap = imageLoader.loadImage(iv, url);
			if (bitmap != null) {
				iv.getmImageView().setImageBitmap(bitmap);
			}
			// if (bitmap.isRecycled()) {
			// bitmap.recycle();
			// }
		}
	}

	public Bitmap drawableToBitamp(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
		SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap,
				queue);
		if (bitmapref != null) {
			bitmap = (Bitmap) bitmapref.get();
		}
		bitmap = bd.getBitmap();
		return bitmap;
	}

	public static File getAlbumDir() {
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private static String getAlbumName() {
		return "saveCompressImage";
	}

}
