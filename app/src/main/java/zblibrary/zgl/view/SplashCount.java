package zblibrary.zgl.view;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.SplashActivity;
import zuo.biao.library.util.GlideUtil;

public class SplashCount extends CountDownTimer {

    private TextView textView;
    private ImageView imageView;
    private ArrayList<String> images;
    private Context context;
    public SplashCount(Context context,TextView btn,ImageView imageView ,ArrayList<String> images,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.textView = btn;
        this.imageView = imageView;
        this.images = images;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int time  = (int) (millisUntilFinished / 1000);
        textView.setText(time  + "s");
        if(time>=4){
            GlideUtil.loadNoLoading(context,images.get(0),imageView);
        } else if(time>=2){
            GlideUtil.loadNoLoading(context,images.get(1),imageView);
        }else{
            GlideUtil.loadNoLoading(context,images.get(2),imageView);
        }
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        textView.setText(0);
        context.startActivity(MainTabActivity.createIntent(context).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
