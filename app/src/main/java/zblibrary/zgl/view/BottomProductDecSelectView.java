package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.ProductDesSelectBottom;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.FlowLayout;
public class BottomProductDecSelectView extends BaseView<ProductDesSelectBottom.AttributionListModel> {

	private ViewGroup parent;
	private View.OnClickListener radioButtonListener;
	public String seleResult;
	private ItemListener itemListener;
	public BottomProductDecSelectView(Activity context, ViewGroup parent,ItemListener itemListener) {
		super(context, R.layout.bottom_product_dec_sele_item, parent);
		this.parent = parent;
		this.itemListener = itemListener;
		radioButtonListener = v -> {
			for(int i=0;i<flBottomProductItem.getChildCount();i++){
				RadioButton radioButton = (RadioButton) flBottomProductItem.getChildAt(i);
				radioButton.setChecked(false);
//				data.valueList.get(i).isSelect=false;
			}
				((RadioButton)v).setChecked(true);
			int pos = (int) v.getTag();
			seleResult =  data.attributionValName.get(pos);
			if(itemListener!=null){
				itemListener.onItemListener();
			}
		};
	}

	public TextView ivBottomProductItem;
	public FlowLayout flBottomProductItem;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		ivBottomProductItem = findView(R.id.ivBottomProductItem);
		flBottomProductItem = findView(R.id.flBottomProductItem);
		return super.createView();
	}

	@Override
	public void bindView(ProductDesSelectBottom.AttributionListModel data_){
		super.bindView(data_ != null ? data_ : new ProductDesSelectBottom.AttributionListModel());
		ivBottomProductItem.setText(data.attributionKeyName);
		LayoutInflater mInflater = LayoutInflater.from(context);
		for (int i = 0; i < data.attributionValName.size(); i++)
		{
			RadioButton radioButton = (RadioButton) mInflater.inflate(R.layout.pro_dec_sel_bot_item,parent, false);
			radioButton.setText(data.attributionValName.get(i));
			radioButton.setOnClickListener(radioButtonListener);
			radioButton.setTag(i);
			if(i==0){
				radioButton.setChecked(true);
				seleResult =  data.attributionValName.get(0);
			}
			flBottomProductItem.addView(radioButton);
		}
	}

	public interface ItemListener{
		void onItemListener();
	}
}