
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.PayMent;
import zuo.biao.library.base.BaseView;

public class PayMentView extends BaseView<PayMent> {
	public PayMentView(Activity context, ViewGroup parent) {
		super(context, R.layout.pay_ment_view, parent);
	}

	private TextView paymant_name,paymant_contant;
	private TextView paymant_channel_value;
	private TextView paymant_account_value;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		paymant_name = findView(R.id.paymant_name);
		paymant_contant = findView(R.id.paymant_contant);
		paymant_channel_value = findView(R.id.paymant_channel_value);
		paymant_account_value = findView(R.id.paymant_account_value);
		return super.createView();
	}

	@Override
	public void bindView(PayMent data_) {
		super.bindView(data_ != null ? data_ : new PayMent());
		paymant_name.setText(data.receiverName);
		paymant_contant.setText(data.receiverContact);
		if(data.paymentMethod==0){
			paymant_channel_value.setText("Point");
		}else if(data.paymentMethod==1){
			paymant_channel_value.setText("Gcash");
		}
		paymant_account_value.setText(data.receiverAccount);
	}
}