
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
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 我的收藏
 */
public class MyLikeActivity extends BaseHttpListActivity<Customize, ListView, WatchHistoryAdapter> implements OnBottomDragListener {

	private List<Customize> messageDataList;
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
	public void setList(final List<Customize> list) {
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
		HttpRequest.getHelpInfoList(page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<Customize> parseArray(String json) {
		try {
			messageDataList = GsonUtil.jsonToList(GsonUtil.GsonData(json), Customize.class);
			messageDataList.get(messageDataList.size()-1).isEnd = true;
		} catch (Exception e) {
			e.printStackTrace();
			messageDataList = new ArrayList<>();
		}
		onStopRefresh();
		onStopLoadMore(false);
		return messageDataList;
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
		toActivity(HelpDetailsActivity.createIntent(context,messageDataList.get(position).key,messageDataList.get(position).getValue()));
	}
}