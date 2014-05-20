package com.android.zmark.bean.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import junit.framework.Assert;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.LogHelper;
import com.android.zmark.bean.support.CachePath;
import com.android.zmark.bean.support.asyncdrawable.FileDownloaderHttpHelper;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.asyncdrawable.FileManager;
import com.android.zmark.bean.support.http.HttpUtility;

public class ImageUtility {

	public static final int WITH_UNDEFINED = -1;
	public static final int HEIGHT_UNDEFINED = -1;

	/**
	 * 1. convert gif to normal bitmap 2. cut bitmap
	 */
	private static Bitmap getMiddlePictureInTimeLineGif(String filePath,
			int reqWidth, int reqHeight) {

		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);

			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null)
				return null;

			int height = options.outHeight;
			int width = options.outWidth;

			int cutHeight = 0;
			int cutWidth = 0;

			if (height >= reqHeight && width >= reqWidth) {
				cutHeight = reqHeight;
				cutWidth = reqWidth;

			} else if (height < reqHeight && width >= reqWidth) {

				cutHeight = height;
				cutWidth = (reqWidth * cutHeight) / reqHeight;

			} else if (height >= reqHeight && width < reqWidth) {

				cutWidth = width;
				cutHeight = (reqHeight * cutWidth) / reqWidth;

			} else if (height < reqHeight && width < reqWidth) {

				float betweenWidth = ((float) reqWidth - (float) width)
						/ (float) width;
				float betweenHeight = ((float) reqHeight - (float) height)
						/ (float) height;

				if (betweenWidth > betweenHeight) {
					cutWidth = width;
					cutHeight = (reqHeight * cutWidth) / reqWidth;

				} else {
					cutHeight = height;
					cutWidth = (reqWidth * cutHeight) / reqHeight;

				}

			}

			if (cutWidth <= 0 || cutHeight <= 0) {
				return null;
			}

			Bitmap region = Bitmap.createBitmap(bitmap, 0, 0, cutWidth,
					cutHeight);

			if (region != bitmap) {
				bitmap.recycle();
				bitmap = region;
			}

			if (bitmap.getHeight() < reqHeight && bitmap.getWidth() < reqWidth) {
				Bitmap scale = Bitmap.createScaledBitmap(bitmap, reqWidth,
						reqHeight, true);
				if (scale != bitmap) {
					bitmap.recycle();
					bitmap = scale;
				}
			}
			return bitmap;
		} catch (OutOfMemoryError ignored) {
			return null;
		}
	}

	public static long getFileSize(String path) {
		if (TextUtils.isEmpty(path))
			return 0;
		File file = new File(path);
		if (file.isFile() && file.exists())
			return file.length() / (1024 * 8); // kb
		return 0;
	}

	private static boolean isGif(String path) {
		return path.endsWith(".gif");
	}

	public static Bitmap getMiddlePictureInTimeLine(String url, int reqWidth,
			int reqHeight,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {
		try {

			String filePath = FileManager.getFilePathFromUrl(url,
					FileLocationMethod.picture_bmiddle);

			File file = new File(filePath);

			if (!file.exists()) {
				return null;
			}

			if (!file.exists()) {
				getBitmapFromNetWork(url, filePath, downloadListener);
			}

			if (isGif(filePath)) {
				return getMiddlePictureInTimeLineGif(filePath, reqWidth,
						reqHeight);
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			int height = options.outHeight;
			int width = options.outWidth;

			int cutHeight = 0;
			int cutWidth = 0;

			if (height >= reqHeight && width >= reqWidth) {
				cutHeight = reqHeight;
				cutWidth = reqWidth;

			} else if (height < reqHeight && width >= reqWidth) {

				cutHeight = height;
				cutWidth = (reqWidth * cutHeight) / reqHeight;

			} else if (height >= reqHeight && width < reqWidth) {

				cutWidth = width;
				cutHeight = (reqHeight * cutWidth) / reqWidth;

			} else if (height < reqHeight && width < reqWidth) {

				float betweenWidth = ((float) reqWidth - (float) width)
						/ (float) width;
				float betweenHeight = ((float) reqHeight - (float) height)
						/ (float) height;

				if (betweenWidth > betweenHeight) {
					cutWidth = width;
					cutHeight = (reqHeight * cutWidth) / reqWidth;

				} else {
					cutHeight = height;
					cutWidth = (reqWidth * cutHeight) / reqHeight;

				}

			}

			if (cutWidth > 0 && cutHeight > 0) {

				int startX = 0;

				if (cutWidth < width) {
					startX = (width - cutWidth) / 2;
				}

				try {
					BitmapRegionDecoder decoder = BitmapRegionDecoder
							.newInstance(filePath, false);
					if (decoder != null) {
						Bitmap bitmap = decoder.decodeRegion(new Rect(startX,
								0, startX + cutWidth, cutHeight), null);
						if (bitmap.getHeight() < reqHeight
								&& bitmap.getWidth() < reqWidth) {
							Bitmap scale = Bitmap.createScaledBitmap(bitmap,
									reqWidth, reqHeight, true);
							if (scale != bitmap) {
								bitmap.recycle();
								bitmap = scale;
							}
						}
						if (bitmap != null) {
							return bitmap;
						}
					}
				} catch (IOException ignored) {

				}

			}

			return null;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static Bitmap getRoundedCornerPic(String url, int reqWidth,
			int reqHeight, FileLocationMethod method) throws Exception {
		return getRoundedCornerPic(url, reqWidth, reqHeight, method, null);
	}

	public static Bitmap getRoundedCornerPic(String url, int reqWidth,
			int reqHeight, FileLocationMethod method,
			FileDownloaderHttpHelper.DownloadListener downloadListener)
			throws Exception {
		try {

			if (!FileManager.isExternalStorageMounted()) {
				return null;
			}

			String filePath = FileManager.getFilePathFromUrl(url, method);
			if (!filePath.endsWith(".jpg") && !filePath.endsWith(".gif"))
				filePath = filePath + ".jpg";

			boolean fileExist = new File(filePath).exists();
			if (!fileExist) {
				return null;
			}
			if (!fileExist) {
				boolean result = getBitmapFromNetWork(url, filePath,
						downloadListener);
				if (!result)
					return null;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null) {
				// this picture is broken,so delete it
				new File(filePath).delete();
				return null;
			}

			int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(),
					reqWidth, reqHeight);
			if (size[0] > 0 && size[1] > 0) {
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
						size[0], size[1], true);
				if (scaledBitmap != bitmap) {
					bitmap.recycle();
					bitmap = scaledBitmap;
				}
			}
			return bitmap;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static Bitmap getRoundedCornerPic(String filePath, int reqWidth,
			int reqHeight) {
		return getRoundedCornerPic(filePath, reqWidth, reqHeight, 0);
	}

	public static Bitmap getRoundedCornerPic(String filePath, int reqWidth,
			int reqHeight, int cornerRadius) {
		try {

			if (!FileManager.isExternalStorageMounted()) {
				return null;
			}

			if (!filePath.endsWith(".jpg") && !filePath.endsWith(".gif")
					&& !filePath.endsWith(".png"))
				filePath = filePath + ".jpg";
			boolean fileExist = new File(filePath).exists();
			if (!fileExist) {
				return null;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null) {
				// this picture is broken,so delete it
				new File(filePath).delete();
				return null;
			}

			if (cornerRadius > 0) {
				int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(),
						reqWidth, reqHeight);
				if (size[0] > 0 && size[1] > 0) {
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
							size[0], size[1], true);
					if (scaledBitmap != bitmap) {
						bitmap.recycle();
						bitmap = scaledBitmap;
					}
				}
			}
			return bitmap;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static Bitmap readNormalPic(String filePath, int reqWidth,
			int reqHeight) {
		try {

			if (!FileManager.isExternalStorageMounted()) {
				return null;
			}

			if (!filePath.endsWith(".jpg") && !filePath.endsWith(".gif")
					&& !filePath.endsWith(".png"))
				filePath = filePath + ".jpg";

			boolean fileExist = new File(filePath).exists();

			if (!fileExist) {
				return null;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			if (reqHeight > 0 && reqWidth > 0)
				options.inSampleSize = calculateInSampleSize(options, reqWidth,
						reqHeight);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null) {
				// this picture is broken,so delete it
				new File(filePath).delete();
				return null;
			}

			if (reqHeight > 0 && reqWidth > 0) {
				int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(),
						reqWidth, reqHeight);
				if (size[0] > 0 && size[1] > 0) {
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
							size[0], size[1], true);
					if (scaledBitmap != bitmap) {
						bitmap.recycle();
						bitmap = scaledBitmap;
					}
				}
			}

			return bitmap;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static Bitmap getMiddlePictureInBrowserMSGActivity(String url,
			FileLocationMethod method,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {

		try {

			String filePath = FileManager.getFilePathFromUrl(url, method);

			File file = new File(filePath);

			if (!file.exists()) {
				return null;
			}

			if (!isThisBitmapCanRead(filePath)) {

				getBitmapFromNetWork(url, filePath, downloadListener);

			}
			file = new File(filePath);
			if (file.exists()) {
				return decodeBitmapFromSDCard(filePath, 480, 900);
			}
			return null;
		} catch (OutOfMemoryError ignored) {
			return null;
		}
	}

	public static String getLargePictureWithoutRoundedCorner(String url,
			FileDownloaderHttpHelper.DownloadListener downloadListener,
			FileLocationMethod fileLocationMethod) {

		String absoluteFilePath = FileManager.getFilePathFromUrl(url,
				fileLocationMethod);

		File file = new File(absoluteFilePath);

		if (file.exists()) {
			return absoluteFilePath;

		} else {

			getBitmapFromNetWork(url, absoluteFilePath, downloadListener);

			if (isThisBitmapCanRead(absoluteFilePath)) {
				return absoluteFilePath;
			} else {
				return "about:blank";
			}

		}

	}

	public static boolean isThisBitmapCanRead(String path) {
		File file = new File(path);

		if (!file.exists()) {
			return false;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;
		if (width == -1 || height == -1) {
			return false;
		}

		return true;
	}

	public static boolean isThisBitmapTooLargeToRead(String path) {
		File file = new File(path);

		if (!file.exists()) {
			return false;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;
		if (width == -1 || height == -1) {
			return false;
		}

		if (width > Utility.getBitmapMaxWidthAndMaxHeight()
				|| height > Utility.getBitmapMaxWidthAndMaxHeight()) {
			return true;
		} else {
			return false;
		}

	}

	public static int[] getBitmapSize(String path) {
		int[] result = { -1, -1 };
		File file = new File(path);

		if (!file.exists()) {
			return result;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;
		if (width > 0 && height > 0) {
			result[0] = width;
			result[1] = height;
		}

		return result;
	}

	public static Bitmap decodeBitmapFromSDCard(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);

	}

	public static Bitmap getWriteWeiboRoundedCornerPic(String url,
			int reqWidth, int reqHeight, FileLocationMethod method) {
		try {

			if (!FileManager.isExternalStorageMounted()) {
				return null;
			}

			String filePath = FileManager.getFilePathFromUrl(url, method);
			if (!filePath.endsWith(".jpg") && !filePath.endsWith(".gif"))
				filePath = filePath + ".jpg";

			boolean fileExist = new File(filePath).exists();

			if (!fileExist) {
				return null;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null) {
				// this picture is broken,so delete it
				new File(filePath).delete();
				return null;
			}

			int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(),
					reqWidth, reqHeight);
			if (size[0] > 0 && size[1] > 0) {
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
						size[0], size[1], true);
				if (scaledBitmap != bitmap) {
					bitmap.recycle();
					bitmap = scaledBitmap;
				}
			}
			return bitmap;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static Bitmap getWriteWeiboPictureThumblr(String filePath) {
		try {

			if (!FileManager.isExternalStorageMounted()) {
				return null;
			}

			boolean fileExist = new File(filePath).exists();

			if (!fileExist) {
				return null;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			options.inSampleSize = calculateInSampleSize(options, 60, 60);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			if (bitmap == null) {
				// this picture is broken,so delete it
				new File(filePath).delete();
				return null;
			}

			int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(), 60,
					60);
			if (size[0] > 0 && size[1] > 0) {
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
						size[0], size[1], true);
				if (scaledBitmap != bitmap) {
					bitmap.recycle();
					bitmap = scaledBitmap;
				}
			}
			return bitmap;
		} catch (OutOfMemoryError ignored) {
			ignored.printStackTrace();
			return null;
		}
	}

	public static boolean getBitmapFromNetWork(String url, String path,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {
		for (int i = 0; i < 3; i++) {
			if (HttpUtility.getInstance().executeDownloadTask(url, path,
					downloadListener)) {
				return true;
			}
			new File(path).delete();
		}

		return false;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (height > reqHeight && reqHeight != 0) {
				inSampleSize = (int) Math.floor((double) height
						/ (double) reqHeight);
			}

			int tmp = 0;

			if (width > reqWidth && reqWidth != 0) {
				tmp = (int) Math.floor((double) width / (double) reqWidth);
			}

			inSampleSize = Math.max(inSampleSize, tmp);

		}
		int roundedSize;
		if (inSampleSize <= 8) {
			roundedSize = 1;
			while (roundedSize < inSampleSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (inSampleSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int[] calcResize(int actualWidth, int actualHeight,
			int reqWidth, int reqHeight) {

		int height = actualHeight;
		int width = actualWidth;

		float betweenWidth = ((float) reqWidth) / (float) actualWidth;
		float betweenHeight = ((float) reqHeight) / (float) actualHeight;

		float min = Math.min(betweenHeight, betweenWidth);

		height = (int) (min * actualHeight);
		width = (int) (min * actualWidth);

		return new int[] { width, height };
	}

	public static String compressPic(Context context, String picPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;

//		switch (SettingUtility.getUploadQuality()) {
//		case 1:
//			return picPath;
//		case 2:
//			options.inSampleSize = 2;
//			break;
//		case 3:
//			options.inSampleSize = 4;
//			break;
//		case 4:
//			options.inSampleSize = 2;
//			if (Utility.isWifi(context)) {
//				return picPath;
//			}
//			break;
//		}

		Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
		FileOutputStream stream = null;
		String tmp = FileManager.getUploadPicTempFile();
		try {
			new File(tmp).getParentFile().mkdirs();
			new File(tmp).createNewFile();
			stream = new FileOutputStream(new File(tmp));
		} catch (IOException ignored) {

		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

		if (stream != null) {
			try {
				stream.close();
				bitmap.recycle();
			} catch (IOException ignored) {

			}
		}
		return tmp;
	}

	public static boolean isThisPictureGif(String url) {
		return !TextUtils.isEmpty(url) && url.endsWith(".gif");
	}

	// /////////////////////bitmap utility//////////////

	/**
	 * 保存图片到本地(JPG)
	 * 
	 * @param bm
	 *            保存的图片
	 * @return 图片路径
	 */
	public static String saveJpeg(Bitmap bm) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		File file = new File(CachePath.getImageCache(
				ZmarkApplication.getInstance()).getAbsolutePath()
				+ File.separator + "temp");
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".jpg";
		String filePath = file.getAbsolutePath() + fileName;
		File f = new File(filePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
				fileOutputStream = new FileOutputStream(filePath);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return filePath;
	}

	/**
	 * 保存图片到本地(PNG)
	 * 
	 * @param bm
	 *            保存的图片
	 * @return 图片路径
	 */
	public static String savePNG(Bitmap bm) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		File file = new File(CachePath.photocachePic);
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".png";
		String filePath = CachePath.photocachePic + fileName;
		File f = new File(filePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
				fileOutputStream = new FileOutputStream(filePath);
				bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return filePath;
	}

	/**
	 * 获取缩略图图片
	 * 
	 * @param imagePath
	 *            图片的路径
	 * @param width
	 *            图片的宽度
	 * @param height
	 *            图片的高度
	 * @return 缩略图图片
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * LOMO特效
	 * 
	 * @param bitmap
	 *            原图片
	 * @return LOMO特效图片
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768
				/ height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = (int) (newR * v) >> 16;
					gi = (int) (newG * v) >> 16;
					bi = (int) (newB * v) >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}

	/**
	 * 旧时光特效
	 * 
	 * @param bmp
	 *            原图片
	 * @return 旧时光特效图片
	 */
	public static Bitmap oldTimeFilter(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR,
						newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 暖意特效
	 * 
	 * @param bmp
	 *            原图片
	 * @param centerX
	 *            光源横坐标
	 * @param centerY
	 *            光源纵坐标
	 * @return 暖意特效图片
	 */
	public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int radius = Math.min(centerX, centerY);

		final float strength = 150F; // 光照强度 100~150
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = pixR;
				newG = pixG;
				newB = pixB;

				// 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
						centerX - k, 2));
				if (distance < radius * radius) {
					// 按照距离大小计算增加的光照值
					int result = (int) (strength * (1.0 - Math.sqrt(distance)
							/ radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 根据饱和度、色相、亮度调整图片
	 * 
	 * @param bm
	 *            原图片
	 * @param saturation
	 *            饱和度
	 * @param hue
	 *            色相
	 * @param lum
	 *            亮度
	 * @return 处理后的图片
	 */
	public static Bitmap handleImage(Bitmap bm, int saturation, int hue, int lum) {
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		ColorMatrix mLightnessMatrix = new ColorMatrix();
		ColorMatrix mSaturationMatrix = new ColorMatrix();
		ColorMatrix mHueMatrix = new ColorMatrix();
		ColorMatrix mAllMatrix = new ColorMatrix();
		float mSaturationValue = saturation * 1.0F / 127;
		float mHueValue = hue * 1.0F / 127;
		float mLumValue = (lum - 127) * 1.0F / 127 * 180;
		mHueMatrix.reset();
		mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);

		mSaturationMatrix.reset();
		mSaturationMatrix.setSaturation(mSaturationValue);
		mLightnessMatrix.reset();

		mLightnessMatrix.setRotate(0, mLumValue);
		mLightnessMatrix.setRotate(1, mLumValue);
		mLightnessMatrix.setRotate(2, mLumValue);

		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix);
		mAllMatrix.postConcat(mLightnessMatrix);

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
		canvas.drawBitmap(bm, 0, 0, paint);
		return bmp;
	}

	/**
	 * 添加图片外边框
	 * 
	 * @param context
	 *            上下文
	 * @param bm
	 *            原图片
	 * @param frameName
	 *            边框名称
	 * @return 带有边框的图片
	 */
	public static Bitmap combinateFrame(Context context, Bitmap bm,
			String frameName) {
		// 原图片的宽高
		int imageWidth = bm.getWidth();
		int imageHeight = bm.getHeight();

		// 边框
		Bitmap leftUp = decodeBitmap(context, frameName, 0);
		Bitmap leftDown = decodeBitmap(context, frameName, 2);
		Bitmap rightDown = decodeBitmap(context, frameName, 4);
		Bitmap rightUp = decodeBitmap(context, frameName, 6);
		Bitmap top = decodeBitmap(context, frameName, 7);
		Bitmap down = decodeBitmap(context, frameName, 3);
		Bitmap left = decodeBitmap(context, frameName, 1);
		Bitmap right = decodeBitmap(context, frameName, 5);

		Bitmap newBitmap = null;
		Canvas canvas = null;

		// 判断大小图片的宽高
		int judgeWidth = 0;
		int judgeHeight = 0;
		if ("frame7".equals(frameName)) {
			judgeWidth = leftUp.getWidth() + rightUp.getWidth()
					+ top.getWidth() * 5;
			judgeHeight = leftUp.getHeight() + leftDown.getHeight()
					+ left.getHeight() * 5;
		} else if ("frame10".equals(frameName)) {
			judgeWidth = leftUp.getWidth() + rightUp.getWidth()
					+ top.getWidth() * 5;
			judgeHeight = leftUp.getHeight() + leftDown.getHeight()
					+ left.getHeight() * 10;
		} else {
			judgeWidth = leftUp.getWidth() + rightUp.getWidth()
					+ top.getWidth();
			judgeHeight = leftUp.getHeight() + leftDown.getHeight()
					+ left.getHeight();
		}
		// 内边框
		if (imageWidth > judgeWidth && imageHeight > judgeHeight) {
			// 重新定义一个bitmap
			newBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
					Config.ARGB_8888);
			canvas = new Canvas(newBitmap);
			Paint paint = new Paint();
			// 画原图
			canvas.drawBitmap(bm, 0, 0, paint);
			// 上空余宽度
			int topWidth = imageWidth - leftUp.getWidth() - rightUp.getWidth();
			// 上空余填充个数
			int topCount = (int) Math.ceil(topWidth * 1.0f / top.getWidth());
			for (int i = 0; i < topCount; i++) {
				canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth() * i,
						0, paint);
			}
			// 下空余宽度
			int downWidth = imageWidth - leftDown.getWidth()
					- rightDown.getWidth();
			// 下空余填充个数
			int downCount = (int) Math.ceil(downWidth * 1.0f / down.getWidth());
			for (int i = 0; i < downCount; i++) {
				canvas.drawBitmap(down, leftDown.getWidth() + down.getWidth()
						* i, imageHeight - down.getHeight(), paint);
			}
			// 左空余高度
			int leftHeight = imageHeight - leftUp.getHeight()
					- leftDown.getHeight();
			// 左空余填充个数
			int leftCount = (int) Math.ceil(leftHeight * 1.0f
					/ left.getHeight());
			for (int i = 0; i < leftCount; i++) {
				canvas.drawBitmap(left, 0,
						leftUp.getHeight() + left.getHeight() * i, paint);
			}
			// 右空余高度
			int rightHeight = imageHeight - rightUp.getHeight()
					- rightDown.getHeight();
			// 右空余填充个数
			int rightCount = (int) Math.ceil(rightHeight * 1.0f
					/ right.getHeight());
			for (int i = 0; i < rightCount; i++) {
				canvas.drawBitmap(right, imageWidth - right.getWidth(),
						rightUp.getHeight() + right.getHeight() * i, paint);
			}
			// 画左上角
			canvas.drawBitmap(leftUp, 0, 0, paint);
			// 画左下角
			canvas.drawBitmap(leftDown, 0, imageHeight - leftDown.getHeight(),
					paint);
			// 画右下角
			canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
					imageHeight - rightDown.getHeight(), paint);
			// 画右上角
			canvas.drawBitmap(rightUp, imageWidth - rightUp.getWidth(), 0,
					paint);

		} else {
			if ("frame7".equals(frameName)) {
				imageWidth = leftUp.getWidth() + top.getWidth() * 5
						+ rightUp.getWidth();
				imageHeight = leftUp.getHeight() + left.getHeight() * 5
						+ leftDown.getHeight();
			} else if ("frame10".equals(frameName)) {
				imageWidth = leftUp.getWidth() + top.getWidth() * 5
						+ rightUp.getWidth();
				imageHeight = leftUp.getHeight() + left.getHeight() * 10
						+ leftDown.getHeight();
			} else {
				imageWidth = leftUp.getWidth() + top.getWidth()
						+ rightUp.getWidth();
				imageHeight = leftUp.getHeight() + left.getHeight()
						+ leftDown.getHeight();
			}
			newBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
					Config.ARGB_8888);
			canvas = new Canvas(newBitmap);
			Paint paint = new Paint();
			int newImageWidth = imageWidth - left.getWidth() - right.getWidth()
					+ 5;
			int newImageHeight = imageHeight - top.getHeight()
					- down.getHeight() + 5;
			bm = Bitmap.createScaledBitmap(bm, newImageWidth, newImageHeight,
					true);
			canvas.drawBitmap(bm, left.getWidth(), top.getHeight(), paint);
			if ("frame7".equals(frameName)) {

				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth()
							* i, 0, paint);
				}

				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(left, 0,
							leftUp.getHeight() + left.getHeight() * i, paint);
				}

				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(right, imageWidth - right.getWidth(),
							rightUp.getHeight() + right.getHeight() * i, paint);
				}

				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(down,
							leftDown.getWidth() + down.getWidth() * i,
							imageHeight - down.getHeight(), paint);
				}
				canvas.drawBitmap(leftUp, 0, 0, paint);
				canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth()
						* 5, 0, paint);
				canvas.drawBitmap(leftDown, 0,
						leftUp.getHeight() + left.getHeight() * 5, paint);
				canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
						rightUp.getHeight() + right.getHeight() * 5, paint);

			} else if ("frame10".equals(frameName)) {
				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth()
							* i, 0, paint);
				}

				for (int i = 0; i < 10; i++) {
					canvas.drawBitmap(left, 0,
							leftUp.getHeight() + left.getHeight() * i, paint);
				}

				for (int i = 0; i < 10; i++) {
					canvas.drawBitmap(right, imageWidth - right.getWidth(),
							rightUp.getHeight() + right.getHeight() * i, paint);
				}

				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(down,
							leftDown.getWidth() + down.getWidth() * i,
							imageHeight - down.getHeight(), paint);
				}
				canvas.drawBitmap(leftUp, 0, 0, paint);
				canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth()
						* 5, 0, paint);
				canvas.drawBitmap(leftDown, 0,
						leftUp.getHeight() + left.getHeight() * 10, paint);
				canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
						rightUp.getHeight() + right.getHeight() * 10, paint);
			} else {
				canvas.drawBitmap(leftUp, 0, 0, paint);
				canvas.drawBitmap(top, leftUp.getWidth(), 0, paint);
				canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth(),
						0, paint);
				canvas.drawBitmap(left, 0, leftUp.getHeight(), paint);
				canvas.drawBitmap(leftDown, 0,
						leftUp.getHeight() + left.getHeight(), paint);
				canvas.drawBitmap(right, imageWidth - right.getWidth(),
						rightUp.getHeight(), paint);
				canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
						rightUp.getHeight() + right.getHeight(), paint);
				canvas.drawBitmap(down, leftDown.getWidth(),
						imageHeight - down.getHeight(), paint);
			}
		}
		// 回收
		leftUp.recycle();
		leftUp = null;
		leftDown.recycle();
		leftDown = null;
		rightDown.recycle();
		rightDown = null;
		rightUp.recycle();
		rightUp = null;
		top.recycle();
		top = null;
		down.recycle();
		down = null;
		left.recycle();
		left = null;
		right.recycle();
		right = null;
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newBitmap;
	}

	public static void doClippingImage(String path, String newpath) {

		if (!TextUtils.isEmpty(path)) {
			try {
				File f = new File(newpath);
				Bitmap bm = doCropBitmap(path);
				FileOutputStream fos = new FileOutputStream(f);
				bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
				if (!bm.isRecycled())
					bm.recycle();
				Utility.deleteFile(path);
				doUpdateSystemGallery();
			} catch (Exception e) {
				Log.e(TAG, "error", e);
			}
		}
	}

	public static void doUpdateSystemGallery() {

		try {
			ZmarkApplication.getInstance().sendBroadcast(
					new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		} catch (Exception e) {
			Log.e(TAG, "error", e);
		}
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap doCropBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 获取边框图片
	 * 
	 * @param context
	 *            上下文
	 * @param frameName
	 *            边框名称
	 * @param position
	 *            边框的类型
	 * @return 边框图片
	 */
	private static Bitmap decodeBitmap(Context context, String frameName,
			int position) {
		try {
			switch (position) {
			case 0:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/leftup.png"));
			case 1:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/left.png"));
			case 2:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/leftdown.png"));
			case 3:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/down.png"));
			case 4:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/rightdown.png"));
			case 5:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/right.png"));
			case 6:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/rightup.png"));
			case 7:
				return BitmapFactory.decodeStream(context.getAssets().open(
						"frames/" + frameName + "/up.png"));
			default:
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 添加内边框
	 * 
	 * @param bm
	 *            原图片
	 * @param frame
	 *            内边框图片
	 * @return 带有边框的图片
	 */
	public static Bitmap addBigFrame(Bitmap bm, Bitmap frame) {
		Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		Paint paint = new Paint();
		canvas.drawBitmap(bm, 0, 0, paint);
		frame = Bitmap.createScaledBitmap(frame, bm.getWidth(), bm.getHeight(),
				true);
		canvas.drawBitmap(frame, 0, 0, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newBitmap;

	}

	/**
	 * 创建一个缩放的图片
	 * 
	 * @param path
	 *            图片地址
	 * @param w
	 *            图片宽度
	 * @param h
	 *            图片高度
	 * @return 缩放后的图片
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 黑白效果
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	// 浮雕效果
	public static Bitmap toFuDiao(Bitmap mBitmap) {
		Paint mPaint;

		// Bitmap mBitmap;
		// Bitmap mBitmap = toGrayscale(bmpOriginal);
		int mBitmapWidth = 0;
		int mBitmapHeight = 0;

		int mArrayColor[] = null;
		int mArrayColorLengh = 0;
		long startTime = 0;
		int mBackVolume = 0;

		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		Log.d("zlc", "2");
		// Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
		// Bitmap.Config.ARGB_8888);
		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Bitmap.Config.RGB_565);
		Log.d("zlc", "3");
		mArrayColorLengh = mBitmapWidth * mBitmapHeight;
		Log.d("zlc", "4");
		// mArrayColor = new int[mArrayColorLengh];
		// int[] mArrayColor2 = new int[mArrayColorLengh];
		// mBitmap.getPixels(mArrayColor2, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		Log.d("zlc", "5");
		int count = 0;
		int preColor = 0;
		int prepreColor = 0;
		int color = 0;
		preColor = mBitmap.getPixel(0, 0);
		// preColor = mArrayColor2[0];
		Log.d("zlc", "4");

		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);
				int r = Color.red(curr_color) - Color.red(prepreColor) + 128;
				int g = Color.green(curr_color) - Color.red(prepreColor) + 128;
				int b = Color.green(curr_color) - Color.blue(prepreColor) + 128;
				int a = Color.alpha(curr_color);
				int modif_color = Color.argb(a, r, g, b);
				bmpReturn.setPixel(i, j, modif_color);
				prepreColor = preColor;
				preColor = curr_color;
			}
		}

		// for (int i = 0; i < mArrayColorLengh; i++) {
		// color = mArrayColor2[i];
		// int r = Color.red(color) - Color.red(prepreColor) +128;
		// int g = Color.green(color) - Color.red(prepreColor) +128;
		// int b = Color.green(color) - Color.blue(prepreColor) +128;
		// int a = Color.alpha(color);
		//
		// mArrayColor[i] = Color.argb(a, r, g, b);
		// prepreColor = preColor;
		// preColor = color;
		//
		// }

		Log.d("zlc", "6");
		// bmpReturn.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		Canvas c = new Canvas(bmpReturn);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpReturn, 0, 0, paint);
		Log.d("zlc", "7");
		return bmpReturn;
	}

	/* 设置图片模糊 */
	public static Bitmap toMohu(Bitmap bmpSource, int Blur) // 源位图，模糊强度
	{
		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
				bmpSource.getHeight(), Bitmap.Config.ARGB_8888);
		int pixels[] = new int[bmpSource.getWidth() * bmpSource.getHeight()];
		int pixelsRawSource[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3];
		int pixelsRawNew[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3];

		bmpSource.getPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight());

		for (int k = 1; k <= Blur; k++) {
			// 从图片中获取每个像素三原色的值
			for (int i = 0; i < pixels.length; i++) {
				pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
				pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
				pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
			}
			// 取每个点上下左右点的平均值作自己的值
			int CurrentPixel = bmpSource.getWidth() * 3 + 3;
			// 当前处理的像素点，从点(2,2)开始
			for (int i = 0; i < bmpSource.getHeight() - 3; i++) // 高度循环
			{
				for (int j = 0; j < bmpSource.getWidth() * 3; j++) // 宽度循环
				{
					CurrentPixel += 1; // 取上下左右，取平均值
					int sumColor = 0; // 颜色和
					sumColor = pixelsRawSource[CurrentPixel
							- bmpSource.getWidth() * 3]; // 上一点
					sumColor = sumColor + pixelsRawSource[CurrentPixel - 3]; // 左一点
					sumColor = sumColor + pixelsRawSource[CurrentPixel + 3]; // 右一点
					sumColor = sumColor
							+ pixelsRawSource[CurrentPixel
									+ bmpSource.getWidth() * 3]; // 下一点
					pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4); // 设置像素点
				}
			}

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0],
						pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
			}
		}

		bmpReturn.setPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight()); // 必须新建位图然后填充，不能直接填充源图像，否则内存报错
		return bmpReturn;
	}

	/* 设置图片积木效果 */
	public static Bitmap toJiMu(Bitmap mBitmap) {
		Paint mPaint;

		int mBitmapWidth = 0;
		int mBitmapHeight = 0;

		int mArrayColor[] = null;
		int mArrayColorLengh = 0;
		long startTime = 0;
		int mBackVolume = 0;

		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		// Log.d("zlc", "2");
		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Bitmap.Config.ARGB_8888);
		// Log.d("zlc", "3");
		mArrayColorLengh = mBitmapWidth * mBitmapHeight;
		// Log.d("zlc", "4");
		// mArrayColor = new int[mArrayColorLengh];
		// int[] mArrayColor2 = new int[mArrayColorLengh];
		// mBitmap.getPixels(mArrayColor2, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		// Log.d("zlc", "5");
		int count = 0;
		int preColor = 0;
		int color = 0;

		// Log.d("zlc", "4");
		int iPixel = 0;
		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);

				int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
						.blue(curr_color)) / 3;
				if (avg >= 100) {
					iPixel = 255;
				} else {
					iPixel = 0;
				}
				int modif_color = Color.argb(255, iPixel, iPixel, iPixel);

				bmpReturn.setPixel(i, j, modif_color);
			}
		}

		// Log.d("zlc", "6");
		// bmpReturn.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		// Log.d("zlc", "7");
		return bmpReturn;
	}

	/* 设置油画 */
	public static Bitmap toYouHua(Bitmap bmpSource) // 源位图
	{
		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
				bmpSource.getHeight(), Bitmap.Config.ARGB_8888);
		int color = 0;
		int Radio = 0;
		int width = bmpSource.getWidth();
		int height = bmpSource.getHeight();

		Random rnd = new Random();
		int iModel = 10;
		int i = width - iModel;
		while (i > 1) {
			int j = height - iModel;
			while (j > 1) {
				int iPos = rnd.nextInt(100000) % iModel;
				// 将半径之内取任一点
				color = bmpSource.getPixel(i + iPos, j + iPos);
				bmpReturn.setPixel(i, j, color);
				j = j - 1;
			}
			i = i - 1;
		}
		return bmpReturn;
	}

	// /////////////////////bitmap util./////////////////////////
	private static String TAG = "BitmapUtil";
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] bmpToByteArray(Bitmap src, Bitmap.CompressFormat format) {
		if (src == null) {
			return null;
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		src.compress(format, 100, outStream);
		return outStream.toByteArray();
	}

	public static Bitmap extractThumbNail(final String path, final int height,
			final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0
				&& width > 0);
		BitmapFactory.Options options = new BitmapFactory.Options();
		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height
					+ ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = "
					+ beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
					: (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight
					+ ", orig=" + options.outWidth + "x" + options.outHeight
					+ ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG,
					"bitmap decoded size=" + bm.getWidth() + "x"
							+ bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
					newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm,
						(bm.getWidth() - width) >> 1,
						(bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG,
						"bitmap croped size=" + bm.getWidth() + "x"
								+ bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}

	public static byte[] extractBmpByteArrayUrl(String url) {
		ByteArrayOutputStream outStream = null;
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(15 * 1000);
			conn.setRequestMethod("GET");
			InputStream stream = conn.getInputStream();
			byte[] buffer = new byte[4 * 1024];
			outStream = new ByteArrayOutputStream();
			int temp;
			while ((temp = stream.read(buffer)) != -1) {
				outStream.write(buffer, 0, temp);
			}
			buffer = null;
			stream.close();
			byte[] bytes = outStream.toByteArray();
			outStream.close();
			return bytes;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] extractBmpByteArrayUrl(String url, Handler handler) {
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(30 * 1000);
			conn.setRequestMethod("GET");
			InputStream stream = conn.getInputStream();
			byte[] buffer = new byte[4 * 1024];
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int temp;
			int mProgressPos = 0;
			long fileLength = conn.getContentLength();
			while ((temp = stream.read(buffer)) != -1) {
				outStream.write(buffer, 0, temp);
				mProgressPos += temp;
				android.os.Message msg = handler.obtainMessage(0);
				msg.arg1 = (int) ((float) mProgressPos / fileLength * 100);
				msg.sendToTarget();
			}
			buffer = null;
			stream.close();
			byte[] bytes = outStream.toByteArray();
			outStream.close();
			return bytes;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] extractBmpByteArrayPath(String path) {
		if (path == null) {
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			LogHelper.d(TAG, " file not found");
			return null;
		}
		int len = (int) file.length();
		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(path, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.readFully(b);
			in.close();
		} catch (Exception e) {
			LogHelper.e(TAG, "errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}

	public static byte[] extractBmpByteArrayPath(String fileName, int offset,
			int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			LogHelper.d(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		LogHelper.d(TAG, "readFromFile : offset = " + offset + " len = " + len
				+ " offset + len = " + (offset + len));

		if (offset < 0) {
			LogHelper.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if (len <= 0) {
			LogHelper.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if (offset + len > (int) file.length()) {
			LogHelper.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			LogHelper.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}

	public static Bitmap DrawableToBitmap(Drawable src) {
		return ((BitmapDrawable) src).getBitmap();
	}

	public static Drawable BitmapToDrawable(Bitmap src) {
		return new BitmapDrawable(src);
	}

	public static Drawable extractDrawableById(Context context, int id) {
		return new BitmapDrawable(extracBitmapById(context, id));
	}

	public static Bitmap extracBitmapById(Context context, int id) {
		try {

			if (id <= 0)
				return null;
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			// 获取资源图片
			InputStream is = context.getResources().openRawResource(id);
			return BitmapFactory.decodeStream(is, null, opt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap extractBmpPath(String path, int width, int height) {
		final byte[] bytes = extractBmpByteArrayPath(path);
		if (bytes == null)
			return null;
		return extractBmpByte(bytes, width, height, false);
	}

	/**
	 * 根据原图片URL orginPath保存一个新的本地图片，得到新的url savePath
	 */
	public static String extractSaveBmpPath(String orginPath, String savePath,
			int width, int height) {
		try {
			Bitmap bmp = extractBmpPath(orginPath, width, height);
			if (bmp != null)
				return extractSaveBitmap(savePath, bmp);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean extractSaveBmpPath(String orginPath, String savePath,
			String saveName, int width, int height) {
		try {
			Bitmap bmp = extractBmpPath(orginPath, width, height);
			if (bmp != null
					&& extractSaveBitmap(savePath, saveName, bmp) != null)
				return true;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Bitmap extractBmpPath(String path, int width, int height,
			boolean crop) {
		final byte[] bytes = extractBmpByteArrayPath(path);
		if (bytes == null)
			return null;
		return extractBmpByte(bytes, height, width, crop);
	}

	public static Bitmap extractBmpByte(byte[] bytes, int width, int height,
			boolean crop) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		if (width == 0)
			width = 480;
		if (height == 0)
			height = 800;
		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
			if (tmp != null) {
				// tmp.recycle();
				tmp = null;
			}
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			try {
				options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
						: (beY < beX ? beX : beY));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}
			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options); // 缩小到很小的时候
			if (bm == null) {
				return null;
			}
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
					newHeight, true);
			if (scale != null) {
				// bm.recycle();
				bm = scale;
			}
			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm,
						(bm.getWidth() - width) >> 1,
						(bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}
				// bm.recycle();
				bm = cropped;
			}
			bm = ThumbnailUtils.extractThumbnail(bm, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bm;

		} catch (final OutOfMemoryError e) {
			options = null;
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap extractBmpUrl(String url) {
		final byte[] bytes = extractBmpByteArrayUrl(url);
		if (null != bytes)
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static Bitmap extractBmpUrl(String url, int width, int height) {
		final byte[] bytes = extractBmpByteArrayUrl(url);
		if (null != bytes)
			return extractBmpByte(bytes, width, height, true);
		return null;
	}

	public static Bitmap extractBmpPath(String path) {
		return BitmapFactory.decodeFile(path);
	}

	public long extractBitmapSize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return bitmap.getByteCount();
		else
			return bitmap.getRowBytes() * bitmap.getHeight();

	}

	public static Bitmap extractBmpUrl(String url, int width, int height,
			boolean crop) {
		final byte[] bytes = extractBmpByteArrayUrl(url);
		if (null != bytes)
			return extractBmpByte(bytes, width, height, crop);
		return null;
	}

	/**
	 * 图片处理圆角
	 */
	public static Bitmap extractRoundCornerBmp(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap extractRoundCornerBmp(String pathurl, int pixels,
			boolean isfile) {
		Bitmap bitmap = isfile ? extractBmpPath(pathurl)
				: extractBmpUrl(pathurl);
		Bitmap output;
		if (bitmap == null)
			return null;
		output = extractRoundCornerBmp(bitmap, pixels);
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		return output;
	}

	public static Bitmap extractRoundCornerBmp(String pathurl, int pixels,
			int width, int height, boolean isfile) {
		Bitmap bitmap = isfile ? extractBmpPath(pathurl, width, height)
				: extractBmpUrl(pathurl, width, height, true);
		Bitmap output;
		if (bitmap == null)
			return null;
		output = extractRoundCornerBmp(bitmap, pixels);
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		return output;
	}

	public static String extractSaveBitmap(String savepath, Bitmap mBitmap) {
		if (TextUtils.isEmpty(savepath))
			return null;
		File f = new File(savepath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		try {
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			return savepath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String extractSaveBitmap(String savepath, String fileName,
			Bitmap mBitmap) {
		if (TextUtils.isEmpty(savepath))
			return null;
		try {
			Utility.mkDirs(savepath);
		} catch (Exception e) {
		}
		File f = new File(savepath, fileName);

		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		try {
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			return savepath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static Bitmap extractBmpUrl(Context context, String url, long uid,
	// boolean flag1) {
	// File file = CachePath.getImageCache(context);
	// if (file == null)
	// return null;
	// String path = file.getAbsolutePath();
	// if (!flag1)
	// return extractBmpPath(path + File.separator + String.valueOf(uid)
	// + ".jpg");
	// else {
	// if (com.gogotown.bean.util.Utility.isExist(path + File.separator
	// + String.valueOf(uid) + ".jpg"))
	// return extractBmpPath(path + File.separator
	// + String.valueOf(uid) + ".jpg");
	// else // 先下载
	// {
	// com.gogotown.bean.util.Utility.downGuideImgLoadFile(url,
	// path, String.valueOf(uid) + ".jpg");
	// return extractBmpPath(path + File.separator
	// + String.valueOf(uid) + ".jpg");
	// }
	// }
	// }

	// public static boolean extractDeleteBmpUrl(Context context, long uid) {
	// File file = CachePath.getImageCache(context);
	// if (file == null)
	// return false;
	// String path = file.getAbsolutePath();
	// try {
	// com.gogotown.bean.util.Utility.deleteFile(path + File.separator
	// + String.valueOf(uid) + ".jpg");
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// return true;
	// }
	// public static boolean extractRenderView(Context context,RenderView view)
	// {
	//
	// }
	// public void saveMyBitmap(String bitName, Bitmap mBitmap) {
	//
	// File f = new File(Environment.getExternalStorageDirectory(), fileName);
	//
	// FileOutputStream fOut = null;
	//
	// try {
	//
	// f.createNewFile();
	//
	// fOut = new FileOutputStream(f);
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	//
	// }
	//
	// mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	//
	// try {
	//
	// fOut.flush();
	//
	// fOut.close();
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	//
	// }
	//
}
