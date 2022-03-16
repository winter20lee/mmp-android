package zblibrary.zgl.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.FirstTabLayoutAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.GoodsCategory;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

public class ClassificationSearchItemFragment extends BaseFragment implements OnHttpResponseListener {
    private static final String TAG = "ClassificationSearchLootFragment";
    public static final int REQUEST_GOODSCATEGORY = 110000;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirstTabLayoutAdapter adapter;
    private List<GoodsCategory> goodsCategoryList = new ArrayList<>();;
    private String type;
    private String keyWord;
    public static ClassificationSearchItemFragment createInstance(String type,String keyword) {
        ClassificationSearchItemFragment fragment = new ClassificationSearchItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CLASSIFICATION_SEARCH_TYPE, type);
        bundle.putString(CLASSIFICATION_SEARCH_KEYWORD, keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.classification_search_loot_fragment);
        argument = getArguments();
        if (argument != null) {
            type = argument.getString(CLASSIFICATION_SEARCH_TYPE, CLASSIFICATION_SEARCH_TYPE_LOOT);
            keyWord = argument.getString(CLASSIFICATION_SEARCH_KEYWORD,"");
        }
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        tabLayout = findView(R.id.classfi_ser_lot_tablayout);
        viewPager = findView(R.id.classfi_ser_lot_view_pager);
    }
    @Override
    public void initData() {//必须在onCreateView方法内调用
        adapter = new FirstTabLayoutAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.id=1;
        goodsCategory.name=getString(R.string.serch_catagory_commond);
        goodsCategoryList.add(goodsCategory);
        if(StringUtil.isNotEmpty(keyWord,true)){
            adapter.setList(goodsCategoryList);
            tabLayout.setVisibility(View.GONE);
        }else{
            adapter.setList(goodsCategoryList);
            HttpRequest.getGoodsCategory(REQUEST_GOODSCATEGORY, new OnHttpResponseListenerImpl(this));
        }
    }

    @Override
    public void initEvent() {//必须在onCreateView方法内调用
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        goodsCategoryList.addAll(GsonUtil.jsonToList(resultData, GoodsCategory.class));
        adapter.setList(goodsCategoryList);
        viewPager.setOffscreenPageLimit(goodsCategoryList.size());
        for (int i=0 ;i< goodsCategoryList.size();i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e, String message) {

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.classification_search_tab_item_view, null);
        TextView txt_title = (TextView) view.findViewById(R.id.classification_search_tab_item);
        txt_title.setText(goodsCategoryList.get(position).name);
        return view;
    }
}
