package zblibrary.zgl.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import java.util.List;

import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.MemberCenter;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class MemberCardAdapter extends BaseAdapter {

    private List<MemberCenter> memberCenters;
    private Context mContext;
    public MemberCardAdapter(Context ctx, List<MemberCenter> memberCenters) {
        this.mContext = ctx;
        this.memberCenters = memberCenters;
    }

    @Override
    public int getCount() {
        return memberCenters.size();// 返回数据的个数
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
        GlideUtil.load(mContext,memberCenters.get(i).cardImg,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(StringUtil.dp2px(mContext,240), StringUtil.dp2px(mContext,125)));
        return imageView;
    }
}
