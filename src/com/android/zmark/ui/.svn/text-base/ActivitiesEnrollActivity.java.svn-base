package com.android.zmark.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.bean.PhotoType;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.support.CachePath;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.Utility;

/**
 * 活动
 * 
 * 报名
 * 
 * @author yangwenfang
 * 
 * @param <T>
 */
public class ActivitiesEnrollActivity<T> extends AbstractActivity implements
		OnClickListener, DialogInterface.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_enroll_activity);
		init();
	}

	private String ID = null;
	private int index = 0;

	private void init() {
		onBackPressed(R.id.iv_button_menu_back);
		Bundle bundle = getIntent().getBundleExtra("data");
		ID = bundle.getString("ID");
		findViewById(R.id.bt_upload_pics).setOnClickListener(this);
		findViewById(R.id.bt_enroll_submit).setOnClickListener(this);
		findViewById(R.id.ed_bayby_sexy).setOnClickListener(this);
		findViewById(R.id.ed_baby_age).setOnClickListener(this);

	}

	protected final int LOADER_UPLOADPIC = 0x1;
	private String familyId = null;
	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("name",
						((EditText) findViewById(R.id.ed_family_nickname))
								.getText().toString());
				paramMap.put("uid", SettingUtility.getDefaultUserId());
				paramMap.put("mobilephone", SettingUtility.getDefaultPhone());
				paramMap.put("token", SettingUtility.getDefaultToken());
				paramMap.put("fathername",
						((EditText) findViewById(R.id.ed_father_name))
								.getText().toString());
				paramMap.put("mothername",
						((EditText) findViewById(R.id.ed_mather_name))
								.getText().toString());
				paramMap.put("babyname",
						((EditText) findViewById(R.id.ed_baby_name)).getText()
								.toString());
				paramMap.put("babysex",
						((TextView) findViewById(R.id.ed_bayby_sexy)).getText()
								.toString());
				paramMap.put("babyage",
						((TextView) findViewById(R.id.ed_baby_age)).getText()
								.toString());
				paramMap.put("description",
						((EditText) findViewById(R.id.ed_bayby_slogan))
								.getText().toString());
				paramMap.put("aid", ID);
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_JIONIN, paramMap);
			case LOADER_UPLOADPIC:
				if (TextUtils.isEmpty(familyId))
					break;
				Map<String, String> mMap = new HashMap<String, String>();
				mMap.put("fid", familyId);
				mMap.put("desc", "图片描述");
				// 1.fid=家庭ID
				// 2.desc=描述
				mMap.put("mobilephone", SettingUtility.getDefaultPhone());
				mMap.put("token", SettingUtility.getDefaultToken());
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_UPLOADALBUM, mMap, "file",
						mPathsList.get(index));
			default:
				break;
			}
			return null;

		}

		@Override
		public void onLoadFinished(Loader<AsyncTaskLoaderResult<T>> loader,
				AsyncTaskLoaderResult<T> result) {
			T data = result != null ? result.data : null;
			HttpResponseBean responseBean = (HttpResponseBean) data;
			switch (loader.getId()) {
			case LOADER_INITDATA:
				DismissDialog();
				if (null == responseBean) {
					Toast.makeText(mContext, "报名失败!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {
					Toast.makeText(
							mContext,
							TextUtils.isEmpty(responseBean.getMessage()) ? "报名失败!"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject familyModel = responseBean.getData().optJSONObject(
						"familyModel");

				if (familyModel == null) {
					Toast.makeText(mContext, "报名失败!", Toast.LENGTH_SHORT)
							.show();

					return;
				}

				familyId = familyModel.optString("ID");

				// {
				// "status": 255,
				// "message": "成功",
				// "data": {
				// "familyModel": {
				// "ID": ID,
				// "UID": 用户ID,
				// "Name": "名称",
				// "URL": 图片,
				// "Description": "口号",
				// "CreateTime": "时间",
				// "FatherName": "爸爸名称",
				// "MotherName": "妈妈名称",
				// "BabyName": "宝宝名称",
				// "BabySex": "性别",
				// "BabyAge": 年龄
				// }
				// }
				// }
				mHandler.sendEmptyMessage(index);
				break;
			case LOADER_UPLOADPIC:

				if (responseBean != null
						&& responseBean.getStates() != HttpResponseBean.HTTP_OK) {
					Toast.makeText(mContext, String.format("图片%d上传失败", index),
							Toast.LENGTH_SHORT).show();
				}
				index++;
				mHandler.sendEmptyMessage(index);
				break;
			default:
				break;
			}
			getSupportLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mPathsList != null && index < mPathsList.size()) {
				ShowDialog(String.format("图片%d上传中...", (index + 1)));
				getSupportLoaderManager().restartLoader(LOADER_UPLOADPIC, null,
						httpCallback);
			} else {
				DismissDialog();
				Toast.makeText(mContext, "报名成功!", Toast.LENGTH_SHORT).show();
				finish();
			}

		}

	};

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		if (vId == R.id.bt_upload_pics) {
			if (mPathsList != null && mPathsList.size() >= 5) {
				Toast.makeText(mContext, "照片最多只能选择5张", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			new AlertDialog.Builder(this).setTitle("选择图片方式")
					.setItems(new String[] { "照相", "相册" }, this).show();

		} else if (vId == R.id.bt_enroll_submit) {
			if (mPathsList == null || mPathsList.size() == 0) {
				Toast.makeText(mContext, "请选择照片", Toast.LENGTH_SHORT).show();
				return;
			}
			ShowDialog("报名提交中...");
			getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
					httpCallback);

		} else if (vId == R.id.ed_bayby_sexy) {
			showDialog(DIALOG_SEX);
		} else if (vId == R.id.ed_baby_age) {
			showDialog(DIALOG_AGE);
		}
	}

	private final int DIALOG_SEX = 0x1;
	private final int DIALOG_AGE = 0x2;
	CharSequence[] sex = { "男", "女" };
	CharSequence[] age = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder alDialog = new AlertDialog.Builder(mContext);
		switch (id) {
		case DIALOG_SEX:
			alDialog.setTitle("性别");
			alDialog.setItems(sex, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((TextView) findViewById(R.id.ed_bayby_sexy))
							.setText(sex[which]);
				}
			});
			break;
		case DIALOG_AGE:
			alDialog.setTitle("年龄");
			alDialog.setItems(age, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((TextView) findViewById(R.id.ed_baby_age))
							.setText(age[which]);
				}
			});
			break;
		default:
			break;
		}
		return alDialog.create();
	}

	private File mTempFile;

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == 0) // 照相
		{
			if (!Utility.isSDExist()) {
				Toast.makeText(mContext, "SD卡不可用，请检查SD卡", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mTempFile = new File(CachePath.photocachePic,
					System.currentTimeMillis() + ".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
			startActivityForResult(intent, PhotoType.TYPE_PHOTOCAMRA);

		} else if (which == 1) // 相册
		{
			Intent i = new Intent(Intent.ACTION_PICK, null);
			i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(i, PhotoType.TYPE_GALLERY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == PhotoType.TYPE_PHOTOCAMRA) {
			String mPhotePath = null;
			Object object = null;
			if (data != null) {
				// HTC
				if (data.getData() != null) {
					// 根据返回的URI获取对应的SQLite信息
					Cursor cursor = this.getContentResolver().query(
							data.getData(), null, null, null, null);
					if (cursor.moveToFirst()) {
						mPhotePath = cursor.getString(cursor
								.getColumnIndex("_data"));// 获取绝对路径
					}
					cursor.close();
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
				addNewPhoto(mPhotePath);
			}

		} else if (requestCode == PhotoType.TYPE_GALLERY) {

			String mPhotePath = PhotoType.getPhotoAbsPath(this, data.getData());
			if (mPhotePath != null && !"".equals(mPhotePath)) {
				addNewPhoto(mPhotePath);
			} else
				Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();

		}
	}

	private ArrayList<String> mPathsList;
	int mPhotoWidth = -1;

	private void addNewPhoto(final String path) {
		if (TextUtils.isEmpty(path))
			return;
		if (mPathsList == null)
			mPathsList = new ArrayList<String>();
		if (mPhotoWidth == -1)
			mPhotoWidth = (Utility.DisplayPixels(mContext)[0] - 10 * 5) / 5;
		ImageView mAddPhoto = new ImageView(mContext);
		mAddPhoto.setScaleType(ScaleType.CENTER_CROP);
		LinearLayout.LayoutParams params = (LayoutParams) new LayoutParams(
				mPhotoWidth - 10, mPhotoWidth - 10);
		params.leftMargin = 5;
		params.topMargin = 5;
		mPathsList.add(path);
		mAddPhoto.setImageBitmap(Utility.decodeBitmapFromSDCard(path, 60, 60));
		((LinearLayout) findViewById(R.id.ly_upload_pics)).addView(mAddPhoto,
				params);
	}

}
