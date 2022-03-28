package zblibrary.zgl.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.FirstSideWindow;
import zblibrary.zgl.activity.SearchActivity;
import zblibrary.zgl.activity.WatchHistoryActivity;
import zblibrary.zgl.adapter.FirstTabLayoutAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstTabPosEvent;
import zblibrary.zgl.model.GoodsCategory;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.ImageAlertDialog;
import zuo.biao.library.ui.TextAlertDialog;
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
		EventBus.getDefault().register(this);
		initView();
		initData();
		initEvent();
		return view;
	}


	@Override
	public void initView() {//必须调用
		findView(R.id.et_searchtext_search,this);
		findView(R.id.button_history,this);
		findView(R.id.first_side,this);
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
//		final String sText = "测试自定义标签：<br><br><br><br><br><br><br><br><br><br><br>" +
//				"<br><br><br><br><br><br><br><br><br><br><br><br><br>" +
//				"<br><br><br><br><br><br><br><br><br><br><br><br><br>" +
//				"<br><br><br><br><br><br><br><br><br><br><br><br><br>" +
//				"<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><h1><mxgsa>测试自定义标签</mxgsa></h1>";
//		new TextAlertDialog(context,"最新公告",sText).show();

//		new ImageAlertDialog(context,"https://t7.baidu.com/it/u=1267369966,436219007&fm=193&f=GIF","https://www.baidu.com").show();
	}

	@Override
	public void initEvent() {//必须调用
		findView(R.id.et_searchtext_search).setFocusable(false);
		findView(R.id.et_searchtext_search).setOnClickListener(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFirstTabPosEvent(FirstTabPosEvent firstTabPosEvent){
		tabLayout.setScrollPosition(0, firstTabPosEvent.pos, true);
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.et_searchtext_search:
				toActivity(SearchActivity.createIntent(getActivity(),0));
				break;
			case R.id.button_history:
				toActivity(WatchHistoryActivity.createIntent(getActivity()));
				break;
			case R.id.first_side:
				toActivity(FirstSideWindow.createIntent(getActivity()));
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

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}