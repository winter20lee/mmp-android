
package zblibrary.zgl.model;


import java.io.Serializable;
import java.util.List;

/**购物车
 */
public class ShoppingCart {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel implements Serializable {
		private static final long serialVersionUID = 1L;
		public boolean isSelect;
		public String id;
		public List<String> mainImage;
		public String goodsName;
		public int goodsId;
		public int skuId;
		public List<GoodsSpecModel> goodsSpec;
		public int goodsPrice;
		public int goodsCount;
		public int stock;
		public String createTime;

		public static class GoodsSpecModel implements Serializable {
			public String attributionKeyId;
			public String attributionKeyName;
			public String attributionValId;
			public String attributionValName;
		}
	}
}
