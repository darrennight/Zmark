package com.android.zmark.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.support.http.IHttpLoader;
import com.android.zmark.bean.util.Regular;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.ui.fragment.HomePageFragment;

/**
 * 登录
 * 
 * 
 */
public class LoginActivity<T> extends AbstractActivity implements
		OnClickListener {
	EditText edUserName, edPassWord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logion_activity);
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = intent.getBundleExtra("data");
		if (bundle != null) {
			edUserName.setText(bundle.getString("phone"));
			edPassWord.setText(bundle.getString("password"));
			ShowDialog("登陆中...");
			getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
					httpCallback);
			Utility.onHideInputSoftKeyboard(edUserName);
			Utility.onHideInputSoftKeyboard(edPassWord);
		}
	}

	public void init() {
		edPassWord = (EditText) findViewById(R.id.ed_password);
		edUserName = (EditText) findViewById(R.id.ed_phone);
		findViewById(R.id.bt_register).setOnClickListener(this);
		findViewById(R.id.bt_login).setOnClickListener(this);
		findViewById(R.id.bt_back).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			this.finish();
			break;
		case R.id.bt_register:
			IntentInterface.IntentAll(this, null, RegisterActivity.class, -1);
			break;
		case R.id.bt_login:
			if (TextUtils.isEmpty(edUserName.getText().toString())) {
				Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(edPassWord.getText().toString())) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			if (!(PhoneNumberUtils.isGlobalPhoneNumber(edUserName.getText()
					.toString()) && edUserName.getText().toString().length() == 11)) {
				Toast.makeText(LoginActivity.this, "你输入的手机号码位数不正确，请输入正确的电话号码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ShowDialog("登陆中...");
			getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
					httpCallback);
			Utility.onHideInputSoftKeyboard(edUserName);
			Utility.onHideInputSoftKeyboard(edPassWord);
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
				Map<String, String> map = new HashMap<String, String>();
				map.clear();
				// http://app10014.yunbosoft.com:9090/Interface/GetSlide.ashx?w=720&h=1280
				map.put("mobileId", Utility.getDeviceId(mContext));
				map.put("phone", edUserName.getText().toString());
				map.put("password", edPassWord.getText().toString());
				return new HttpLoader(mContext, ApiServerPath.DM_USER_LOGIN,
						map);

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
				Toast.makeText(mContext, "登录成功!", Toast.LENGTH_SHORT).show();
				JSONArray jsonArray = responseBean.getData().optJSONArray(
						"userInfo");
				SettingUtility.setUserInfo(jsonArray.optJSONObject(0)
						.toString());
				SettingUtility.setDefaultUserId(jsonArray.optJSONObject(0).optString("Id"));
				SettingUtility.setDefaultToken(jsonArray.optJSONObject(0).optString("Token"));
				SettingUtility.setDefaultPhone(jsonArray.optJSONObject(0).optString("PhoneNo"));
				finish();
				break;

			}
			getLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};
}
