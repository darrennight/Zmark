/**
 * Created on 2012-4-27 下午4:05:57
 */
package com.android.zmark.bean.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.android.zmark.R;


public class AsyncImageLoader {

	private final static String TAG = "AsyncImageLoader";

	private ExecutorService executorService = Executors.newFixedThreadPool(3);

	private static AsyncImageLoader asyncImageLoader;

	private static Context myContext;

	public static AsyncImageLoader getInstance(Context context) {
		myContext = context;
		if (asyncImageLoader == null) {
			asyncImageLoader = new AsyncImageLoader();
		}
		return asyncImageLoader;
	}

	public synchronized Drawable loadDrawable(final String imageUrl, final ImageCallback callback) {
		final Handler handler = new Handler(Looper.getMainLooper()) {

			
			@Override
			public void handleMessage(Message msg) {
				try {
					callback.imageLoaded((Drawable) msg.obj, imageUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		// load data

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				if (CacheManager.isSdCardMounted() && CacheManager.existCacheObjectData(imageUrl)) {
					byte[] bytes = CacheManager.readCacheData(imageUrl);
					if (bytes != null && bytes.length > 0) {
						GetImageResponse response = new GetImageResponse();
						response.parseFrom(bytes);
						Drawable cacheImage = response.getDrawable();
						Message msg = Message.obtain();
						msg.obj = cacheImage;
						handler.sendMessage(msg);
					} else {
						CacheManager.clearCacheObjectData(imageUrl);
						getDrawableFromNetwork(handler, imageUrl);
					}
				} else {
					getDrawableFromNetwork(handler, imageUrl);
				}
			}
		});
		return null;
	}

	/**
	 * 下载图片
	 * 
	 * @param imgURL 图片URL地址
	 * @return 代表图片信息的byte数组
	 * @throws IOException @see {@link IOException}
	 */
	public byte[] downloadImg(URL imgURL) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) imgURL.openConnection();
		// conn.setRequestProperty("Referer", "http://m.weiguanli.cn/");
		conn.setDoInput(true);
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			byteOut.write(buffer, 0, len);
		}
		byteOut.close();
		in.close();

		return byteOut.toByteArray();
	}

	private synchronized Drawable loadImageFromUrl(String imageSrc) {

		if (TextUtils.isEmpty(imageSrc)) {
			return null;
		}

		String encodeUrl = MyTools.encodeURLChinese(imageSrc);

		Bitmap resultBitmap = null;
		InputStream in = null;

		try {
			URL url = new URL(encodeUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			in = conn.getInputStream();
			resultBitmap = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (resultBitmap != null) {
			return new BitmapDrawable(resultBitmap);
		}

		return null;

	}

	// call back interface
	public interface ImageCallback {

		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	public interface ImageCallbackAndReturnDrawable {

		public Drawable imageLoadedAndReturnDrawable(Drawable imageDrawable, String imageUrl);
	}

	private synchronized void getDrawableFromNetwork(Handler handler, String imageUrl) {
		Drawable drawable = null;
		try {
			if (MyTools.isConnect(myContext)) {
				if (imageUrl != null) {
					drawable = loadImageFromUrl(imageUrl);
				}
				if (drawable == null) {
					drawable = myContext.getResources().getDrawable(R.drawable.ic_launcher);
					// Log.d(TAG, imageUrl);
				} else {
					// change to byte array and save to sdcad
					BitmapDrawable drawableBitmap = (BitmapDrawable) drawable;
					Bitmap bitmap = drawableBitmap.getBitmap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					CacheManager.writeCacheData(imageUrl, bos.toByteArray());
				}
			}
			handler.sendMessage(handler.obtainMessage(0, drawable));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Drawable getDrawable(String imageUrl) {
		if (CacheManager.isSdCardMounted() && CacheManager.existCacheObjectData(imageUrl)) {
			byte[] bytes = CacheManager.readCacheData(imageUrl);
			if (bytes != null && bytes.length > 0) {
				GetImageResponse response = new GetImageResponse();
				response.parseFrom(bytes);
				Drawable cacheImage = response.getDrawable();
				// mImageCache.put(imageUrl, new SoftReference<Drawable>(cacheImage));
				return cacheImage;
			} else {
				CacheManager.clearCacheObjectData(imageUrl);
				return getDrawableFromNetwork(imageUrl);
			}
		} else {
			return getDrawableFromNetwork(imageUrl);
		}
	}

	private Drawable getDrawableFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			if (MyTools.isConnect(myContext)) {
				if (imageUrl != null) {
					drawable = loadImageFromUrl(imageUrl);
				}
				if (drawable == null) {
					drawable = myContext.getResources().getDrawable(R.drawable.ic_launcher);
				} else {
					// change to byte array and save to sdcad
					BitmapDrawable drawableBitmap = (BitmapDrawable) drawable;
					Bitmap bitmap = drawableBitmap.getBitmap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					CacheManager.writeCacheData(imageUrl, bos.toByteArray());
					// Seven add recycle
					// bitmap.recycle();
					// mImageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static final class GetImageResponse {

		private Drawable mDrawable = null;

		public Drawable getDrawable() {
			return mDrawable;
		}

		public void parseFrom(byte[] bytes) {
			if (bytes == null) {
				mDrawable = null;
				return;
			}
			ByteArrayInputStream bs = null;
			try {
				// System.gc();
				bs = new ByteArrayInputStream(bytes, 0, bytes.length);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inSampleSize = 2;
				opts.inPurgeable = true;
				/* opts.inInputShareable= false; */
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				// opts.inJustDecodeBounds = true;
				// Bitmap b = BitmapFactory.decodeResourceStream(null, null, bs, null, opts);
				mDrawable = Drawable.createFromResourceStream(null, null, bs, null, opts);
				// mDrawable = new BitmapDrawable(b);
				// b.recycle();
				// mDrawable = Drawable.
				// mDrawable = Drawable.createFromStream(bs, null);
				// mDrawable = ImageUtil.list_continue;
				if (mDrawable == null) {
				}
			} catch (Throwable e) {
				mDrawable = null;
				e.printStackTrace();
			} finally {
				try {
					bs.close();
					bs = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
