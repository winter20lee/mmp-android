package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.PointsAndStatus;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.PayCodeCount;
import zuo.biao.library.R;
import zuo.biao.library.base.BaseBottomWindow;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

public class BottomPayWindow extends BaseBottomWindow implements AlertDialog.OnDialogButtonClickListener, PayCodeCount.FinishCallback, OnHttpResponseListener {
    private static final String TAG = "BottomPayWindow";
    private static final int REQUEST_POINTSANDSTATUS = 13002;
    /**启动BottomMenuWindow的Intent
     * @param context
     * @return
     */
    private int amount;
    private String bizOrderId;
    private String bizOrderType;
    private boolean isOrder;
    private int points;
    public static Intent createIntent(Context context,int amount,String bizOrderId,String bizOrderType,int points) {
        return new Intent(context, BottomPayWindow.class).
                putExtra(INTENT_ITEMS, amount)
                .putExtra(INTENT_ITEM_IDS, bizOrderId)
                .putExtra(INTENT_ITEM_TITLE, bizOrderType)
                .putExtra(RESULT_TITLE, points);
    }
    public static Intent createIntent(Context context,int amount,String bizOrderId,String bizOrderType,boolean isOrder,int points) {
        return new Intent(context, BottomPayWindow.class).
                putExtra(INTENT_ITEMS, amount)
                .putExtra(INTENT_ITEM_IDS, bizOrderId)
                .putExtra(INTENT_ITEM_TITLE, bizOrderType)
                .putExtra(INTENT_ITEM_TYPE, isOrder)
                .putExtra(RESULT_TITLE, points);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_pay_window);
        intent = getIntent();
        amount = intent.getIntExtra(INTENT_ITEMS,amount);
        bizOrderId = intent.getStringExtra(INTENT_ITEM_IDS);
        bizOrderType = intent.getStringExtra(INTENT_ITEM_TITLE);
        isOrder = intent.getBooleanExtra(INTENT_ITEM_TYPE,false);
        points = intent.getIntExtra(RESULT_TITLE,points);
        initView();
        initData();
        initEvent();
        HttpRequest.getPointsAndStatus(amount,REQUEST_POINTSANDSTATUS,new OnHttpResponseListenerImpl(this));
    }

    private ImageView ivBaseClose;
    private Button tvBottomMenuCancel;
    private TextView bottom_pay_price,bottom_pay_time,bottom_pay_remind_points;
    private PayCodeCount count;
    @Override
    public void initView() {//必须调用
        super.initView();
        bottom_pay_price = findView(R.id.bottom_pay_price);
        ivBaseClose = findView(R.id.ivBaseClose);
        tvBottomMenuCancel = findView(R.id.tvBottomMenuCancel);
        bottom_pay_time = findView(R.id.bottom_pay_time);
        bottom_pay_remind_points = findView(R.id.bottom_pay_remind_points);
    }


    @Override
    public void initData() {//必须调用
        super.initData();
        String price = points+" Points";
        SpannableStringBuilder sb = new SpannableStringBuilder(price);
        int start = price.indexOf("Points");
        int end = price.length();
        sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bottom_pay_price.setText(sb);
        count = new PayCodeCount(this,bottom_pay_time, 90000, 1000,this);
        count.start();
        bottom_pay_remind_points.setText(MApplication.getInstance().getPointsRules().points+"");
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivBaseClose.setOnClickListener(v -> {
            showConfirmDialog();
        });
        vBaseBottomWindowRoot.setOnTouchListener((v, event) -> {
            return true;
        });
        tvBottomMenuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PayActivity.createIntent(context,amount,bizOrderId,bizOrderType,"score"));
                finish();
            }
        });
    }

    private void showConfirmDialog(){
        new AlertDialog(context, "", getString(R.string.bottom_pay_dialog_title),  "Keep paying","Give up",0, this).show();
    }

    @Override
    protected void setResult() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {
        if (isPositive) {
            return;
        }
        switch (requestCode) {
            case 0:
                if(isOrder){
                    finish();
                }else{
                    toOrderActivity();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onFinish() {
        tvBottomMenuCancel.setOnClickListener(null);
        tvBottomMenuCancel.setBackgroundColor(Color.parseColor("#F5F5F5"));
        runThread(TAG + "initData", new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toOrderActivity();
            }
        });
    }

    private void toOrderActivity(){
        runUiThread(new Runnable() {
            @Override
            public void run() {
                toActivity(AllOrdersActivity.createIntent(BottomPayWindow.this));
                finish();
            }
        });
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        try {
            PointsAndStatus pointsAndStatus = GsonUtil.GsonToBean(resultData, PointsAndStatus.class);
            if(pointsAndStatus!=null){
                String price = pointsAndStatus.points+" Points";
                SpannableStringBuilder sb = new SpannableStringBuilder(price);
                int start = price.indexOf("Points");
                int end = price.length();
                sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                bottom_pay_price.setText(sb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showShortToast("Failed to obtain required points");
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e, String message) {
        showShortToast(message);
    }
}
