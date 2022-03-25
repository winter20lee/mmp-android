
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.FirstLast;
import zblibrary.zgl.view.FirstLastView;
import zuo.biao.library.base.BaseAdapter;

public class FirstLastAdapter extends BaseAdapter<FirstLast.ResultModel, FirstLastView> {

	public FirstLastAdapter(Activity context) {
		super(context);
	}

	@Override
	public FirstLastView createView(int position, ViewGroup parent) {
		return new FirstLastView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}