package com.tuibian.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.widget.ImageView;

import com.tuibian.common.CGlobal;

public class ZoomImageUtils {
	/** sWidth想要的宽度 sHeight想要的高度。 */
	public static void Zoomimage(ImageView imgeview, Uri uri, int sWidth,
			int sHeight) {
		ContentResolver cr = CGlobal.mContext.getContentResolver();

		InputStream in = null;
		try {
			in = cr.openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);

		try {
			in.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		/** 图片的实际宽高 */
		int mWidth = options.outWidth;
		int mHeight = options.outHeight;

		int s = 1;// 分辨率缩放倍数。
		while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2)) {
			s *= 2;
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = s;
		try {
			in = cr.openInputStream(uri);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);

		try {
			in.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		if (null == bitmap) {

			return;
		}

		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		float scaleWidth = (float) sWidth / bmpWidth;
		float scaleHeight = (float) sHeight / bmpHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
				bmpHeight, matrix, false);
		imgeview.setImageBitmap(resizeBitmap);
		bitmap.recycle();
	}

}