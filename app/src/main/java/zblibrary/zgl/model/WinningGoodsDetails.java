
package zblibrary.zgl.model;


import java.util.ArrayList;
import java.util.List;

/**中奖商品详情
 */
public class WinningGoodsDetails {

	public List<String> skuImage;
	public String goodsName;
	public List<GoodsSpecModel> goodsSpec;
	public int goodsPrice;
	public int lootCount;
	public int lootPrice;
	public String orderNo;
	public String createTime;
	public int invalidSecond;
	public int lootId;
	public int planId;
	public int planNum;
	public int totalAmount;
	public int winStatus;
	public String receiverAddress;
	public String luckNumber;
	public String pickTime;
	public String sendTime;
	public String receiveTime;
	public byte abnormalStatus;
	public String purchaseImage;
	public Integer lootPurchasePrice;
	public String receiverName;
	public String receiverContact;
	public String receiverArea;
	public String transferTime;
	public String finishTime;
	public int paymentMethod;
	public String receiverAccount;
	public int lootPurchasePercent;
	public int orderAmount;
	public static class GoodsSpecModel {
		public String attributionKeyId;
		public String attributionKeyName;
		public String attributionValId;
		public String attributionValName;
	}

	public OrderDetails.OrderItemListModel transData(){
		OrderDetails.OrderItemListModel orderItemListModel = new OrderDetails.OrderItemListModel();
		orderItemListModel.goodsId = this.lootId+"";
		orderItemListModel.skuImage = this.skuImage;
		orderItemListModel.goodsCount = 1;
		orderItemListModel.goodsName = this.goodsName;
		orderItemListModel.goodsPrice = this.goodsPrice;
		List<OrderDetails.GoodsSpecModel> goodsSpec = new ArrayList<>();
		for (GoodsSpecModel goodsSpec1:this.goodsSpec) {
			OrderDetails.GoodsSpecModel goodsSpecModel = new OrderDetails.GoodsSpecModel();
			goodsSpecModel.attributionValName = goodsSpec1.attributionValName;
			goodsSpec.add(goodsSpecModel);
		}
		orderItemListModel.goodsSpec = goodsSpec;
		return orderItemListModel;
	}
}
