
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.FirstCategory;
import zblibrary.zgl.view.FirstCategoryViewItem;
import zuo.biao.library.base.BaseAdapter;

public class FirstCategoryAdapter extends BaseAdapter<FirstCategory.ResultModel, FirstCategoryViewItem> {

	public FirstCategoryAdapter(Activity context) {
		super(context);
	}

	@Override
	public FirstCategoryViewItem createView(int position, ViewGroup parent) {
		return new FirstCategoryViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}