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
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.FirstCategoryAdapter;
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstCategoryViewItem;
import zblibrary.zgl.view.SpaceItemDecoration;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;

/**
 *  搜索
 */
public class SearchFragment extends BaseHttpRecyclerFragment
		<SecondCategory.VideoListBean.ResultBean, FirstCategoryViewItem, FirstCategoryAdapter> {

	private String keyWord = "";
	public static SearchFragment createInstance(String keyWord) {
		SearchFragment fragment = new SearchFragment();
		Bundle bundle = new Bundle();
		bundle.putString(INTENT_PHONE, keyWord);
		fragment.setArguments(bundle);
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.search_fragment);
		argument = getArguments();
		if (argument != null) {
			keyWord = argument.getString(INTENT_PHONE);
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
	public void setList(final List<SecondCategory.VideoListBean.ResultBean> list) {
		setList(new AdapterCallBack<FirstCategoryAdapter>() {

			@Override
			public FirstCategoryAdapter createAdapter() {
				return new FirstCategoryAdapter(context);
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
		rvBaseRecycler.addItemDecoration(new SpaceItemDecoration(15,15));
		rvBaseRecycler.setEmptyView(findView(R.id.empty_view));
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		rvBaseRecycler.setLayoutManager(layoutManager);
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getSearch(page,0,keyWord,-page, this);
	}

	@Override
	public List<SecondCategory.VideoListBean.ResultBean> parseArray(String json) {
		onStopRefresh();
		if(json==null){
			return new ArrayList<>();
		}
		SecondCategory.VideoListBean videoListBean = GsonUtil.GsonToBean(json, SecondCategory.VideoListBean.class);
		if(videoListBean ==null || videoListBean.result == null ){
			return new ArrayList<>();
		}

		if(videoListBean.totalPage > videoListBean.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return videoListBean.result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			toActivity(PlayVideoDetailsActivity.createIntent(context, id));
	}
}