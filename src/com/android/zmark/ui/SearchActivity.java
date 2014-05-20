package com.android.zmark.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.zmark.R;

/**
 * 搜索页
 * @author yangwenfang
 *
 */
public class SearchActivity extends AbstractActivity implements OnClickListener {

	EditText ed_search;// 搜索

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_layout);
		findViewById(R.id.serach_but).setOnClickListener(this);
		ed_search = (EditText) findViewById(R.id.serach_edit);
		onBackPressed(R.id.iv_button_menu_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.serach_but:
			if (!TextUtils.isEmpty(ed_search.getText().toString())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
				Intent intentSearch = new Intent(this, SearchListActivity.class);
				intentSearch.putExtra("info", ed_search.getText().toString());
				startActivity(intentSearch);
				ed_search.setText("");
				finish();
			} else {
				Toast.makeText(this, "请输入查询内容", Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}
}
