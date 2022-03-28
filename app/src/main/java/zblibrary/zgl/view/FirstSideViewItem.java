package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxu.shadowdrawable.ShadowDrawable;

import zblibrary.zgl.R;
import zblibrary.zgl.model.GoodsCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;

public class FirstSideViewItem extends BaseView<GoodsCategory>{

	public FirstSideViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_side_item_view, parent);
	}
	public LinearLayout first_side_ll;
	public TextView first_side_tv;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_side_ll = findView(R.id.first_side_ll);
		first_side_tv = findView(R.id.first_side_tv);
		ShadowDrawable.setShadowDrawable(first_side_ll, Color.parseColor("#FFFFFF"), StringUtil.dp2px(context,2),
				Color.parseColor("#26000000"), StringUtil.dp2px(context,2), 0, 0);
		return super.createView();
	}

	@Override
	public void bindView(GoodsCategory data_){
		super.bindView(data_ != null ? data_ : new GoodsCategory());
		first_side_tv.setText(data.name);
	}
}