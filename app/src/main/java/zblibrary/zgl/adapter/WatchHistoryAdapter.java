package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.MyLike;
import zblibrary.zgl.view.WatchHistoryView;
import zuo.biao.library.base.BaseAdapter;

public class WatchHistoryAdapter extends BaseAdapter<MyLike.ResultBean, WatchHistoryView> {
	public WatchHistoryAdapter(Activity context) {
		super(context);
	}
	@Override
	public WatchHistoryView createView(int position, ViewGroup parent) {
		return new WatchHistoryView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).videoId;
	}

}