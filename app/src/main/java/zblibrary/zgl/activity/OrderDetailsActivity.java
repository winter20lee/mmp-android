
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.OrderDetailsAdapter;
import zblibrary.zgl.adapter.OrderDetailsOrderInfoAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.OrderDetails;
import zblibrary.zgl.model.OrderDetailsOrderInfo;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.model.RefreshOrdeStateEvent;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.ReceivingAddressView;
import zblibrary.zgl.view.ToPayItemCount;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.ExpandListView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity implements OnBottomDragListener ,  OnHttpResponseListener, View.OnClickListener {
	private static final int REQUEST_CODE_DEFULT_ADDRESS = 12000;
	private static final int REQUEST_CODE_ORDER_DETAILS = 12001;
	private LinearLayout submit_order_defult_address;
	private TextView submit_order_totle,submit_order_submit,submit_order_chakan,order_details_state,order_details_total;
	private ReceivingAddress.ResultModel resultModelAddress;
	private int totalPrice = 0;
	private String orderNo;
	private OrderDetails orderDetails;
	private int orderState;
	private ExpandListView expandListViewGoods,expandListViewOrder;
	private OrderDetailsAdapter orderDetailsAdapter;
	private OrderDetailsOrderInfoAdapter orderDetailsAdapterOrder;
	private ArrayList<OrderDetailsOrderInfo> orderDetailsOrderInfos = new ArrayList<>();
	private ToPayItemCount count;
	public static Intent createIntent(Context context,String orderNo,int orderState) {
		return new Intent(context, OrderDetailsActivity.class)
				.putExtra(INTENT_ID,orderNo)
				.putExtra(INTENT_TITLE,orderState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.order_details_activity, this);
		intent = getIntent();
		orderNo =  intent.getStringExtra(INTENT_ID);
		orderState =  intent.getIntExtra(INTENT_TITLE,0);
		initView();
		initData();
		initEvent();
		HttpRequest.getDefultAddress(REQUEST_CODE_DEFULT_ADDRESS,new OnHttpResponseListenerImpl(this));
		HttpRequest.getOrderDetails(orderNo,REQUEST_CODE_ORDER_DETAILS,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void initView() {//必须调用
		submit_order_defult_address = findView(R.id.submit_order_defult_address);
		submit_order_totle = findView(R.id.submit_order_totle);
		submit_order_submit = findView(R.id.submit_order_submit);
		expandListViewGoods = findView(R.id.order_details_listview);
		submit_order_chakan = findView(R.id.submit_order_chakan);
		expandListViewOrder = findView(R.id.order_details_order);
		order_details_state = findView(R.id.order_details_state);
		order_details_total = findView(R.id.order_details_total);
		findView(R.id.kefu,this);
		if(orderState == 1){
			order_details_state.setText(context.getString(R.string.myorder_dzf));
			submit_order_totle.setVisibility(View.VISIBLE);
			submit_order_submit.setText(getString(R.string.myorder_ljfk));
		} else if(orderState == 2){
			order_details_state.setText(context.getString(R.string.myorder_dfh));
			submit_order_totle.setVisibility(View.GONE);
			submit_order_submit.setText(getString(R.string.myorder_txfh));
		}else if(orderState == 3){
			order_details_state.setText(context.getString(R.string.myorder_dsh));
			submit_order_chakan.setVisibility(View.VISIBLE);
			submit_order_totle.setVisibility(View.GONE);
			submit_order_submit.setText(getString(R.string.myorder_qrsh));
			submit_order_chakan.setText(getString(R.string.myorder_ckwl));
		}else if(orderState == 4 ){
			order_details_state.setText(context.getString(R.string.myorder_finish));
			submit_order_totle.setVisibility(View.GONE);
			submit_order_submit.setVisibility(View.GONE);
		}  else if(orderState == 0){
			order_details_state.setText(context.getString(R.string.myorder_close));
			submit_order_totle.setVisibility(View.GONE);
			submit_order_submit.setVisibility(View.GONE);
		}
	}

	@Override
	public void initData() {//必须调用
		orderDetailsAdapter = new OrderDetailsAdapter(this);
		expandListViewGoods.setAdapter(orderDetailsAdapter);

		orderDetailsAdapterOrder = new OrderDetailsOrderInfoAdapter(this);
		expandListViewOrder.setAdapter(orderDetailsAdapterOrder);
	}


	@Override
	public void initEvent() {//必须调用
		submit_order_submit.setOnClickListener(this);

		orderDetailsAdapter.setOnItemClickListener((parent, view, position, id) -> {
			toActivity(PlayVideoDetailsActivity.createIntent(context,id));
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.submit_order_submit:
				if(orderState==1){
					toActivity(PayActivity.createIntent(context,Integer.parseInt(orderDetails.totalAmount),orderNo,"MALL","third"));
				}else if(orderState == 2){
					new AlertDialog(context, "", "Hello, we have received your reminder, the merchant will ship it as soon as possible",
							false, "OK, i already know",0, null).show();
				}
				break;
			case R.id.kefu:
				if(StringUtil.isNotEmpty(MApplication.getInstance().getServiceUrl(),true)){
					toActivity(WebViewActivity.createIntent(context,"Service",MApplication.getInstance().getServiceUrl()));
				}
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRefreshOrdeStateEvent(RefreshOrdeStateEvent refreshOrdeStateEvent){
		HttpRequest.getOrderDetails(orderNo,REQUEST_CODE_ORDER_DETAILS,new OnHttpResponseListenerImpl(this));
	}


	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_CODE_DEFULT_ADDRESS:
				resultModelAddress = GsonUtil.GsonToBean(resultData,ReceivingAddress.ResultModel.class);
				ReceivingAddressView receivingAddressView = new ReceivingAddressView(this,submit_order_defult_address);
				receivingAddressView.goneEditButton();
				submit_order_defult_address.addView(receivingAddressView.createView());
				receivingAddressView.bindView(resultModelAddress);
				break;
			case REQUEST_CODE_ORDER_DETAILS:
				if(StringUtil.isNotEmpty(resultData,true)){
					orderDetails = GsonUtil.GsonToBean(resultData,OrderDetails.class);
					getGoodInfo();
				}
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	private void getGoodInfo(){
		if(orderDetails!=null && orderDetails.orderItemList!=null && orderDetails.orderItemList.size()>0){
			for (OrderDetails.OrderItemListModel resultModel:orderDetails.orderItemList) {
				totalPrice+=resultModel.goodsPrice;
			}
		}

		getBottomList(orderState);
		order_details_total.setText(StringUtil.changeF2Y(totalPrice));
		orderDetailsAdapter.refresh(orderDetails.orderItemList);
		orderDetailsAdapterOrder.refresh(orderDetailsOrderInfos);
	}

	private void getBottomList(int orderState){
		switch (orderState){
			case 1:
				OrderDetailsOrderInfo orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.order_details_state);
				orderDetailsOrderInfo.name = getString(R.string.myorder_dzf);
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);

				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.myorder_time);
				orderDetailsOrderInfo.name = orderDetails.createTime;
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);

				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.myorder_num);
				orderDetailsOrderInfo.name = orderDetails.orderNo;
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);

				if(submit_order_totle.getVisibility() == View.VISIBLE){
					if(count == null){
						count = new ToPayItemCount(context,submit_order_totle, orderDetails.invalidSecond*1000, 1000,getString(R.string.allorder_topay_time_des));
						count.start();
					}
				}
				break;
			case 2:
				getBottomList(1);
				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.order_details_paytime);
				orderDetailsOrderInfo.name = orderDetails.paymentTime;
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);
				break;
			case 3:
				getBottomList(2);
				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.order_details_sendtime);
				orderDetailsOrderInfo.name = orderDetails.sendTime;
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);
				break;
			case 4:
				getBottomList(3);
				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.order_details_gettime);
				orderDetailsOrderInfo.name = orderDetails.receiveTime;
				orderDetailsOrderInfos.add(orderDetailsOrderInfo);
				break;
			case 0:
				getBottomList(1);
				orderDetailsOrderInfo = new OrderDetailsOrderInfo();
				orderDetailsOrderInfo.title = getString(R.string.order_details_close);
				orderDetailsOrderInfo.name = getString(R.string.order_details_timeout);
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}