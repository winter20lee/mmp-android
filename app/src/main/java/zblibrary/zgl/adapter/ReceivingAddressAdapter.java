
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.view.ReceivingAddressView;
import zuo.biao.library.base.BaseAdapter;

/**收货地址 adapter
 */
public class ReceivingAddressAdapter extends BaseAdapter<ReceivingAddress.ResultModel, ReceivingAddressView> {
	public ReceivingAddressAdapter(Activity context) {
		super(context);
	}

	@Override
	public ReceivingAddressView createView(int position, ViewGroup parent) {
		return new ReceivingAddressView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

}