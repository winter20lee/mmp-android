package zblibrary.zgl.adapter;

import android.view.View;
import com.stx.xhb.androidx.holder.ViewHolder;
import zblibrary.zgl.R;
import zblibrary.zgl.model.ListByPos;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.GlideUtil;

class BannerViewPagerHolder  implements ViewHolder<ListByPos> {

    @Override
    public int getLayoutId() {
        return R.layout.first_banner_item;
    }


    @Override
    public void onBind(View itemView, ListByPos data, int position) {
        RoundImageView imageView = itemView.findViewById(R.id.normal_banner_image);
        GlideUtil.load(itemView.getContext(),data.imgUrl,imageView);
    }
}

