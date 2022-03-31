package zblibrary.zgl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	public static final String MOBILE = "mobile";
	public static final String CODE = "code";
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
	public static void getListByPos(String pos,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("pos",pos);
		HttpManager.getInstance().get(request, URL_BASE + "/api/ads/listByPos", requestCode, listener);
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
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		HttpManager.getInstance().post(request, URL_BASE + "/api/video/newest", true,requestCode, listener);
	}


	/**站内信
	 */
	public static void getMessageList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/user/message/list", requestCode, listener);
	}



	/**首页商城
	 */
	public static void getFirstMall(int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		getMallSearch(pageNo,"","","","","",requestCode,listener);
	}


	/**商品搜索接口
	 */
	public static void getMallSearch(int pageNo,String tagType,String goodsCategoryId,String orderByname,
									 String orderBy,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		request.put("tagType", tagType);
		request.put("goodsCategoryId", goodsCategoryId);
		request.put("orderByName", orderByname);
		request.put("orderBy", orderBy);
		request.put("keywords", keywords);
		HttpManager.getInstance().get(request, URL_BASE + "/goods/spu/index/list", requestCode, listener);
	}

	/**商品详情
	 */
	public static void getProductDes(long goodsId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(GOODS_ID, goodsId);
		HttpManager.getInstance().get(request, URL_BASE + "/goods/spu/info", requestCode, listener);
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
		request.put(DEVICETYPE, 1);
		request.put(DEVICEINFO, android.os.Build.MODEL);
		HttpManager.getInstance().post(request, URL_BASE + "/user/loginByVerifyCode",true, requestCode, listener);
	}

	/**发送验证码
	 * @param mobileNo
	 * @param listener
	 */
	public static void sendVerifyCode(final String mobileNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("mobileNo", mobileNo);
		HttpManager.getInstance().get(request, URL_BASE + "/user/sendVerifyCode", requestCode, listener);
	}

	/**修改昵称
	 * @param requestCode
	 * @param listener
	 */
	public static void getUploadToken(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/user/getUploadToken",requestCode, listener);
	}


	/**修改头像
	 * @param avatar
	 * @param requestCode
	 * @param listener
	 */
	public static void updateUserAvatar(String avatar, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("avatar", avatar);
		HttpManager.getInstance().post(request, URL_BASE + "/user/updateUserAvatar", true,requestCode, listener);
	}

	/**获取头像列表
	 * @param requestCode
	 * @param listener
	 */
	public static void getUserDefultHeadList(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/customize/USER_HEADER_ICON/list", requestCode, listener);
	}

	/**获取帮助信息
	 * @param requestCode
	 * @param listener
	 */
	public static void getHelpInfoList(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/customize/HELP_INFO/list", requestCode, listener);
	}


	/**订单列表
	 */
	public static void getLootPlanWinList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plan/win/list", requestCode, listener);
	}



	/**获取用户积分和抵扣比例
	 */
	public static void getUserPointsRules(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/user/userPoints/get", requestCode, listener);
	}

}