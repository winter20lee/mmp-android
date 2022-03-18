
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Customize;
import zuo.biao.library.base.BaseView;

public class WatchHistoryView extends BaseView<Customize>{
	private static final String TAG = "HelpView";

	public WatchHistoryView(Activity context, ViewGroup parent) {
		super(context, R.layout.watch_history_view, parent);
	}

	public TextView watch_history_name;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		watch_history_name = findView(R.id.watch_history_name);
		return super.createView();
	}

	@Override
	public void bindView(Customize data_){
		super.bindView(data_ != null ? data_ : new Customize());
		watch_history_name.setText(data.key);
	}
}