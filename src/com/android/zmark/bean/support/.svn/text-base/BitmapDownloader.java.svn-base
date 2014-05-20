package com.android.zmark.bean.support;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.LogHelper;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.asyncdrawable.FileManager;
import com.android.zmark.bean.support.asyncdrawable.IPictureWorker;
import com.android.zmark.bean.support.asyncdrawable.IWeiciyuanDrawable;
import com.android.zmark.bean.support.asyncdrawable.MyAsyncTask;
import com.android.zmark.bean.support.asyncdrawable.PictureBitmapDrawable;
import com.android.zmark.bean.support.asyncdrawable.ReadWorker;
import com.android.zmark.bean.support.asyncdrawable.TaskCache;
import com.android.zmark.bean.util.ImageUtility;

/**
 * User: qii Date: 12-12-12
 */
public class BitmapDownloader {


	private Handler handler;

	static volatile boolean pauseDownloadWork = false;
	static final Object pauseDownloadWorkLock = new Object();

	static volatile boolean pauseReadWork = false;
	static final Object pauseReadWorkLock = new Object();

	private static final Object lock = new Object();

	private static BitmapDownloader instance;

	private BitmapDownloader(Handler handler) {
		this.handler = handler;
	}

	public static BitmapDownloader getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new BitmapDownloader(new Handler(
						Looper.getMainLooper()));
			}
		}
		return instance;
	}

	public static void refreshThemePictureBackground() {
		synchronized (lock) {
			instance = new BitmapDownloader(new Handler(Looper.getMainLooper()));
		}
	}

	/**
	 * Pause any ongoing background work. This can be used as a temporary
	 * measure to improve performance. For example background work could be
	 * paused when a ListView or GridView is being scrolled using a
	 * {@link android.widget.AbsListView.OnScrollListener} to keep scrolling
	 * smooth.
	 * <p/>
	 * If work is paused, be sure setPauseDownloadWork(false) is called again
	 * before your fragment or activity is destroyed (for example during
	 * {@link android.app.Activity#onPause()}), or there is a risk the
	 * background thread will never finish.
	 */
	public void setPauseDownloadWork(boolean pauseWork) {
		synchronized (pauseDownloadWorkLock) {
			BitmapDownloader.pauseDownloadWork = pauseWork;
			if (!BitmapDownloader.pauseDownloadWork) {
				pauseDownloadWorkLock.notifyAll();
			}
		}
	}

	public void setPauseReadWork(boolean pauseWork) {
		synchronized (pauseReadWorkLock) {
			BitmapDownloader.pauseReadWork = pauseWork;
			if (!BitmapDownloader.pauseReadWork) {
				pauseReadWorkLock.notifyAll();
			}
		}
	}

	protected Bitmap getBitmapFromMemCache(String key) {
		if (TextUtils.isEmpty(key))
			return null;
		else
			return ZmarkApplication.getInstance().getAvatarCache().get(key);
	}







	/**
	 * when user open weibo detail, the activity will setResult to previous
	 * Activity, timeline will refresh at the time user press back button to
	 * display the latest repost count and comment count. But sometimes, weibo
	 * detail's pictures are very large that bitmap memory cache has cleared
	 * those timeline bitmap to save memory, app have to read bitmap from sd
	 * card again, then app play annoying animation , this method will check
	 * whether we should read again or not.
	 */
	private boolean shouldReloadPicture(ImageView view, String urlKey) {
		if (urlKey.equals(view.getTag())
				&& view.getDrawable() != null
				&& view.getDrawable() instanceof BitmapDrawable
				&& ((BitmapDrawable) view.getDrawable() != null && ((BitmapDrawable) view
						.getDrawable()).getBitmap() != null)) {
			LogHelper.d("shouldReloadPicture=false");
			return false;
		} else {
			view.setTag(null);
			LogHelper.d("shouldReloadPicture=true");
			return true;
		}
	}

	public void displayImageView(final ImageView view, final String urlKey,
			final FileLocationMethod method, boolean isFling,
			boolean isMultiPictures) {
		view.clearAnimation();

		if (!shouldReloadPicture(view, urlKey))
			return;

		final Bitmap bitmap = getBitmapFromMemCache(urlKey);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			view.setTag(urlKey);
			// if (view.getAlpha() != 1.0f) {
			// view.setAlpha(1.0f);
			// }
			cancelPotentialDownload(urlKey, view);
		} else {

			if (isFling) {
				return;
			}

			if (!cancelPotentialDownload(urlKey, view)) {
				return;
			}

			final ReadWorker newTask = new ReadWorker(view, urlKey, method,
					isMultiPictures);
			PictureBitmapDrawable downloadedDrawable = new PictureBitmapDrawable(
					newTask);
			view.setImageDrawable(downloadedDrawable);

			// listview fast scroll performance
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {

					if (getBitmapDownloaderTask(view) == newTask) {
						newTask.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
					}
					return;

				}
			}, 400);

		}

	}

	private void display(final IWeiciyuanDrawable view, final String urlKey,
			final FileLocationMethod method, boolean isFling,
			boolean isMultiPictures) {
		view.getImageView().clearAnimation();

		if (!shouldReloadPicture(view.getImageView(), urlKey))
			return;

		final Bitmap bitmap = getBitmapFromMemCache(urlKey);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			view.getImageView().setTag(urlKey);
			if (view.getProgressBar() != null)
				view.getProgressBar().setVisibility(View.INVISIBLE);
			// if (view.getImageView().getAlpha() != 1.0f) {
			// view.getImageView().setAlpha(1.0f);
			// }
			view.setGifFlag(ImageUtility.isThisPictureGif(urlKey));
			cancelPotentialDownload(urlKey, view.getImageView());
		} else {
			if (isFling) {
				// view.getImageView().setImageResource(R.drawable.defalut_image);
				if (view.getProgressBar() != null)
					view.getProgressBar().setVisibility(View.INVISIBLE);
				view.setGifFlag(ImageUtility.isThisPictureGif(urlKey));
				return;
			}

			if (!cancelPotentialDownload(urlKey, view.getImageView())) {
				return;
			}

			final ReadWorker newTask = new ReadWorker(view, urlKey, method,
					isMultiPictures);
			PictureBitmapDrawable downloadedDrawable = new PictureBitmapDrawable(
					newTask);
			view.setImageDrawable(downloadedDrawable);
			/**
			 * 修改这里，添加了一行代码
			 */
			// view.getImageView()
			// .setImageResource(R.drawable.defalut_image_large);
			// listview fast scroll performance
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (getBitmapDownloaderTask(view.getImageView()) == newTask) {
						newTask.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
					}
					return;

				}
			}, 400);

		}

	}

	public void totalStopLoadPicture() {

	}

	private static boolean cancelPotentialDownload(String url,
			ImageView imageView) {
		IPictureWorker bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.getUrl();
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				if (bitmapDownloaderTask instanceof MyAsyncTask)
					((MyAsyncTask) bitmapDownloaderTask).cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static IPictureWorker getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof PictureBitmapDrawable) {
				PictureBitmapDrawable downloadedDrawable = (PictureBitmapDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	public void display(final ImageView imageView, final int width,
			final int height, final String url, final FileLocationMethod method) {
		if (TextUtils.isEmpty(url))
			return;

		new MyAsyncTask<Void, Bitmap, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap bitmap = null;
				boolean downloaded = TaskCache.waitForPictureDownload(url,
						null, FileManager.getFilePathFromUrl(url, method),
						method);
				if (downloaded)
					bitmap = ImageUtility.readNormalPic(
							FileManager.getFilePathFromUrl(url, method), width,
							height);
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				super.onPostExecute(bitmap);
				if (bitmap != null)
					imageView.setImageDrawable(new BitmapDrawable(ZmarkApplication
							.getInstance().getResources(), bitmap));
				AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
				alphaAnimation.setDuration(400);
				imageView.startAnimation(alphaAnimation);
			}
		}.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void display(final ImageView imageView, final int width,
			final int height, final String url,
			final FileLocationMethod method, int type) {
		if (TextUtils.isEmpty(url))
			return;
		imageView.clearAnimation();
		new MyAsyncTask<Void, Bitmap, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap bitmap = null;
				boolean downloaded = TaskCache.waitForPictureDownload(url,
						null, FileManager.getFilePathFromUrl(url, method),
						method);
				if (downloaded)
					bitmap = ImageUtility.readNormalPic(
							FileManager.getFilePathFromUrl(url, method), width,
							height);
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				super.onPostExecute(bitmap);
				if (bitmap != null)
					imageView.setImageDrawable(new BitmapDrawable(ZmarkApplication
							.getInstance().getResources(), bitmap));
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
				alphaAnimation.setDuration(1000);
				imageView.startAnimation(alphaAnimation);
			}
		}.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}
}