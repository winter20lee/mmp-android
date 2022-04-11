package zblibrary.zgl.model;

import java.util.List;

public class UploadAvatar {

    public int total;
    public DataBean data;
    public int success;
    public int failure;
    public int resultCode;
    public int time;

    public static class DataBean {
        public List<ImagesBean> images;

        public static class ImagesBean {
            public String oFileName;
            public String oUrl;
            public int status;
            public String tUrl;
        }
    }
}
