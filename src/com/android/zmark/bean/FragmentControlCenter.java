package com.android.zmark.bean;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.android.zmark.ui.fragment.CollectionFragment;
import com.android.zmark.ui.fragment.HistoryListFragment;
import com.android.zmark.ui.fragment.HomePageFragment;
import com.android.zmark.ui.fragment.SetingFragment;

public class FragmentControlCenter {

	private static final CommonLog log = LogFactory.createLog();

	private static FragmentControlCenter instance;
	private static Context mContext;

	private Map<String, FragmentModel> mFragmentModelMaps = new HashMap<String, FragmentModel>();

	private FragmentControlCenter(Context context) {
		mContext = context;
	}

	public static synchronized FragmentControlCenter getInstance(Context context) {
		if (instance == null) {
			instance = new FragmentControlCenter(context);
		}
		return instance;
	}

	public FragmentModel getTouTiaoFragmentModel() {
		FragmentModel fragmentModel = mFragmentModelMaps
				.get(FragmentBuilder.TOU_TIAO_FRAGMENT);
		if (fragmentModel == null) {
			fragmentModel = FragmentBuilder.getTouTiaoFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.TOU_TIAO_FRAGMENT,
					fragmentModel);
		}
		return fragmentModel;
	}

	public FragmentModel getYuLeFragmentModel() {
		FragmentModel fragmentModel = mFragmentModelMaps
				.get(FragmentBuilder.YU_LE_FRAGMENT);
		if (fragmentModel == null) {
			fragmentModel = FragmentBuilder.getYuLeFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.YU_LE_FRAGMENT,
					fragmentModel);
		}
		return fragmentModel;
	}

	public FragmentModel getTechFragmentModel() {
		FragmentModel fragmentModel = mFragmentModelMaps
				.get(FragmentBuilder.TECH_FRAGMENT);
		if (fragmentModel == null) {
			fragmentModel = FragmentBuilder.getTechFragmentModel();
			mFragmentModelMaps
					.put(FragmentBuilder.TECH_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}

	public FragmentModel getBlogFragmentModel() {
		FragmentModel fragmentModel = mFragmentModelMaps
				.get(FragmentBuilder.BLOG_FRAGMENT);
		if (fragmentModel == null) {
			fragmentModel = FragmentBuilder.getBlogFragmentModel();
			mFragmentModelMaps
					.put(FragmentBuilder.BLOG_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}

	public FragmentModel getFragmentModel(String name) {
		return mFragmentModelMaps.get(name);
	}

	public void addFragmentModel(String name, FragmentModel fragment) {
		mFragmentModelMaps.put(name, fragment);
	}

	public static class FragmentBuilder {
		public static final String TOU_TIAO_FRAGMENT = "TOU_TIAO_FRAGMENT";
		public static final String YU_LE_FRAGMENT = "YU_LE_FRAGMENT";
		public static final String TECH_FRAGMENT = "TECH_FRAGMENT";
		public static final String BLOG_FRAGMENT = "BLOG_FRAGMENT";

		public static FragmentModel getTouTiaoFragmentModel() {
			HistoryListFragment fragment = new HistoryListFragment(mContext);
			FragmentModel fragmentModel = new FragmentModel("浏览历史", fragment);
			return fragmentModel;
		}

		public static FragmentModel getYuLeFragmentModel() {
			CollectionFragment fragment = new CollectionFragment(mContext);
			FragmentModel fragmentModel = new FragmentModel("收藏", fragment);
			return fragmentModel;
		}

		public static FragmentModel getTechFragmentModel() {
			SetingFragment fragment = new SetingFragment(mContext);
			FragmentModel fragmentModel = new FragmentModel("设置", fragment);
			return fragmentModel;
		}

		public static FragmentModel getBlogFragmentModel() {
			FragmentModel fragmentModel = new FragmentModel("主页",
					new HomePageFragment());
			return fragmentModel;
		}
	}
}
