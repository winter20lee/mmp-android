
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.ReceivingAddressAdapter;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 收货地址
 */
public class ReceivingAddressActivity extends BaseHttpListActivity<ReceivingAddress.ResultModel, ListView, ReceivingAddressAdapter> implements OnBottomDragListener {

	private  boolean isSelect;
	public static Intent createIntent(Context context) {
		return new Intent(context, ReceivingAddressActivity.class);
	}

	public static Intent createIntent(Context context,boolean isSelect) {
		return new Intent(context, ReceivingAddressActivity.class).putExtra(INTENT_TYPE,isSelect);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiving_address_activity, this);
		intent = getIntent();
		isSelect =  intent.getBooleanExtra(INTENT_TYPE,false);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		onRefresh();
	}

	@Override
	public void initView() {
		super.initView();
		vBaseEmptyView.setEmptyImageAndText(R.mipmap.empty,getString(R.string.add_address_empty));
	}

	@Override
	public void setList(final List<ReceivingAddress.ResultModel> list) {
		setList(new AdapterCallBack<ReceivingAddressAdapter>() {

			@Override
			public ReceivingAddressAdapter createAdapter() {
				return new ReceivingAddressAdapter(context);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
		lvBaseList.setDividerHeight(0);
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getAddressList(page, -page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<ReceivingAddress.ResultModel> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			ReceivingAddress receivingAddress = GsonUtil.GsonToBean(data,ReceivingAddress.class);
			List<ReceivingAddress.ResultModel> resultModelList = receivingAddress.result;
			onStopRefresh();
			if(receivingAddress.totalPage > receivingAddress.pageNo){
				onStopLoadMore(true);
			}else{
				onStopLoadMore(false);
			}
			return resultModelList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();
		findView(R.id.add_new_address).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toActivity(AddAndEditAddressActivity.createIntent(context,null));
			}
		});
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
		if(isSelect){
			EventBus.getDefault().post(list.get(position));
			finish();
		}
	}
}