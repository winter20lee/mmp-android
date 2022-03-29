package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;
import zblibrary.zgl.adapter.FirstSideAdapter;
import zblibrary.zgl.model.FirstTabPosEvent;
import zblibrary.zgl.model.FirstCategory;
import zuo.biao.library.R;
import zuo.biao.library.base.BaseBottomWindow;

public class FirstSideWindow extends BaseBottomWindow {
    private GridView expandableGridView;
    private FirstSideAdapter firstSideAdapter;
    private List<FirstCategory.FirstCategorySerializable> firstCategoryList;
    private ImageView first_side_close;
    public static Intent createIntent(Context context,List<FirstCategory.FirstCategorySerializable> firstCategoryList) {
        return new Intent(context, FirstSideWindow.class).putExtra(INTENT_ID, (Serializable) firstCategoryList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_side_window);
        intent = getIntent();
        firstCategoryList = (List<FirstCategory.FirstCategorySerializable>) intent.getSerializableExtra(INTENT_ID);
        initView();
        initData();
        initEvent();
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
        firstSideAdapter.refresh(firstCategoryList);
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

        firstSideAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
            EventBus.getDefault().post(new FirstTabPosEvent(i));
            finish();
        });
    }


    @Override
    protected void setResult() {

    }
}
