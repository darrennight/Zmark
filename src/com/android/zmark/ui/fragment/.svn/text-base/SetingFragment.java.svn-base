package com.android.zmark.ui.fragment;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.DownloadService;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.util.CameraHelpyer;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.ui.FeedBackActivity;
import com.android.zmark.ui.ILocationActivity;
import com.android.zmark.ui.LoginActivity;
import com.android.zmark.ui.MainActivity;
import com.android.zmark.ui.ModifyPasswordActivity;
import com.android.zmark.ui.UserCenterActivity;

/**
 * 设置
 * 
 * @author yangwenfang
 * 
 */
public class SetingFragment extends AbstractFragment implements OnClickListener {

	private static final CommonLog log = LogFactory.createLog();

	RelativeLayout set_rels_1, set_rels_2, set_rels_3, set_rels_4, set_rels_5,
			set_rels_6, set_rels_7, set_rels_8;
	TextView t_vison;
	String version;
	private DownloadService.DownloadBinder binder;
	private TextView text;
	private ProgressBar progress;
	private Context mContext;
	View view;
	ZmarkApplication app;

	public SetingFragment(Context context) {
		this.mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.e("TechFragment onCreate");
		if (app != null) {

		} else {
			app = ZmarkApplication.getInstance();
		}
		super.onCreate(savedInstanceState);
	}

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (DownloadService.DownloadBinder) service;
			Log.i("====bing sucess", "ok");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	protected final Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				String mgs = "立即更新吗？";

				new AlertDialog.Builder(mContext)
						.setTitle("发现新的版本可更新")
						.setMessage(mgs)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										binder.start();
										// listenProgress();
									}
								}).setNegativeButton("取消", null).create()
						.show();
				break;
			case 1:
				Toast.makeText(mContext, "您当前已经是最新版本！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(mContext, "最新版本正在下载！", Toast.LENGTH_SHORT)
						.show();
				break;

			}
			;
		}
	};

	public String getVersion() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			version = "" + "1.0.1";
			return version;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getVersion();
		Intent intent = new Intent(mContext, DownloadService.class);
		mContext.startService(intent); // 如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
		mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);

		view = LayoutInflater.from(mContext).inflate(
				R.layout.setting_fragment_layout, null);
		t_vison = (TextView) view.findViewById(R.id.t_vison);
		t_vison.setText("版本  Android v" + version);
		// set_rels_1=(RelativeLayout) view.findViewById(R.id.set_rels_1);
		set_rels_2 = (RelativeLayout) view.findViewById(R.id.set_rels_2);
		set_rels_3 = (RelativeLayout) view.findViewById(R.id.set_rels_3);
		set_rels_4 = (RelativeLayout) view.findViewById(R.id.set_rels_4);
		set_rels_5 = (RelativeLayout) view.findViewById(R.id.set_rels_5);
		set_rels_6 = (RelativeLayout) view.findViewById(R.id.set_rels_6);
		set_rels_7 = (RelativeLayout) view.findViewById(R.id.set_rels_7);
		set_rels_8 = (RelativeLayout) view.findViewById(R.id.set_rels_8);

		view.findViewById(R.id.set_back).setOnClickListener(this);
		// set_rels_1.setOnClickListener(this);
		set_rels_2.setOnClickListener(this);
		set_rels_3.setOnClickListener(this);
		set_rels_4.setOnClickListener(this);
		set_rels_5.setOnClickListener(this);
		set_rels_6.setOnClickListener(this);
		set_rels_7.setOnClickListener(this);
		set_rels_8.setOnClickListener(this);
		view.findViewById(R.id.bt_search).setOnClickListener(this);
		view.findViewById(R.id.bt_mycenter).setOnClickListener(this);
		view.findViewById(R.id.bt_more).setOnClickListener(this);
		view.findViewById(R.id.bt_activity).setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("TechFragment onActivityCreated");

		setupViews();
	}

	private void setupViews() {

	}

	@Override
	public void onDestroy() {
		log.e("TechFragment onDestroy");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.bt_activity:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentActivity();
			break;
		case R.id.bt_search:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSearch();
			break;
		case R.id.bt_mycenter:
			Intent mIntent = null;
			if (!ZmarkApplication.getInstance().userIsOline()) {
				mIntent = new Intent(getActivity(), LoginActivity.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(getActivity(), UserCenterActivity.class);
				startActivity(mIntent);
			}
			mIntent = null;
			break;
		case R.id.bt_more:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSetting();
			break;
		case R.id.set_rels_2:
			if (ZmarkApplication.getInstance().userIsOline()) {
				intent = new Intent(mContext, UserCenterActivity.class);
				mContext.startActivity(intent);
			} else {
				Toast.makeText(mContext, "你还未登陆，不能查看个人信息", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.set_rels_3:
			if (ZmarkApplication.getInstance().userIsOline()) {
				intent = new Intent(mContext, ModifyPasswordActivity.class);
				mContext.startActivity(intent);
			} else {
				Toast.makeText(mContext, "你还未登陆，不能修改", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.set_rels_4:
			intent = new Intent(mContext, ILocationActivity.class);
			mContext.startActivity(intent);

			break;
		case R.id.set_rels_5:
			final ProgressDialog pd = new ProgressDialog(mContext);
			pd.setMessage("正在检测是否有新的版本...");
			pd.setCancelable(false);
			pd.show();

			new Thread() {
				public void run() {

					if (!binder.isCancelled()) {
						binder.start();
						hand.sendEmptyMessage(3);
						return;
					}

					if (binder.isNewVersion() == 0) {
						hand.sendEmptyMessage(0);
					} else {
						hand.sendEmptyMessage(1);
					}

					pd.dismiss();
				}
			}.start();
			break;
		case R.id.set_back:
			app.sm.toggle();
			break;
		case R.id.set_rels_6:
			Uri uri = Uri.parse("smsto:");
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body",
					"我正在使用芝麻开门手机APP客户端，这款软件很适合用户查找美食娱乐与酒店。特推荐给你。");
			mContext.startActivity(it);
			break;
		case R.id.set_rels_7:
			intent = new Intent(mContext, FeedBackActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.set_rels_8:
			if (ZmarkApplication.getInstance().userIsOline()) {
				new AlertDialog.Builder(mContext)
						.setTitle("清除缓存")
						.setMessage("是否确认清除用户信息缓存")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										SettingUtility.setUserInfo("");
										File file = CameraHelpyer.avatarImageFile;
										if (file.exists()) {
											file.delete();
										}
										Toast.makeText(mContext, "缓存数据已清空",
												Toast.LENGTH_SHORT).show();
									}
								}).setNegativeButton("取消", null).create()
						.show();

			} else {
				Toast.makeText(mContext, "你还未登陆", Toast.LENGTH_SHORT)
						.show();
			}

			break;

		default:
			break;
		}
	}
}
