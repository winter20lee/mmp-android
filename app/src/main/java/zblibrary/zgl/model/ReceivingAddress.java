
package zblibrary.zgl.model;

import java.util.List;

import zuo.biao.library.base.BaseModel;

/**收货地址
 */
public class ReceivingAddress {

	public int pageNo;
	public int pageSize;
	public List<ResultModel> result;
	public int totalCount;
	public int totalPage;

	public static class ResultModel extends BaseModel {
		public String address;
		public String contact;
		public String gmtCreate;
		public String gmtModify;
		public boolean isDefault;
		public String realName;
		public int userId;
		public String area;
		@Override
		protected boolean isCorrect() {
			return false;
		}
	}
}
