
package zblibrary.zgl.model;

import java.util.List;

/**演员推荐
 */
public class ActorRecommend {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public int lootPlanId;
		public int lootId;
		public int goodsId;
		public String goodsName;
		public List<String> goodsImg;
		public String showGoodsSpec;
		public int planNum;
		public String winUserMobile;
		public int orderNumber;
		public int winLuckNumber;
		public String gmtFinish;
	}
}