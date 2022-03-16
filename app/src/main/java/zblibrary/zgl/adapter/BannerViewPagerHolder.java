package zblibrary.zgl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import zblibrary.zgl.R;
import zblibrary.zgl.model.FirstBanner;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.GlideUtil;

public  class BannerViewPagerHolder implements MZViewHolder<FirstBanner> {
    private RoundImageView mImageView;
    private TextView mTitle;
    private TextView mDesc;
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
        mDesc =  view.findViewById(R.id.page_desc);
        if(isNoRound){
            mImageView.setRadius(0);
        }
        return view;
    }

    @Override
    public void onBind(Context context, int position, FirstBanner data) {
        GlideUtil.load(context,data.imgUrl,mImageView);
        mDesc.setText(data.title);
    }
}

