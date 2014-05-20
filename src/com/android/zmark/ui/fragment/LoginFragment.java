package com.android.zmark.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.R;
/**
 * 登陆 UI
 * 
 * 2014/4/1 21:45 
 * by: 杨雯方
 */
public class LoginFragment extends Fragment{
	
	private static final CommonLog log = LogFactory.createLog();
	
	private View mView;
	
	public LoginFragment(){
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		log.e("SettingFragment onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.e("SettingFragment onCreateView");
		View view = inflater.inflate(R.layout.setting_fragment_layout, null);
		
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("SettingFragment onActivityCreated");
		
		setupViews();
	}
	
	private void setupViews(){
	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		log.e("SettingFragment onDestroy");
	}
}
