
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.model.ShoppingCart;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class ShoppingCartViewItem extends BaseView<ShoppingCart.ResultModel> implements OnClickListener {
	private static final String TAG = "ShoppingCartViewItem";
	private boolean isSubmitOrder;
//	public ShoppingCartViewItem(Activity context, ViewGroup parent, OnSelectClickListener onSelectClickListener) {
//		super(context, R.layout.shopping_cart_item_view, parent);
//		this.onSelectClickListener = onSelectClickListener;
//	}

	public ShoppingCartViewItem(Activity context, ViewGroup parent, OnSelectClickListener onSelectClickListener,boolean isSubmitOrder) {
		super(context, R.layout.shopping_cart_item_view, parent);
		this.onSelectClickListener = onSelectClickListener;
		this.isSubmitOrder = isSubmitOrder;
	}

	public OnSelectClickListener onSelectClickListener;

	private ImageView shopping_cart_item_select,shopping_cart_item_pic;
	private TextView shopping_cart_item_title;
	private TextView shopping_cart_item_canshu;
	private TextView shopping_cart_item_num;
	private TextView shopping_cart_item_price;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		findView(R.id.shopping_cart_item_spec,this);
		shopping_cart_item_select = findView(R.id.shopping_cart_item_select,this);
		findView(R.id.shopping_cart_item_del,this);
		shopping_cart_item_pic = findView(R.id.shopping_cart_item_pic);
		shopping_cart_item_title = findView(R.id.shopping_cart_item_title);
		shopping_cart_item_canshu = findView(R.id.shopping_cart_item_canshu);
		shopping_cart_item_num = findView(R.id.shopping_cart_item_num);
		shopping_cart_item_price = findView(R.id.shopping_cart_item_price);
		return super.createView();
	}

	@Override
	public void bindView(ShoppingCart.ResultModel data_){
		super.bindView(data_ != null ? data_ : new ShoppingCart.ResultModel());
		if(data.mainImage!=null && data.mainImage.size()>0){
			GlideUtil.load(context,data.mainImage.get(0),shopping_cart_item_pic);
		}
		shopping_cart_item_title.setText(data.goodsName);
		String spec = "";
		if(data.goodsSpec!=null && data.goodsSpec.size()>0){
			for (ShoppingCart.ResultModel.GoodsSpecModel goodsSpecModel:data.goodsSpec) {
				spec+=goodsSpecModel.attributionValName+" ";
			}
		}
		shopping_cart_item_canshu.setText(spec);
		shopping_cart_item_num.setText(getString(R.string.shopcart_shuliang)+data.goodsCount);
		shopping_cart_item_price.setText(StringUtil.changeF2Y(data.goodsPrice));
		if(isSubmitOrder){
			shopping_cart_item_select.setVisibility(View.GONE);
		}else{
			if(data.isSelect){
				shopping_cart_item_select.setImageResource(R.mipmap.shopping_car_select);
			}else{
				shopping_cart_item_select.setImageResource(R.mipmap.shopping_car_unselect);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopping_cart_item_pic:
			toActivity(PlayVideoDetailsActivity.createIntent(context,data.goodsId));
			break;
		case R.id.shopping_cart_item_select:
			data.isSelect = !data.isSelect;
//			bindView(data);
			if(onSelectClickListener!=null){
				onSelectClickListener.onSelect();
			}
			break;
		case R.id.shopping_cart_item_del:
			if(onSelectClickListener!=null){
				onSelectClickListener.onDelete(data);
			}
			break;
		case R.id.shopping_cart_item_spec:
			if(onSelectClickListener!=null){
				onSelectClickListener.onSelSpec(data);
			}
			break;
		}
	}

	public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener){
		this.onSelectClickListener = onSelectClickListener;
	}

	public interface OnSelectClickListener{
		void onSelect();
		void onDelete(ShoppingCart.ResultModel resultModel);
		void onSelSpec(ShoppingCart.ResultModel resultModel);
	}
}