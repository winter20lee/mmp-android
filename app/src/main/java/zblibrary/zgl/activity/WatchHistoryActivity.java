
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
import zblibrary.zgl.adapter.WatchHistoryAdapter;
import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.MyLike;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 观看历史
 */
public class WatchHistoryActivity extends BaseHttpListActivity<MyLike.ResultBean, ListView, WatchHistoryAdapter> implements OnBottomDragListener {

	private List<MyLike.ResultBean> messageDataList;
	public static Intent createIntent(Context context) {
		return new Intent(context, WatchHistoryActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.watch_history_activity, this);

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
	public void setList(final List<MyLike.ResultBean> list) {
		setList(new AdapterCallBack<WatchHistoryAdapter>() {

			@Override
			public WatchHistoryAdapter createAdapter() {
				if (context == null) {
					throw new RuntimeException("please pass an activity first to use this call");
				}
				return new WatchHistoryAdapter(context);
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
		vBaseEmptyView.setEmptyText("暂时没有观看记录");
		vBaseEmptyView.setEmptySecondText("您可以去首页推荐看看");
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getMyplay(page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<MyLike.ResultBean> parseArray(String json) {
		MyLike myLike ;
		try {
			myLike = GsonUtil.GsonToBean(GsonUtil.GsonData(json), MyLike.class);
		} catch (Exception e) {
			return new ArrayList<>();
		}
		if(myLike ==null || myLike.result == null ){
			return new ArrayList<>();
		}

		if(myLike.totalPage > myLike.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return myLike.result;
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
		toActivity(PlayVideoDetailsActivity.createIntent(context,list.get(position).videoId));
	}
}