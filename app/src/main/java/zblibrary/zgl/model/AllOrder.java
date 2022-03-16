
package zblibrary.zgl.model;

import java.io.Serializable;
import java.util.List;

public class AllOrder {


	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public List<String> mainImage;
		public String goodsName;
		public List<GoodsSpecModel> goodsSpec;
		public int goodsPrice;
		public int lootCount;
		public int lootPrice;
		public int orderStatus;
		public int winStatus;
		public String orderNo;
		public String createTime;
		public int invalidSecond;
		public int lootId;
		public int planId;
		public String planNum;
		public int orderAmount;
		public Integer paymentMethod;
		public int orderPoint;
		public String refundRemark = "";

		public static class GoodsSpecModel implements Serializable {
			public String attributionKeyId;
			public String attributionKeyName;
			public String attributionValId;
			public String attributionValName;
		}
	}
}
