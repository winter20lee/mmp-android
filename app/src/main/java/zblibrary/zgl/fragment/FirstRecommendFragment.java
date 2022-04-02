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
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GsonUtil;

/**首页
 */
public class FirstRecommendFragment extends BaseFragment implements
		OnHttpResponseListener, OnStopLoadListener, OnRefreshListener, FirstCategoryView.OnClickChangeListener {
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
	private FirstCategoryView firstCategoryViewLast;
	private ArrayList<FirstCategoryView> firstCategoryViewArrayList = new ArrayList<>();
	private boolean isRefresh;
	private int posFirstCategoryView;
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
		mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
			@Override
			public void onPageClick(View view, int position) {
				toActivity(WebViewActivity.createIntent(context,"",firstBannerList.get(position).link));
			}
		});

	}

	@Override
	public void initEvent() {//必须调用
		srlBaseHttpRecycler.setOnRefreshListener(this);
	}


	@Override
	public void onRefresh(RefreshLayout refreshLayout) {
		isRefresh = true;
		if(isCommend){
			pageNew = 1;
			//banner
			HttpRequest.getListByPos(1,0,REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
			//最新
			HttpRequest.getNewest(pageNew,4,REQUEST_NEW_REFRESH,new OnHttpResponseListenerImpl(this));
			//推荐
			HttpRequest.getIndex(pageComm,catalogId,0,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
		}else{
			//banner
			HttpRequest.getListByPos(catalogId,0,REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
			//一级分类
			HttpRequest.getIndex(pageComm,catalogId,1,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
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
				ArrayList<SecondCategory> secondCategory = (ArrayList<SecondCategory>) GsonUtil.jsonToList(resultData, SecondCategory.class);
				if(isRefresh){
					if(!isCommend){
						first_categoty_content.removeAllViews();
					}
					firstCategoryViewArrayList.clear();
					for(int i=0;i<secondCategory.size();i++){
						FirstCategoryView firstCategoryViewRecomm = new FirstCategoryView(context,first_categoty_content,true,isCommend,false);
						firstCategoryViewRecomm.setPos(i);
						firstCategoryViewRecomm.setOnClickChangeListener(this);
						first_categoty_content.addView(firstCategoryViewRecomm.createView());
						firstCategoryViewRecomm.bindView(secondCategory.get(i));
						firstCategoryViewArrayList.add(firstCategoryViewRecomm);
					}
					onStopRefresh();
					onStopLoadMore(false);
					isRefresh = false;
				}else{
					FirstCategoryView firstCategoryViewRecomm = firstCategoryViewArrayList.get(posFirstCategoryView);
					firstCategoryViewRecomm.bindView(secondCategory.get(0));
				}

				break;
			case REQUEST_NEW_REFRESH:
				FirstLast firstLast = GsonUtil.GsonToBean(resultData, FirstLast.class);
				if(pageNew == 1){
					first_categoty_content.removeAllViews();
					firstCategoryViewLast = new FirstCategoryView(context,first_categoty_content,true,isCommend,true);
					firstCategoryViewLast.setOnClickChangeListener(this);
					first_categoty_content.addView(firstCategoryViewLast.createView(),0);
					firstCategoryViewLast.bindView(firstLast.transData());
				}else{
					firstCategoryViewLast.bindView(firstLast.transData());
				}

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


	@Override
	public void onClickChangeLast() {
		++pageNew;
		HttpRequest.getNewest(pageNew,4,REQUEST_NEW_REFRESH,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void onClickChangeRecom(int catalogId,int pos,int pageNum) {
		posFirstCategoryView = pos;
		if(isCommend){
			HttpRequest.getIndex(pageNum,catalogId,0,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
		}else{
			HttpRequest.getIndex(pageNum,catalogId,2,REQUEST_COMM_REFRESH, new OnHttpResponseListenerImpl(this));
		}
	}
}