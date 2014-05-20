package com.android.zmark.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.PhotoType;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.support.CachePath;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.UpLoadTouXiang;
import com.android.zmark.bean.util.Utility;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 用户资料信息
 * 
 * 
 * @author yangwenfang
 * 
 */

public class UserCenterActivity<T> extends AbstractActivity implements
		OnClickListener {

	ImageView ivHeader;
	EditText edNickName, edAddress, edIdcard;
	TextView tvSex, tvPhone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_activity_layout);
		init();
	}

	public void init() {
		onBackPressed(R.id.iv_back);
		ivHeader = (ImageView) findViewById(R.id.iv_user_header);
		edNickName = (EditText) findViewById(R.id.ed_nickname);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		edIdcard = (EditText) findViewById(R.id.ed_idcard);
		edAddress = (EditText) findViewById(R.id.ed_address);
		tvSex = (TextView) findViewById(R.id.tv_sex);
		findViewById(R.id.bt_submit).setOnClickListener(this);
		findViewById(R.id.bt_modify_header).setOnClickListener(this);
		findViewById(R.id.rel_sex).setOnClickListener(this);
		if (ZmarkApplication.getInstance().userIsOline()) {
			try {
				JSONObject jsonInfo = new JSONObject(
						SettingUtility.getUserInfo());
				edNickName.setText(jsonInfo.optString("BabyName"));
				tvPhone.setText(jsonInfo.optString("PhoneNo"));
				edIdcard.setText(jsonInfo.optString("Consignee"));
				edAddress.setText(jsonInfo.optString("Address"));
				tvSex.setText(jsonInfo.optString("Sex"));
				if (!TextUtils.isEmpty(jsonInfo.optString("Img")))
					imageLoader.displayImage(
							DateInfo.IP + jsonInfo.optString("Img"), ivHeader,
							options, new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									ivHeader.setImageBitmap(ImageUtil
											.toRoundBitmap(loadedImage));
								}

								@Override
								public void onLoadingCancelled(String imageUri,
										View view) {
								}
							});
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private File mTempFile;
	private String mPhotePath; // 图片地址 false

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_sex:
			new AlertDialog.Builder(mContext)
					.setTitle("性别选择")
					.setSingleChoiceItems(new String[] { "男", "女" }, 0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										tvSex.setText("男");

										break;
									case 1:
										tvSex.setText("女");
										break;
									default:
										break;
									}
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).create().show();
			break;
		case R.id.bt_submit:
			ShowDialog("提交中...");
			getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
					httpCallback);
			Utility.onHideInputSoftKeyboard(edAddress);
			Utility.onHideInputSoftKeyboard(edNickName);
			break;
		case R.id.bt_modify_header:
			CharSequence[] items = { "拍照", "相册" };
			AlertDialog.Builder mPhoteBuilder = new Builder(this);
			mPhoteBuilder.setTitle("修改头像")
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (which == 0) {
								if (!Utility.isSDExist()) {
									Toast.makeText(mContext, "SD卡不可用，请检查SD卡",
											Toast.LENGTH_SHORT).show();
									return;
								}
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								mTempFile = new File(CachePath.photocachePic,
										System.currentTimeMillis() + ".jpg");
								if (!mTempFile.getParentFile().exists())
									mTempFile.getParentFile().mkdirs();
								intent.putExtra(MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(mTempFile));
								startActivityForResult(intent,
										PhotoType.TYPE_PHOTOCAMRA);

							} else if (which == 1) {
								Intent i = new Intent(Intent.ACTION_PICK, null);
								i.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(i,
										PhotoType.TYPE_GALLERY);
							}

						}
					}).create().show();
			break;
		default:
			break;
		}
	}

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		// praisBbundle.putBoolean("isPrais", type != 0 && selectType == 1);
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(SettingUtility.getUserInfo());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Map<String, String> map = new HashMap<String, String>();
				map.clear();
				// http://app10014.yunbosoft.com:9090/Interface/GetSlide.ashx?w=720&h=1280
				map.put("id", SettingUtility.getDefaultUserId());
				map.put("name", edNickName.getText().toString());
				map.put("mobilephone", tvPhone.getText().toString());
				map.put("password",
						jsonObject != null ? jsonObject.optString("Password")
								: "");
				map.put("consignee", edIdcard.getText().toString());
				map.put("address", edAddress.getText().toString());
				map.put("sex", tvSex.getText().toString());
				return new HttpLoader(mContext,
						ApiServerPath.DM_USER_MODIFY_INFO, map);

			}
			return null;
		}

		@Override
		public void onLoadFinished(Loader<AsyncTaskLoaderResult<T>> loader,
				AsyncTaskLoaderResult<T> result) {
			T data = result != null ? result.data : null;
			Bundle args = result != null ? result.args : null;
			HttpResponseBean responseBean = (HttpResponseBean) data;
			switch (loader.getId()) {
			case LOADER_INITDATA:
				DismissDialog();
				if (responseBean == null) {
					Toast.makeText(mContext, "网络连接失败!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {

					Toast.makeText(
							mContext,
							TextUtils.isEmpty(responseBean.getMessage()) ? "登录失败!"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, "修改成功!", Toast.LENGTH_SHORT).show();
				JSONArray jsonArray = responseBean.getData().optJSONArray(
						"userInfo");
				SettingUtility.setUserInfo(jsonArray.optJSONObject(0)
						.toString());
				SettingUtility.setDefaultUserId(jsonArray.optJSONObject(0)
						.optString("Id"));
				SettingUtility.setDefaultToken(jsonArray.optJSONObject(0)
						.optString("Token"));
				SettingUtility.setDefaultPhone(jsonArray.optJSONObject(0)
						.optString("PhoneNo"));
				finish();
				break;

			}
			getLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};
	private Bitmap mUploadBmp;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == PhotoType.TYPE_PHOTOCAMRA) {
			// if (android.os.Build.VERSION.SDK_INT < 10) {
			// if (mTempFile == null || !mTempFile.exists()) {
			// LogHelper.i(tag, "图片获取失败!");
			// Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
			// return;
			// }
			// mPhotePath = mTempFile.getAbsolutePath();
			// if (mPhotePath != null && !"".equals(mPhotePath)) {
			// doCropImage();
			// }
			// } else if (android.os.Build.VERSION.SDK_INT >= 10) {
			// Uri uri = data.getData();
			// Cursor cursor = this.getContentResolver().query(uri, null,
			// null, null, null);
			// if (cursor.moveToFirst()) {
			// mPhotePath = cursor.getString(cursor
			// .getColumnIndex("_data"));// 获取绝对路径
			// if (mPhotePath != null && !"".equals(mPhotePath)) {
			// doCropImage();
			// }
			// cursor.close();
			// }
			// }
			String mPhotePath = null;
			Object object = null;
			if (data != null) {
				// HTC
				if (data.getData() != null) {
					// 根据返回的URI获取对应的SQLite信息
					Cursor cursor = this.getContentResolver().query(
							data.getData(), null, null, null, null);
					String path = data.getData().getPath();
					if (cursor == null && path == null)
						return;
					if (path != null)
						mPhotePath = path;
					else if (cursor != null && cursor.moveToFirst()) {
						mPhotePath = cursor.getString(cursor
								.getColumnIndex("_data"));// 获取绝对路径
						cursor.close();
					} else
						return;

				} else {
					// 三星 小米(小米手机不会自动存储DCIM... 这点让哥又爱又恨...)
					object = (Bitmap) (data.getExtras() == null ? null : data
							.getExtras().get("data"));
				}
			}

			if (mPhotePath == null && mTempFile != null)
				mPhotePath = mTempFile.getAbsolutePath();

			if (mPhotePath == null && object != null) {
				Bitmap bitmap = object == null ? null : (Bitmap) object;
				if (bitmap != null) {
					try {
						Utility.writeToFile(mTempFile, bitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (mPhotePath == null && mTempFile != null)
						mPhotePath = mTempFile.getAbsolutePath();
				}
			}
			if (!TextUtils.isEmpty(mPhotePath)) {
				doCropImage(mPhotePath);
			} else {
				Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
				return;
			}

		} else if (requestCode == PhotoType.TYPE_GALLERY && data != null) {
			mPhotePath = PhotoType.getPhotoAbsPath(this, data.getData());
			if (mPhotePath != null && !"".equals(mPhotePath)) {
				doCropImage(mPhotePath);

			} else
				Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
		} else if (requestCode == PhotoType.TYPE_PHOTOCROP && data != null) {
			mUploadBmp = data.getParcelableExtra("data");
			if (mUploadBmp != null) {
				File file = new File(CachePath.photocachePic,
						System.currentTimeMillis() + ".jpg");
				try {
					Utility.writeToFile(file, mUploadBmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!file.exists()) {
					Toast.makeText(mContext, "头像上传失败", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				ivHeader.setImageBitmap(mUploadBmp);
				UpLoadTouXiang.upload(SettingUtility.getDefaultUserId(), file);
			} else
				Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 剪切
	 */
	public void doCropImage(String path) {
		File f = new File(path);
		final Intent intent = getCropImageIntent(Uri.fromFile(f));
		startActivityForResult(intent, PhotoType.TYPE_PHOTOCROP);
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", true);
		return intent;
	}

}
