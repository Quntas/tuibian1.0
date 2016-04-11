package com.tuibian.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class PictureUtils {

	static final String catch_path = "/tuibian_catche";

	public static boolean addSDPicture(Context context, Bitmap bitmap,
			String url) {

		System.out.println("-----放入图片到SDC中");
		boolean result = false;
		FileOutputStream out;

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 缓存到存储卡
			String picturePath0 = Environment.getExternalStorageDirectory()
					.getPath();

			String picturePath = picturePath0;
			picturePath = picturePath + catch_path;
			File dirFile = new File(picturePath);
			String filename = getFileName(url);

			if (!(dirFile.exists()) || !(dirFile.isDirectory())) {
				dirFile.mkdir();
			}
			File pictureFile = new File(dirFile, filename);

			try {
				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}
				out = new FileOutputStream(pictureFile);
				result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

				// 修改最后修改时间
				// long lasttime = CommUtils.getLastModifyTime(url);
				// pictureFile.setLastModified(lasttime);

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public static Bitmap getSDPicture(Context context, String url) {

		System.out.println("-----取出图片从SDK中：" + url);
		String filename = getFileName(url);
		String path = Environment.getExternalStorageDirectory().getPath()
				+ catch_path + "/" + filename;
		Bitmap bitmap = null;

		File localFile = new File(path);

		if (path != null && localFile.exists()) {
			FileInputStream fs = null;
			BufferedInputStream bs = null;
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inPreferredConfig = Bitmap.Config.RGB_565;
				opt.inPurgeable = true;
				opt.inInputShareable = true;
				fs = new FileInputStream(path);
				bs = new BufferedInputStream(fs);
				// opt.inSampleSize = 3;
				bitmap = BitmapFactory.decodeStream(bs, null, opt);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fs.close();
					bs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static String getFileName(String path) {

		String filename = path.substring(path.lastIndexOf('/') + 1);
		if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
			filename = UUID.randomUUID() + ".tmp";// 默认取一个文件名
		}
		return filename;

	}

	public static long getLocalFileTime(final String imageUrl) {
		long local = 0;

		String filename = getFileName(imageUrl);
		String path = Environment.getExternalStorageDirectory().getPath()
				+ catch_path + "/" + filename;
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				local = file.lastModified();
			}
		}
		return local;
	}

}
