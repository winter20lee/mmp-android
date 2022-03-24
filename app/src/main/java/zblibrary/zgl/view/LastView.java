package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.Last;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class LastView extends BaseView<Last.ResultModel> {

	public LastView(Activity context, ViewGroup parent) {
		super(context, R.layout.last_view, parent);
	}

	public ImageView last_pic;
	public TextView last_title;
	public TextView last_price;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		last_pic = findView(R.id.last_pic);
		last_title = findView(R.id.last_title);
		last_price = findView(R.id.last_price);
		return super.createView();
	}

	@Override
	public void bindView(Last.ResultModel data_){
		super.bindView(data_ != null ? data_ : new Last.ResultModel());

		if(data!=null && data.mainImage!=null && data.mainImage.size()>0){
			GlideUtil.loadRound(context,data.mainImage.get(0),last_pic,R.dimen.dim_2);
		}

		last_title.setText(data.name);
		last_price.setText(StringUtil.changeF2Y(data.price));
	}
}