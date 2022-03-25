
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.OrderDetailsOrderInfoAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.OrderDetailsOrderInfo;
import zblibrary.zgl.model.PayMent;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.model.WinningGoodsDetails;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.OrderDetailsViewItem;
import zblibrary.zgl.view.PayMentView;
import zblibrary.zgl.view.ReceivingAddressView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.ExpandListView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 获奖商品详情
 */
public class WinningGoodsDetailsActivity extends BaseActivity implements OnBottomDragListener ,  OnHttpResponseListener, View.OnClickListener {
    private static final int REQUEST_CODE_ORDER_DETAILS = 12001;
    private TextView submit_order_totle,submit_order_submit,submit_order_chakan,order_details_value,order_details_single_price,order_details_purchasing,order_details_total;
    private String orderNo;
    private WinningGoodsDetails winningGoodsDetails;
    private ExpandListView expandListViewOrder;
    private LinearLayout expandListViewGoods,order_details_hgpz;
    private OrderDetailsOrderInfoAdapter orderDetailsAdapterOrder;
    private ArrayList<OrderDetailsOrderInfo> orderDetailsOrderInfos = new ArrayList<>();
    private ImageView order_details_image;
    private LinearLayout submit_order_defult_address;
    public static Intent createIntent(Context context,String orderNo) {
        return new Intent(context, WinningGoodsDetailsActivity.class)
                .putExtra(INTENT_ID,orderNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winning_goods_details_activity, this);
        intent = getIntent();
        orderNo =  intent.getStringExtra(INTENT_ID);
        initView();
        initData();
        initEvent();
        HttpRequest.getOrderWinDetails(orderNo,REQUEST_CODE_ORDER_DETAILS,new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        submit_order_totle = findView(R.id.submit_order_totle);
        submit_order_submit = findView(R.id.submit_order_submit);
        expandListViewGoods = findView(R.id.order_details_listview);
        submit_order_chakan = findView(R.id.submit_order_chakan);
        expandListViewOrder = findView(R.id.order_details_order);
        order_details_value =findView(R.id.order_details_value);
        order_details_single_price =findView(R.id.order_details_single_price);
        order_details_purchasing =findView(R.id.order_details_purchasing);
        order_details_total = findView(R.id.order_details_total);
        order_details_hgpz = findView(R.id.order_details_hgpz);
        order_details_image = findView(R.id.order_details_image);
        submit_order_defult_address = findView(R.id.submit_order_defult_address);
        findView(R.id.kefu,this);
    }

    @Override
    public void initData() {//必须调用

        orderDetailsAdapterOrder = new OrderDetailsOrderInfoAdapter(this);
        expandListViewOrder.setAdapter(orderDetailsAdapterOrder);
    }


    @Override
    public void initEvent() {//必须调用
        submit_order_submit.setOnClickListener(this);
        submit_order_chakan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_order_chakan:
                if(winningGoodsDetails.winStatus==1){
                    toActivity(RewardinKindActivity.createIntent(context,orderNo));
                }
                break;
            case R.id.submit_order_submit:
                if(winningGoodsDetails.winStatus==1){
                }
                break;
            case R.id.kefu:
                if(StringUtil.isNotEmpty(MApplication.getInstance().getServiceUrl(),true)){
                    toActivity(WebViewActivity.createIntent(context,"Service",MApplication.getInstance().getServiceUrl()));
                }
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
        int orderState = winningGoodsDetails.winStatus;
        if(orderState == 1){
            ((TextView)findView(R.id.state)).setText(context.getString(R.string.winning_goods_dlq));
            submit_order_totle.setVisibility(View.GONE);
            submit_order_chakan.setVisibility(View.VISIBLE);
            submit_order_chakan.setText("Reward in kind");
            submit_order_submit.setText("Commodity repurchase");
        }else if(orderState == 2){
            ((TextView)findView(R.id.state)).setText(context.getString(R.string.myorder_dfh));
            submit_order_totle.setVisibility(View.GONE);
            submit_order_submit.setText(getString(R.string.myorder_txfh));
        }else if(orderState == 3){
            ((TextView)findView(R.id.state)).setText(context.getString(R.string.myorder_dsh));
            submit_order_chakan.setVisibility(View.VISIBLE);
            submit_order_submit.setText(getString(R.string.myorder_qrsh));
            submit_order_chakan.setText(getString(R.string.myorder_ckwl));
        }else if(orderState == 4 || orderState == 0){
            ((TextView)findView(R.id.state)).setText(context.getString(R.string.myorder_finish));
            submit_order_totle.setVisibility(View.GONE);
            submit_order_submit.setVisibility(View.GONE);
        }else if(orderState == 5){
            ((TextView)findView(R.id.state)).setText("Pending transfer");
            submit_order_totle.setVisibility(View.GONE);
            submit_order_submit.setVisibility(View.GONE);
        }
            if( orderState == 5){
                if(StringUtil.isNotEmpty(winningGoodsDetails.transferTime,true)){
                    order_details_hgpz.setVisibility(View.VISIBLE);
                    GlideUtil.load(this,winningGoodsDetails.purchaseImage,order_details_image);
                }
                PayMent payMent =new PayMent();
                payMent.paymentMethod = winningGoodsDetails.paymentMethod;
                payMent.receiverAccount = winningGoodsDetails.receiverAccount;
                payMent.receiverContact = winningGoodsDetails.receiverContact;
                payMent.receiverName = winningGoodsDetails.receiverName;
                PayMentView payMentView = new PayMentView(this,submit_order_defult_address);
                submit_order_defult_address.addView(payMentView.createView());
                payMentView.bindView(payMent);
            }else{
                ReceivingAddress.ResultModel resultModelAddress =new ReceivingAddress.ResultModel();
                resultModelAddress.address = winningGoodsDetails.receiverAddress;
                resultModelAddress.area = winningGoodsDetails.receiverArea;
                resultModelAddress.contact = winningGoodsDetails.receiverContact;
                resultModelAddress.realName = winningGoodsDetails.receiverName;
                ReceivingAddressView receivingAddressView = new ReceivingAddressView(this,submit_order_defult_address);
                receivingAddressView.goneEditButton();
                submit_order_defult_address.addView(receivingAddressView.createView());
                receivingAddressView.bindView(resultModelAddress);
            }
        OrderDetailsViewItem orderDetailsViewItem = new OrderDetailsViewItem(this,expandListViewGoods);
        expandListViewGoods.addView(orderDetailsViewItem.createView());
        orderDetailsViewItem.bindView(winningGoodsDetails.transData());
        orderDetailsViewItem.shopping_cart_item_price.setVisibility(View.GONE);


        order_details_value.setText(StringUtil.changeF2Y(winningGoodsDetails.goodsPrice));
        order_details_single_price.setText(StringUtil.changeF2Y(winningGoodsDetails.lootPrice));
        order_details_purchasing.setText(winningGoodsDetails.lootCount+"");
        order_details_total.setText(StringUtil.changeF2Y(winningGoodsDetails.orderAmount));

        getBottomList(orderState);
        orderDetailsAdapterOrder.refresh(orderDetailsOrderInfos);

        if(winningGoodsDetails.abnormalStatus==1){

        }
    }

    private void getBottomList(int orderState){
        switch (orderState){
            case 1:
                OrderDetailsOrderInfo orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.order_details_state);
                if( winningGoodsDetails.winStatus==1){
                    orderDetailsOrderInfo.name = getString(R.string.winning_goods_dlq);
                }else  if( winningGoodsDetails.winStatus==2){
                    orderDetailsOrderInfo.name = getString(R.string.myorder_dfh);
                }else  if( winningGoodsDetails.winStatus==3){
                    orderDetailsOrderInfo.name = getString(R.string.myorder_dsh);
                }else  if( winningGoodsDetails.winStatus==4){
                    orderDetailsOrderInfo.name = getString(R.string.myorder_finish);
                }else  if( winningGoodsDetails.winStatus==5){
                    orderDetailsOrderInfo.name = "Pending transfer";
                }

                orderDetailsOrderInfos.add(orderDetailsOrderInfo);

                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.loot_details_yijiexiao_time);
                orderDetailsOrderInfo.name = winningGoodsDetails.createTime;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);

                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.myorder_num);
                orderDetailsOrderInfo.name = winningGoodsDetails.orderNo;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);

                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.plan_win_no);
                orderDetailsOrderInfo.name = winningGoodsDetails.planNum+"";
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);

                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.winning_goods_xyhm);
                orderDetailsOrderInfo.name = winningGoodsDetails.luckNumber;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                break;
            case 2:
                getBottomList(1);
                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.winning_goods_ljsj);
                orderDetailsOrderInfo.name = winningGoodsDetails.pickTime;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                break;
            case 3:
                getBottomList(2);
                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.order_details_sendtime);
                orderDetailsOrderInfo.name = winningGoodsDetails.sendTime;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                break;
            case 4:
                getBottomList(3);
                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = getString(R.string.order_details_gettime);
                orderDetailsOrderInfo.name = winningGoodsDetails.finishTime;
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                break;
            case 5:
                getBottomList(2);
                orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                orderDetailsOrderInfo.title = "Buyback price";
                orderDetailsOrderInfo.name = StringUtil.changeF2Y(winningGoodsDetails.lootPurchasePrice);
                orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                if(StringUtil.isNotEmpty(winningGoodsDetails.transferTime,true)){
                    orderDetailsOrderInfo = new OrderDetailsOrderInfo();
                    orderDetailsOrderInfo.title = "Remittance time";
                    orderDetailsOrderInfo.name = winningGoodsDetails.transferTime;
                    orderDetailsOrderInfos.add(orderDetailsOrderInfo);
                }
                break;
        }
    }
}