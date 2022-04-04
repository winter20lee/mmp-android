
package zblibrary.zgl.model;

import java.util.List;

public class MyLike  {
		public int totalCount;
		public int totalPage;
		public int pageNo;
		public int pageSize;
		public List<ResultBean> result;

		public static class ResultBean {
			public int videoId;
			public String videoName;
			public String videoCoverUrl;
			public String videoTags;
			public String gmtCreate;
		}
}
