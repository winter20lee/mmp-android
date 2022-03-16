package zblibrary.zgl.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import zblibrary.zgl.R;
import zblibrary.zgl.model.RefreshOrdeStateEvent;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;

public class ToPayItemCount extends CountDownTimer {

    private TextView button;
    private Context context;
    private String content;

    /**
     * @param millisInFuture    The NUMBER of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public ToPayItemCount(Context context,TextView btn, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = btn;
        this.context = context;
    }

    public ToPayItemCount(Context context,TextView btn, long millisInFuture, long countDownInterval,String content) {
        super(millisInFuture, countDownInterval);
        this.button = btn;
        this.context = context;
        this.content = content;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //防止计时过程中重复点击
        String time = TimeUtil.getTime(millisUntilFinished,"min");
        String text;
        if(StringUtil.isNotEmpty(content,true)){
            text = String.format(content, time);
            SpannableStringBuilder sb = new SpannableStringBuilder(text);
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),text.indexOf(time),text.indexOf(time)+time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            button.setText(sb);
        }else{
            text = String.format(context.getString(R.string.allorder_topay_time), time);
            SpannableStringBuilder sb = new SpannableStringBuilder(text);
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E2291F")),text.indexOf(time),text.indexOf(time)+time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),text.indexOf(time),text.indexOf(time)+time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            button.setText(sb);
        }
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        String text = String.format(context.getString(R.string.allorder_topay_time), "0");
        button.setText(text);
        //设置可点击
        if(StringUtil.isNotEmpty(content,true)){
            EventBus.getDefault().post(new RefreshOrdeStateEvent());
        }
    }
}
