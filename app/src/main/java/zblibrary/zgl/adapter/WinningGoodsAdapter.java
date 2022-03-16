
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.WinningGoods;
import zblibrary.zgl.view.WinningGoodsView;
import zuo.biao.library.base.BaseAdapter;

public class WinningGoodsAdapter extends BaseAdapter<WinningGoods.ResultModel, WinningGoodsView> {

	public WinningGoodsAdapter(Activity context) {
		super(context);
	}

	@Override
	public WinningGoodsView createView(int position, ViewGroup parent) {
		return new WinningGoodsView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).planId;
	}

}