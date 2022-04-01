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
import zblibrary.zgl.model.FirstLast;
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
	private static final int REQUEST_COMM_REFRESH = 10002;
	private static final int REQUEST_NEW_REFRESH = 10003;
	private MZBannerView mMZBanner ;
	private SmartRefreshLayout srlBaseHttpRecycler;
	private List<ListByPos> firstBannerList = new ArrayList<>();
	private MZHolderCreator mzHolderCreator;
	private int pageComm = 1;
	private LinearLayout first_categoty_content;
	private boolean isCommend;
	private int pageNew=1;
	private int catalogId=0;
	public static FirstRecommendFragment createInstance(boolean isCommend,int catalogId) {
		FirstRecommendFragment fragment = new FirstRecommendFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(INTENT_TITLE, isCommend);
		bundle.putInt(INTENT_ID, catalogId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.first_recomm_fragment);
		argument = getArguments();
		if (argument != null) {
			isCommend = argument.getBoolean(INTENT_TITLE);
			catalogId = argument.getInt(INTENT_ID);
		}
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
		if(isCommend){
			//banner
			HttpRequest.getListByPos("1",REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
			//最新
			HttpRequest.getNewest(pageNew,4,REQUEST_NEW_REFRESH,new OnHttpResponseListenerImpl(this));
			//推荐
			HttpRequest.getIndex(pageComm,catalogId,1,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
		}else{
			//banner
			HttpRequest.getListByPos("1",REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
			//一级分类
			HttpRequest.getIndex(pageComm,catalogId,2,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
		}
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
			case REQUEST_COMM_REFRESH:
				if(!isCommend){
					first_categoty_content.removeAllViews();
				}
				ArrayList<SecondCategory> secondCategory = (ArrayList<SecondCategory>) GsonUtil.jsonToList(resultData, SecondCategory.class);
				for(int i=0;i<secondCategory.size();i++){
					FirstCategoryView receivingAddressView = new FirstCategoryView(context,first_categoty_content,true);
					first_categoty_content.addView(receivingAddressView.createView());
					receivingAddressView.bindView(secondCategory.get(i));
				}

				onStopRefresh();
				onStopLoadMore(false);
				break;
			case REQUEST_NEW_REFRESH:
				first_categoty_content.removeAllViews();
				FirstLast firstLast = GsonUtil.GsonToBean(resultData, FirstLast.class);
				FirstCategoryView receivingAddressView = new FirstCategoryView(context,first_categoty_content,true);
				first_categoty_content.addView(receivingAddressView.createView(),0);
				receivingAddressView.bindView(firstLast.transData());
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