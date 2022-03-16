
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
import zblibrary.zgl.adapter.PlanWinAdapter;
import zblibrary.zgl.model.PlaneWin;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 往期揭晓
 */
public class PlanWinActivity extends BaseHttpListActivity<PlaneWin.ResultModel, ListView, PlanWinAdapter> implements OnBottomDragListener {

	private long goodsId;
	public static Intent createIntent(Context context,long goodsId) {
		return new Intent(context, PlanWinActivity.class).putExtra(INTENT_ID,goodsId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paln_win_activity, this);
		intent = getIntent();
		goodsId = intent.getLongExtra(INTENT_ID,0);
		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
	}

	@Override
	public void setList(final List<PlaneWin.ResultModel> list) {
		setList(new AdapterCallBack<PlanWinAdapter>() {

			@Override
			public PlanWinAdapter createAdapter() {
				return new PlanWinAdapter(context);
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
		HttpRequest.getPlaneWin(goodsId,page,-page, this);
		if(page==0){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<PlaneWin.ResultModel> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			PlaneWin planeWin = GsonUtil.GsonToBean(data,PlaneWin.class);
			List<PlaneWin.ResultModel> resultModelList = planeWin.result;
			onStopRefresh();
			if(planeWin.totalPage > planeWin.pageNo){
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

	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

//	@Override
//	public void onStopRefresh() {
//		runUiThread(() -> {
//			srlBaseHttpList.finishRefresh();
//			srlBaseHttpList.setLoadmoreFinished(false);
//		});
//	}
//
//	@Override
//	public void onStopLoadMore(boolean isHaveMore) {
//		runUiThread(() -> {
//			if (isHaveMore) {
//				srlBaseHttpList.finishLoadmore();
//			} else {
//				srlBaseHttpList.finishLoadmoreWithNoMoreData();
//			}
//			srlBaseHttpList.setLoadmoreFinished(! isHaveMore);
//		});
//	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}