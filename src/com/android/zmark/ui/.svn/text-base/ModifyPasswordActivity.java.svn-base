package com.android.zmark.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;

/**
 * 修改密码
 * 
 * @author yangwenfang
 * 
 */
public class ModifyPasswordActivity extends Activity implements OnClickListener {

	EditText regsiter_tel, regsiter_pas, regsiter_yazhenma;
	Button register_login;
	ImageView back;
	Context context;
	String newpas;// 新密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_password_activity_layout);
		context = this;
		this.init();
	}

	public void init() {
		regsiter_tel = (EditText) findViewById(R.id.regsiter_tel);
		regsiter_pas = (EditText) findViewById(R.id.regsiter_pas);
		regsiter_yazhenma = (EditText) findViewById(R.id.regsiter_yazhenma);

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		register_login = (Button) findViewById(R.id.register_login);
		register_login.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.register_login:
			String oldpas = regsiter_tel.getText().toString();
			newpas = regsiter_pas.getText().toString();
			String newpasagin = regsiter_yazhenma.getText().toString();
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(ModifyPasswordActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			if (!"".equals(oldpas) && !"".equals(newpas)
					&& !"".equals(newpasagin)) {
				if (newpas.equals(newpasagin)) {
					// 手机号,密码,手机唯一码,Id,唯一Token
					if (ZmarkApplication.getInstance().userIsOline()) {
						UpdatePasswordTask task = new UpdatePasswordTask();
						task.execute(SettingUtility.getDefaultUserId(),
								Utility.getDeviceId(context),
								SettingUtility.getDefaultPhone(), oldpas,
								newpas, SettingUtility.getDefaultToken());
					}
				} else {
					Toast.makeText(context, "两次输入的新密码不相同", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(context, "你输入的信息不完整", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 异步加载首页数据
	 * 
	 * @author lenovo
	 * 
	 */

	class UpdatePasswordTask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromUser(ModifyPasswordActivity.this,
					DateInfo.UpdatePassword, arg0[0], arg0[1], arg0[2],
					arg0[3], arg0[4], arg0[5]);

		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub

			if (result != null) {
				List<String> list = (List<String>) result;
				String strs = "" + list.get(0);
				if ("1".equals(strs)) {
					Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT)
							.show();
					ModifyPasswordActivity.this.finish();
				} else if ("-1".equals(strs)) {
					Toast.makeText(context, "密码修改失败", Toast.LENGTH_SHORT)
							.show();
				}

			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

}
