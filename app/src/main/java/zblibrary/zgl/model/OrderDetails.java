
package zblibrary.zgl.model;


import java.util.List;

/**订单详情
 */
public class OrderDetails {

	public String orderStatus;
	public String goodsName;
	public String orderNo;
	public String createTime;
	public String paymentTime;
	public String sendTime;
	public String receiveTime;
	public String receiverAddress;
	public List<FeeListModel> feeList;
	public List<LogisticsListModel> logisticsList;
	public int invalidSecond;
	public String cancelTime;
	public String finishTime;
	public String totalAmount;
	public String receiverName;
	public String receiverContact;
	public List<OrderItemListModel> orderItemList;

	public static class FeeListModel {
		public String name;
		public int val;
	}

	public static class LogisticsListModel {
		public String expressName;
		public String waybillNo;
	}

	public static class OrderItemListModel {
		public String goodsId;
		public String skuId;
		public List<String> skuImage;
		public String goodsName;
		public List<GoodsSpecModel> goodsSpec;
		public int goodsPrice;
		public int goodsCount;
	}

	public static class GoodsSpecModel {
		public String attributionKeyId;
		public String attributionKeyName;
		public String attributionValId;
		public String attributionValName;
	}
}
