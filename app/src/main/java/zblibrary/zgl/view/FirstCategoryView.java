
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.FirstCategoryAdapter;
import zblibrary.zgl.model.SecondCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.ExpandableGridView;

public class FirstCategoryView extends BaseView<SecondCategory> {

	private List<SecondCategory.ResultModel> firstHotProductList = new ArrayList<>();
	private FirstCategoryAdapter firstCategoryAdapter;
	private boolean isShowChangeButton;
	public FirstCategoryView(Activity context, ViewGroup parent) {
		super(context, R.layout.first_category_view, parent);
	}

	public FirstCategoryView(Activity context, ViewGroup parent,boolean isShowChangeButton) {
		super(context, R.layout.first_category_view, parent);
		this.isShowChangeButton = isShowChangeButton;
	}

	public TextView first_category_title;
	public LinearLayout first_category_more;
	private ExpandableGridView expandableGridView;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_category_title = findView(R.id.first_category_title);
		first_category_more = findView(R.id.first_category_more);
		expandableGridView = findView(R.id.first_gridview);
		firstCategoryAdapter = new FirstCategoryAdapter(context);
		expandableGridView.setAdapter(firstCategoryAdapter);
		firstCategoryAdapter.setOnItemClickListener((parent, view, position, id) -> {
			toActivity(PlayVideoDetailsActivity.createIntent(context, id));
		});
		if(isShowChangeButton){
			findView(R.id.first_change).setVisibility(View.VISIBLE);
		}
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory data_){
		super.bindView(data_ != null ? data_ : new SecondCategory());
		firstHotProductList.addAll(data.result);
		firstCategoryAdapter.refresh(firstHotProductList);
	}

	public void setTitle(String title){
		first_category_title.setText(title);
	}
}