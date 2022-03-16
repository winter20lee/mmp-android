package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.GoodsCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;

public class ClassifSearSecoViewItem extends BaseView<GoodsCategory.SublevelsModel>  {
	private static final String TAG = "ClassifSearSecoViewItem";

	public ClassifSearSecoViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.calssico_search_second_item_view, parent);
	}

	public ImageView classfi_sear_seco_pic;
	public TextView classfi_sear_seco_text;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		classfi_sear_seco_pic = findView(R.id.classfi_sear_seco_pic);
		classfi_sear_seco_text = findView(R.id.classfi_sear_seco_text);
		return super.createView();
	}

	@Override
	public void bindView(GoodsCategory.SublevelsModel data_){
		super.bindView(data_ != null ? data_ : new GoodsCategory.SublevelsModel());

		GlideUtil.load(context,data.image,classfi_sear_seco_pic);
		classfi_sear_seco_text.setText(data.name);
	}
}