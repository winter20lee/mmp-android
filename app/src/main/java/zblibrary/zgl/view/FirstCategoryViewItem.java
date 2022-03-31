package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.SecondCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class FirstCategoryViewItem extends BaseView<SecondCategory.VideoListBean.ResultBean>{
	private static final String TAG = "UserView";

	public FirstCategoryViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_category_item_view, parent);
	}

	public ImageView first_category_item_iv;
	public TextView first_category_item_play;
	public TextView first_category_item_longs,first_category_item_title,first_category_item_label;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_category_item_iv = findView(R.id.first_category_item_iv);
		first_category_item_play = findView(R.id.first_category_item_play);
		first_category_item_longs = findView(R.id.first_category_item_longs);
		first_category_item_title = findView(R.id.first_category_item_title);
		first_category_item_label = findView(R.id.first_category_item_label);
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory.VideoListBean.ResultBean data_){
		super.bindView(data_ != null ? data_ : new SecondCategory.VideoListBean.ResultBean());

		if(data!=null && StringUtil.isNotEmpty(data.coverUrl,true)){
			GlideUtil.loadRound(context,data.coverUrl,first_category_item_iv,R.dimen.dim_2);
		}

		first_category_item_title.setText(data.name);
	}
}