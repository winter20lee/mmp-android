
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.OrderDetailsOrderInfo;
import zblibrary.zgl.view.OrderDetailsOrderInfoViewItem;
import zuo.biao.library.base.BaseAdapter;

/**
 * 订单详情
 */
public class OrderDetailsOrderInfoAdapter extends BaseAdapter<OrderDetailsOrderInfo, OrderDetailsOrderInfoViewItem> {
	public OrderDetailsOrderInfoAdapter(Activity context) {
		super(context);
	}

	@Override
	public OrderDetailsOrderInfoViewItem createView(int position, ViewGroup parent) {
		return new OrderDetailsOrderInfoViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}