
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
import zblibrary.zgl.activity.SearchActivity;
import zblibrary.zgl.adapter.FirstCategoryAdapter;
import zblibrary.zgl.model.FirstTabIdEvent;
import zblibrary.zgl.model.SecondCategory;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.ExpandableGridView;
import zuo.biao.library.util.AppManger;
import zuo.biao.library.util.StringUtil;

public class FirstCategoryView extends BaseView<SecondCategory> implements View.OnClickListener {

	private List<SecondCategory.VideoListBean.ResultBean> firstHotProductList = new ArrayList<>();
	private FirstCategoryAdapter firstCategoryAdapter;
	private boolean isShowChangeButton;
	private boolean isRecommend;
	private boolean isLast;
	private OnClickChangeListener onClickChangeListener;
	private int pos;
	private int pageNum=1;
	private String tag = "";
	public FirstCategoryView(Activity context, ViewGroup parent,String tag) {
		super(context, R.layout.first_category_view, parent);
		this.tag = tag;
	}

	public FirstCategoryView(Activity context, ViewGroup parent,boolean isShowChangeButton) {
		super(context, R.layout.first_category_view, parent);
		this.isShowChangeButton = isShowChangeButton;
	}

	public FirstCategoryView(Activity context, ViewGroup parent,boolean isShowChangeButton,boolean isRecommend,boolean isLast) {
		super(context, R.layout.first_category_view, parent);
		this.isShowChangeButton = isShowChangeButton;
		this.isRecommend = isRecommend;
		this.isLast = isLast;
	}

	public void setOnClickChangeListener(OnClickChangeListener onClickChangeListener){
		this.onClickChangeListener = onClickChangeListener;
	}

	public TextView first_category_title;
	public LinearLayout first_category_more,first_bottom_more,first_bottom_change;
	private ExpandableGridView expandableGridView;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		first_category_title = findView(R.id.first_category_title);
		first_category_more = findView(R.id.first_category_more);
		expandableGridView = findView(R.id.first_gridview);
		first_bottom_more = findView(R.id.first_bottom_more);
		first_bottom_change = findView(R.id.first_bottom_change);
		firstCategoryAdapter = new FirstCategoryAdapter(context);
		expandableGridView.setAdapter(firstCategoryAdapter);
		firstCategoryAdapter.setOnItemClickListener((parent, view, position, id) -> {
			toActivity2(PlayVideoDetailsActivity.createIntent(context, id));
		});
		if(isShowChangeButton){
			findView(R.id.first_category_bottom).setVisibility(View.VISIBLE);
		}
		first_bottom_more.setOnClickListener(this);
		first_category_more.setOnClickListener(this);
		first_bottom_change.setOnClickListener(this);
		return super.createView();
	}

	@Override
	public void bindView(SecondCategory data_){
		super.bindView(data_ != null ? data_ : new SecondCategory());
		firstHotProductList.clear();
		if(data.videoPageData!=null && data.videoPageData.result!=null){
			firstHotProductList.addAll(data.videoPageData.result);
		}
		firstCategoryAdapter.refresh(firstHotProductList);
		first_category_title.setText(data.videoCatalog.name);
	}

	public void setPos(int pos){
		this.pos = pos;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.first_category_more:
			case R.id.first_bottom_more:
				if(isRecommend){
					EventBus.getDefault().post(new FirstTabIdEvent(data.videoCatalog.id));
				}else{
					if(StringUtil.isNotEmpty(tag,true)){
						toActivity(SearchActivity.createIntent(context,tag));
					}else{
						toActivity(SearchActivity.createIntent(context,data.videoCatalog.id));
					}
				}
				break;
			case R.id.first_bottom_change:
				if(onClickChangeListener!=null){
					if(isLast){
						onClickChangeListener.onClickChangeLast();
					}else{
						++pageNum;
						onClickChangeListener.onClickChangeRecom(data.videoCatalog.id,pos,pageNum);
					}
				}
				break;
		}
	}

	public interface OnClickChangeListener{
		void onClickChangeLast();
		void onClickChangeRecom(int id,int pos,int pageNum);
	}
}