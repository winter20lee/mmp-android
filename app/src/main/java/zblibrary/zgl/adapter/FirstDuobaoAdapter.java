
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.FirstDuobao;
import zblibrary.zgl.view.FirstDuobaoViewItem;
import zuo.biao.library.base.BaseAdapter;

public class FirstDuobaoAdapter extends BaseAdapter<FirstDuobao, FirstDuobaoViewItem> {

	public FirstDuobaoAdapter(Activity context) {
		super(context);
	}

	@Override
	public FirstDuobaoViewItem createView(int position, ViewGroup parent) {
		return new FirstDuobaoViewItem(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).lootId;
	}

}