package zblibrary.zgl.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import zblibrary.zgl.R;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;

public class PayCodeCount extends CountDownTimer {

    private TextView button;
    private Context context;
    private FinishCallback finishCallback;
    /**
     * @param millisInFuture    The NUMBER of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public PayCodeCount(Context context,TextView btn, long millisInFuture, long countDownInterval,FinishCallback finishCallback) {
        super(millisInFuture, countDownInterval);
        this.button = btn;
        this.context = context;
        this.finishCallback = finishCallback;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //防止计时过程中重复点击
        String time = TimeUtil.getTime(millisUntilFinished);
        String timeShow = context.getString(R.string.bottom_time) +"\n"+ time;
        SpannableStringBuilder sb = new SpannableStringBuilder(timeShow);
        int start = timeShow.indexOf(time);
        int end = timeShow.length();
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(context,14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(sb);
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        button.setText(context.getString(R.string.bottom_time_over));
        button.setTextColor(Color.parseColor("#E2291F"));
        if(finishCallback!=null){
            finishCallback.onFinish();
        }
    }

    public interface FinishCallback{
        void onFinish();
    }
}
