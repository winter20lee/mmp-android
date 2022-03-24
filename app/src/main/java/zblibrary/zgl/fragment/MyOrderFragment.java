package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.OrderDetailsActivity;
import zblibrary.zgl.adapter.MyOrderAdapter;
import zblibrary.zgl.model.MyOrder;
import zblibrary.zgl.model.RefreshOrdeStateEvent;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.MyOrderView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 *  我的订单
 */
public class MyOrderFragment extends BaseHttpRecyclerFragment<MyOrder.ResultModel, MyOrderView, MyOrderAdapter> implements MyOrderView.OnConfirmReceiverListener {

	private String type;
	private MyOrder myOrder;
	public static MyOrderFragment createInstance(String type) {
		MyOrderFragment fragment = new MyOrderFragment();
		Bundle bundle = new Bundle();
		bundle.putString(INTENT_TITLE, type);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.last_fragment);
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

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRefreshOrdeStateEvent(RefreshOrdeStateEvent refreshOrdeStateEvent){
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
	}

	@Override
	public void setList(final List<MyOrder.ResultModel> list) {
		setList(new AdapterCallBack<MyOrderAdapter>() {

			@Override
			public MyOrderAdapter createAdapter() {
				return new MyOrderAdapter(context,MyOrderFragment.this);
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
		HttpRequest.getOrderList(type,page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<MyOrder.ResultModel> parseArray(String json) {
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		myOrder = GsonUtil.GsonToBean(json,MyOrder.class);
		List<MyOrder.ResultModel> resultModelList = myOrder.result;
		if(resultModelList==null){
			return new ArrayList<>();
		}
		onStopRefresh();
		if(myOrder.totalPage > myOrder.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return resultModelList;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		toActivity(OrderDetailsActivity.createIntent(context,myOrder.result.get(0).orderNo, (int) id));
	}

	@Override
	public void onConfirmReceiver(String orderNo) {
		onRefresh();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}