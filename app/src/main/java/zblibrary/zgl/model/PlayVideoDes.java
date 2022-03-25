
package zblibrary.zgl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayVideoDes {

	public int id;
	public int goodsCategoryId;
	public int price;
	public String code;
	public String name;
	public String desc;
	public String keywords;
	public List<String> mainImage;
	public int tagType;
	public int toLoot;
	public int viewTimes;
	public int status;
	public String gmtOnline;
	public String gmtOffline;
	public String gmtCreate;
	public String gmtModify;
	public String detail;
	public String mobileDetail;
	public int saleNum;
	public int stock;
	public String goodsSpec;
	public List<GoodsSpecModel> goodsSpec1;
	public List<String> detailsImage;

	public static class GoodsSpecModel implements Serializable {
		public String attributionKeyId;
		public String attributionKeyName;
		public String attributionValId;
		public String attributionValName;
	}
}
