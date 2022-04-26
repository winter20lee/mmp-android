
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;
import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.manager.DataManager;
import zblibrary.zgl.model.LoginEvent;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.base.BaseEvent;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.CleanDataUtils;


/**
 * 设置
 */
public class SetActivity extends BaseActivity implements OnBottomDragListener, View.OnClickListener {
    private TextView set_clear_text;
    private ImageView set_autoplay_iv;
    public static Intent createIntent(Context context) {
        return new Intent(context, SetActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity, this);
        initView();
        initData();
        initEvent();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {//必须调用
        set_clear_text = findView(R.id.set_clear_text);
        set_autoplay_iv = findView(R.id.set_autoplay_iv);
    }

    @Override
    public void initData() {//必须调用
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(this));
            set_clear_text.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isAutoPlay = DataManager.getInstance().getAutoPlayState();
        set_autoplay_iv.setImageResource(isAutoPlay == true ? R.mipmap.auto_play_open:R.mipmap.auto_play_close);
        if(MApplication.getInstance().isBindUserPhone()){
            findView(R.id.set_login).setVisibility(View.VISIBLE);
        }
        if(MApplication.getInstance().getCurrentUserPush()==0){
            ((TextView)findView(R.id.set_push_state)).setText("已关闭");
        }else{
            ((TextView)findView(R.id.set_push_state)).setText("已开启");
        }
    }


    @Override
    public void initEvent() {//必须调用
        findView(R.id.set_info,this);
        findView(R.id.set_phone,this);
        findView(R.id.set_autoplay,this);
        findView(R.id.set_push,this);
        findView(R.id.set_clear,this);
        findView(R.id.set_login,this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent baseEvent){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_info:
                toActivity(UserInfoActivity.createIntent(this));
                break;
            case R.id.set_phone:
                toActivity(BingPhoneActivity.createIntent(this));
                break;
            case R.id.set_autoplay:
                boolean isAutoPlay = DataManager.getInstance().getAutoPlayState();
                if(isAutoPlay){
                    DataManager.getInstance().saveAutoPlayState(false);
                    set_autoplay_iv.setImageResource(R.mipmap.auto_play_close);
                }else{
                    DataManager.getInstance().saveAutoPlayState(true);
                    set_autoplay_iv.setImageResource(R.mipmap.auto_play_open);
                }
                break;
            case R.id.set_push:
                if(MApplication.getInstance().getCurrentUserPush()==0){
                    new AlertDialog(this,"","确定开启消息通知？","确定",
                            "取消",0,new AlertDialog.OnDialogButtonClickListener(){

                        @Override
                        public void onDialogButtonClick(int requestCode, boolean isPositive) {
                            if(isPositive){
                                MApplication.getInstance().setCurrentUserPush(1);
                                ((TextView)findView(R.id.set_push_state)).setText("已开启");
                            }
                        }
                    }).show();
                }else{
                    new AlertDialog(this,"","确定关闭消息通知？","确定",
                            "取消",0,new AlertDialog.OnDialogButtonClickListener(){

                        @Override
                        public void onDialogButtonClick(int requestCode, boolean isPositive) {
                            if(isPositive){
                                MApplication.getInstance().setCurrentUserPush(0);
                                ((TextView)findView(R.id.set_push_state)).setText("已关闭");
                            }
                        }
                    }).show();
                }

                break;
            case R.id.set_clear:
                CleanDataUtils.clearAllCache(Objects.requireNonNull(this));
                String clearSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(this));
                set_clear_text.setText(clearSize);
                showShortToast("清除成功");
                break;
            case R.id.set_login:
                toActivity(LoginActivity.createIntent(this));
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}