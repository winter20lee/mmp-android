
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Customize;
import zuo.biao.library.base.BaseView;

public class HelpView extends BaseView<Customize>{
	private static final String TAG = "HelpView";

	public HelpView(Activity context, ViewGroup parent) {
		super(context, R.layout.help_view, parent);
	}

	public TextView help_title;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		help_title = findView(R.id.help_title);
		return super.createView();
	}

	@Override
	public void bindView(Customize data_){
		super.bindView(data_ != null ? data_ : new Customize());
		help_title.setText(data.key);
		findView(R.id.divider).setVisibility(data.isEnd?View.GONE:View.VISIBLE);
	}
}