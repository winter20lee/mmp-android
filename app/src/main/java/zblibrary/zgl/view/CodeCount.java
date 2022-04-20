package zblibrary.zgl.view;

import android.os.CountDownTimer;
import android.widget.TextView;

public class CodeCount extends CountDownTimer {

    private TextView button;

    /**
     * @param millisInFuture    The NUMBER of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CodeCount(TextView btn, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //防止计时过程中重复点击
        button.setClickable(false);
        button.setText("" + millisUntilFinished / 1000 + " s后重新获取");
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        button.setText("重新获取验证码");
        //设置可点击
        button.setClickable(true);
    }
}
