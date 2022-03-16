package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.LootDetailsActivity;
import zblibrary.zgl.adapter.AllOrderAdapter;
import zblibrary.zgl.model.AllOrder;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.AllOrderView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 *  全部订单
 */
public class AllOrderFragment extends BaseHttpRecyclerFragment<AllOrder.ResultModel, AllOrderView, AllOrderAdapter> {

	private String type;
	private AllOrder allOrder;
	public static AllOrderFragment createInstance(String type) {
		AllOrderFragment fragment = new AllOrderFragment();
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
	public void setList(final List<AllOrder.ResultModel> list) {
		setList(new AdapterCallBack<AllOrderAdapter>() {

			@Override
			public AllOrderAdapter createAdapter() {
				return new AllOrderAdapter(context);
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
		HttpRequest.getLootOrderList(type,page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<AllOrder.ResultModel> parseArray(String json) {
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		allOrder = GsonUtil.GsonToBean(json,AllOrder.class);
		List<AllOrder.ResultModel> resultModelList = allOrder.result;
		if(resultModelList==null){
			return new ArrayList<>();
		}
		onStopRefresh();
		if(allOrder.totalPage > allOrder.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return resultModelList;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(list.get(position).orderStatus == 1 || list.get(position).orderStatus == 2 ){
			toActivity(LootDetailsActivity.createIntent(context, id));
		}else if(list.get(position).orderStatus == 3){
			toActivity(LootDetailsActivity.createIntent(context, list.get(position).planId+""));
		}
	}

}