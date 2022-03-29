package zblibrary.zgl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhouwei.mzbanner.holder.MZViewHolder;

import zblibrary.zgl.R;
import zblibrary.zgl.model.ListByPos;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.GlideUtil;

public  class BannerViewPagerHolder implements MZViewHolder<ListByPos> {
    private RoundImageView mImageView;
    private boolean isNoRound;
    public BannerViewPagerHolder(){

    }

    public BannerViewPagerHolder(boolean noRound){
        this.isNoRound = noRound;
    }
    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.first_banner_item,null);
        mImageView = view.findViewById(R.id.normal_banner_image);
        if(isNoRound){
            mImageView.setRadius(0);
        }
        return view;
    }

    @Override
    public void onBind(Context context, int position, ListByPos data) {
        GlideUtil.load(context,data.imgUrl,mImageView);
    }
}

