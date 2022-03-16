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
import zblibrary.zgl.model.FirstCategory;
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
		<FirstCategory.ResultModel, FirstCategoryViewItem, FirstCategoryAdapter> {

	private String tagType;
	private String goodsCategoryId = "";
	private String orderByname = "gmtOnline";
	private String orderBy = "asc";
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
	public void setList(final List<FirstCategory.ResultModel> list) {
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
		HttpRequest.getMallSearch(page,tagType,goodsCategoryId,orderByname,orderBy,keyWord,-page, this);
	}

	@Override
	public List<FirstCategory.ResultModel> parseArray(String json) {
		onStopRefresh();
		if(json==null){
			return new ArrayList<>();
		}
		FirstCategory firstCategory = GsonUtil.GsonToBean(json, FirstCategory.class);
		if(firstCategory ==null || firstCategory.result==null){
			return new ArrayList<>();
		}

		if(firstCategory.totalPage > firstCategory.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return firstCategory.result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			toActivity(PlayVideoDetailsActivity.createIntent(context, id));
	}
}