
package zblibrary.zgl.model;

import java.util.List;

public class MyOrder {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public String orderNo;
		public int invalidSecond;
		public int orderStatus;
		public List<FeeListModel> feeList;
		public int totalAmount;
		public List<OrderItemListModel> orderItemList;
		public String createTime;

		public static class FeeListModel {
			public String name;
			public int val;
		}

		public static class OrderItemListModel {
			public int goodsId;
			public int skuId;
			public List<String> mainImage;
			public String goodsName;
			public List<GoodsSpecModel> goodsSpec;
			public int goodsPrice;
			public int goodsCount;

			public static class GoodsSpecModel {
				public String attributionKeyId;
				public String attributionKeyName;
				public String attributionValId;
				public String attributionValName;
			}
		}
	}
}
