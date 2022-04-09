package zblibrary.zgl.model;

import java.util.ArrayList;

public class AppInitInfo {


        public ArrayList<String> domains;
        public String csLink;
        public boolean maintenance;
        public String maintenanceInfo;
        public SysNoticeBean sysNotice;
        public ArrayList<AdsBean> ads;
        public static class SysNoticeBean {
            public String id;
            public String contentType;
            public String title;
            public String content;
    }

    public static class AdsBean{
        public int id;
        public int position;
        public int catalogId;
        public String imgUrl;
        public String link;
    }
}
