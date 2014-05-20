/**
 * @author ChenDu GOGO  oblivion
 * @ʱ��:    2013 ����10:13:13
 * @����   com.gogotown
   @������ GoGoCity
 */
package com.android.zmark.bean;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

public class IntentInterface {
	public static void IntentApkInstall(Context context, String apkPath) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apkPath)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static void IntentAll(Activity context, Bundle bundle, Class<?> cls,
			int requestCode) {
		Intent intent = new Intent(context, cls);
		if (bundle != null)
			intent.putExtra("data", bundle);
		if (requestCode != -1)
			context.startActivityForResult(intent, requestCode);
		else
			context.startActivity(intent);
	}

	public static void IntentCall(Context context, String phoneNumber) {
		Uri uri = Uri.parse("tel:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}

	public static void IntentDial(Context context, String phoneNumber) {
		Uri uri = Uri.parse("tel:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(intent);
	}

	public static void IntentEmail(Context context, String emailBody) {
		Uri uri = Uri.parse("mailto:");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.putExtra(Intent.EXTRA_TEXT, emailBody);
		intent.putExtra(Intent.EXTRA_CC, "");
		intent.putExtra(Intent.EXTRA_SUBJECT, "邮件分享");
		context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
	}

	public static void IntentEmail(Context context, String toUser,
			String emailBody) {
		Uri uri = Uri.parse("mailto:" + toUser);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra(Intent.EXTRA_TEXT, emailBody);
		intent.putExtra(Intent.EXTRA_CC, "");
		intent.putExtra(Intent.EXTRA_SUBJECT, "邮件分享");
		context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
	}

	public static void IntentEmail(Context context, String toUser,
			String ccUser, String emailBody, String subJect) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		String[] tos = { toUser };
		String[] ccs = { ccUser };
		intent.putExtra(Intent.EXTRA_EMAIL, tos);
		intent.putExtra(Intent.EXTRA_CC, ccs);
		intent.putExtra(Intent.EXTRA_TEXT, emailBody);
		intent.putExtra(Intent.EXTRA_SUBJECT, subJect);
		intent.setType("message/rfc822");
		context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
		// intent.putExtra(Intent.EXTRA_SUBJECT, "主题");
		// intent.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/ataaw.mp3");
		// sendIntent.setType("audio/mp3");
	}

	public static void IntentESMS(Context context, String smsBody) {
		Uri uri = Uri.parse("content://media/external/images/media/exp");
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra("sms_body", smsBody);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setType("image/png");
		context.startActivity(intent);
	}

	public static void IntentMap(Context context, String geola, String geoln) {
		Uri uri = Uri.parse("geo:" + geola + "," + geoln);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
		// Intent i = new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
		// + mDetail.getDpWDZB() + "," + mDetail.getdPJDZB()));
	}

	public static void IntentMap(Context context, String geola, String geoln,
			String geoName) {
		// Uri uri = Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
		// + geola + "," + geoln + "(" + geoName + ")");
		Uri uri = Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
				+ geola + "," + geoln);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(intent);
	}

	public static void IntentSMS(Context context, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}

	public static void IntentSMS(Context context, String phoneNumber,
			String smsBody) {
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}

	public static void IntentVideo(Context context, String mediaUrl,
			String ccUser, String emailBody, String subJect) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(mediaUrl);
		intent.setDataAndType(uri, "audio/mp3");
		context.startActivity(intent);
	}

	public static void IntentWebUrl(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
	public static void IntentBroadcast(Context mContext, String action,
			Bundle bundle) {
		Intent intent = new Intent(action);
		if (bundle != null)
			intent.putExtra("data", bundle);
		mContext.sendBroadcast(intent);
	}
}
