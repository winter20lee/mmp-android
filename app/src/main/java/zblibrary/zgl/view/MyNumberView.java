package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.MyNumber;
import zuo.biao.library.base.BaseView;

public class MyNumberView extends BaseView<MyNumber.LuckNumberListModel> {

	public MyNumberView(Activity context, ViewGroup parent) {
		super(context, R.layout.my_number_gird_item, parent);
	}

	public TextView ivLootBottomGridItem;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		ivLootBottomGridItem = findView(R.id.ivLootBottomGridItem);
		return super.createView();
	}

	@Override
	public void bindView(MyNumber.LuckNumberListModel data_){
		super.bindView(data_ != null ? data_ : new MyNumber.LuckNumberListModel());
		ivLootBottomGridItem.setText(data.luckNumber+"");
	}
}