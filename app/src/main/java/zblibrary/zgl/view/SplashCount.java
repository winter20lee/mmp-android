package zblibrary.zgl.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.SplashActivity;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.util.GlideUtil;

public class SplashCount extends CountDownTimer {

    private TextView textView;
    private Activity context;
    public SplashCount(Activity context, TextView btn, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.textView = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int time  = (int) (millisUntilFinished / 1000);
        textView.setText(time  + "s 跳过");
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
//        textView.setText(0);
        context.startActivity(MainTabActivity.createIntent(context).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        context.finish();
    }

}
