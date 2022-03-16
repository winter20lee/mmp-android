
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.OrderDetails;
import zblibrary.zgl.view.OrderDetailsViewItem;
import zuo.biao.library.base.BaseAdapter;

/**
 * 订单详情
 */
public class OrderDetailsAdapter extends BaseAdapter<OrderDetails.OrderItemListModel, OrderDetailsViewItem> {
	public OrderDetailsAdapter(Activity context) {
		super(context);
	}

	@Override
	public OrderDetailsViewItem createView(int position, ViewGroup parent) {
		return new OrderDetailsViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return Integer.parseInt(getItem(position).goodsId);
	}

}