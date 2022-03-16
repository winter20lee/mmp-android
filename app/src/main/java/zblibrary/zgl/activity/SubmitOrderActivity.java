
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.ShoppingCartAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.model.RequestOrderInfo;
import zblibrary.zgl.model.ShoppingCart;
import zblibrary.zgl.model.SubmitOrder;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.ReceivingAddressView;
import zblibrary.zgl.view.ShoppingCartViewItem;
import zuo.biao.library.base.BaseRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 提交订单
 */
public class SubmitOrderActivity extends BaseRecyclerActivity<ShoppingCart.ResultModel, ShoppingCartViewItem, ShoppingCartAdapter>
		implements OnBottomDragListener , OnHttpResponseListener, View.OnClickListener {
	private static final int REQUEST_CODE_DEFULT_ADDRESS = 12000;
	private static final int REQUEST_CODE_ORDER_TOKEN = 12001;
	private static final int REQUEST_CODE_CREATE_ORDER = 12002;
	private ArrayList<ShoppingCart.ResultModel> resultModelList;
	private FrameLayout submit_order_defult_address;
	private TextView submit_order_totle,submit_order_submit,submit_order_pieces;
	private ReceivingAddress.ResultModel resultModelAddress;
	private ArrayList<RequestOrderInfo> goodsList = new ArrayList<>();
	private ArrayList<String> shoppingCarGoodsIdList = new ArrayList<>();
	private int totalPrice = 0;
	private int totalNum = 0;
	private ShoppingCart.ResultModel resultModel;
	private String token;
	public static Intent createIntent(Context context,ArrayList<ShoppingCart.ResultModel> resultModelList) {
		return new Intent(context, SubmitOrderActivity.class)
				.putExtra(INTENT_ID,(Serializable) resultModelList);
	}

	public static Intent createIntent(Context context,ShoppingCart.ResultModel resultMode) {
		return new Intent(context, SubmitOrderActivity.class)
				.putExtra(INTENT_TITLE,resultMode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_order_activity, this);
		EventBus.getDefault().register(this);
		intent = getIntent();
		resultModelList =  (ArrayList<ShoppingCart.ResultModel>) intent.getSerializableExtra(INTENT_ID);
		resultModel = (ShoppingCart.ResultModel) intent.getSerializableExtra(INTENT_TITLE);
		if(resultModelList == null){
			resultModelList = new ArrayList<>();
			resultModelList.add(resultModel);
		}
		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		submit_order_defult_address = findView(R.id.submit_order_defult_address);
		submit_order_totle = findView(R.id.submit_order_totle);
		submit_order_submit = findView(R.id.submit_order_submit);
		submit_order_pieces = findView(R.id.submit_order_pieces);
	}

	@Override
	public void setList(final List<ShoppingCart.ResultModel> list) {
		setList(new AdapterCallBack<ShoppingCartAdapter>() {

			@Override
			public ShoppingCartAdapter createAdapter() {
				return new ShoppingCartAdapter(context,null,true);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	@Override
	public void initData() {//必须调用
		super.initData();
		getGoodInfo();
		String totalPrice1 = StringUtil.changeF2Y(totalPrice);
		String text = "Total: "+totalPrice1;
		SpannableStringBuilder sb = new SpannableStringBuilder(text);
		int start = text.indexOf(totalPrice1);
		int end = text.length();
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,16)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		submit_order_totle.setText(sb);

		String numShow =  totalNum+"";
		String textNum = String.format(getString(R.string.suborder_total_num),numShow);
		sb = new SpannableStringBuilder(textNum);
		start = textNum.indexOf(numShow);
		end = start + numShow.length();
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		submit_order_pieces.setText(sb);

		HttpRequest.getOrderToken(REQUEST_CODE_ORDER_TOKEN,new OnHttpResponseListenerImpl(this));
		HttpRequest.getDefultAddress(REQUEST_CODE_DEFULT_ADDRESS,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void getListAsync(final int page) {
		onLoadSucceed(page, resultModelList);
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();
		submit_order_submit.setOnClickListener(this);
		submit_order_defult_address.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.submit_order_submit:
				if(resultModelAddress == null){
					showShortToast(getString(R.string.add_address_add_toast));
					return;
				}
				HttpRequest.createMallOrder(token,totalPrice+"",resultModelAddress.realName,
						resultModelAddress.contact,resultModelAddress.area,resultModelAddress.address,
						goodsList,shoppingCarGoodsIdList,REQUEST_CODE_CREATE_ORDER,new OnHttpResponseListenerImpl(this));
				break;
			case R.id.submit_order_defult_address:
				toActivity(ReceivingAddressActivity.createIntent(this,true));
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_CODE_DEFULT_ADDRESS:
				resultModelAddress = GsonUtil.GsonToBean(resultData,ReceivingAddress.ResultModel.class);
				getAddress();
				break;
			case REQUEST_CODE_ORDER_TOKEN:
				try {
					token = GsonUtil.GsonToken(resultData);
				} catch (Exception e) {
					e.printStackTrace();
					showShortToast("Failed to get token");
				}
				break;
			case REQUEST_CODE_CREATE_ORDER:
				SubmitOrder submitOrder  = GsonUtil.GsonToBean(resultData,SubmitOrder.class);
				if(resultCode == 1000 && submitOrder!=null && StringUtil.isNotEmpty(submitOrder.orderNo,true)){
					toActivity(PayActivity.createIntent(context,totalPrice,submitOrder.orderNo,"MALL","third"));
					showShortToast("Order submitted successfully");

				}else{
					showShortToast(message);
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

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getModelAddress(ReceivingAddress.ResultModel resultModelAddress) {
		this.resultModelAddress = resultModelAddress;
		getAddress();
	}


	private void getGoodInfo(){
		for (ShoppingCart.ResultModel resultModel:resultModelList) {
			RequestOrderInfo requestOrderInfo = new RequestOrderInfo();
			requestOrderInfo.amount = (resultModel.goodsPrice*resultModel.goodsCount)+"";
			requestOrderInfo.count = resultModel.goodsCount+"";
			requestOrderInfo.goodsId = resultModel.goodsId+"";
			requestOrderInfo.skuId = resultModel.skuId+"";
			requestOrderInfo.price = resultModel.goodsPrice+"";
			goodsList.add(requestOrderInfo);

			totalPrice+=resultModel.goodsPrice*resultModel.goodsCount;
			totalNum+=resultModel.goodsCount;
			shoppingCarGoodsIdList.add(resultModel.id);
		}
	}

	private void getAddress(){
		if(resultModelAddress == null){
			submit_order_submit.setOnClickListener(null);
			submit_order_submit.setBackgroundResource(R.drawable.radius_20_gray_shap);
			submit_order_submit.setTextColor(Color.parseColor("#5F5F5F"));
			return;
		}
		submit_order_submit.setOnClickListener(this);
		submit_order_submit.setBackgroundResource(R.drawable.submit_order_button_shap);
		submit_order_submit.setTextColor(Color.parseColor("#FFFFFF"));
		submit_order_defult_address.removeAllViews();
		ReceivingAddressView receivingAddressView = new ReceivingAddressView(this,submit_order_defult_address,true);
		submit_order_defult_address.addView(receivingAddressView.createView());
		receivingAddressView.bindView(resultModelAddress);
		receivingAddressView.goneEditButton();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}