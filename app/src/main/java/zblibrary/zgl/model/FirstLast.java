
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
			public String length;
			public String coverUrl;
			public String tag;
			public int playCnt;
			public int  catalogFirstLevelId;
			public int  catalogSecondLevelId;
			public String gmtCreate;
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
			secondCategory.videPageData = videoListBean;
			return  secondCategory;
		}
}
