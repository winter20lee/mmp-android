package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.Customize;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;

public class BottomGridView extends BaseView<Customize> {

	public BottomGridView(Activity context, ViewGroup parent) {
		super(context, R.layout.bottom_gird_item, parent);
	}

	public ImageView ivUserViewHead;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		ivUserViewHead = findView(R.id.ivBottomGridItem);
		return super.createView();
	}

	@Override
	public void bindView(Customize data_){
		super.bindView(data_ != null ? data_ : new Customize());
		GlideUtil.loadCircle(context,data.getValue(),ivUserViewHead);
		if(data.isSelect()){
			ivUserViewHead.setSelected(true);
		}else{
			ivUserViewHead.setSelected(false);
		}
	}
}