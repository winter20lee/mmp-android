
package zblibrary.zgl.model;

import java.util.ArrayList;
import java.util.List;

public class Last {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public int id;
		public int price;
		public int toSell;
		public int saleNum;
		public int goodsCategoryId;
		public String code;
		public String name;
		public String desc;
		public String keywords;
		public ArrayList<String> mainImage;
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
	}
}
