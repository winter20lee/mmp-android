
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.OrderDetails;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class OrderDetailsViewItem extends BaseView<OrderDetails.OrderItemListModel>{
	public OrderDetailsViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.order_details_item_view, parent);
	}

	private ImageView shopping_cart_item_pic;
	private TextView shopping_cart_item_title;
	private TextView shopping_cart_item_canshu;
	private TextView shopping_cart_item_num;
	public TextView shopping_cart_item_price;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		shopping_cart_item_pic = findView(R.id.shopping_cart_item_pic);
		shopping_cart_item_title = findView(R.id.shopping_cart_item_title);
		shopping_cart_item_canshu = findView(R.id.shopping_cart_item_canshu);
		shopping_cart_item_num = findView(R.id.shopping_cart_item_num);
		shopping_cart_item_price = findView(R.id.shopping_cart_item_price);
		return super.createView();
	}

	@Override
	public void bindView(OrderDetails.OrderItemListModel data_){
		super.bindView(data_ != null ? data_ : new OrderDetails.OrderItemListModel());
		GlideUtil.loadRound(context,data.skuImage.get(0),shopping_cart_item_pic);
		shopping_cart_item_title.setText(data.goodsName);
		String spec = "";
		if(data.goodsSpec!=null && data.goodsSpec.size()>0){
			for (OrderDetails.GoodsSpecModel goodsSpecModel:data.goodsSpec) {
				spec+=goodsSpecModel.attributionValName+"  ";
			}
		}
		shopping_cart_item_canshu.setText(getString(R.string.shopcart_peizhi)+spec);
		shopping_cart_item_num.setText(getString(R.string.shopcart_shuliang)+data.goodsCount);
		shopping_cart_item_price.setText(StringUtil.changeF2Y(data.goodsPrice));
	}
}