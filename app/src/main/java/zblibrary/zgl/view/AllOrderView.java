package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.BottomPayWindow;
import zblibrary.zgl.activity.MyNumberActivity;
import zblibrary.zgl.activity.PayActivity;
import zblibrary.zgl.activity.WinningGoodsActivity;
import zblibrary.zgl.model.AllOrder;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class AllOrderView extends BaseView<AllOrder.ResultModel> implements OnClickListener {

	public AllOrderView(Activity context, ViewGroup parent) {
		super(context, R.layout.allorder_view, parent);
	}

	private TextView myorder_time,myorder_youfei,myorder_item_title,myorder_numcode;
	private TextView myorder_code,myorder_price,myorder_item_peizhi;
	private TextView myorder_state,myorder_pay,myorder_des,myorder_item_num,allorder_price,
			allorder_untprice,allorder_num,myorder_result;
	private LinearLayout myorder_content;
	private RelativeLayout myOrderView;
	private ImageView myorder_item_pic,myorder_win;
	private ToPayItemCount count;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		myorder_time = findView(R.id.myorder_time);
		myorder_youfei = findView(R.id.myorder_youfei);
		myorder_code = findView(R.id.myorder_code);
		myorder_price = findView(R.id.myorder_price);
		myorder_state = findView(R.id.myorder_state);
		myorder_pay = findView(R.id.myorder_pay, this);
		myorder_des = findView(R.id.myorder_des, this);
		myorder_content = findView(R.id.myorder_content);
		allorder_price = findView(R.id.allorder_price);
		allorder_untprice = findView(R.id.allorder_untprice);
		allorder_num = findView(R.id.allorder_num);
		myorder_numcode = findView(R.id.myorder_numcode);
		myorder_win = findView(R.id.myorder_win);
		myorder_result = findView(R.id.myorder_result);
		return super.createView();
	}

	@Override
	public void bindView(AllOrder.ResultModel data_){
		super.bindView(data_ != null ? data_ : new AllOrder.ResultModel());
		myorder_time.setText(getString(R.string.myorder_time)+data.createTime);
		myorder_code.setText(getString(R.string.myorder_num)+data.orderNo);
		myorder_numcode.setText("Period No.: "+data.planNum);
		myorder_pay.setVisibility(View.VISIBLE);
		myorder_des.setVisibility(View.VISIBLE);
		myorder_win.setVisibility(View.GONE);
		myorder_result.setText("");
		myorder_result.setVisibility(View.GONE);
		if(data.orderStatus==0){
			myorder_state.setText(getString(R.string.myorder_close));
			myorder_state.setTextColor(Color.parseColor("#333333"));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setVisibility(View.GONE);
			myorder_result.setText(getString(R.string.allorder_resul_2));
			myorder_result.setVisibility(View.VISIBLE);
		}else if(data.orderStatus==1){
			myorder_state.setText(getString(R.string.myorder_dzf));
			myorder_state.setTextColor(Color.parseColor("#E2291F"));
			myorder_des.setVisibility(View.VISIBLE);
			myorder_des.setText("Points settlement");
			myorder_des.setBackgroundResource(R.drawable.radius_17_yellow_line_shap);
			myorder_des.setTextColor(Color.parseColor("#DD8C28"));
			if(count == null){
				count = new ToPayItemCount(context,myorder_result, data.invalidSecond*1000, 1000);
				count.start();
			}
			myorder_pay.setText("Cash settlement");
			myorder_result.setVisibility(View.VISIBLE);
		}else if(data.orderStatus==2){
			myorder_state.setText(getString(R.string.allorder_djx));
			myorder_state.setTextColor(Color.parseColor("#333333"));
			myorder_des.setVisibility(View.GONE);
			myorder_pay.setText(getString(R.string.allorder_wdhm));
			myorder_result.setVisibility(View.GONE);
		}else if(data.orderStatus==3){
			myorder_state.setText(getString(R.string.allorder_yjx));
			myorder_state.setTextColor(Color.parseColor("#333333"));
			myorder_pay.setText(getString(R.string.allorder_wdhm));
			myorder_des.setVisibility(View.GONE);
			if(data.winStatus>0) {
				myorder_win.setVisibility(View.VISIBLE);
				myorder_des.setVisibility(View.VISIBLE);
				myorder_des.setText(getString(R.string.allorder_ckjl));
				myorder_des.setBackgroundResource(R.drawable.radius_17_gray_line_shap);
				myorder_des.setTextColor(Color.parseColor("#333333"));
			}
		}else if(data.orderStatus==-1){
			myorder_state.setText(getString(R.string.allorder_ytk));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setVisibility(View.GONE);
			myorder_result.setText(data.refundRemark);
			myorder_result.setVisibility(View.VISIBLE);
		}
		myorder_content.removeAllViews();
		myOrderView =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.myorder_item_view, myorder_content,false);
		myorder_item_pic = myOrderView.findViewById(R.id.myorder_item_pic);
		myorder_item_title = myOrderView.findViewById(R.id.myorder_item_title);
		myorder_item_peizhi = myOrderView.findViewById(R.id.myorder_item_peizhi);
		myorder_item_num = myOrderView.findViewById(R.id.myorder_item_num);
		myOrderView.findViewById(R.id.myorder_item_price).setVisibility(View.GONE);
		myOrderView.findViewById(R.id.myorder_item_divider).setVisibility(View.GONE);
		if(data.mainImage!=null && data.mainImage.size()>0){
			GlideUtil.loadRound(context,data.mainImage.get(0),myorder_item_pic);
		}
		myorder_item_title.setText(data.goodsName);
		String spec = "";
		if(data.goodsSpec!=null && data.goodsSpec.size()>0){
			for (AllOrder.ResultModel.GoodsSpecModel goodsSpecModel:data.goodsSpec) {
				spec+=goodsSpecModel.attributionValName+" ";
			}
		}
		myorder_item_peizhi.setText(spec);
		myorder_item_num.setText(getString(R.string.shopcart_shuliang)+"*1");
		allorder_price.setText(StringUtil.changeF2Y(data.goodsPrice));
		allorder_untprice.setText(StringUtil.changeF2Y(data.lootPrice));
		allorder_num.setText(data.lootCount+"");
//		myorder_youfei.setText(getString(R.string.myorder_time)+data.orderStatus);

		if(data.paymentMethod !=null && data.paymentMethod==0){
			String point = data.orderPoint+" points";
			SpannableStringBuilder sb = new SpannableStringBuilder(point);
			int start = point.indexOf("points");
			int end =  point.length();
			sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,12)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			myorder_price.setText(sb);
		}else{
			myorder_price.setText(StringUtil.changeF2Y(data.orderAmount));
		}
		myorder_content.addView(myOrderView);
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myorder_pay:
			if(data.orderStatus==1){
				toActivity(PayActivity.createIntent(context,data.orderAmount,data.orderNo,"LOOT","third"));
			}else if(data.orderStatus==2 || data.orderStatus==3 || data.orderStatus==4){
				toActivity(MyNumberActivity.createIntent(context,data.orderNo));
			}
			break;
		case R.id.myorder_des:
			if(data.orderStatus==1){
				toActivity(BottomPayWindow.createIntent(context,data.orderAmount,data.orderNo,"LOOT",true,data.orderPoint));
			} else if(data.orderStatus==3){
				toActivity(WinningGoodsActivity.createIntent(context));
			}
			break;
		}
	}
}