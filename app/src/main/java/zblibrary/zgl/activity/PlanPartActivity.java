
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.PlanPartAdapter;
import zblibrary.zgl.model.PlanePart;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 本期参与
 */
public class PlanPartActivity extends BaseHttpListActivity<PlanePart.ResultModel, ListView, PlanPartAdapter> implements OnBottomDragListener {

	private long goodsId;
	private String num;
	private int total;
	public static Intent createIntent(Context context,long goodsId,String num,int total) {
		return new Intent(context, PlanPartActivity.class)
				.putExtra(INTENT_TITLE,num)
				.putExtra(INTENT_TYPE,total)
				.putExtra(INTENT_ID,goodsId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paln_part_activity, this);
		intent = getIntent();
		goodsId = intent.getLongExtra(INTENT_ID,0);
		num = intent.getStringExtra(INTENT_TITLE);
		total = intent.getIntExtra(INTENT_TYPE,0);
		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		lvBaseList.setDividerHeight(10);
		lvBaseList.setDivider(new ColorDrawable(0xffffffff));
	}

	@Override
	public void setList(final List<PlanePart.ResultModel> list) {
		setList(new AdapterCallBack<PlanPartAdapter>() {

			@Override
			public PlanPartAdapter createAdapter() {
				return new PlanPartAdapter(context);
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
		((TextView)findView(R.id.my_number)).setText(num+"");
		((TextView)findView(R.id.my_number_purchasing)).setText(total+"");
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getPlanePart(goodsId,page,-page, this);
		if(page==0){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<PlanePart.ResultModel> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			PlanePart planeWin = GsonUtil.GsonToBean(data,PlanePart.class);
			List<PlanePart.ResultModel> resultModelList = planeWin.result;
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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}