package com.tuibian.update;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class ApkDownLoadUtils {

	public static void downloadApk(Context context, String url, String name) {

		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);

		Uri uri = Uri.parse(url);
		Request request = new Request(uri);

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);

		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);

		// 不显示下载界面
		request.setVisibleInDownloadsUi(false);
		request.setDestinationInExternalPublicDir("/download/", name);

		File file = new File(Environment.getExternalStorageDirectory()
				.toString() + "/download/", name);
		if (file.exists()) {
			file.delete();
		}
		/*
		 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
		 * 在/mnt/sdcard
		 * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错
		 * ，不设置，下载后的文件在/cache这个目录下面
		 */
		// request.setDestinationInExternalFilesDir(this, null, "tar.apk");
				long id = downloadManager.enqueue(request);
		// TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面

	}

	public static void openApkFile(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
