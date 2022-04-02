
package zblibrary.zgl.model;

import java.util.List;

/**商城
 */
public class SecondCategory {
		public VideoCatalogBean videoCatalog;
		public VideoListBean videoPageData;
		public static class VideoCatalogBean {
			public int id;
			public String name;
		}

		public static class VideoListBean {
			public int totalCount;
			public int totalPage;
			public int pageNo;
			public int pageSize;
			public List<ResultBean> result;

			public static class ResultBean {
				public int id;
				public String name;
				public String length;
				public String coverUrl;
				public String tag;
				public int playCnt;
				public int  catalogFirstLevelId;
				public int  catalogSecondLevelId;
			}
		}
}
