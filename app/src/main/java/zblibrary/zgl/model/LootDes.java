
package zblibrary.zgl.model;

import java.util.List;

import zuo.biao.library.base.BaseModel;

public class LootDes extends BaseModel {

	public int goodsId;
	public int planeId;
	public int lootId;
	public String planNum;
	public String goodsName;
	public List<GoodsSpecListModel> goodsSpecList;
	public int price;
	public int unitPrice;
	public int allNumber;
	public int buyNumber;
	public int limitNumber;
	public double lootSchedule;
	public String lootScheduleShow;
	public List<String> detailsImage;
	public boolean participationStatus;
	public String mobileDetail;
	public int status;//状态:1待上线 ;2 夺宝中;3 已结束
	public int payMoney;
	public int tailNumber;
	public int lockNumber;
	public String mobile;
	public String winUsername;
	public String gmtFinish;
	public String winLuckNumber;
	public String avatar;
	public String goodsSpec;
	@Override
	protected boolean isCorrect() {
		return false;
	}

	public static class GoodsSpecListModel extends BaseModel{
		public String name;
		public String value;

		@Override
		protected boolean isCorrect() {
			return false;
		}
	}
}
