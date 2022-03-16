package zblibrary.zgl.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.LoginActivity;
import zblibrary.zgl.activity.MessageActivity;
import zblibrary.zgl.activity.SearchActivity;
import zblibrary.zgl.adapter.FirstTabLayoutAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.GoodsCategory;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.GsonUtil;

/**首页
 */
public class FirstFragment extends BaseFragment implements OnClickListener,
		OnHttpResponseListener {
	public static final int REQUEST_GOODSCATEGORY = 110000;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FirstTabLayoutAdapter adapter;
	private List<GoodsCategory> goodsCategoryList = new ArrayList<>();;
	public static FirstFragment createInstance() {
		return new FirstFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.first_fragment);
		initView();
		initData();
		initEvent();
		return view;
	}


	@Override
	public void initView() {//必须调用
		findView(R.id.et_searchtext_search,this);
		findView(R.id.button_history,this);
		tabLayout = findView(R.id.classfi_ser_lot_tablayout);
		viewPager = findView(R.id.classfi_ser_lot_view_pager);
	}

	@Override
	public void initData() {//必须调用
		adapter = new FirstTabLayoutAdapter(getChildFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.id=1;
		goodsCategory.name="推荐";
		goodsCategoryList.add(goodsCategory);
		goodsCategory = new GoodsCategory();
		goodsCategory.id=2;
		goodsCategory.name="最新";
		goodsCategoryList.add(goodsCategory);
		adapter.setList(goodsCategoryList);
		HttpRequest.getGoodsCategory(REQUEST_GOODSCATEGORY, new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void initEvent() {//必须调用
		findView(R.id.et_searchtext_search).setFocusable(false);
		findView(R.id.et_searchtext_search).setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.et_searchtext_search:
				toActivity(SearchActivity.createIntent(getActivity(),0));
				break;
			case R.id.button_history:
				if(MApplication.getInstance().isLoggedIn()){
					toActivity(MessageActivity.createIntent(getActivity()));
				}else{
					toActivity(LoginActivity.createIntent(getActivity()));
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_GOODSCATEGORY:
				goodsCategoryList.addAll(GsonUtil.jsonToList(resultData, GoodsCategory.class));
				adapter.setList(goodsCategoryList);
				viewPager.setOffscreenPageLimit(goodsCategoryList.size());
				for (int i=0 ;i< goodsCategoryList.size();i++) {
					tabLayout.getTabAt(i).setCustomView(getTabView(i));
				}
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}
	public View getTabView(int position) {
		View view = LayoutInflater.from(context).inflate(R.layout.classification_search_tab_item_view, null);
		TextView txt_title = (TextView) view.findViewById(R.id.classification_search_tab_item);
		txt_title.setText(goodsCategoryList.get(position).name);
		return view;
	}

}