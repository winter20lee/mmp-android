
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.MyLike;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class WatchHistoryView extends BaseView<MyLike.ResultBean>{
	private static final String TAG = "HelpView";

	public WatchHistoryView(Activity context, ViewGroup parent) {
		super(context, R.layout.watch_history_view, parent);
	}
	public RoundImageView watch_history_img;
	public TextView watch_history_name,watch_history_label,watch_history_time;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		watch_history_img = findView(R.id.watch_history_img);
		watch_history_name = findView(R.id.watch_history_name);
		watch_history_label = findView(R.id.watch_history_label);
		watch_history_time = findView(R.id.watch_history_time);
		return super.createView();
	}

	@Override
	public void bindView(MyLike.ResultBean data_){
		super.bindView(data_ != null ? data_ : new MyLike.ResultBean());
		watch_history_img.setRadius(StringUtil.dp2px(context,2));
		GlideUtil.load(context,data.videoCoverUrl,watch_history_img);
		watch_history_name.setText(data.videoName);
		watch_history_label.setText("# "+data.videoTags);
		watch_history_time.setText(data.gmtCreate);
	}
}