package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.MemberCenter;
import zblibrary.zgl.view.PayMethodView;
import zuo.biao.library.base.BaseAdapter;

public class PayMethodAdapter extends BaseAdapter<MemberCenter.PaymentMethodMembershipRespListBean, PayMethodView> {

	public PayMethodAdapter(Activity context) {
		super(context);
	}

	@Override
	public PayMethodView createView(int position, ViewGroup parent) {
		return new PayMethodView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).paymentMethodId;
	}

}