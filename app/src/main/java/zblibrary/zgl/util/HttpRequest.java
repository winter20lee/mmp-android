package zblibrary.zgl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zblibrary.zgl.model.RequestLootOrderInfo;
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
	public static final String URL_BASE = SettingUtil.getCurrentServerAddress();
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


	/**首页广告轮播
	 */
	public static void getFirstBanner(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/ads/app/info", requestCode, listener);
	}

	/**站内信
	 */
	public static void getMessageList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/user/message/list", requestCode, listener);
	}

	/**站内信已读状态
	 */
	public static void getReadMessage(long id, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(ID, id);
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/user/message/read", 0, listener);
	}

	/**首页夺宝
	 */
	public static void getFirstDuobao(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/loot/home/goods/list", requestCode, listener);
	}

	/**首页夺宝
	 */
	public static void getFirstDuobaoList(int goodsId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("goodsId",goodsId);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/goods/list", requestCode, listener);
	}
	/**商品分类夺宝
	 */
	public static void getLootSearch(int pageNo,String tagType,String goodsCategoryId,String orderByname,
									 String orderBy,String keywords,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		request.put("tagType", tagType);
		request.put("goodsCategoryId", goodsCategoryId);
		request.put("orderByName", orderByname);
		request.put("orderBy", orderBy);
		request.put("keywords", keywords);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/list", requestCode, listener);
	}

	/**首页商城
	 */
	public static void getFirstMall(int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		getMallSearch(pageNo,"","","","","",requestCode,listener);
	}

	/**首页获奖信息
	 */
	public static void getFirstWinList(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/loot/home/win/list", requestCode, listener);
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

	/**夺宝详情
	 */
	public static void getLootDes(long lootId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(LOOT_ID, lootId);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/details", requestCode, listener);
	}

	/**已开奖夺宝详情
	 */
	public static void getLootWinDes(int lootPlanId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("lootPlanId", lootPlanId);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plan/win/details", requestCode, listener);
	}

	/**往期揭晓
	 */
	public static void getPlaneWin(long lootId,int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(LOOT_ID, lootId);
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plane/win/list", requestCode, listener);
	}

	/**本期参与
	 */
	public static void getPlanePart(long lootId,int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("lootPlanId", lootId);
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plane/part/user/list", requestCode, listener);
	}

	/**购买夺宝计划幸运号码
	 */
	public static void getBuyLuck(long lootPlanId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(LOOT_PLAN_ID, lootPlanId);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/buy/luck/list", requestCode, listener);
	}

	/**购买夺宝计划幸运号码
	 */
	public static void getBuyLuckNum(String orderNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo", orderNo);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plan/order/luck/number", requestCode, listener);
	}

	/**商品详情-规格
	 */
	public static void getProductDesSpec(long goodsId,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(GOODS_ID, goodsId);
		HttpManager.getInstance().get(request, URL_BASE + "/goods/spu/spec/list", requestCode, listener);
	}

	/**分类搜索-分类
	 */
	public static void getGoodsCategory(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/goods/category/list", requestCode, listener);
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

	/**修改昵称
	 * @param nickname
	 * @param requestCode
	 * @param listener
	 */
	public static void updateUserNickname(String nickname, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("nickname", nickname);
		HttpManager.getInstance().post(request, URL_BASE + "/user/updateUserNickname", true,requestCode, listener);
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

	/**获取客服地址
	 * @param requestCode
	 * @param listener
	 */
	public static void getServiceInfoList(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/customize/APP_BASE_INO/list", requestCode, listener);
	}

	/**地址列表
	 */
	public static void getAddressList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().post(request, URL_BASE + "/user/address/list", true,requestCode, listener);
	}

	/**添加购物车
	 */
	public static void addProduct(long goodsId,int skuId,int count,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(GOODS_ID, goodsId);
		request.put("skuId", skuId);
		request.put("count",count);
		HttpManager.getInstance().post(request, URL_BASE + "/order/shopping/car/goods/add", true,requestCode, listener);
	}

	/**购物车列表
	 */
	public static void getProductList(int pageNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/order/shopping/car/goods/list", requestCode, listener);
	}
	/**删除购物车
	 */
	public static void removeShopCarGoods(ArrayList<String> idList,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("idList", idList);
		HttpManager.getInstance().post(request, URL_BASE + "/order/shopping/car/goods/remove",true, requestCode, listener);
	}
	/**修改购物车
	 */
	public static void modifyShopCarGoods(String id,int skuId,int count,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		request.put("skuId", skuId);
		request.put("count", count);
		HttpManager.getInstance().post(request, URL_BASE + "/order/shopping/car/goods/modify",true, requestCode, listener);
	}
	/**添加地址
	 */
	public static void addAddress(String address ,String area ,String contact ,
								  boolean isDefault ,String realName,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", 0);
		request.put("area", area);
		request.put("contact", contact);
		request.put("isDefault", isDefault);
		request.put("realName", realName);
		request.put("address", address);
		HttpManager.getInstance().post(request, URL_BASE + "/user/address/create", true,requestCode, listener);
	}
	/**更新地址
	 */
	public static void updateAddress(long id ,String address ,String area ,String contact ,
								  boolean isDefault ,String realName,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		request.put("area", area);
		request.put("contact", contact);
		request.put("isDefault", isDefault);
		request.put("realName", realName);
		request.put("address", address);
		HttpManager.getInstance().post(request, URL_BASE + "/user/address/update", true,requestCode, listener);
	}
	/**删除地址
	 */
	public static void delAddress(long id ,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("id", id);
		HttpManager.getInstance().post(request, URL_BASE + "/user/address/delete", true,requestCode, listener);
	}

	/**订单列表
	 */
	public static void getOrderList(String status,int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("status", status);
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/order/mall/order/list", requestCode, listener);
	}

	/**获取默认地址
	 */
	public static void getDefultAddress(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/user/address/getDefault",requestCode, listener);
	}

	/**获取下单token
	 */
	public static void getOrderToken(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/order/mall/order/create/info",requestCode, listener);
	}
	/**获取下单token
	 */
	public static void getLootOrderToken(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/order/loot/order/create/info",requestCode, listener);
	}
	/**提交商城订单
	 */
	public static void createMallOrder(String token, String orderAmount, String receiverName, String receiverContact, String receiverArea,
									   String receiverAddress, ArrayList<RequestOrderInfo> goodsList,
									   ArrayList<String> shoppingCarGoodsIdList,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("token", token);
		request.put("orderAmount", orderAmount);
		request.put("receiverName", receiverName);
		request.put("receiverContact", receiverContact);
		request.put("receiverArea", receiverArea);
		request.put("receiverAddress", receiverAddress);
		request.put("goodsList", goodsList);
		request.put("shoppingCarGoodsIdList", shoppingCarGoodsIdList);
		HttpManager.getInstance().post(request, URL_BASE + "/order/mall/order/create",true,requestCode, listener);
	}
	/**提交商城订单
	 */
	public static void createLootOrder(String token, int orderAmount, RequestLootOrderInfo lootList, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("token", token);
		request.put("orderAmount", orderAmount);
		ArrayList<RequestLootOrderInfo> requestLootOrderInfos = new ArrayList<>();
		requestLootOrderInfos.add(lootList);
		request.put("lootList", requestLootOrderInfos);
		HttpManager.getInstance().post(request, URL_BASE + "/order/loot/order/create",true,requestCode, listener);
	}

	/**获取订单详情
	 */
	public static void getOrderDetails(String orderNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo", orderNo);
		HttpManager.getInstance().get(request, URL_BASE + "/order/mall/order/detail",requestCode, listener);
	}

	/**订单列表
	 */
	public static void getLootOrderList(String status,int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("status", status);
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/order/loot/order/list", requestCode, listener);
	}
	/**订单列表
	 */
	public static void getLootPlanWinList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/loot/plan/win/list", requestCode, listener);
	}

	/**订单列表
	 */
	public static void createPay(int amount,String bizOrderId,String bizOrderType,String paymentMethodCode, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("amount", amount);
		request.put("bizOrderId", bizOrderId);
		request.put("bizOrderType", bizOrderType);
		request.put("currency", "PHP");
		request.put("paymentMethod", paymentMethodCode);
		request.put("source", "APP");
		HttpManager.getInstance().post(request, URL_BASE + "/payment/create", true,requestCode, listener);
	}
	/**中奖列表
	 */
	public static void getOrderWinList(String status,int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("status", status);
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 10);
		HttpManager.getInstance().get(request, URL_BASE + "/order/loot/order/win/list", requestCode, listener);
	}

	/**中奖详情
	 */
	public static void getOrderWinDetails(String orderNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo", orderNo);
		HttpManager.getInstance().get(request, URL_BASE + "/order/loot/order/detail",requestCode, listener);
	}

	/**获取用户积分和抵扣比例
	 */
	public static void getUserPointsRules(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/user/userPoints/get", requestCode, listener);
	}

	/**积分明细
	 */
	public static void getUserPointsInfoList(int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put(PAGE_NUM, pageNo);
		request.put(PAGE_SiZE, 20);
		HttpManager.getInstance().post(request, URL_BASE + "/user/userPointsInfo/list",true, requestCode, listener);
	}

	/**获取消息数量
	 */
	public static void getBusinessCount(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/user/business/count", requestCode, listener);
	}

	/**回购
	 */
	public static void postPurchase(String orderNo, String receiverName,String receiverContact,String receiverAccount,
								   String purchaseMethod,int purchasePercent,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo", orderNo);
		request.put("receiverName", receiverName);
		request.put("receiverContact", receiverContact);
		request.put("receiverAccount", receiverAccount);
		request.put("purchaseMethod", purchaseMethod);
		request.put("purchasePercent", purchasePercent);
		HttpManager.getInstance().post(request, URL_BASE + "/order/loot/order/win/purchase",true,requestCode, listener);
	}

	/**获取需要积分
	 */
	public static void getPointsAndStatus(int money,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("money", money);
		HttpManager.getInstance().post(request, URL_BASE + "/user/userPoints/getPointsAndStatus",requestCode, listener);
	}

	/**获取获得积分
	 * @param requestCode
	 * @param listener
	 */
	public static void getUserPoints(final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		HttpManager.getInstance().get(request, URL_BASE + "/promotion/customize/USER_POINTS_RULES/list", requestCode, listener);
	}

	/**确认收货
	 * @param requestCode
	 * @param listener
	 */
	public static void getReceiveConfirm(String orderNo,final int requestCode, final OnHttpResponseListener listener) {
		Map<String, Object> request = new HashMap<>();
		request.put("orderNo", orderNo);
		HttpManager.getInstance().post(request, URL_BASE + "/order/mall/order/receive/confirm",true, requestCode, listener);
	}

}