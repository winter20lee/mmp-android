
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
	public TextView pay_tag,paymethod_text_comm;
	public RelativeLayout pay_ll;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		paymethod_icon = findView(R.id.paymethod_icon);
		paymethod_text = findView(R.id.paymethod_text );
		paymethod_sel = findView(R.id.paymethod_sel);
		pay_tag = findView(R.id.pay_tag);
		pay_ll = findView(R.id.pay_ll);
		paymethod_text_comm = findView(R.id.paymethod_text_comm);
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
			pay_ll.setBackgroundResource(R.drawable.radius_2_yellow_shap);
			paymethod_sel.setVisibility(View.VISIBLE);
		}else{
			pay_ll.setBackgroundResource(R.drawable.radius_2_shap);
			paymethod_sel.setVisibility(View.GONE);
		}
		if(StringUtil.isNotEmpty(data.tag,true)){
			paymethod_text_comm.setVisibility(View.VISIBLE);
			pay_tag.setText(data.tag);
			pay_tag.setVisibility(View.VISIBLE);
		}else{
			paymethod_text_comm.setVisibility(View.GONE);
			pay_tag.setText(data.tag);
			pay_tag.setVisibility(View.INVISIBLE);
		}
		pay_ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EventBus.getDefault().post(data);
			}
		});
	}
}