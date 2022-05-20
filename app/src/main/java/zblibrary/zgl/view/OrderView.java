
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Order;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

public class OrderView extends BaseView<Order.MessageData>{
	public OrderView(Activity context, ViewGroup parent) {
		super(context, R.layout.paymethod_view, parent);
	}

	public TextView order_num;
	public TextView order_copy;
	public TextView order_state,order_name;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		order_num = findView(R.id.order_num);
		order_copy = findView(R.id.order_copy );
		order_state = findView(R.id.order_state);
		order_name = findView(R.id.order_name);
		return super.createView();
	}

	@Override
	public void bindView(Order.MessageData data_){
		super.bindView(data_ != null ? data_ : new Order.MessageData());
		order_num.setText(StringUtil.getTrimedString(data.title));
		order_copy.setText(data.messageGmtCreate);
		order_state.setText(data.content);
	}
}