package zblibrary.zgl.model;

import java.util.ArrayList;

public class AppInitInfo {


        public ArrayList<String> domains;
        public String csLink;
        public boolean maintenance;
        public String maintenanceInfo;
        public SysNoticeBean sysNotice;
        public ArrayList<String> guideImg;
        public static class SysNoticeBean {
            public String id;
            public String contentType;
            public String title;
            public String content;
    }
}
