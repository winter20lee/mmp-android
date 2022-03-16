
package zblibrary.zgl.model;

import java.util.List;

public class WinningGoods {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
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
		public String planNum;
		public int orderAmount;
		public int winStatus;
		public String receiverAddress;
		public Integer paymentMethod;
		public int orderPoint;

		public static class GoodsSpecModel {
			public String attributionKeyId;
			public String attributionKeyName;
			public String attributionValId;
			public String attributionValName;
		}
	}
}
