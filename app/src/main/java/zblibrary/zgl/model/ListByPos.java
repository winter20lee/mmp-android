package zblibrary.zgl.model;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class ListByPos implements BaseBannerInfo {
    public String id;
    public String positon;
    public String catalogId;
    public String imgUrl;
    public String link;

    @Override
    public Object getXBannerUrl() {
        return imgUrl;
    }

    @Override
    public String getXBannerTitle() {
        return "";
    }
}
