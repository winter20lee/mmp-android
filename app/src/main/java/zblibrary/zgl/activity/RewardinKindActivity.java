
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.model.WinningGoodsDetails;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.OrderDetailsViewItem;
import zblibrary.zgl.view.ReceivingAddressView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 实物领奖
 */
public class RewardinKindActivity extends BaseActivity implements OnBottomDragListener ,  OnHttpResponseListener, View.OnClickListener {
    private static final int REQUEST_CODE_DEFULT_ADDRESS = 12000;
    private static final int REQUEST_CODE_ORDER_DETAILS = 12001;
    private LinearLayout submit_order_defult_address;
    private TextView submit_order_chakan,order_details_value;
    private ReceivingAddress.ResultModel resultModelAddress;
    private String orderNo;
    private WinningGoodsDetails winningGoodsDetails;
    private LinearLayout expandListViewGoods;
    public static Intent createIntent(Context context,String orderNo) {
        return new Intent(context, RewardinKindActivity.class)
                .putExtra(INTENT_ID,orderNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_in_kind_activity, this);
        intent = getIntent();
        orderNo =  intent.getStringExtra(INTENT_ID);
        initView();
        initData();
        initEvent();
        HttpRequest.getDefultAddress(REQUEST_CODE_DEFULT_ADDRESS,new OnHttpResponseListenerImpl(this));
        HttpRequest.getOrderWinDetails(orderNo,REQUEST_CODE_ORDER_DETAILS,new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        submit_order_defult_address = findView(R.id.submit_order_defult_address);
        expandListViewGoods = findView(R.id.order_details_listview);
        submit_order_chakan = findView(R.id.submit_order_chakan);
        order_details_value =findView(R.id.order_details_value);
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
                break;
        }
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
                    winningGoodsDetails = GsonUtil.GsonToBean(resultData,WinningGoodsDetails.class);
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
        OrderDetailsViewItem orderDetailsViewItem = new OrderDetailsViewItem(this,expandListViewGoods);
        expandListViewGoods.addView(orderDetailsViewItem.createView());
        orderDetailsViewItem.bindView(winningGoodsDetails.transData());
        orderDetailsViewItem.shopping_cart_item_price.setVisibility(View.GONE);
        order_details_value.setText(StringUtil.changeF2Y(winningGoodsDetails.lootPrice));
    }
}