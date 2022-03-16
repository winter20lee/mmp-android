package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.Points;
import zblibrary.zgl.view.PointsView;
import zuo.biao.library.base.BaseAdapter;

public class PointsAdapter extends BaseAdapter<Points.ResultModel, PointsView> {

	public PointsAdapter(Activity context) {
		super(context);
	}

	@Override
	public PointsView createView(int position, ViewGroup parent) {
		return new PointsView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}