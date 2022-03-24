
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.Last;
import zblibrary.zgl.view.LastView;
import zuo.biao.library.base.BaseAdapter;

public class LastAdapter extends BaseAdapter<Last.ResultModel, LastView> {

	public LastAdapter(Activity context) {
		super(context);
	}

	@Override
	public LastView createView(int position, ViewGroup parent) {
		return new LastView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}