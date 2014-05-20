package com.android.zmark.bean.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Looper;
import android.util.Log;

import com.android.zmark.bean.db.DateInfo;

public class UpLoadTouXiang{
	// 上传用户头像
		public static void upload(final String id,final File file) {
			new Thread() {
				public void run() {
					String end = "\r\n";
					String twoHyphens = "--";
					String boundary = "*****";
					try {
						Looper.prepare();

						URL url = new URL(DateInfo.ROOT + DateInfo.UPLOAD_IMG
								+ "?id=" + id);

						HttpURLConnection con = (HttpURLConnection) url
								.openConnection();

						/* 允许Input、Output，不使用Cache */
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setUseCaches(false);
						/* 设定传送的method=POST */
						con.setRequestMethod("POST");
						/* setRequestProperty */
						con.setRequestProperty("Connection", "Keep-Alive");
						con.setRequestProperty("Charset", "UTF-8");
						con.setRequestProperty("Content-Type",
								"multipart/form-data;boundary=" + boundary);
						/* 设定DataOutputStream */
						DataOutputStream ds = new DataOutputStream(
								con.getOutputStream());
						ds.writeBytes(twoHyphens + boundary + end);
						ds.writeBytes("Content-Disposition: form-data;"
								+ "name=\"file1\";filename=\"" + file.getName()
								+ "\"" + end);
						ds.writeBytes(end);

						/* 取得文件的FileInputStream */

						FileInputStream fStream = new FileInputStream(file);// 要上传的文件、
						/* 设定每次写入1024bytes */
						int bufferSize = 200;

						byte[] buffer = new byte[bufferSize];

						int length = fStream.read(buffer);
						/* 从文件读取数据到缓冲区 */
						while (length > 0) {
							/* 将数据写入DataOutputStream中 */
							ds.write(buffer, 0, length);
							length = fStream.read(buffer);
						}
						ds.writeBytes(end);
						ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
						fStream.close();
						ds.flush();

						/* 取得Response内容 */

						InputStream is = con.getInputStream();
						InputStreamReader in = new InputStreamReader(is, "UTF-8");
						BufferedReader bf = new BufferedReader(in);

						String str = bf.readLine();
						StringBuffer b = new StringBuffer();
						while (str != null) {
							b.append(str);
							str = bf.readLine();
						}
						Log.i("===", b.toString());
						ds.close();

					} catch (Exception e) {
						e.printStackTrace();
						
						Log.i("=====", "头像上传失败");
					}
				}
			}.start();

		}

}
	
	