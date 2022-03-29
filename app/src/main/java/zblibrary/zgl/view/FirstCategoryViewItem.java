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

public class FirstCategoryViewItem extends BaseView<SecondCategory.ResultModel>{
	private static final String TAG = "UserView";

	public FirstCategoryViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_category_item_view, parent);
	}

	public ImageView hot_product_pic;
	public TextView hot_product_title;
	public TextView hot_product_price;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		hot_product_pic = findView(R.id.hot_product_pic);
		hot_product_title = findView(R.id.hot_product_title);
		hot_product_price = findView(R.id.hot_product_price);
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory.ResultModel data_){
		super.bindView(data_ != null ? data_ : new SecondCategory.ResultModel());

		if(data!=null && data.mainImage!=null && data.mainImage.size()>0){
			GlideUtil.loadRound(context,data.mainImage.get(0),hot_product_pic,R.dimen.dim_2);
		}

		hot_product_title.setText(data.name);
		hot_product_price.setText(StringUtil.changeF2Y(data.price));
	}
}