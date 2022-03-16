
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import zblibrary.zgl.R;
import zblibrary.zgl.model.FirstDuobao;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class FirstDuobaoViewItem extends BaseView<FirstDuobao> {

	public FirstDuobaoViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_category_item_view, parent);
	}

	public ImageView hot_product_pic;
	public TextView hot_product_title;
	public TextView hot_product_price;
	public TextView hot_product_sold;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		hot_product_pic = findView(R.id.hot_product_pic);
		hot_product_title = findView(R.id.hot_product_title);
		hot_product_price = findView(R.id.hot_product_price);
		hot_product_sold = findView(R.id.hot_product_sold);
		return super.createView();
	}

	@Override
	public void bindView(FirstDuobao data_){
		super.bindView(data_ != null ? data_ : new FirstDuobao());

		if(data!=null && data.mainImage!=null && data.mainImage.size()>0){
			GlideUtil.loadRound(context,data.mainImage.get(0),hot_product_pic,R.dimen.dim_2);
		}

		hot_product_title.setText(data.goodsName);
		hot_product_price.setText(StringUtil.changeF2Y(data.unitPrice));
		hot_product_sold.setText("Raffle Process " +data.lootScheduleShow);
	}
}