
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.OrderDetailsOrderInfo;
import zuo.biao.library.base.BaseView;

public class OrderDetailsOrderInfoViewItem extends BaseView<OrderDetailsOrderInfo>{
	public OrderDetailsOrderInfoViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.order_details_order_info_item_view, parent);
	}

	private TextView ord_des_ord_info_title;
	private TextView ord_des_ord_info_text;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		ord_des_ord_info_title = findView(R.id.ord_des_ord_info_title);
		ord_des_ord_info_text = findView(R.id.ord_des_ord_info_text);
		return super.createView();
	}

	@Override
	public void bindView(OrderDetailsOrderInfo data_){
		super.bindView(data_ != null ? data_ : new OrderDetailsOrderInfo());
		ord_des_ord_info_title.setText(data.title);
		ord_des_ord_info_text.setText(data.name);
		if("Buyback price".equals(data.title)){
			ord_des_ord_info_text.setTextColor(Color.parseColor("#E4393C"));
		}else{
			ord_des_ord_info_text.setTextColor(Color.parseColor("#333333"));
		}
	}
}