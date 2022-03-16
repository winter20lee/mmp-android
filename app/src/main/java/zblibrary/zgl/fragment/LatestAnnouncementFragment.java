package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.LootDetailsActivity;
import zblibrary.zgl.adapter.LatestAnnoAdapter;
import zblibrary.zgl.model.LatestAnnou;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.LatestAnnoView;
import zblibrary.zgl.view.SpaceItemDecoration;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 *  最新揭晓
 */
public class LatestAnnouncementFragment extends BaseHttpRecyclerFragment<LatestAnnou.ResultModel, LatestAnnoView, LatestAnnoAdapter> {

	public static LatestAnnouncementFragment createInstance() {
		LatestAnnouncementFragment fragment = new LatestAnnouncementFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.latest_anno_fragment);
		initView();
		initData();
		initEvent();
		onRefresh();
		return view;
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		rvBaseRecycler.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_5)));
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		rvBaseRecycler.setLayoutManager(layoutManager);
	}

	@Override
	public void setList(final List<LatestAnnou.ResultModel> list) {
		setList(new AdapterCallBack<LatestAnnoAdapter>() {

			@Override
			public LatestAnnoAdapter createAdapter() {
				return new LatestAnnoAdapter(context);
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

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getLootPlanWinList(page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<LatestAnnou.ResultModel> parseArray(String json) {
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		LatestAnnou latestAnnou = GsonUtil.GsonToBean(json, LatestAnnou.class);
		List<LatestAnnou.ResultModel> resultModelList = latestAnnou.result;
		if(resultModelList==null){
			return new ArrayList<>();
		}
		onStopRefresh();
		if(latestAnnou.totalPage > latestAnnou.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return resultModelList;
	}


	public void initEvent() {//必须调用
		super.initEvent();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (id > 0) {
			toActivity(LootDetailsActivity.createIntent(context,id+""));
		}
	}
}