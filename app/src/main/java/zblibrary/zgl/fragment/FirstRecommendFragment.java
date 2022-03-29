package zblibrary.zgl.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.BannerViewPagerHolder;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstCategoryView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.util.GsonUtil;

/**首页
 */
public class FirstRecommendFragment extends BaseFragment implements
		OnHttpResponseListener, OnStopLoadListener, OnRefreshListener {
	private static final int REQUEST_BANNER = 10000;
	private static final int REQUEST_MALL_REFRESH = 10002;
	private MZBannerView mMZBanner ;
	private SmartRefreshLayout srlBaseHttpRecycler;
	private List<ListByPos> firstBannerList = new ArrayList<>();
	private MZHolderCreator mzHolderCreator;
	private int pageNoMall = 1;
	private LinearLayout first_categoty_content;
	public static FirstRecommendFragment createInstance() {
		return new FirstRecommendFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.first_recomm_fragment);
		initView();
		initData();
		initEvent();
		onRefresh(null);
		return view;
	}


	@Override
	public void initView() {//必须调用
		mMZBanner = findView(R.id.first_banner);
		srlBaseHttpRecycler = findView(R.id.srlBaseHttpRecycler);
		first_categoty_content = findView(R.id.first_categoty_content);

	}

	@Override
	public void initData() {//必须调用
		mzHolderCreator = (MZHolderCreator<BannerViewPagerHolder>) () -> new BannerViewPagerHolder();
		mMZBanner.setPages(firstBannerList,mzHolderCreator );

	}

	@Override
	public void initEvent() {//必须调用
		srlBaseHttpRecycler.setOnRefreshListener(this);
	}


	@Override
	public void onRefresh(RefreshLayout refreshLayout) {
		//banner
		HttpRequest.getListByPos("1",REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
		//商城
		HttpRequest.getFirstMall(pageNoMall,REQUEST_MALL_REFRESH, new OnHttpResponseListenerImpl(this));
	}


	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_BANNER:
				firstBannerList.clear();
				firstBannerList.addAll(GsonUtil.jsonToList(resultData, ListByPos.class));
				mMZBanner.setPages(firstBannerList,mzHolderCreator );
				mMZBanner.start();
				onStopRefresh();
				break;
			case REQUEST_MALL_REFRESH:
				SecondCategory secondCategory = GsonUtil.GsonToBean(resultData, SecondCategory.class);
				FirstCategoryView receivingAddressView = new FirstCategoryView(context,first_categoty_content,true);
				first_categoty_content.addView(receivingAddressView.createView());
				receivingAddressView.bindView(secondCategory);
				onStopRefresh();
				onStopLoadMore(false);
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		onStopLoadMore(true);
		onStopRefresh();
		showShortToast(message);
	}


	@Override
	public void onStopRefresh() {
		runUiThread(() -> {
			srlBaseHttpRecycler.finishRefresh();
			srlBaseHttpRecycler.finishLoadMore();
		});
	}

	@Override
	public void onStopLoadMore(boolean isHaveMore) {
		runUiThread(() -> {
			if (isHaveMore) {
				srlBaseHttpRecycler.finishLoadMore();
			} else {
				srlBaseHttpRecycler.finishLoadMoreWithNoMoreData();
			}
		});
	}
}