package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.FirstLastAdapter;
import zblibrary.zgl.model.FirstLast;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstLastView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 * 最新
 */
public class FirstLastFragment extends BaseHttpRecyclerFragment<FirstLast.ResultBean, FirstLastView, FirstLastAdapter> {

    private String type;
    private int page2;
    private FirstLast firstLast;
    List<FirstLast.ResultBean> resultModelList = null;

    public static FirstLastFragment createInstance(String type) {
        FirstLastFragment fragment = new FirstLastFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_TITLE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.first_last_fragment);
        argument = getArguments();
        if (argument != null) {
            type = argument.getString(INTENT_TITLE);
        }
        initView();
        initData();
        initEvent();
        onRefresh();
        return view;
    }


    @Override
    public void initView() {//必须调用
        super.initView();
    }

    @Override
    public void setList(final List<FirstLast.ResultBean> list) {
        setList(new AdapterCallBack<FirstLastAdapter>() {

            @Override
            public FirstLastAdapter createAdapter() {
                return new FirstLastAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void initData() {//必须调用
        super.initData();
    }

    public void initEvent() {//必须调用
        super.initEvent();
    }

    @Override
    public void getListAsync(final int page) {
        //最新
        //HttpRequest.getNewest(page,4,REQUEST_NEW_REFRESH,new OnHttpResponseListenerImpl(this));
        page2 = page;
        HttpRequest.getNewest(page, 4, this);
        if (page == 1) {
            onStopLoadMore(true);
        }
    }

    @Override
    public List<FirstLast.ResultBean> parseArray(String json) {
        dismissProgressDialog();
        if (StringUtil.isEmpty(json)) {
            return new ArrayList<>();
        }
        firstLast = GsonUtil.GsonToBean(json, FirstLast.class);
        if (resultModelList == null) {
            resultModelList = new ArrayList<>();
        }
        if (page2 != 1) {
            boolean istrue = resultModelList.addAll(resultModelList.size(),firstLast.result);
        } else {
            resultModelList = firstLast.result;
        }
        onStopRefresh();
        if (firstLast.totalPage > firstLast.pageNo) {
            onStopLoadMore(true);
        } else {
            onStopLoadMore(false);
        }
        return resultModelList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(PlayVideoDetailsActivity.createIntent(context, resultModelList.get(position).id));
    }

}