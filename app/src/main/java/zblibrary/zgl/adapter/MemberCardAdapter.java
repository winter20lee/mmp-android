package zblibrary.zgl.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.MemberCenter;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class MemberCardAdapter extends BaseAdapter {

    private List<MemberCenter> memberCenters;
    private List<ImageView> imageViews = new ArrayList<>();
    private Context mContext;
    public MemberCardAdapter(Context ctx, List<MemberCenter> memberCenters) {
        this.mContext = ctx;
        this.memberCenters = memberCenters;
    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;// 返回数据的个数
    }

    @Override
    public Object getItem(int position) {
        if (position >= memberCenters.size()) {
            position = position % memberCenters.size();
        }
        return position;
    }

    @Override
    public long getItemId(int position) {
        if (position >= memberCenters.size()) {
            position = position % memberCenters.size();
        }
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (position >= memberCenters.size()) {
            position = position % memberCenters.size();
        }
        ImageView imageView = new ImageView(mContext);
        GlideUtil.load(mContext,memberCenters.get(position).cardImg,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(StringUtil.dp2px(mContext,240), StringUtil.dp2px(mContext,125)));
//        imageViews.add(imageView);

        return imageView;
    }
}
