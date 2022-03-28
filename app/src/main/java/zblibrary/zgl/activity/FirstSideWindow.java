package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.adapter.FirstSideAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstTabPosEvent;
import zblibrary.zgl.model.GoodsCategory;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.R;
import zuo.biao.library.base.BaseBottomWindow;
import zuo.biao.library.util.GsonUtil;

public class FirstSideWindow extends BaseBottomWindow implements  OnHttpResponseListener {
    public static final int REQUEST_GOODSCATEGORY = 110000;
    private GridView expandableGridView;
    private FirstSideAdapter firstSideAdapter;
    private List<GoodsCategory> goodsCategoryList = new ArrayList<>();
    private ImageView first_side_close;
    public static Intent createIntent(Context context) {
        return new Intent(context, FirstSideWindow.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_side_window);
        intent = getIntent();
        initView();
        initData();
        initEvent();
        HttpRequest.getGoodsCategory(REQUEST_GOODSCATEGORY, new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        super.initView();
        first_side_close = findView(R.id.first_side_close);
        expandableGridView = findView(R.id.first_side_gridview);

    }


    @Override
    public void initData() {//必须调用
        super.initData();
        firstSideAdapter = new FirstSideAdapter(this);
        expandableGridView.setAdapter(firstSideAdapter);
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        first_side_close.setOnClickListener(v -> {
            finish();
        });
        vBaseBottomWindowRoot.setOnTouchListener((v, event) -> {
            return true;
        });

        firstSideAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(new FirstTabPosEvent(i));
                finish();
            }
        });
    }


    @Override
    protected void setResult() {

    }


    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.id=1;
        goodsCategory.name="推荐";
        goodsCategoryList.add(goodsCategory);
        goodsCategory = new GoodsCategory();
        goodsCategory.id=2;
        goodsCategory.name="最新";
        goodsCategoryList.add(goodsCategory);
        goodsCategoryList.addAll(GsonUtil.jsonToList(resultData, GoodsCategory.class));
        firstSideAdapter.refresh(goodsCategoryList);
    }

    @Override
    public void onHttpError(int requestCode, Exception e, String message) {
        showShortToast(message);
    }
}
