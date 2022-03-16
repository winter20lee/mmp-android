
package zblibrary.zgl.model;

import java.util.List;

public class PlanePart {
	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public String avatar;
		public String mobile;
		public String nickname;
		public int partNumber;
		public int payMoney;
		public String participateCreate;
	}
}
