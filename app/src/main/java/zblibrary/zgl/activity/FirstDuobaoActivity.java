
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.FirstDuobaoAdapter;
import zblibrary.zgl.model.FirstDuobao;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstDuobaoViewItem;
import zblibrary.zgl.view.SpaceItemDecoration;
import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;


/**
 * 首页夺宝
 */
public class FirstDuobaoActivity extends BaseHttpRecyclerActivity<FirstDuobao, FirstDuobaoViewItem, FirstDuobaoAdapter> implements OnBottomDragListener {

	private List<FirstDuobao> messageDataList;
	private int goodId;
	public static Intent createIntent(Context context,int goodId) {
		return new Intent(context, FirstDuobaoActivity.class).putExtra(INTENT_ID,goodId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_duobao_activity, this);
		intent = getIntent();
		goodId = intent.getIntExtra(INTENT_ID,0);
		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		rvBaseRecycler.addItemDecoration(new SpaceItemDecoration(StringUtil.dp2px(context,5), StringUtil.dp2px(context,5)));
		rvBaseRecycler.setEmptyView(findView(R.id.empty_view));
		StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		rvBaseRecycler.setLayoutManager(layoutManager1);
	}

	@Override
	public void setList(final List<FirstDuobao> list) {
		setList(new AdapterCallBack<FirstDuobaoAdapter>() {

			@Override
			public FirstDuobaoAdapter createAdapter() {
				return new FirstDuobaoAdapter(context);
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
		HttpRequest.getFirstDuobaoList(goodId,page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<FirstDuobao> parseArray(String json) {
		try {
			messageDataList = GsonUtil.jsonToList(GsonUtil.GsonData(json), FirstDuobao.class);
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
		toActivity(LootDetailsActivity.createIntent(context,id));
	}
}