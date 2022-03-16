package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.WinningGoodsDetailsActivity;
import zblibrary.zgl.adapter.WinningGoodsAdapter;
import zblibrary.zgl.model.WinningGoods;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.WinningGoodsView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 *  获奖商品
 */
public class WinningGoodsFragment extends BaseHttpRecyclerFragment<WinningGoods.ResultModel, WinningGoodsView, WinningGoodsAdapter> {

	private String type;
	private WinningGoods winningGoods;
	public static WinningGoodsFragment createInstance(String type) {
		WinningGoodsFragment fragment = new WinningGoodsFragment();
		Bundle bundle = new Bundle();
		bundle.putString(INTENT_TITLE, type);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.myorder_fragment);
		argument = getArguments();
		if (argument != null) {
			type = argument.getString(INTENT_TITLE);
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
	public void setList(final List<WinningGoods.ResultModel> list) {
		setList(new AdapterCallBack<WinningGoodsAdapter>() {

			@Override
			public WinningGoodsAdapter createAdapter() {
				return new WinningGoodsAdapter(context);
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

	public void initEvent() {//必须调用
		super.initEvent();
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getOrderWinList(type,page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<WinningGoods.ResultModel> parseArray(String json) {
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		winningGoods = GsonUtil.GsonToBean(json,WinningGoods.class);
		List<WinningGoods.ResultModel> resultModelList = winningGoods.result;
		if(resultModelList==null){
			return new ArrayList<>();
		}
		onStopRefresh();
		if(winningGoods.totalPage > winningGoods.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return resultModelList;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		toActivity(WinningGoodsDetailsActivity.createIntent(context,winningGoods.result.get(position).orderNo));
	}

}