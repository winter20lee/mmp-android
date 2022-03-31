package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.FirstLast;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class FirstLastView extends BaseView<FirstLast.ResultBean> {

	public FirstLastView(Activity context, ViewGroup parent) {
		super(context, R.layout.first_last_view, parent);
	}

	public ImageView last_pic;
	public TextView last_title;
	public TextView last_time,last_times,last_length;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		last_pic = findView(R.id.last_pic);
		last_title = findView(R.id.last_title);
		last_time = findView(R.id.last_time);
		last_times = findView(R.id.last_times);
		last_length = findView(R.id.last_length);
		return super.createView();
	}

	@Override
	public void bindView(FirstLast.ResultBean data_){
		super.bindView(data_ != null ? data_ : new FirstLast.ResultBean());
		if(data!=null && StringUtil.isNotEmpty(data.coverUrl,true)){
			GlideUtil.loadRound(context,data.coverUrl,last_pic,R.dimen.dim_2);
		}
		last_title.setText(data.name);
		last_time.setText(data.name);
		last_times.setText(data.name);
		last_length.setText(data.name);
	}
}