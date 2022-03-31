
package zblibrary.zgl.model;

import java.util.ArrayList;
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

		public SecondCategory transData(){
			SecondCategory secondCategory = new SecondCategory();
			SecondCategory.VideoCatalogBean videoCatalogBean = new SecondCategory.VideoCatalogBean();
			videoCatalogBean.name = "最新";
			secondCategory.videoCatalog = videoCatalogBean;

			ArrayList<SecondCategory.VideoListBean.ResultBean> resultBeans = new ArrayList<>();
			for (ResultBean resultBean1:result) {
				SecondCategory.VideoListBean.ResultBean resultBean = new SecondCategory.VideoListBean.ResultBean();
				resultBean.coverUrl = resultBean1.coverUrl;
				resultBean.name =resultBean1.name;
				resultBeans.add(resultBean);
			}
			SecondCategory.VideoListBean videoListBean = new SecondCategory.VideoListBean();
			videoListBean.result = resultBeans;
			secondCategory.videoList = videoListBean;
			return  secondCategory;
		}
}
