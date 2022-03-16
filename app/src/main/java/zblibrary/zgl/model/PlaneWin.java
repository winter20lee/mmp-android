
package zblibrary.zgl.model;

import java.util.List;

public class PlaneWin {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public String planNum;
		public String avatar;
		public String mobile;
		public String winUsername;
		public String gmtFinish;
		public int winLuckNumber;
	}
}
