
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
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 我的收藏
 */
public class MyLikeActivity extends BaseHttpListActivity<MyLike.ResultBean, ListView, WatchHistoryAdapter> implements OnBottomDragListener {

	private List<MyLike> messageDataList;
	public static Intent createIntent(Context context) {
		return new Intent(context, MyLikeActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_like_activity, this);

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

	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getMyfav(page,-page,this);
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