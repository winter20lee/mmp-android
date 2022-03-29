
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.FirstCategory;
import zblibrary.zgl.view.FirstSideViewItem;
import zuo.biao.library.base.BaseAdapter;

public class FirstSideAdapter extends BaseAdapter<FirstCategory.FirstCategorySerializable, FirstSideViewItem> {

	public FirstSideAdapter(Activity context) {
		super(context);
	}

	@Override
	public FirstSideViewItem createView(int position, ViewGroup parent) {
		return new FirstSideViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}