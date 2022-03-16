
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Customize;
import zblibrary.zgl.view.BottomGridView;
import zuo.biao.library.base.BaseAdapter;

public class BottomGridAdapter extends BaseAdapter<Customize, BottomGridView> {

	public BottomGridAdapter(Activity context) {
		super(context);
	}

	@Override
	public BottomGridView createView(int position, ViewGroup parent) {
		return new BottomGridView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

}