package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import zblibrary.zgl.activity.LootDetailsActivity;
import zblibrary.zgl.model.WinningGoods;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class WinningGoodsView extends BaseView<WinningGoods.ResultModel> implements OnClickListener {

	public WinningGoodsView(Activity context, ViewGroup parent) {
		super(context, R.layout.winning_goods_view, parent);
	}

	private TextView myorder_time,myorder_youfei,myorder_item_title,myorder_numcode;
	private TextView myorder_code,myorder_price,myorder_item_peizhi,myorder_wuliu;
	private TextView myorder_state,myorder_pay,myorder_des,myorder_item_num,allorder_price,allorder_untprice,allorder_num;
	private LinearLayout myorder_content;
	private RelativeLayout myOrderView;
	private ImageView myorder_item_pic;
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
		myorder_wuliu = findView(R.id.myorder_wuliu);
		return super.createView();
	}

	@Override
	public void bindView(WinningGoods.ResultModel data_){
		super.bindView(data_ != null ? data_ : new WinningGoods.ResultModel());

		myorder_time.setText(getString(R.string.myorder_time)+data.createTime);
		myorder_code.setText(getString(R.string.myorder_num)+data.orderNo);
		myorder_numcode.setText(getString(R.string.plan_win_no)+data.planNum);
		myorder_pay.setVisibility(View.VISIBLE);
		myorder_des.setVisibility(View.VISIBLE);
		myorder_wuliu.setVisibility(View.VISIBLE);
	 	if(data.winStatus==1){
			myorder_state.setText(getString(R.string.winning_goods_dlq));
			myorder_des.setVisibility(View.GONE);
			myorder_wuliu.setVisibility(View.GONE);
			myorder_pay.setText(getString(R.string.winning_goods_zxdb));
		}else if(data.winStatus==2){
			myorder_state.setText(getString(R.string.myorder_dfh));
			myorder_wuliu.setVisibility(View.GONE);
			myorder_pay.setText(getString(R.string.winning_goods_zxdb));
			myorder_des.setText(getString(R.string.winning_goods_txfh));
		}else if(data.winStatus==3){
			myorder_state.setText(getString(R.string.myorder_dsh));
			myorder_pay.setText(getString(R.string.winning_goods_zxdb));
			myorder_des.setText(getString(R.string.myorder_qrsh));
			myorder_wuliu.setText(getString(R.string.myorder_ckwl));
		}else if(data.winStatus==4){
			myorder_state.setText(getString(R.string.myorder_finish));
			myorder_des.setVisibility(View.GONE);
			myorder_wuliu.setVisibility(View.GONE);
			myorder_pay.setText(getString(R.string.winning_goods_zxdb));
		} else if(data.winStatus==5){
			myorder_state.setText("Pending transfer");
			myorder_des.setVisibility(View.GONE);
			myorder_wuliu.setVisibility(View.GONE);
			myorder_pay.setText(getString(R.string.winning_goods_zxdb));
		}
		myorder_content.removeAllViews();
		myOrderView =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.myorder_item_view, myorder_content,false);
		myorder_item_pic = myOrderView.findViewById(R.id.myorder_item_pic);
		myorder_item_title = myOrderView.findViewById(R.id.myorder_item_title);
		myorder_item_peizhi = myOrderView.findViewById(R.id.myorder_item_peizhi);
		myorder_item_num = myOrderView.findViewById(R.id.myorder_item_num);
		myOrderView.findViewById(R.id.myorder_item_price).setVisibility(View.GONE);
		myOrderView.findViewById(R.id.myorder_item_divider).setVisibility(View.GONE);
		if(data.skuImage!=null && data.skuImage.size()>0){
			GlideUtil.loadRound(context,data.skuImage.get(0),myorder_item_pic);
		}
		myorder_item_title.setText(data.goodsName);
		String spec = "";
		if(data.goodsSpec!=null && data.goodsSpec.size()>0){
			for (WinningGoods.ResultModel.GoodsSpecModel goodsSpecModel:data.goodsSpec) {
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
				if(data.winStatus==1){
					toActivity(LootDetailsActivity.createIntent(context,data.lootId));
				}
				break;
			case R.id.myorder_des:
				break;
			case R.id.myorder_wuliu:
				break;
		}
	}
}