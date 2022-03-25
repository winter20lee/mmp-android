package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Order;
import zblibrary.zgl.view.OrderView;
import zuo.biao.library.base.BaseAdapter;

public class OrderAdapter extends BaseAdapter<Order.MessageData, OrderView> {

	public OrderAdapter(Activity context) {
		super(context);
	}

	@Override
	public OrderView createView(int position, ViewGroup parent) {
		return new OrderView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}