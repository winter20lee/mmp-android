
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.FirstCategoryAdapter;
import zblibrary.zgl.model.FirstTabIdEvent;
import zblibrary.zgl.model.SecondCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.ExpandableGridView;

public class FirstCategoryView extends BaseView<SecondCategory> implements View.OnClickListener {

	private List<SecondCategory.VideoListBean.ResultBean> firstHotProductList = new ArrayList<>();
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
	public LinearLayout first_category_more,first_bottom_more;
	private ExpandableGridView expandableGridView;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_category_title = findView(R.id.first_category_title);
		first_category_more = findView(R.id.first_category_more);
		expandableGridView = findView(R.id.first_gridview);
		first_bottom_more = findView(R.id.first_bottom_more);
		firstCategoryAdapter = new FirstCategoryAdapter(context);
		expandableGridView.setAdapter(firstCategoryAdapter);
		firstCategoryAdapter.setOnItemClickListener((parent, view, position, id) -> {
			toActivity(PlayVideoDetailsActivity.createIntent(context, id));
		});
		if(isShowChangeButton){
			findView(R.id.first_category_bottom).setVisibility(View.VISIBLE);
		}
		first_bottom_more.setOnClickListener(this);
		first_category_more.setOnClickListener(this);
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory data_){
		super.bindView(data_ != null ? data_ : new SecondCategory());
		firstHotProductList.addAll(data.videPageData.result);
		firstCategoryAdapter.refresh(firstHotProductList);
		first_category_title.setText(data.videoCatalog.name);
	}

	public void setTitle(String title){
		first_category_title.setText(title);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.first_category_more:
			case R.id.first_bottom_more:
				EventBus.getDefault().post(new FirstTabIdEvent(data.videoCatalog.id));
				break;
		}
	}
}