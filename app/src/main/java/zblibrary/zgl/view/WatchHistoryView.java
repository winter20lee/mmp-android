
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
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
	public ImageView watch_history_select_iv;
	public RoundImageView watch_history_img;
	public TextView watch_history_name,watch_history_label,watch_history_time;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		watch_history_select_iv  = findView(R.id.watch_history_select_iv);
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
		if(MApplication.getInstance().isEditFav){
			watch_history_select_iv.setVisibility(View.VISIBLE);
		}else{
			watch_history_select_iv.setVisibility(View.GONE);
		}
		if(!data.isSele){
			watch_history_select_iv.setImageResource(R.mipmap.shopping_car_unselect);
		}else{
			watch_history_select_iv.setImageResource(R.mipmap.shopping_car_select);
		}
		watch_history_select_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				data.isSele = !data.isSele;
				if(!data.isSele){
					watch_history_select_iv.setImageResource(R.mipmap.shopping_car_unselect);
				}else{
					watch_history_select_iv.setImageResource(R.mipmap.shopping_car_select);
				}
			}
		});
	}
}