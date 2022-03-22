
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
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.LootDes;
import zblibrary.zgl.model.PointsAndStatus;
import zblibrary.zgl.model.RequestLootOrderInfo;
import zblibrary.zgl.model.SubmitOrder;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 * 提交订单
 */
public class LootSubmitOrderActivity extends BaseActivity implements OnBottomDragListener , OnHttpResponseListener, View.OnClickListener {
	private static final int REQUEST_CODE_CREATE_ORDER = 13000;
	private static final int REQUEST_CODE_ORDER_TOKEN = 13001;
	private static final int REQUEST_POINTSANDSTATUS = 13002;
	private TextView submit_order_totle,submit_order_submit,submit_order_pieces;
	private LootDes lootDes;
	private ImageView loot_submit_order_item_pic;
	private TextView loot_submit_order_item_title;
	private TextView loot_submit_order_item_canshu;
	private TextView loot_submit_order_item_num;
	private TextView loot_submit_order_item_price_;
	private TextView loot_submit_order_item_sale;
	private TextView loot_submit_order_item_price;
	private TextView loot_submit_order_5,loot_submit_order_10,loot_submit_order_20,loot_submit_order_50,
			loot_submit_order_baowei,shop_cart_num,shop_cart_limit_num,submit_order_submit_points;
	private String token;
	private boolean isTail;
	private boolean isJifen;
	private PointsAndStatus pointsAndStatus;
	public static Intent createIntent(Context context, LootDes lootDes) {
		return new Intent(context, LootSubmitOrderActivity.class)
				.putExtra(INTENT_TITLE,lootDes);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loot_submit_order_activity, this);
		intent = getIntent();
		lootDes = (LootDes) intent.getSerializableExtra(INTENT_TITLE);
		initView();
		initData();
		initEvent();
		HttpRequest.getLootOrderToken(REQUEST_CODE_ORDER_TOKEN,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void initView() {//必须调用
		loot_submit_order_item_pic = findView(R.id.loot_submit_order_item_pic);
		loot_submit_order_item_title = findView(R.id.loot_submit_order_item_title);
		loot_submit_order_item_canshu = findView(R.id.loot_submit_order_item_canshu);
		loot_submit_order_item_num = findView(R.id.loot_submit_order_item_num);
		loot_submit_order_item_price_ = findView(R.id.loot_submit_order_item_price_);
		loot_submit_order_item_sale = findView(R.id.loot_submit_order_item_sale);
		loot_submit_order_item_price = findView(R.id.loot_submit_order_item_price);
		submit_order_totle = findView(R.id.submit_order_totle);
		submit_order_submit = findView(R.id.submit_order_submit);
		submit_order_pieces = findView(R.id.submit_order_pieces);
		shop_cart_limit_num = findView(R.id.shop_cart_limit_num);

		loot_submit_order_5 = findView(R.id.loot_submit_order_5,this);
		loot_submit_order_10 = findView(R.id.loot_submit_order_10,this);
		loot_submit_order_20 = findView(R.id.loot_submit_order_20,this);
		loot_submit_order_50 = findView(R.id.loot_submit_order_50,this);
		loot_submit_order_baowei = findView(R.id.loot_submit_order_baowei,this);
		submit_order_submit_points = findView(R.id.submit_order_submit_points,this);
		shop_cart_num = findView(R.id.shop_cart_num);
		findView(R.id.shop_cart_minus,this);
		findView(R.id.shop_cart_plus,this);
	}

	@Override
	public void initData() {//必须调用
		GlideUtil.loadRound(context,lootDes.detailsImage.get(0),loot_submit_order_item_pic);
		loot_submit_order_item_title.setText(lootDes.goodsName);
		String good_spe = "";
		if(lootDes.goodsSpecList!=null && lootDes.goodsSpecList.size()>0){
			for (LootDes.GoodsSpecListModel goodsSpecListModel:lootDes.goodsSpecList) {
				good_spe += goodsSpecListModel.name+" "+goodsSpecListModel.value+" ";
			}
		}
		String tv_good_spe_for = String.format(getString(R.string.first_good_spec), good_spe);
		loot_submit_order_item_canshu.setText(tv_good_spe_for);
		loot_submit_order_item_num.setText(getString(R.string.shopcart_shuliang)+lootDes.buyNumber);
		loot_submit_order_item_price_.setText( getString(R.string.loot_sub_spjz)+StringUtil.changeF2Y(lootDes.price));
		loot_submit_order_item_price.setText( StringUtil.changeF2Y(lootDes.unitPrice));
		loot_submit_order_item_sale.setText(getString(R.string.loot_details_ys)+lootDes.lootScheduleShow);
		String limit = String.format(getString(R.string.loot_sub_dcxg), lootDes.limitNumber);
		SpannableStringBuilder sb = new SpannableStringBuilder(limit);
		int start = limit.indexOf(lootDes.limitNumber+"");
		int end = limit.length();
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#DEA054")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		shop_cart_limit_num.setText(sb);
		String numShow = shop_cart_num.getText().toString();
		int price = lootDes.unitPrice*Integer.parseInt(numShow);
		HttpRequest.getPointsAndStatus(price,REQUEST_POINTSANDSTATUS,new OnHttpResponseListenerImpl(this));
	}


	@Override
	public void initEvent() {//必须调用
		submit_order_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int leaveTialNum = lootDes.allNumber - lootDes.lockNumber;
		switch (v.getId()){
			case R.id.submit_order_submit:
				isJifen = false;
				HttpRequest.createLootOrder(token,lootDes.unitPrice*Integer.parseInt(shop_cart_num.getText().toString()),getGoodInfo(),REQUEST_CODE_CREATE_ORDER,new OnHttpResponseListenerImpl(this));
				break;
			case R.id.submit_order_submit_points:
				isJifen = true;
				HttpRequest.createLootOrder(token,lootDes.unitPrice*Integer.parseInt(shop_cart_num.getText().toString()),getGoodInfo(),REQUEST_CODE_CREATE_ORDER,new OnHttpResponseListenerImpl(this));
				break;
			case R.id.loot_submit_order_5:
				if(lootDes.limitNumber<5){
					if(lootDes.limitNumber>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else{
						shop_cart_num.setText(lootDes.limitNumber+"");
					}
				}else{
					if(5>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else {
						shop_cart_num.setText(5+"");
					}
				}
				setBaoweiState(false);
				onClickStyle((TextView) v);
				break;
			case R.id.loot_submit_order_10:
				if(lootDes.limitNumber<10){
					if(lootDes.limitNumber>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else{
						shop_cart_num.setText(lootDes.limitNumber+"");
					}
				}else{
					if(10>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else {
						shop_cart_num.setText(10+"");
					}
				}
				setBaoweiState(false);
				onClickStyle((TextView) v);
				break;
			case R.id.loot_submit_order_20:
				if(lootDes.limitNumber<20){
					if(lootDes.limitNumber>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else{
						shop_cart_num.setText(lootDes.limitNumber+"");
					}
				}else{
					if(20>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else {
						shop_cart_num.setText(20+"");
					}
				}
				setBaoweiState(false);
				onClickStyle((TextView) v);
				break;
			case R.id.loot_submit_order_50:
				if(lootDes.limitNumber<50){
					if(lootDes.limitNumber>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else{
						shop_cart_num.setText(lootDes.limitNumber+"");
					}
				}else{
					if(50>leaveTialNum){
						shop_cart_num.setText(leaveTialNum+"");
					}else {
						shop_cart_num.setText(50+"");
					}
				}
				setBaoweiState(false);
				onClickStyle((TextView) v);
				break;
			case R.id.loot_submit_order_baowei:
				int canTailNum = (lootDes.tailNumber * lootDes.allNumber) /100;
				if( leaveTialNum < canTailNum ){
					setBaoweiState(true);
					shop_cart_num.setText(leaveTialNum+"");
				}
				break;
			case R.id.shop_cart_minus:
				int num = Integer.parseInt(shop_cart_num.getText().toString());
				if(num>0){
					num = num-1;
					shop_cart_num.setText(num+"");
					setBaoweiState(false);
				}
				break;
			case R.id.shop_cart_plus:
				num = Integer.parseInt(shop_cart_num.getText().toString());
				if(num<lootDes.limitNumber && num<leaveTialNum){
					num = num+1;
					shop_cart_num.setText(num+"");
					setBaoweiState(false);
				}
				break;
		}
	}


	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
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
					if(isJifen){
						toActivity(BottomPayWindow.createIntent(context,lootDes.unitPrice*Integer.parseInt(shop_cart_num.getText().toString()),submitOrder.orderNo,"LOOT",pointsAndStatus.points));
					}else {
						toActivity(PayActivity.createIntent(context,lootDes.unitPrice*Integer.parseInt(shop_cart_num.getText().toString()),submitOrder.orderNo,"LOOT","third"));
					}
					showShortToast("Order submitted successfully");

				}else{
					showShortToast(message);
				}
				break;
			case REQUEST_POINTSANDSTATUS:
				try {
					 pointsAndStatus = GsonUtil.GsonToBean(resultData, PointsAndStatus.class);
					if(pointsAndStatus!=null){
						changePrice();
					}
				} catch (Exception e) {
					e.printStackTrace();
					showShortToast("Failed to obtain required points");
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

	private void onClickStyle(TextView view){
		view.setBackgroundResource(R.drawable.radius_4_black_shap);
		view.setTextColor(Color.parseColor("#ffffff"));
		new Thread(() -> {
			try {
				Thread.sleep(300);  //线程休眠10秒执行
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runUiThread(() -> {
				view.setBackgroundResource(R.drawable.radius_4_line_shap);
				view.setTextColor(Color.parseColor("#333333"));
			});
		}).start();
	}

	private RequestLootOrderInfo getGoodInfo(){
			RequestLootOrderInfo requestOrderInfo = new RequestLootOrderInfo();
			requestOrderInfo.amount = lootDes.unitPrice*Integer.parseInt(shop_cart_num.getText().toString());
			requestOrderInfo.count = Integer.parseInt(shop_cart_num.getText().toString());
			requestOrderInfo.lootId = lootDes.lootId;
			requestOrderInfo.planId = lootDes.planeId;
			requestOrderInfo.price = lootDes.unitPrice;
			requestOrderInfo.tailFlag = isTail;
			return requestOrderInfo;
	}

	private void setBaoweiState(boolean isSele){
		isTail = isSele;
		if(isSele){
			loot_submit_order_baowei.setBackgroundResource(R.drawable.radius_4_black_shap);
			loot_submit_order_baowei.setTextColor(Color.parseColor("#ffffff"));
		}else{
			loot_submit_order_baowei.setBackgroundResource(R.drawable.radius_4_line_shap);
			loot_submit_order_baowei.setTextColor(Color.parseColor("#333333"));
		}
		String numShow = shop_cart_num.getText().toString();
		int price = lootDes.unitPrice*Integer.parseInt(numShow);
		HttpRequest.getPointsAndStatus(price,REQUEST_POINTSANDSTATUS,new OnHttpResponseListenerImpl(this));
	}

	private void changePrice(){
		String numShow = shop_cart_num.getText().toString();
		int price = lootDes.unitPrice*Integer.parseInt(numShow);
		String priceShow = StringUtil.changeF2Y(price);
		String text = String.format(getString(R.string.suborder_total), priceShow,pointsAndStatus.points +"");
		SpannableStringBuilder sb = new SpannableStringBuilder(text);
		int start = text.indexOf(priceShow);
		int end = start + priceShow.length();
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,16)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		int startPoint = text.indexOf("(");
		int endPoint = text.indexOf(")")+1;
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#DEA054")),startPoint,endPoint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,16)),startPoint,endPoint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		submit_order_totle.setText(sb);


		String textNum = String.format(getString(R.string.suborder_total_num), numShow);
		sb = new SpannableStringBuilder(textNum);
		start = textNum.indexOf(numShow);
		end = start + numShow.length();
		sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		submit_order_pieces.setText(sb);

		if(pointsAndStatus.status == 0){
			submit_order_submit_points.setBackgroundResource(R.drawable.radius_20_gray_shap);
			submit_order_submit_points.setTextColor(Color.parseColor("#5F5F5F"));
			submit_order_submit_points.setOnClickListener(null);
		}else{
			submit_order_submit_points.setBackgroundResource(R.drawable.radius_20_yellow_shap);
			submit_order_submit_points.setTextColor(Color.parseColor("#ffffff"));
			submit_order_submit_points.setOnClickListener(this);
		}
	}
}