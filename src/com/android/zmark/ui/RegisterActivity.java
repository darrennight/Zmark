package com.android.zmark.ui;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
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
 * 注册
 * 
 * 
 */
public class RegisterActivity<T> extends AbstractActivity implements
		OnClickListener {
	EditText edUserName, edPassWord, edIDcard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		init();
	}

	public void init() {
		edPassWord = (EditText) findViewById(R.id.ed_password);
		edUserName = (EditText) findViewById(R.id.ed_phone);
		edIDcard = (EditText) findViewById(R.id.ed_idcard);
		findViewById(R.id.bt_register).setOnClickListener(this);
		findViewById(R.id.bt_back).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			this.finish();
			break;
		case R.id.bt_register:
			if (TextUtils.isEmpty(edUserName.getText().toString())) {
				Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(edIDcard.getText().toString())) {
				Toast.makeText(this, "身份证号码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(edPassWord.getText().toString())) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			if (!(PhoneNumberUtils.isGlobalPhoneNumber(edUserName.getText()
					.toString()) && edUserName.getText().toString().length() == 11)) {
				Toast.makeText(RegisterActivity.this,
						"你输入的手机号码位数不正确，请输入正确的电话号码", Toast.LENGTH_SHORT).show();
				return;
			}

			if (!Regular.checkIdCard(edIDcard.getText().toString())) {
				Toast.makeText(RegisterActivity.this, "你输入的身份证号码位数不正确",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ShowDialog("注册中...");
			getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
					httpCallback);
			Utility.onHideInputSoftKeyboard(edUserName);
			Utility.onHideInputSoftKeyboard(edPassWord);
			Utility.onHideInputSoftKeyboard(edIDcard);
			break;
		default:
			break;
		}
	}

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				Map<String, String> map = new HashMap<String, String>();
				map.clear();
				// 1.mobileId=手机唯一标识码
				// 2.phone=手机号码
				// 3.password=密码
				// 4.idcard=身份证号码
				// http://app10014.yunbosoft.com:9090/Interface/GetSlide.ashx?w=720&h=1280
				map.put("mobileId", Utility.getDeviceId(mContext));
				map.put("phone", edUserName.getText().toString());
				map.put("password", edPassWord.getText().toString());
				map.put("idcard", edIDcard.getText().toString());
				return new HttpLoader(mContext, ApiServerPath.DM_USER_REGISTER,
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
							TextUtils.isEmpty(responseBean.getMessage()) ? "注册失败!"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("phone", edUserName.getText().toString());
				bundle.putString("password", edPassWord.getText().toString());
				IntentInterface.IntentAll(RegisterActivity.this, bundle,
						LoginActivity.class, -1);
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
