package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.executor.GlideExecutor;

import zblibrary.zgl.R;
import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.SecondCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class FirstCategoryViewItem extends BaseView<SecondCategory.VideoListBean.ResultBean>{
	private static final String TAG = "UserView";

	public FirstCategoryViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.first_category_item_view, parent);
	}

	public RoundImageView first_category_item_iv;
	public TextView first_category_item_play;
	public TextView first_category_item_longs,first_category_item_title,first_category_item_label;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_category_item_iv = findView(R.id.first_category_item_iv);
		first_category_item_play = findView(R.id.first_category_item_play);
		first_category_item_longs = findView(R.id.first_category_item_longs);
		first_category_item_title = findView(R.id.first_category_item_title);
		first_category_item_label = findView(R.id.first_category_item_label);
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory.VideoListBean.ResultBean data_){
		super.bindView(data_ != null ? data_ : new SecondCategory.VideoListBean.ResultBean());

		if(data!=null && StringUtil.isNotEmpty(data.coverUrl,true)){
			float pxValue2 = getResources().getDimension(R.dimen.dp_102);//获取对应资源文件下的dp值
			int dpValue = StringUtil.px2dip(context, pxValue2);//将px值转换成dp值
			Log.d("h_dp102", "转化屏幕高度--：" + dpValue);
			first_category_item_iv.setRadius(StringUtil.dp2px(context,2));
			GlideUtil.load(context.getApplicationContext(),data.coverUrl,first_category_item_iv,R.mipmap.banner_deful);
		}

		first_category_item_title.setText(data.name);
		if(StringUtil.isEmpty(data.length)){
			first_category_item_longs.setText(data.length+"");
		}else{
			first_category_item_longs.setText(data.length);
		}
		first_category_item_label.setText("# "+data.tag);
		first_category_item_play.setText(data.playCnt+"");
	}
}