
package zblibrary.zgl.model;

import java.util.List;

/**积分
 */
public class Points {

	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;

	public static class ResultModel {
		public String orderNo;
		public int changeAmount;
		public int changeType;
		public String gmtCreate;
	}
}
