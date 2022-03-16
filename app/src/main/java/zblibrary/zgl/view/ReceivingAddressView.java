
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.AddAndEditAddressActivity;
import zblibrary.zgl.model.ReceivingAddress;
import zuo.biao.library.base.BaseView;

public class ReceivingAddressView extends BaseView<ReceivingAddress.ResultModel> implements OnClickListener {
	public ReceivingAddressView(Activity context, ViewGroup parent) {
		super(context, R.layout.receiving_address_view, parent);
	}

	public ReceivingAddressView(Activity context, ViewGroup parent,boolean isItem) {
		super(context, R.layout.receiving_address_view_no_top, parent);
	}

	private TextView rece_add_address,rece_add_address_des;
	private TextView rece_add_name;
	private TextView rece_add_phone;
	private TextView rece_add_def;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		findView(R.id.rece_add_edit, this);
		rece_add_address = findView(R.id.rece_add_address);
		rece_add_name = findView(R.id.rece_add_name);
		rece_add_phone = findView(R.id.rece_add_phone);
		rece_add_def = findView(R.id.rece_add_def);
		rece_add_address_des = findView(R.id.rece_add_address_des);
		return super.createView();
	}

	@Override
	public void bindView(ReceivingAddress.ResultModel data_) {
		super.bindView(data_ != null ? data_ : new ReceivingAddress.ResultModel());
		rece_add_name.setText(data.realName);
		rece_add_phone.setText(data.contact);
		rece_add_address_des.setText(data.area);
		if(data.isDefault){
			rece_add_def.setVisibility(View.VISIBLE);
		}else{
			rece_add_def.setVisibility(View.GONE);
		}
		rece_add_address.setText(data.address);
	}

	public void goneEditButton(){
		findView(R.id.rece_add_edit).setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rece_add_edit:
				toActivity(AddAndEditAddressActivity.createIntent(context,data));
				break;
		}
	}
}