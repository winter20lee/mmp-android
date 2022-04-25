
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.OrderAdapter;
import zblibrary.zgl.model.Order;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;

/**
 * 订单
 */
public class OrderActivity extends BaseHttpListActivity<Order.MessageData, ListView, OrderAdapter> implements OnBottomDragListener {


	public static Intent createIntent(Context context) {
		return new Intent(context, OrderActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_activity, this);

		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		lvBaseList.setDividerHeight(0);
	}

	@Override
	public void setList(final List<Order.MessageData> list) {
		setList(new AdapterCallBack<OrderAdapter>() {

			@Override
			public OrderAdapter createAdapter() {
				return new OrderAdapter(context);
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
		vBaseEmptyView.setEmptyText("暂时没有订单记录");
		vBaseEmptyView.setEmptySecondText("您可以去首页推荐看看");
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getMessageList(page, -page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<Order.MessageData> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			Order message = GsonUtil.GsonToBean(data, Order.class);
			List<Order.MessageData> messageDataList = message.result;
			onStopRefresh();
			if(message.totalPage > message.pageNo){
				onStopLoadMore(true);
			}else{
				messageDataList.get(messageDataList.size()-1).isEnd = true;
				onStopLoadMore(false);
			}
			return messageDataList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();

	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
}