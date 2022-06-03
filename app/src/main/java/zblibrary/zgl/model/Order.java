
package zblibrary.zgl.model;
import java.util.List;

/**订单
 */
public class Order {
	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<MessageData> result; //头像

	public static class MessageData{
		public int id;
		public String orderNo;
		public String buyMemberLevelCode;
		public String buyMemberLevelName;
		public int orderAmount;
		public String gmtUpdate;
	}
}
