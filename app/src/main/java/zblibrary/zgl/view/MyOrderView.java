package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.PayActivity;
import zblibrary.zgl.model.MyOrder;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class MyOrderView extends BaseView<MyOrder.ResultModel> implements OnClickListener  {

	public MyOrderView(Activity context, ViewGroup parent) {
		super(context, R.layout.myorder_view, parent);
	}
	private TextView myorder_time,myorder_youfei,myorder_item_title,myorder_item_price,myorder_count;
	private TextView myorder_code,myorder_price,myorder_item_peizhi;
	private TextView myorder_state,myorder_pay,myorder_des,myorder_item_num;
	private LinearLayout myorder_content;
	private RelativeLayout myOrderView;
	private ImageView myorder_item_pic;
	private ToPayItemCount count;
	private OnConfirmReceiverListener onConfirmReceiverListener;
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
		myorder_count = findView(R.id.myorder_count);
		return super.createView();
	}

	@Override
	public void bindView(MyOrder.ResultModel data_){
		super.bindView(data_ != null ? data_ : new MyOrder.ResultModel());
		myorder_time.setText(data.createTime);
		myorder_code.setText(data.orderNo);
		myorder_pay.setVisibility(View.VISIBLE);
		myorder_des.setVisibility(View.VISIBLE);
		myorder_price.setTextColor(Color.parseColor("#333333"));
		myorder_state.setTextColor(Color.parseColor("#333333"));
		if(data.orderStatus==0){
			myorder_state.setText(getString(R.string.myorder_close));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setVisibility(View.GONE);
		}else if(data.orderStatus==1){
			myorder_state.setText(getString(R.string.myorder_dzf));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setText("Pay now");
			myorder_count.setVisibility(View.VISIBLE);
			if(count == null){
				count = new ToPayItemCount(context,myorder_count, data.invalidSecond*1000, 1000);
				count.start();
			}
			myorder_price.setTextColor(Color.parseColor("#E4393C"));
			myorder_state.setTextColor(Color.parseColor("#E4393C"));
		}else if(data.orderStatus==2){
			myorder_state.setText(getString(R.string.myorder_dfh));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setText(getString(R.string.myorder_txfh));
		}else if(data.orderStatus==3){
			myorder_state.setText(getString(R.string.myorder_dsh));
			myorder_des.setText(getString(R.string.myorder_qrsh));
			myorder_pay.setText(getString(R.string.myorder_ckwl));
		}else if(data.orderStatus==4){
			myorder_state.setText(getString(R.string.myorder_finish));
			myorder_pay.setVisibility(View.GONE);
			myorder_des.setVisibility(View.GONE);
		}
//		myorder_youfei.setText(getString(R.string.myorder_time)+data.orderStatus);
		myorder_price.setText(StringUtil.changeF2Y(data.totalAmount));

		if(data.orderItemList==null || data.orderItemList.size()==0){
			return;
		}
		myorder_content.removeAllViews();
		for(int i=0;i<data.orderItemList.size();i++){
			myOrderView =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.myorder_item_view, myorder_content,false);
			myorder_item_pic = myOrderView.findViewById(R.id.myorder_item_pic);
			myorder_item_title = myOrderView.findViewById(R.id.myorder_item_title);
			myorder_item_peizhi = myOrderView.findViewById(R.id.myorder_item_peizhi);
			myorder_item_num = myOrderView.findViewById(R.id.myorder_item_num);
			myorder_item_price = myOrderView.findViewById(R.id.myorder_item_price);
			MyOrder.ResultModel.OrderItemListModel orderItemListModel = data.orderItemList.get(i);
			if(orderItemListModel!=null && orderItemListModel.mainImage!=null && orderItemListModel.mainImage.size()>0){
				GlideUtil.loadRound(context,orderItemListModel.mainImage.get(0),myorder_item_pic);
			}
			myorder_item_title.setText(orderItemListModel.goodsName);
			String spec = "";
			for (MyOrder.ResultModel.OrderItemListModel.GoodsSpecModel goodsSpecModel:orderItemListModel.goodsSpec) {
				spec+=goodsSpecModel.attributionValName+" ";
			}
			myorder_item_peizhi.setText(spec);
			myorder_item_num.setText(getString(R.string.shopcart_shuliang)+orderItemListModel.goodsCount);
			myorder_item_price.setText(StringUtil.changeF2Y(orderItemListModel.goodsPrice));
			myorder_content.addView(myOrderView);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myorder_pay:
			break;
		case R.id.myorder_des:
			if(data.orderStatus==1){
				toActivity(PayActivity.createIntent(context,data.totalAmount,data.orderNo,"MALL","third"));
			}else if(data.orderStatus==2){
				new AlertDialog(context, "", "Hello, we have received your reminder, the merchant will ship it as soon as possible",
						false, "OK, i already know",0, null).show();
			}else if(data.orderStatus==3){
				new AlertDialog(context, "", "Hello, please confirm receipt after the item has been received",
						true, 0, new AlertDialog.OnDialogButtonClickListener() {
					@Override
					public void onDialogButtonClick(int requestCode, boolean isPositive) {
						if(isPositive){
							HttpRequest.getReceiveConfirm(data.orderNo, 0, new OnHttpResponseListener() {
								@Override
								public void onHttpResponse(int requestCode, String resultJson, Exception e) {
									if(onConfirmReceiverListener !=null){
										onConfirmReceiverListener.onConfirmReceiver(data.orderNo);
									}
								}
							});
						}
					}
				}).show();
			}
			break;
		}
	}

	public void setOnConfirmReceiverListener(OnConfirmReceiverListener onConfirmReceiverListener){
		this.onConfirmReceiverListener = onConfirmReceiverListener;
	}

	public interface OnConfirmReceiverListener{
		void onConfirmReceiver(String orderNo);
	}
}