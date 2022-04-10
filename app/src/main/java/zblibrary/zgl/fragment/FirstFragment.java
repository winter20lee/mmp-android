package zblibrary.zgl.fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.FirstSideWindow;
import zblibrary.zgl.activity.SearchActivity;
import zblibrary.zgl.activity.WatchHistoryActivity;
import zblibrary.zgl.adapter.FirstTabLayoutAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.FirstTabIdEvent;
import zblibrary.zgl.model.FirstTabPosEvent;
import zblibrary.zgl.model.FirstCategory;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.CustomStateImageView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.ImageAlertDialog;
import zuo.biao.library.ui.TextAlertDialog;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**首页
 */
public class FirstFragment extends BaseFragment implements OnClickListener,
		OnHttpResponseListener {
	public static final int REQUEST_GOODSCATEGORY = 110000;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FirstTabLayoutAdapter adapter;
	private List<FirstCategory> firstCategoryList = new ArrayList<>();
	private List<FirstCategory.FirstCategorySerializable> firstCategorySerializables = new ArrayList<>();
	public static FirstFragment createInstance() {
		return new FirstFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.first_fragment);
		EventBus.getDefault().register(this);
		initView();
		initData();
		initEvent();
		showDialog();
		return view;
	}


	@Override
	public void initView() {//必须调用
		findView(R.id.et_searchtext_search,this);
		findView(R.id.button_history,this);
		findView(R.id.first_side,this);
		tabLayout = findView(R.id.classfi_ser_lot_tablayout);
		viewPager = findView(R.id.classfi_ser_lot_view_pager);
	}

	@Override
	public void initData() {//必须调用
		adapter = new FirstTabLayoutAdapter(getChildFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		FirstCategory firstCategory = new FirstCategory();
		firstCategory.id=-1;
		firstCategory.name="推荐";
		firstCategory.drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.first_comm, null);
		firstCategory.drawableSelected = ResourcesCompat.getDrawable(getResources(), R.mipmap.first_comm_select, null);
		firstCategoryList.add(firstCategory);
		firstCategory = new FirstCategory();
		firstCategory.id=0;
		firstCategory.name="最新";
		firstCategory.drawable =  ResourcesCompat.getDrawable(getResources(),R.mipmap.first_new, null);
		firstCategory.drawableSelected = ResourcesCompat.getDrawable(getResources(),R.mipmap.first_new_select, null);
		firstCategoryList.add(firstCategory);
		adapter.setList(firstCategoryList);
		HttpRequest.getFirstCategory(REQUEST_GOODSCATEGORY, new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void initEvent() {//必须调用
		findView(R.id.et_searchtext_search).setFocusable(false);
		findView(R.id.et_searchtext_search).setOnClickListener(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFirstTabPosEvent(FirstTabPosEvent firstTabPosEvent){
		tabLayout.setScrollPosition(0, firstTabPosEvent.pos, true);
		viewPager.setCurrentItem(firstTabPosEvent.pos);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFirstTabIdEvent(FirstTabIdEvent firstTabIdEvent){
		int pos =-1;
		for (int i=0;i<firstCategoryList.size();i++) {
			FirstCategory category = firstCategoryList.get(i);
			if(category.id == firstTabIdEvent.id){
				pos = i;
			}
		}
		if(pos !=-1){
			tabLayout.setScrollPosition(0, pos, true);
			viewPager.setCurrentItem(pos);
		}
	}

	private void showDialog(){
		AppInitInfo appInitInfo = MApplication.getInstance().getAppInitInfo();
		if(appInitInfo!=null){
			AppInitInfo.SysNoticeBean sysNotice = appInitInfo.sysNotice;
			if(sysNotice!=null){
				String contentType = sysNotice.contentType;
				if(StringUtil.isNotEmpty(contentType,true)){
					if(contentType.equals("1")){
						new TextAlertDialog(context,sysNotice.title,sysNotice.content).show();
					}else if(contentType.equals("2")){
						new ImageAlertDialog(context,sysNotice.imgUrl,sysNotice.content).show();
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.et_searchtext_search:
				toActivity(SearchActivity.createIntent(getActivity(),0));
				break;
			case R.id.button_history:
				toActivity(WatchHistoryActivity.createIntent(getActivity()));
				break;
			case R.id.first_side:
				toActivity(FirstSideWindow.createIntent(getActivity(),firstCategorySerializables));
				break;
			default:
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_GOODSCATEGORY:
				firstCategoryList.addAll(GsonUtil.jsonToList(resultData, FirstCategory.class));
				adapter.setList(firstCategoryList);
				viewPager.setOffscreenPageLimit(firstCategoryList.size());
				for (int i = 0; i< firstCategoryList.size(); i++) {
					tabLayout.getTabAt(i).setCustomView(getTabView(i));
					FirstCategory.FirstCategorySerializable firstCategorySerializable= firstCategoryList.get(i).transData();
					firstCategorySerializables.add(firstCategorySerializable);
				}
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}
	public View getTabView(int position) {
		View view = LayoutInflater.from(context).inflate(R.layout.first_tab_item_view, null);
		TextView txt_title = (TextView) view.findViewById(R.id.first_tab_item_text);
		txt_title.setText(firstCategoryList.get(position).name);
		CustomStateImageView image_view = (CustomStateImageView) view.findViewById(R.id.first_tab_item_iv);
		if(firstCategoryList.get(position).drawable==null){
			Glide.with(context).load(firstCategoryList.get(position).icon).into(new SimpleTarget<Drawable>() {

				@Override
				public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
					firstCategoryList.get(position).drawable = resource;
				}

			});
		}
		if(firstCategoryList.get(position).drawableSelected==null){
			Glide.with(context).load(firstCategoryList.get(position).iconSelected).into(new SimpleTarget<Drawable>() {

				@Override
				public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
					firstCategoryList.get(position).drawableSelected = resource;
					image_view.setBackgroundDrawable(image_view.getResource(firstCategoryList.get(position).drawable, firstCategoryList.get(position).drawableSelected));
				}

			});
		}else{
			image_view.setBackgroundDrawable(image_view.getResource(firstCategoryList.get(position).drawable, firstCategoryList.get(position).drawableSelected));
		}

		return view;
	}


	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}