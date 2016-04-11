package com.tuibian.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

public class FileUtil {

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	public static File getCacheFile(Context context) {
		File file = context.getCacheDir();
		File myfile = new File(file, "tuibian");
		if (!myfile.exists()) {
			myfile.mkdirs();
		}
		return myfile;
	}

	public static void SaveFile(String toSaveString, File saveFile)
			throws IOException {

		if (!saveFile.exists()) {

			File file = new File(saveFile.getParent());

			file.mkdirs();
			saveFile.createNewFile();
		}
		FileOutputStream outStream = new FileOutputStream(saveFile);
		outStream.write(toSaveString.getBytes());
		outStream.close();

	}

	/** 获取文件的后缀。 */
	public static String getFileSuffix(File file) {

		String Suffix = file.getName().substring(file.getName().length() - 4,
				file.getName().length());
		return Suffix;

	}

	/** 返回文件路径 */
	public static File getPicFiles(String FileName) {
		File RootFile = Environment.getExternalStorageDirectory();
		String SuffixFilePath = RootFile.getAbsolutePath();
		File SuffixFile = new File(SuffixFilePath + "/" + FileName);
		try {
			if (!SuffixFile.exists()) {
				SuffixFile.mkdirs();
			}
			return SuffixFile;
		} catch (Exception e) {
			return null;
		}

	}

	/** 返回缩略图文件 */
	public static File getSuffixFiles(String FileName) {
		File RootFile = Environment.getExternalStorageDirectory();
		String SuffixFilePath = RootFile.getAbsolutePath();
		File suffixFile = new File(SuffixFilePath);

		if (!suffixFile.exists())
			suffixFile.mkdirs();

		File SuffixFile = new File(SuffixFilePath + "/" + FileName);
		try {

			return SuffixFile;
		} catch (Exception e) {
			return null;
		}

	}

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	public static boolean saveBitmap(Bitmap bitmap, File saveFile, String Suffix) {

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(saveFile));

			if (Suffix.equals(".jpg")) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			} else if (Suffix.equals(".png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			}

			bos.flush();
			bos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

}
