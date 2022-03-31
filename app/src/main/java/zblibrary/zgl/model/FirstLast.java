
package zblibrary.zgl.model;

import java.util.List;

public class FirstLast {

		public int totalCount;
		public int totalPage;
		public int pageNo;
		public int pageSize;
		public List<ResultBean> result;

		public static class ResultBean {
			public int id;
			public String name;
			public int length;
			public ActorBean actor;
			public String bango;
			public String director;
			public String coverUrl;
			public String videoUrl;
			public String tag;
			public TopLevelCatalogBean topLevelCatalog;
			public SecondLevelCatalogBean secondLevelCatalog;

			public static class ActorBean {
				public int id;
				public String name;
				public String birthday;
				public int height;
				public String bwh;
				public String img;
			}

			public static class TopLevelCatalogBean {
				public int id;
				public String name;
				public int level;
				public int weight;
				public String icon;
				public String iconSelected;
				public String iconBig;
			}

			public static class SecondLevelCatalogBean {
				public int id;
				public String name;
				public int level;
				public int weight;
				public String icon;
				public String iconSelected;
				public String iconBig;
			}
		}
}
