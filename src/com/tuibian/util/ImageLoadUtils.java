package com.tuibian.util;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.tuibian.common.CGlobal;

public class ImageLoadUtils {

	private String Uri;

	public void imageLoad(ImageView imageView, String Uri, Context context,
			int width, int height) {
		this.Uri = Uri;

		StringBuilder Path = new StringBuilder();
		Path.append(CGlobal.SERVICE_ADDR).append("/" + Uri);

		ImageTask imageTask = new ImageTask(imageView, context, width, height);
		imageTask.execute(Path.toString());

	}

	private class ImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;
		private int Width, height;
		private Context context;

		public ImageTask(ImageView imageView, Context context, int width,
				int height) {
			this.imageView = imageView;
			this.Width = width;
			this.height = height;
			this.context = context;
		}

		protected Uri doInBackground(String... params) {
			try {

				File file = FileUtil.getCacheFile(context);

				if (!file.exists()) {
					file.mkdirs();
				}
				return ImageUtil.getImage(params[0], file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Uri result) {
			if (result == null || imageView == null
					|| imageView.getTag() == null)
				return;

			if (!imageView.getTag().equals(Uri)) {
				imageView.setTag(null);
				return;
			}

			ZoomImageUtils.Zoomimage(imageView, result, Width, height);

		}
	}

}
