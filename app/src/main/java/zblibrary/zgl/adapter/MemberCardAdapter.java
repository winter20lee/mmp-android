package zblibrary.zgl.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import java.util.List;

import zblibrary.zgl.model.FirstBanner;
import zuo.biao.library.util.GlideUtil;

public class MemberCardAdapter extends BaseAdapter {

    private List<FirstBanner> firstBannerList;
    private Context mContext;
    public MemberCardAdapter(Context ctx, List<FirstBanner> firstBannerList) {
        this.mContext = ctx;
        this.firstBannerList = firstBannerList;
    }

    @Override
    public int getCount() {
        return firstBannerList.size();// 返回数据的个数
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        GlideUtil.load(mContext,firstBannerList.get(i).imgUrl,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(1000, 500));
        return imageView;
    }
}
