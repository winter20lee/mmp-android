
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;
import zblibrary.zgl.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.CleanDataUtils;


/**
 * 设置
 */
public class SetActivity extends BaseActivity implements OnBottomDragListener, View.OnClickListener {
    private TextView set_clear_text;
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
    }

    @Override
    public void initView() {//必须调用
        set_clear_text = findView(R.id.set_clear_text);
    }

    @Override
    public void initData() {//必须调用
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(this));
            set_clear_text.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initEvent() {//必须调用
        findView(R.id.set_info,this);
        findView(R.id.set_phone,this);
        findView(R.id.set_autoplay,this);
        findView(R.id.set_push,this);
        findView(R.id.set_clear,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_info:
                toActivity(UserInfoActivity.createIntent(this));
                break;
            case R.id.set_phone:
                break;
            case R.id.set_autoplay:
                break;
            case R.id.set_push:
                break;
            case R.id.set_clear:
                CleanDataUtils.clearAllCache(Objects.requireNonNull(this));
                String clearSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(this));
                set_clear_text.setText(clearSize);
                showShortToast("清除成功");
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
}