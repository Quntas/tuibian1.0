package com.tuibian.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tuibian.util.PictureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.YuvImage;
import android.os.Handler;
import android.util.Log;

/**
 * 图片异步加载
 */
public class AsyncImageLoader {
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	public Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	private final Handler handler = new Handler();

	private static final int BUFFER_IO_SIZE = 16000;
	private static final String TAG = "AsyncImageLoader";

	private static AsyncImageLoader mAsyncImageLoader = null;

	private Context context;
	private boolean isCancel = false;

	public static AsyncImageLoader getIntance() {

		if (mAsyncImageLoader == null) {
			mAsyncImageLoader = new AsyncImageLoader();
		}

		return mAsyncImageLoader;
	}

	public void setContext(Context context) {
		this.context = context;
		isCancel = false;
	}

	private Bitmap loadImageFromUrlNew(final String url) {
		try {
			// Addresses bug in SDK :
			// http://groups.google.com/group/android-developers/browse_thread/thread/4ed17d7e48899b26/
			BufferedInputStream bis = new BufferedInputStream(
					new URL(url).openStream(), BUFFER_IO_SIZE);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(baos,
					BUFFER_IO_SIZE);
			copy(bis, bos);
			bos.flush();
			Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(),
					0, baos.size());
			bis.close();
			baos.close();
			return bitmap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void copy(final InputStream bis, final OutputStream baos)
			throws IOException {
		byte[] buf = new byte[256];
		int l;
		while ((l = bis.read(buf)) >= 0)
			baos.write(buf, 0, l);
	}

	/**
	 * 加载图片
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public void loadDrawable(final String imageUrl, final ImageCallback callback) {

		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {

			System.out.println("imageCache 含有 " + imageUrl);

			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				callback.bitmapLoaded(imageUrl, softReference.get());
				return;
			}
		}
		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Runnable() {
			public void run() {
				try {
					// final Drawable drawable = loadImageFromUrl(imageUrl);
					final Bitmap bitmap = loadImageFromUrlNew(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));

					handler.post(new Runnable() {
						public void run() {
							// callback.imageLoaded(drawable);
							callback.bitmapLoaded(imageUrl, bitmap);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * 加载图片
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */

	public void loadDrawableComplete(final String imageUrl,
			final ImageCallback callback) {

		Log.println(0, "-----loadDrawableComplete:" + imageUrl, imageUrl);
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {

			System.out.println("imageCache 含有 " + imageUrl);
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				callback.bitmapLoaded(imageUrl, softReference.get());
				return;
			}
		}

		// 没有从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Runnable() {
			public void run() {
				/*
				 * try { //每次加载图片的时候都先判断网上图片是否已经更新 long webLastTime =
				 * CommUtils.getLastModifyTime(imageUrl); long localTime =
				 * PictureUtils.getLocalFileTime(imageUrl);
				 * 
				 * System.out.println("本地版本号与远程版本号为："+localTime+"------------"+
				 * webLastTime);
				 * 
				 * //没有修改过从缓存和SDK 中 取 if(webLastTime != localTime){ final Bitmap
				 * bitmap = loadImageFromUrlNew(imageUrl);
				 * imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
				 * PictureUtils.addSDPicture(context, bitmap, imageUrl);
				 * handler.post(new Runnable() { public void run() {
				 * callback.bitmapLoaded(imageUrl,bitmap); } });
				 * 
				 * 
				 * //从本地取数据 }else{ final Bitmap bitmapSdc =
				 * PictureUtils.getSDPicture(context, imageUrl);
				 * 
				 * handler.post(new Runnable() { public void run() {
				 * callback.bitmapLoaded(imageUrl,bitmapSdc); } });
				 * 
				 * }
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }
				 */

				final Bitmap bitmapSdc = PictureUtils.getSDPicture(context,
						imageUrl);
				if (bitmapSdc != null) {
					imageCache.put(imageUrl, new SoftReference<Bitmap>(
							bitmapSdc));
					handler.post(new Runnable() {
						public void run() {
							callback.bitmapLoaded(imageUrl, bitmapSdc);
						}
					});

				} else {

					Bitmap bitmap = null;
					try {
						bitmap = loadImageFromUrlNew(imageUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}

					final Bitmap bitmap2 = bitmap;

					if (bitmap2 != null) {
						imageCache.put(imageUrl, new SoftReference<Bitmap>(
								bitmap2));
						PictureUtils.addSDPicture(context, bitmap2, imageUrl);
						handler.post(new Runnable() {
							public void run() {
								callback.bitmapLoaded(imageUrl, bitmap2);
							}
						});
					}

				}

			}
		});
	}

	public void cancelDownLoad() {
		// executorService.shutdownNow();
		isCancel = true;
	}

	// 对外界开放的回调接口
	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void bitmapLoaded(String imageUrl, Bitmap bitmap);
	}

	public void clearCatche() {
		imageCache.clear();
	}

}