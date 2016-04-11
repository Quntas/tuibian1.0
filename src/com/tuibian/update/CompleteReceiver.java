package com.tuibian.update;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CompleteReceiver extends BroadcastReceiver {

	private DownloadManager downloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

			Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			Query query = new Query();
			query.setFilterById(id);

			downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);

			Cursor cursor = downloadManager.query(query);
			int columnCount = cursor.getColumnCount();
			String path = null;

			while (cursor.moveToNext()) {
				for (int j = 0; j < columnCount; j++) {
					String columnName = cursor.getColumnName(j);
					String string = cursor.getString(j);
					if (columnName.equals("local_uri")) {
						path = string;
					}
					if (string != null) {
						System.out.println(columnName + ": " + string);
					} else {
						System.out.println(columnName + ": null");
					}
				}
			}
			cursor.close();
			Log.i("mark", "path = " + path);

			if (path.startsWith("content:")) {
				cursor = context.getContentResolver().query(Uri.parse(path),
						null, null, null, null);
				columnCount = cursor.getColumnCount();
				while (cursor.moveToNext()) {
					for (int j = 0; j < columnCount; j++) {
						String columnName = cursor.getColumnName(j);
						String string = cursor.getString(j);
						if (string != null) {
							Log.i("mark", columnName + "=" + string);
							System.out.println(columnName + ": " + string);

						} else {

							System.out.println(columnName + ": null");

						}
					}
				}
				cursor.close();
			}

			else {

				String path0 = Environment.getExternalStorageDirectory()
						.toString() + "/download/";
				Log.i("mark", "path0=" + path0);
				Log.i("mark", "path=" + path);
				String downloadpath = path.subSequence(8, path.length())
						.toString();
				Log.i("mark", "downloadpath= " + downloadpath);
				ApkDownLoadUtils.openApkFile(context, new File(downloadpath));

			}

		} else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

			Toast.makeText(context, "点击通知栏进行查看", Toast.LENGTH_SHORT).show();

		}
	}

}
