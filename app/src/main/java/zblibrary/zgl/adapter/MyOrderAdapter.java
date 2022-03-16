
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.MyOrder;
import zblibrary.zgl.view.MyOrderView;
import zuo.biao.library.base.BaseAdapter;

public class MyOrderAdapter extends BaseAdapter<MyOrder.ResultModel, MyOrderView> {

	private  MyOrderView.OnConfirmReceiverListener onConfirmReceiverListener;
	public MyOrderAdapter(Activity context, MyOrderView.OnConfirmReceiverListener onConfirmReceiverListener) {
		super(context);
		this.onConfirmReceiverListener = onConfirmReceiverListener;
	}

	@Override
	public MyOrderView createView(int position, ViewGroup parent) {
		MyOrderView myOrderView = new MyOrderView(context, parent);
		myOrderView.setOnConfirmReceiverListener(onConfirmReceiverListener);
		return myOrderView;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).orderStatus;
	}

}