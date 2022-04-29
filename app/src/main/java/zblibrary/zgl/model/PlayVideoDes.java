
package zblibrary.zgl.model;

import java.util.ArrayList;

public class PlayVideoDes {

		public int id;
		public String name;
		public String length;
		public VideoActorBean videoActor;
		public String bango;
		public String director;
		public String coverUrl;
		public String videoUrl;
		public String tag;
		public int catalogFirstLevelId;
		public int catalogSecondLevelId;
		public int favCnt;
		public int viewCnt;
		public int playCnt;
		public String gmtCreate;
		public int isfav;
		public ArrayList<ActorVideoListBean> actorVideoList;
		public static class VideoActorBean {
			public int id;
			public String name;
			public String birthday;
			public int height;
			public String bwh;
			public String img;
			public String bango;
			public String director;
		}

	public static class ActorVideoListBean {

		public int id;
		public String name;
		public String length;
		public String coverUrl;
		public String tag;
		public int playCnt;
		public int catalogFirstLevelId;
		public int catalogSecondLevelId;
		public String gmtCreate;
	}
}
