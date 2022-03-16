package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.PlanePart;
import zblibrary.zgl.view.PlanPartView;
import zuo.biao.library.base.BaseAdapter;

public class PlanPartAdapter extends BaseAdapter<PlanePart.ResultModel, PlanPartView> {

	public PlanPartAdapter(Activity context) {
		super(context);
	}

	@Override
	public PlanPartView createView(int position, ViewGroup parent) {
		return new PlanPartView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}