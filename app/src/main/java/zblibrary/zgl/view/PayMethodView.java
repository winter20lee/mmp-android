
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import zblibrary.zgl.R;
import zblibrary.zgl.model.MemberCenter;
import zblibrary.zgl.model.Order;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

public class PayMethodView extends BaseView<MemberCenter.PaymentMethodMembershipRespListBean>{
	public PayMethodView(Activity context, ViewGroup parent) {
		super(context, R.layout.paymethod_view, parent);
	}

	public ImageView paymethod_icon;
	public TextView paymethod_text;
	public ImageView paymethod_sel;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		paymethod_icon = findView(R.id.paymethod_icon);
		paymethod_text = findView(R.id.paymethod_text );
		paymethod_sel = findView(R.id.paymethod_sel);
		return super.createView();
	}

	@Override
	public void bindView(MemberCenter.PaymentMethodMembershipRespListBean data_){
		super.bindView(data_ != null ? data_ : new MemberCenter.PaymentMethodMembershipRespListBean());
		if(data.payType.equals("wx")){
			paymethod_icon.setImageResource(R.mipmap.weixin);
		}else{
			paymethod_icon.setImageResource(R.mipmap.zhifubao);
		}
		paymethod_text.setText(data.paymentMethodName);
		if(data.isSel){
			paymethod_sel.setImageResource(R.mipmap.shopping_car_select);
		}else{
			paymethod_sel.setImageResource(R.mipmap.shopping_car_unselect);
		}
		paymethod_sel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EventBus.getDefault().post(data);
			}
		});
	}
}