package zblibrary.zgl.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.model.RequestOrderInfo;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.SettingUtil;

/**HTTP请求工具类
 * @use 添加请求方法xxxMethod >> HttpRequest.xxxMethod(...)
 * @must 所有请求的url、请求方法(GET, POST等)、请求参数(key-value方式，必要key一定要加，没提供的key不要加，value要符合指定范围)
 *       都要符合后端给的接口文档
 */
public class HttpRequest {

	/**基础URL，这里服务器设置可切换*/
	public static  String URL_BASE = SettingUtil.getCurrentServerAddress();
	public static final String PAGE_NUM = "pageNo";
	public static final String PAGE_SiZE = "pageSize";
	public static final String RANGE = "range";
	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";
	public static final String GOODS_ID = "goodsId";
	public static final String LOOT_ID = "lootId";
	public static final String LOOT_PLAN_ID = "lootPlanId";
	public static final String MOBILE = "mobileNo";
	public static final String CODE = "verifyCode";
	public static final String PASSWORD = "password";
	public static final String DEVICETYPE = "deviceType";
	public static final String DEVICEINFO = "deviceInfo";

	/**启动接口
	 */
	public static void getAppInitInfo(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/appInitInfo", requestCode, listener);
	}

	/**广告位
	 */
	public static void getListByPos(int pos,int catalogId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("pos",pos);
		request.put("catalogId",catalogId);
		HttpManager.getInstance().post(request, URL_BASE + "/api/ads/list",true, requestCode, listener);
	}


	/**会员
	 */
	public static void getMemberShip(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/membership/list", requestCode, listener);
	}

	/**最新
	 */
	public static void getNewest(int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Log.d("isCommend1", "1");
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/newest", true,requestCode, listener);
	}

	/**最新
	 */
	public static void getNewest(int pageNo,int pageSize,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, pageSize);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/newest", true,requestCode, listener);
	}

	/**推荐
	 */
	public static void getIndex(int pageNo,int catalogId,int level,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 4);
		request.put("catalogId", catalogId);
		request.put("level",level);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/hot/catalog",true,requestCode, listener);
	}


	/**站内信
	 */
	public static void getMessageList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		HttpManager.getInstance().post(request, URL_BASE + "/api/user/rechargeOrder", true,requestCode, listener);
	}


	/**猜你喜欢
	 */
	public static void getRecommend(long videoId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("videoId", videoId);
		HttpManager.getInstance().get(request, URL_BASE + "/api/video/recommend", requestCode, listener);
	}


	/**搜索接口
	 */
	public static void getSearch(int pageNo,int secondLevelCatalogId,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		request.put("secondLevelCatalogId", secondLevelCatalogId);
		request.put("keywords", keywords);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/search", true,requestCode, listener);
	}

	public static void getSearch10(int pageNo,int secondLevelCatalogId,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		request.put("secondLevelCatalogId", secondLevelCatalogId);
		request.put("keywords", keywords);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/moreVideo", true,requestCode, listener);
	}

	/**搜索接口
	 */
	public static void getSearch(int pageNo,int pageSize,int secondLevelCatalogId,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, pageSize);
		request.put("secondLevelCatalogId", secondLevelCatalogId);
		request.put("keywords", keywords);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/search", true,requestCode, listener);
	}

	/**搜索接口
	 */
	public static void getSearchLike(int pageNo,int secondLevelCatalogId,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		request.put("secondLevelCatalogId", secondLevelCatalogId);
		request.put("keywords", keywords);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/guess", true,requestCode, listener);
	}



	/**视频详情
	 */
	public static void getVideoDes(long id,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(ID, id);
		HttpManager.getInstance().get(request, URL_BASE + "/api/video/detail", requestCode, listener);
	}


	/**一级分类
	 */
	public static void getFirstCategory(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/videoCatalog/top", requestCode, listener);
	}


	/**登陆
	 * @param mobile
	 * @param code
	 * @param listener
	 */
	public static void loginByVerifyCode(final String mobile, final String code,
			final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(MOBILE, mobile);
		request.put(CODE, code);
		HttpManager.getInstance().post(request, URL_BASE + "/api/loginByMobile",true, requestCode, listener);
	}

	/**登陆新的
	 * @param mobile
	 * @param password
	 * @param listener
	 */
	public static void loginByPassword(final String mobile, final String password,
										 final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(MOBILE, mobile);
		request.put(PASSWORD, password);
		HttpManager.getInstance().post(request, URL_BASE + "/api/loginByMobile",true, requestCode, listener);
	}


	/**绑定
	 * @param mobile
	 * @param password
	 * @param listener
	 */
	public static void bindMobile(final String mobile, final String password,
										 final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(MOBILE, mobile);
		request.put(PASSWORD, password);
		HttpManager.getInstance().post(request, URL_BASE + "/api/bindMobile",true, requestCode, listener);
	}

	/**发送验证码
	 * @param mobileNo
	 * @param listener
	 */
	public static void sendVerifyCode(final String mobileNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("mobileNo", mobileNo);
		HttpManager.getInstance().get(request, URL_BASE + "/api/sendVerifyCode", requestCode, listener);
	}

	/**设备id登录
	 */
	public static void loginByDeviceId(String deviceToken,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("deviceToken", deviceToken);
		HttpManager.getInstance().post(request, URL_BASE + "/api/loginByDeviceId", true,requestCode, listener);
	}


	/**取消收藏
	 */
	public static void getFavCancel(final long id,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		ArrayList<Long> arrayList = new ArrayList<>();
		arrayList.add(id);
		request.put("ids", arrayList);
		HttpManager.getInstance().post(request, URL_BASE + "/api/vedio/fav/cancel",true,requestCode, listener);
	}

	/**取消收藏
	 */
	public static void getFavCancel(final ArrayList<Integer> arrayList,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("ids", arrayList);
		HttpManager.getInstance().post(request, URL_BASE + "/api/vedio/fav/cancel",true,requestCode, listener);
	}

	/**添加藏
	 */
	public static void getFav(final long id,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		HttpManager.getInstance().post(request, URL_BASE + "/api/vedio/fav",true,requestCode, listener);
	}

	/**添加播放记录
	 */
	public static void getPlay(final long id,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		HttpManager.getInstance().post(request, URL_BASE + "/api/vedio/play",true,requestCode, listener);
	}


	/**修改用户信息
	 */
	public static void updateUserInfo(String headImg,String nickName, String birthday,int gender,String personal,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("nickName", nickName);
		request.put("birthday", birthday);
		request.put("gender", gender);
		request.put("personal", personal);
		request.put("headImg", headImg);
		HttpManager.getInstance().post(request, URL_BASE + "/api/user/updateInfo", true,requestCode, listener);
	}


	/**收藏记录
	 */
	public static void getMyfav(final int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		request.put("userId", MApplication.getInstance().getCurrentUserId());
		HttpManager.getInstance().post(request, URL_BASE + "/api/user/getMyfav", true,requestCode, listener);
	}

	/**播放记录
	 */
	public static void getMyplay(final int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		request.put("userId", MApplication.getInstance().getCurrentUserId());
		HttpManager.getInstance().post(request, URL_BASE + "/api/user/getMyplay", true,requestCode, listener);
	}



	/**记录guest 的用户的下载次数
	 */
	public static void getDownload(int id,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		HttpManager.getInstance().post(request, URL_BASE + "/api/vedio/download", true,requestCode, listener);
	}

	/**获取guest用户的下载次数
	 */
	public static void getDownloadCnt(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/user/getDayDownloadCnt", requestCode, listener);
	}

	/**获取guest用户的播放次数
	 */
	public static void getDayPlayCnt(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/user/getDayPlayCnt", requestCode, listener);
	}

	/**上报用户活跃状态
	 */
	public static void reportActive(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().post(request, URL_BASE + "/api/user/reportActive", true,requestCode, listener);
	}

	/**支付
	 */
	public static void getPay(final int pmId,final String lc,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("pmId",pmId);
		request.put("lc",lc);
		HttpManager.getInstance().post(request, URL_BASE + "/api/payment/make",true, requestCode, listener);
	}

	/**会员
	 */
	public static void getPayState(String orderNo,	final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo",orderNo);
		HttpManager.getInstance().get(request, URL_BASE + "/api/payment/check", requestCode, listener);
	}

	/**会员
	 */
	public static void getCurrentUserInfo(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/api/user/getCurrentUserInfo", requestCode, listener);
	}
}