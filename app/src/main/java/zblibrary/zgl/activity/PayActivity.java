
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import zblibrary.zgl.R;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.Pay;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 支付
 */
public class PayActivity extends BaseActivity implements OnBottomDragListener , OnHttpResponseListener, View.OnClickListener {
	private static final int REQUEST_CODE_PAY= 12000;
	private static final int REQUEST_CODE_RESULT= 12001;
	private static final int REQUEST_GET_POINTS= 12002;
	private int amount;
	private String bizOrderId;
	private String bizOrderType;
	private String paymentMethod;
	private ImageView pay_img;
	private TextView pay_text;
	private TextView pay_des;
	private TextView pay_order;
	private int webResultCode;
	public static Intent createIntent(Context context, int amount,String bizOrderId,String bizOrderType,String paymentMethod) {
		return new Intent(context, PayActivity.class)
				.putExtra(INTENT_TITLE, amount)
				.putExtra(INTENT_ID, bizOrderId)
				.putExtra(INTENT_TYPE, bizOrderType)
				.putExtra(INTENT_PHONE,paymentMethod);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_activity, this);
		intent = getIntent();
		amount = intent.getIntExtra(INTENT_TITLE,amount);
		bizOrderId = intent.getStringExtra(INTENT_ID);
		bizOrderType = intent.getStringExtra(INTENT_TYPE);
		paymentMethod = intent.getStringExtra(INTENT_PHONE);
		initView();
		initData();
		initEvent();
		HttpRequest.createPay(amount,bizOrderId,bizOrderType,paymentMethod,REQUEST_CODE_PAY,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void initView() {//必须调用
		pay_img = findView(R.id.pay_img);
		pay_text = findView(R.id.pay_text);
		pay_des = findView(R.id.pay_des);
		pay_order = findView(R.id.pay_order,this);
	}


	@Override
	public void initData() {//必须调用
	}


	@Override
	public void initEvent() {//必须调用
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.pay_order:
				if(bizOrderType.equals("LOOT")){
					toActivity(AllOrdersActivity.createIntent(context));
				}else{
					toActivity(MyOrderActivity.createIntent(context,0));
				}
				break;
		}
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			return;
		}
		finish();
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		try {
			switch (requestCode){
				case REQUEST_CODE_PAY:
						Pay pay = GsonUtil.GsonToBean(resultData,Pay.class);
						if(paymentMethod.equals("score") && StringUtil.isNotEmpty(pay.status,true)){
							if(pay.status.equals("succeeded")){
								webResultCode = WebViewActivity.PAY_SUCCESS;
								setResultState();
								pay_des.setText("");
							}else if(pay.status.equals("faild")){
								webResultCode = WebViewActivity.PAY_FAIL;
								setResultState();
							}
						}else{
							toActivity(WebViewActivity.createIntent(context,"Pay",pay.checkoutUrl),REQUEST_CODE_RESULT);
						}

					break;
				case REQUEST_GET_POINTS:
					ArrayList<Customize> customizes = (ArrayList<Customize>) GsonUtil.jsonToList(resultData, Customize.class);
					if(customizes!=null && customizes.size()>0 && StringUtil.isNotEmpty(customizes.get(0).getValue(),true)){
						pay_des.setText("Get "+customizes.get(0).getValue()+" points, the points can be used to pay for the treasure hunt order.");
					}
					break;
			}
		}catch (Exception e){
			showShortToast(message);
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
		webResultCode = WebViewActivity.PAY_FAIL;
		setResultState();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case REQUEST_CODE_RESULT:
				webResultCode = data.getIntExtra(WebViewActivity.RESULT_TYPE,0);
				setResultState();
				break;
		}
	}

	private void setResultState(){
		if(webResultCode == WebViewActivity.PAY_SUCCESS){
			showShortToast("Successful payment");
			pay_img.setImageResource(R.mipmap.pay_success);
			pay_text.setText("Successful payment");
			pay_des.setText("");
			pay_order.setVisibility(View.VISIBLE);

			if(bizOrderType.equals("LOOT")){
				HttpRequest.getUserPoints(REQUEST_GET_POINTS,new OnHttpResponseListenerImpl(this));
			}

		}else if(webResultCode == WebViewActivity.PAY_FAIL){
			showShortToast("Failed to pay");
			pay_img.setImageResource(R.mipmap.pay_fail);
			pay_text.setText("Failed to pay");
			pay_des.setText("");
			pay_order.setVisibility(View.VISIBLE);
		}
	}
}