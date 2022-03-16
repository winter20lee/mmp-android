
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.AllOrder;
import zblibrary.zgl.view.AllOrderView;
import zuo.biao.library.base.BaseAdapter;

public class AllOrderAdapter extends BaseAdapter<AllOrder.ResultModel, AllOrderView> {

	public AllOrderAdapter(Activity context) {
		super(context);
	}

	@Override
	public AllOrderView createView(int position, ViewGroup parent) {
		return new AllOrderView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).lootId;
	}

}