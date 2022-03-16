
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.WinningGoodsDetails;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.OrderDetailsViewItem;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 回购申请
 */
public class RepurchaseActivity extends BaseActivity implements OnBottomDragListener ,  OnHttpResponseListener, View.OnClickListener {
    private static final int REQUEST_CODE_ORDER_DETAILS = 12001;
    private static final int REQUEST_CODE_PURCHASE = 12002;
    private TextView submit_order_chakan,order_details_value,order_details_buy_back_price,repurchase_radio;
    private String orderNo;
    private WinningGoodsDetails winningGoodsDetails;
    private LinearLayout expandListViewGoods;
    private EditText repurchase_payee,repurchase_contact_number,repurchase_contact_receiving;
    public static Intent createIntent(Context context,String orderNo) {
        return new Intent(context, RepurchaseActivity.class)
                .putExtra(INTENT_ID,orderNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repurchase_activity, this);
        intent = getIntent();
        orderNo =  intent.getStringExtra(INTENT_ID);
        initView();
        initData();
        initEvent();
        HttpRequest.getOrderWinDetails(orderNo,REQUEST_CODE_ORDER_DETAILS,new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        expandListViewGoods = findView(R.id.order_details_listview);
        submit_order_chakan = findView(R.id.submit_order_chakan);
        order_details_value =findView(R.id.order_details_value);
        repurchase_payee = findView(R.id.repurchase_payee);
        repurchase_contact_number = findView(R.id.repurchase_contact_number);
        repurchase_contact_receiving = findView(R.id.repurchase_contact_receiving);
        order_details_buy_back_price = findView(R.id.order_details_buy_back_price);
        repurchase_radio = findView(R.id.repurchase_radio);
    }

    @Override
    public void initData() {//必须调用
    }


    @Override
    public void initEvent() {//必须调用
        submit_order_chakan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_order_chakan:
                String repurchasePayee = repurchase_payee.getText().toString();
                if(StringUtil.isEmpty(repurchasePayee)){
                    showShortToast("Payee is not empty");
                    return;
                }
                String contactNumber = repurchase_contact_number.getText().toString();
                if(StringUtil.isEmpty(contactNumber)){
                    showShortToast("Contact number is not empty");
                    return;
                }
                String receivingAccount = repurchase_contact_receiving.getText().toString();
                if(StringUtil.isEmpty(receivingAccount)){
                    showShortToast("Receiving Account number is not empty");
                    return;
                }
                HttpRequest.postPurchase(orderNo,repurchasePayee,contactNumber,receivingAccount,"1",winningGoodsDetails.lootPurchasePercent,REQUEST_CODE_PURCHASE,new OnHttpResponseListenerImpl(this));
                break;
        }
    }


    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        switch (requestCode){
            case REQUEST_CODE_ORDER_DETAILS:
                if(StringUtil.isNotEmpty(resultData,true)){
                    winningGoodsDetails = GsonUtil.GsonToBean(resultData,WinningGoodsDetails.class);
                    getGoodInfo();
                }
                break;
            case REQUEST_CODE_PURCHASE:
                toActivity(WinningGoodsActivity.createIntent(this));
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
        OrderDetailsViewItem orderDetailsViewItem = new OrderDetailsViewItem(this,expandListViewGoods);
        expandListViewGoods.addView(orderDetailsViewItem.createView());
        orderDetailsViewItem.bindView(winningGoodsDetails.transData());
        orderDetailsViewItem.shopping_cart_item_price.setVisibility(View.GONE);
        order_details_value.setText(StringUtil.changeF2Y(winningGoodsDetails.goodsPrice));
        order_details_buy_back_price.setText(StringUtil.changeF2Y((winningGoodsDetails.goodsPrice * winningGoodsDetails.lootPurchasePercent)/100));
        String repurchase = String.format(context.getString(R.string.repurchase), 100-winningGoodsDetails.lootPurchasePercent);
        repurchase_radio.setText(repurchase);
    }
}