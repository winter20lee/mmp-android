package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.PlaneWin;
import zblibrary.zgl.view.PlanWinView;
import zuo.biao.library.base.BaseAdapter;

public class PlanWinAdapter extends BaseAdapter<PlaneWin.ResultModel, PlanWinView> {

	public PlanWinAdapter(Activity context) {
		super(context);
	}

	@Override
	public PlanWinView createView(int position, ViewGroup parent) {
		return new PlanWinView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}