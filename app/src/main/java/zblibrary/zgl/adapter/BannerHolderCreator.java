package zblibrary.zgl.adapter;

import com.stx.xhb.androidx.holder.HolderCreator;
import com.stx.xhb.androidx.holder.ViewHolder;

//创建 HolderCreator 在里面根据viewType实现多布局的逻辑
public class BannerHolderCreator implements HolderCreator<ViewHolder> {

    @Override
    public ViewHolder createViewHolder(int viewType) {
        if (viewType==0){
            return new BannerViewPagerHolder();
        }
        return new BannerViewPagerHolder();
    }

    @Override
    public int getViewType(int position) {
        return position;
    }
}
