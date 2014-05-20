/**
 * Created on 2012-5-3 下午3:35:11
 */
package com.android.zmark.bean.db;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.zmark.R;


public class ImageUtil {

	private static final int MAX_SIZE = 4;
	 
	private static Map<String, SoftReference<Drawable>> drawableMap = new HashMap<String, SoftReference<Drawable>>();
	
	public static void setDrawable(final ImageView v, String url, Context context) {
		if (TextUtils.isEmpty(url)) {
			v.setImageResource(R.drawable.ic_launcher);
			return;
		}

		if (drawableMap.containsKey(url)) {
			Drawable cacheDraw = drawableMap.get(url).get();
			if (cacheDraw != null && v.getDrawable() != cacheDraw) {
				v.setImageDrawable(cacheDraw);
				return;
			}
		}

		AsyncImageLoader.getInstance(context).loadDrawable(url, new AsyncImageLoader.ImageCallback() {

			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				if (imageDrawable != null && imageDrawable != v.getDrawable()) {
					v.setImageDrawable(imageDrawable);
					if (drawableMap.size() > MAX_SIZE) {
						drawableMap.clear();
					}
					drawableMap.put(imageUrl, new SoftReference<Drawable>(imageDrawable));
				}
			}
		});
	}
	
	
	public static void setBackDrawable(final ImageView v, String url, Context context) {
		if (TextUtils.isEmpty(url)) {
			v.setImageResource(R.drawable.ic_launcher);
			return;
		}

		if (drawableMap.containsKey(url)) {
			Drawable cacheDraw = drawableMap.get(url).get();
			if (cacheDraw != null && v.getDrawable() != cacheDraw) {
				v.setBackgroundDrawable(cacheDraw);
				return;
			}
		}

		AsyncImageLoader.getInstance(context).loadDrawable(url, new AsyncImageLoader.ImageCallback() {

			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				if (imageDrawable != null && imageDrawable != v.getDrawable()) {
					v.setBackgroundDrawable(imageDrawable);
					if (drawableMap.size() > MAX_SIZE) {
						drawableMap.clear();
					}
					drawableMap.put(imageUrl, new SoftReference<Drawable>(imageDrawable));
				}
			}
			
		});
	}
	
	
	//将Drawable 转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        // 取 drawable 的长宽  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }  

	
	
	/** 
	* 转换图片成圆形 
	* @param bitmap 传入Bitmap对象 
	* @return 
	*/ 
	
	public static Bitmap toRoundBitmap(Bitmap bitmap) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
			bitmap.getHeight(), Config.ARGB_8888); 
			Canvas canvas = new Canvas(output); 

			final int color = 0xff424242; 
			final Paint paint = new Paint(); 
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
			final RectF rectF = new RectF(rect); 
			final float roundPx = bitmap.getWidth() / 2; 

			paint.setAntiAlias(true); 
			canvas.drawARGB(0, 0, 0, 0); 
			paint.setColor(color); 
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
			canvas.drawBitmap(bitmap, rect, rect, paint); 
			return output; 
	} 
	
	/**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
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
}
