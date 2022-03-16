
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.PointsAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.model.Points;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;

/**
 * 积分详情
 */
public class PointDestailActivity extends BaseHttpListActivity<Points.ResultModel, ListView, PointsAdapter> implements OnBottomDragListener {

	private TextView tvPoints;
	public static Intent createIntent(Context context) {
		return new Intent(context, PointDestailActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.points_activity, this);
		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		lvBaseList.setDividerHeight(0);
		tvPoints = findView(R.id.points);
	}

	@Override
	public void setList(final List<Points.ResultModel> list) {
		setList(new AdapterCallBack<PointsAdapter>() {

			@Override
			public PointsAdapter createAdapter() {
				return new PointsAdapter(context);
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
		if(MApplication.getInstance().getPointsRules()!=null)
		tvPoints.setText(MApplication.getInstance().getPointsRules().points+"");
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getUserPointsInfoList(page, -page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<Points.ResultModel> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			Points points = GsonUtil.GsonToBean(data,Points.class);
			List<Points.ResultModel> messageDataList = points.result;
			onStopRefresh();
			if(points.totalPage > points.pageNo){
				onStopLoadMore(true);
			}else{
				onStopLoadMore(false);
			}
			return messageDataList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
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