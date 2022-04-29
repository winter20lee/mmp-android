package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.MyLike;
import zblibrary.zgl.view.WatchHistoryView;
import zuo.biao.library.base.BaseAdapter;

public class WatchHistoryAdapter extends BaseAdapter<MyLike.ResultBean, WatchHistoryView> {
	public  WatchHistoryView.ItemClickListener itemClickListener;
	public WatchHistoryAdapter(Activity context) {
		super(context);
	}
	public WatchHistoryAdapter(Activity context, WatchHistoryView.ItemClickListener itemClickListener){
		super(context);
		this.itemClickListener = itemClickListener;
	}
	@Override
	public WatchHistoryView createView(int position, ViewGroup parent) {
		WatchHistoryView watchHistoryView = new WatchHistoryView(context, parent);
		watchHistoryView.setItemClickListener(itemClickListener);
		return watchHistoryView;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).videoId;
	}

}