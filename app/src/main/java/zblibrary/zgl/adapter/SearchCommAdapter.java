
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.SearchComm;
import zblibrary.zgl.view.SearchCommViewItem;
import zuo.biao.library.base.BaseAdapter;

/**搜索推荐
 */
public class SearchCommAdapter extends BaseAdapter<SearchComm, SearchCommViewItem> {

	public SearchCommAdapter(Activity context) {
		super(context);
	}

	@Override
	public SearchCommViewItem createView(int position, ViewGroup parent) {
		return new SearchCommViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

}