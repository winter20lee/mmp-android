
package zblibrary.zgl.model;

import java.util.List;

import zuo.biao.library.base.BaseModel;

/**站内信
 */
public class Message  {
	public int totalCount;
	public int totalPage;
	public int pageNo;
	public int pageSize;
	public List<MessageData> result; //头像

	public static class MessageData extends BaseModel{
		public String messageOpUserName;
		public String title;
		public String content;
		public String messageGmtCreate;
		public boolean readStatus;
		public boolean isEnd;
		@Override
		protected boolean isCorrect() {//根据自己的需求决定，也可以直接 return true
			return id > 0;// && StringUtil.isNotEmpty(phone, true);
		}

	}
}
