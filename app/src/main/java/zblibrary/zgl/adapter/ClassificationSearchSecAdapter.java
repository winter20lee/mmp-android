
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.GoodsCategory;
import zblibrary.zgl.view.ClassifSearSecoViewItem;
import zuo.biao.library.base.BaseAdapter;

/**用户adapter
 */
public class ClassificationSearchSecAdapter extends BaseAdapter<GoodsCategory.SublevelsModel, ClassifSearSecoViewItem> {
	public ClassificationSearchSecAdapter(Activity context) {
		super(context);
	}

	@Override
	public ClassifSearSecoViewItem createView(int position, ViewGroup parent) {
		return new ClassifSearSecoViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}