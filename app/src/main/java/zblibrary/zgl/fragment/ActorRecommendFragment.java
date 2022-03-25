package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.ActorRecommendAdapter;
import zblibrary.zgl.model.ActorRecommend;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.ActorRecommendView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 *  演员推荐
 */
public class ActorRecommendFragment extends BaseHttpRecyclerFragment<ActorRecommend.ResultModel, ActorRecommendView, ActorRecommendAdapter> {

	public static ActorRecommendFragment createInstance() {
		ActorRecommendFragment fragment = new ActorRecommendFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.actor_recommend_fragment);
		initView();
		initData();
		initEvent();
		onRefresh();
		return view;
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
		rvBaseRecycler.setLayoutManager(layoutManager);
	}

	@Override
	public void setList(final List<ActorRecommend.ResultModel> list) {
		setList(new AdapterCallBack<ActorRecommendAdapter>() {

			@Override
			public ActorRecommendAdapter createAdapter() {
				return new ActorRecommendAdapter(context);
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
	}

	@Override
	public List<ActorRecommend.ResultModel> parseArray(String json) {
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		ActorRecommend actorRecommend = GsonUtil.GsonToBean(json, ActorRecommend.class);
		List<ActorRecommend.ResultModel> resultModelList = actorRecommend.result;
		if(resultModelList==null){
			return new ArrayList<>();
		}
		onStopRefresh();
		onStopLoadMore(false);
		return resultModelList;
	}


	public void initEvent() {//必须调用
		super.initEvent();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (id > 0) {
			toActivity(PlayVideoDetailsActivity.createIntent(context,id));
		}
	}
}