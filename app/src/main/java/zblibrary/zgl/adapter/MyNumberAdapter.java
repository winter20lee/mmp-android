
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import zblibrary.zgl.model.MyNumber;
import zblibrary.zgl.view.MyNumberView;
import zuo.biao.library.base.BaseAdapter;

public class MyNumberAdapter extends BaseAdapter<MyNumber.LuckNumberListModel, MyNumberView> {

	public MyNumberAdapter(Activity context) {
		super(context);
	}

	@Override
	public MyNumberView createView(int position, ViewGroup parent) {
		return new MyNumberView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return 0 ;
	}

}