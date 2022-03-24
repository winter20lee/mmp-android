package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.GoodsCategory;
import zuo.biao.library.base.BaseView;

public class FirstSideViewItem extends BaseView<GoodsCategory>{

	public FirstSideViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_side_item_view, parent);
	}

	public TextView first_side_tv;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_side_tv = findView(R.id.first_side_tv);
		return super.createView();
	}

	@Override
	public void bindView(GoodsCategory data_){
		super.bindView(data_ != null ? data_ : new GoodsCategory());
		first_side_tv.setText(data.name);
	}
}