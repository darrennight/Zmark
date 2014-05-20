package com.android.zmark.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.R;

/**
 * Fragment的基类
 * 2014/4/1
 * 01:23
 * @author yangwenfang
 *
 */
public abstract class AbstractFragment  extends Fragment{
	protected final static int LOADER_INITDATA = 0x110;
	protected final static int LOADER_LIST_DATA = 0x111;
	public static final CommonLog log = LogFactory.createLog();
	public ImageView mImageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.common_layout, null);
		mImageView = (ImageView) view.findViewById(R.id.imageView);
		
		return view;
	}
	// 刷新或者第一次加载
	protected  void loadNewData(Object object)
	{
		
	}

}
