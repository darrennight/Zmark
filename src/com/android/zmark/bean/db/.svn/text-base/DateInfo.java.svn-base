package com.android.zmark.bean.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.android.zmark.entities.CityList;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.entities.MyMessage;
import com.android.zmark.entities.UseInfo;
import com.android.zmark.entities.UserMessages;

public class DateInfo {

	private static final String TAG = "DateInfo";

	public static final String iRid = "00000000-0000-0000-0000-000000000000";// 设置总店ID

	public static final String GetHomeData = "GetHomeData.ashx";// 首页信息
	public static final String IP = "http://app10014.yunbosoft.com:9090";// "http://001.app.v-rb.com:9090/";
	public static final String ROOT = IP + "/Interface/";

	public static final String GetHomeShop = "GetHomeShop_Discount.ashx";// 获得热门列表

	public static final String CheckLogin = "CheckUserLogin.ashx";
	public static final String GetMessage = "GetMyMessageList.ashx";// 消息信息
	public static final String YiJianFanKui = "UserFeedBack.ashx";// 意见反馈
	public static final String UpdatePassword = "UpdatePassword.ashx";// 意见反馈
	public static final String UserLogin = "UserLogin.ashx";//
	public static final String WelcomePages = "WelcomePages.ashx";//
	public final static String CHECK_UPDATE = "GetVersions.ashx";

	public static final String GetUserInfo = "GetUserInfo.ashx";
	public static final String AddUser = "EditUserInfo.ashx";
	public static final String GetCityInfo = "GetCityInfo.ashx";
	public static final String UserCollecting = "UserCollecting.ashx";// 添加收藏
	public static final String GetMyCollections = "GetMyCollection_V2.ashx";// 获取收藏
	public static final String GetGourmetShop = "GetGourmetShop_V2.ashx";// 获取美食
	public static final String GetEntertainmentShop = "GetEntertainmentShop_V2.ashx";// 娱乐

	public static final String EditUserInfo = "EditUserInfo.ashx";// 修改用户

	public static final String GetHotelShop = "GetHotelShop_V2.ashx";// 酒店

	/**
	 * 上传图片 传入参数 1.id 用户信息id 2.img 上传图片获取的名称
	 */
	public final static String UPLOAD_IMG = "EditUserImg.ashx";

	public static final String DeleteCollections = "DeleteCollection.ashx";// 删除收藏
	public static final String GetHository = "GetHositoryShop_V2.ashx";
	public static final String GetHomeShopWithSlide = "GetHomeShop_Discount.ashx";
	public static final String GetAdvertisingSlots = "GetAdvertisingSlots.ashx";

	public static List<?> getDataFromSer(Context context, String type,
			String page, String cid, String state, String bigkind) {
		try {
			NameValuePair mvp = null;
			StringBuffer sb = new StringBuffer();
			String returnStr = null;
			NameValuePair mvp1 = null;
			NameValuePair mvp2 = null;
			NameValuePair mvp3 = null;
			NameValuePair mvp4 = null;
			NameValuePair mvp5 = null;

			if (type.equalsIgnoreCase(GetHomeData)) {
				returnStr = MyTools.Post(ROOT + type);
			} else if (type.equalsIgnoreCase(GetMessage)) {
				if (state != null) {
					mvp = new BasicNameValuePair("i", page);
					mvp1 = new BasicNameValuePair("mid", cid);
					if (state != null && bigkind != null) {
						mvp2 = new BasicNameValuePair("n", state);
						mvp3 = new BasicNameValuePair("token", bigkind);
						returnStr = MyTools.Post(ROOT + type, mvp, mvp1, mvp2,
								mvp3);
					} else {
						returnStr = MyTools.Post(ROOT + type, mvp);
					}

				} else {
					mvp = new BasicNameValuePair("i", page);
					returnStr = MyTools.Post(ROOT + type, mvp);
				}
			} else if (type.equalsIgnoreCase(CheckLogin)) {
				mvp = new BasicNameValuePair("n", page);
				mvp1 = new BasicNameValuePair("mid", cid);
				returnStr = MyTools.Post(ROOT + type, mvp, mvp1);
			} else if (type.equalsIgnoreCase(GetUserInfo)) {
				mvp = new BasicNameValuePair("PhoneNo", cid);
				mvp1 = new BasicNameValuePair("Password", state);
				mvp2 = new BasicNameValuePair("Uuid", page);
				Log.i("===sss", ROOT + type + "?PhoneNo=" + page + "&Password="
						+ cid + "&Uuid=" + state);
				returnStr = MyTools.Post(ROOT + type, mvp, mvp1, mvp2);
			} else if (type.equalsIgnoreCase(AddUser)) {
				mvp1 = new BasicNameValuePair("id", page);
				mvp = new BasicNameValuePair("content", cid);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp);

			} else if (type.equalsIgnoreCase(GetCityInfo)) {
				returnStr = MyTools.Post(ROOT + type);
			} else if (type.equalsIgnoreCase(GetMyCollections)) {

				mvp1 = new BasicNameValuePair("id", page);
				mvp = new BasicNameValuePair("i", cid);
				// mvp1 = new BasicNameValuePair("city", page);
				// mvp = new BasicNameValuePair("i", cid);
				mvp2 = new BasicNameValuePair("c", state);

				returnStr = MyTools.Post(ROOT + type, mvp1, mvp, mvp2);
			} else if (type.equalsIgnoreCase(GetHomeShop)) {
				mvp1 = new BasicNameValuePair("city", page);
				mvp = new BasicNameValuePair("i", cid);
				mvp2 = new BasicNameValuePair("c", state);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp, mvp2);
			} else if (type.equalsIgnoreCase(UserCollecting)) {
				mvp1 = new BasicNameValuePair("id", page);
				mvp = new BasicNameValuePair("pid", cid);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp);
			} else if (type.equalsIgnoreCase(DeleteCollections)) {
				mvp1 = new BasicNameValuePair("id", page);
				mvp = new BasicNameValuePair("pid", cid);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp);
			} else if (type.equalsIgnoreCase(GetHository)) {
				mvp1 = new BasicNameValuePair("c", page);
				mvp = new BasicNameValuePair("ids", cid);
				mvp2 = new BasicNameValuePair("i", state);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp, mvp2);
			} else if (type.equalsIgnoreCase(GetHomeShopWithSlide)) {
				mvp1 = new BasicNameValuePair("content", page);
				mvp = new BasicNameValuePair("c", cid);
				mvp2 = new BasicNameValuePair("i", state);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp, mvp2);

			} else if (type.equalsIgnoreCase(GetAdvertisingSlots)) {
				mvp1 = new BasicNameValuePair("w", page);
				mvp = new BasicNameValuePair("h", cid);
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp);
			}

			Log.i("====IP==", ROOT + type);
			if (returnStr == null)
				returnStr = SP.getSp(context).getString(type, null);
			if (TextUtils.isEmpty(returnStr)) {
				Log.e(TAG, "getDataFromSer，获取到的数据为空");
				return null;
			}
			Log.i("info==数据加载成功==", returnStr);
			Editor editor = SP.getSpEdit(context);
			editor.putString(type, returnStr);
			editor.commit();

			List<?> list = null;

			if (type.equalsIgnoreCase(GetMessage)) {
				list = JSONUtil.getInstance()
						.toList(returnStr, MyMessage.class);
			} else if (type.equalsIgnoreCase(CheckLogin)) {
				List<String> li = new ArrayList<String>();
				li.add(returnStr);
				return li;
			} else if (type.equalsIgnoreCase(GetUserInfo)) {
				list = JSONUtil.getInstance().toList(returnStr,
						UserMessages.class);
			} else if (type.equalsIgnoreCase(AddUser)) {
				list = JSONUtil.getInstance().toList(returnStr, UseInfo.class);
			} else if (type.equalsIgnoreCase(GetCityInfo)) {
				list = JSONUtil.getInstance().toList(returnStr, CityList.class);
			} else if (type.equalsIgnoreCase(GetMyCollections)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(GetHomeShop)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(DeleteCollections)) {
				List<String> ll = new ArrayList<String>();
				ll.add(returnStr);
				return ll;
			} else if (type.equalsIgnoreCase(UserCollecting)) {
				List<String> ll = new ArrayList<String>();
				ll.add(returnStr);
				return ll;

			} else if (type.equalsIgnoreCase(GetHository)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(GetHomeShopWithSlide)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(GetAdvertisingSlots)) {
				List<String> ll = new ArrayList<String>();
				ll.add(returnStr);
				return ll;
			}
			return list;
		} catch (Exception e) {
			Log.i(TAG, "调用接口:" + "getDataFromSer出错");
			e.printStackTrace();
		}

		return null;

	}

	// 意见反馈==============================================
	public static String getDataFromYiJianFankui(Context context, String type,
			String page, String cid, String state, String bigkind,
			String oldpw, String uuid) {
		try {
			String returnStr = null;
			NameValuePair mvp = null;

			NameValuePair StateNVP = null;
			NameValuePair mvp1 = null;
			NameValuePair mvp2 = null;
			NameValuePair mvp3 = null;
			if (type.equalsIgnoreCase(YiJianFanKui)) {
				mvp = new BasicNameValuePair("t", page);
				StateNVP = new BasicNameValuePair("c", cid);
				Log.i("====bigkind", "++" + bigkind);
				returnStr = MyTools.Post(ROOT + type, mvp, StateNVP);
			}

			if (returnStr == null)
				returnStr = SP.getSp(context).getString(type, null);
			if (TextUtils.isEmpty(returnStr)) {
				Log.e(TAG, "getDataFromSer，获取到的数据为空");
				return null;
			}
			Log.i("返回数据：", returnStr);
			Editor editor = SP.getSpEdit(context);
			editor.putString(type, returnStr);
			editor.commit();

			return returnStr;
		} catch (Exception e) {
			Log.i(TAG, "调用接口:" + "getDataFromSer出错");
			e.printStackTrace();
		}

		return null;

	}

	// 用户登录==============================================
	public static List<?> getDataFromUser(Context context, String type,
			String page, String cid, String state, String bigkind,
			String oldpw, String uuid) {
		try {
			String returnStr = null;
			NameValuePair mvp = null;

			NameValuePair StateNVP = null;
			NameValuePair mvp1 = null;
			NameValuePair mvp2 = null;
			NameValuePair mvp3 = null;
			NameValuePair mvp4 = null;
			if (type.equalsIgnoreCase(UpdatePassword)) {
				mvp = new BasicNameValuePair("id", page);
				StateNVP = new BasicNameValuePair("mid", cid);
				mvp1 = new BasicNameValuePair("n", state);
				mvp2 = new BasicNameValuePair("oldp", bigkind);
				mvp3 = new BasicNameValuePair("newp", oldpw);
				mvp4 = new BasicNameValuePair("token", uuid);
				returnStr = MyTools.Post(ROOT + type, mvp, StateNVP, mvp1,
						mvp2, mvp3);
			} else if (type.equalsIgnoreCase(UserLogin)) {
				mvp = new BasicNameValuePair("mid", page);
				StateNVP = new BasicNameValuePair("n", cid);
				mvp1 = new BasicNameValuePair("p", state);
				mvp2 = new BasicNameValuePair("r", bigkind);
				returnStr = MyTools
						.Post(ROOT + type, mvp, StateNVP, mvp1, mvp2);
			}

			if (returnStr == null)
				returnStr = SP.getSp(context).getString(type, null);
			if (TextUtils.isEmpty(returnStr)) {
				Log.e(TAG, "getDataFromSer，获取到的数据为空");
				return null;
			}

			Log.i("返回数据：", returnStr);
			Editor editor = SP.getSpEdit(context);
			editor.putString(type, returnStr);
			editor.commit();

			List<?> list = null;

			if (type.equalsIgnoreCase(UpdatePassword)) {
				List<String> lists = new ArrayList<String>();
				lists.add(returnStr);
				return lists;
			} else if (type.equalsIgnoreCase(UserLogin)) {
				List<String> li = new ArrayList<String>();
				li.add(returnStr);
				return li;
			}
			return list;
		} catch (Exception e) {
			Log.i(TAG, "调用接口:" + "getDataFromSer出错");
			e.printStackTrace();
		}

		return null;

	}

	// 获取门店==============================================
	public static List<?> getDataFromShop(Context context, String type,
			String page, String cid, String state, String bigkind,
			String oldpw, String uuid) {
		try {
			String returnStr = null;
			NameValuePair mvp1 = null;
			NameValuePair mvp2 = null;
			NameValuePair mvp3 = null;
			NameValuePair mvp4 = null;
			NameValuePair mvp5 = null;
			NameValuePair mvp6 = null;
			if (type.equalsIgnoreCase(GetGourmetShop)) {
				mvp1 = new BasicNameValuePair("c", page);
				mvp2 = new BasicNameValuePair("i", cid);
				mvp3 = new BasicNameValuePair("content", state);
				mvp4 = new BasicNameValuePair("price", bigkind);
				mvp5 = new BasicNameValuePair("distance", oldpw);
				mvp6 = new BasicNameValuePair("city", "");
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp2, mvp3, mvp4,
						mvp5, mvp6);
				Log.e("========", "" + ROOT + type + "c=" + page + "&content"
						+ state + "&price=" + bigkind + "&distance=" + oldpw
						+ "&city=");

			} else if (type.equalsIgnoreCase(GetEntertainmentShop)) {
				mvp1 = new BasicNameValuePair("c", page);
				mvp2 = new BasicNameValuePair("i", cid);
				mvp3 = new BasicNameValuePair("content", state);
				mvp4 = new BasicNameValuePair("price", bigkind);
				mvp5 = new BasicNameValuePair("distance", oldpw);
				mvp6 = new BasicNameValuePair("city", "");
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp2, mvp3, mvp4,
						mvp5, mvp6);
			} else if (type.equalsIgnoreCase(GetHotelShop)) {
				mvp1 = new BasicNameValuePair("c", page);
				mvp2 = new BasicNameValuePair("i", cid);
				mvp3 = new BasicNameValuePair("content", state);
				mvp4 = new BasicNameValuePair("price", bigkind);
				mvp5 = new BasicNameValuePair("distance", oldpw);
				mvp6 = new BasicNameValuePair("city", "");
				returnStr = MyTools.Post(ROOT + type, mvp1, mvp2, mvp3, mvp4,
						mvp5, mvp6);
			}

			if (returnStr == null)
				returnStr = SP.getSp(context).getString(type, null);
			if (TextUtils.isEmpty(returnStr)) {
				Log.e(TAG, "getDataFromSer，获取到的数据为空");
				return null;
			}

			Log.i("返回数据：", returnStr);
			Editor editor = SP.getSpEdit(context);
			editor.putString(type, returnStr);
			editor.commit();

			List<?> list = null;

			if (type.equalsIgnoreCase(GetGourmetShop)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(GetEntertainmentShop)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			} else if (type.equalsIgnoreCase(GetHotelShop)) {
				list = JSONUtil.getInstance().toList(returnStr,
						FoodEntity.class);
			}
			return list;
		} catch (Exception e) {
			Log.i(TAG, "调用接口:" + "getDataFromSer出错");
			e.printStackTrace();
		}

		return null;

	}
}
