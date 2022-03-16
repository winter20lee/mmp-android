package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Customize;
import zblibrary.zgl.view.HelpView;
import zuo.biao.library.base.BaseAdapter;

public class HelpAdapter extends BaseAdapter<Customize, HelpView> {

	public HelpAdapter(Activity context) {
		super(context);
	}

	@Override
	public HelpView createView(int position, ViewGroup parent) {
		return new HelpView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}