
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.ShoppingCart;
import zblibrary.zgl.view.ShoppingCartViewItem;
import zuo.biao.library.base.BaseAdapter;

/**
 * 购物车
 */
public class ShoppingCartAdapter extends BaseAdapter<ShoppingCart.ResultModel, ShoppingCartViewItem> {
	private  ShoppingCartViewItem.OnSelectClickListener onSelectClickListener;
	private boolean isSubmitOrder;
	public ShoppingCartAdapter(Activity context, ShoppingCartViewItem.OnSelectClickListener onSelectClickListener) {
		super(context);
		this.onSelectClickListener = onSelectClickListener;
	}
	public ShoppingCartAdapter(Activity context, ShoppingCartViewItem.OnSelectClickListener onSelectClickListener,boolean isSubmitOrder) {
		super(context);
		this.onSelectClickListener = onSelectClickListener;
		this.isSubmitOrder = isSubmitOrder;
	}

	@Override
	public ShoppingCartViewItem createView(int position, ViewGroup parent) {
		return new ShoppingCartViewItem(context, parent,onSelectClickListener,isSubmitOrder);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).goodsId;
	}

}