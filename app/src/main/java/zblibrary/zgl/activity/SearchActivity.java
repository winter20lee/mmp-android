
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;

import zblibrary.zgl.R;
import zblibrary.zgl.fragment.FirstLastFragment;
import zblibrary.zgl.fragment.SearchFragment;
import zblibrary.zgl.view.SearchLayout;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.SystemBarTintManager;
import zuo.biao.library.util.StringUtil;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity implements OnBottomDragListener {

	public static final String INTENT_RANGE = "INTENT_RANGE";
	public static final String INTENT_RANGE_KEY = "INTENT_RANGE_KEY";
	private SearchLayout msearchlayout;
	private int cateGoryId;
	public static Intent createIntent(Context context, int cateGoryId) {
		return new Intent(context, SearchActivity.class).putExtra(INTENT_RANGE, cateGoryId);
	}

	public static Intent createIntent(Context context, String keyword) {
		return new Intent(context, SearchActivity.class).putExtra(INTENT_RANGE_KEY, keyword);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity, this);
		intent = getIntent();
		cateGoryId = intent.getIntExtra(INTENT_RANGE,0);
		String keyword = intent.getStringExtra(INTENT_RANGE_KEY);
		initView();
		initData();
		initEvent();
		if(StringUtil.isNotEmpty(keyword,true)){
			showSearchResu(keyword,"");
		}else{
			if(cateGoryId == -88){
				showSearchZuiXin();
				return;
			}
			showSearchResu(keyword);
		}
	}

	@Override
	public void initView() {//必须调用
		msearchlayout = findView(R.id.msearchlayout);
	}


	@Override
	public void initData() {//必须调用
		msearchlayout.isSearchView(true);
		msearchlayout.SetCallBackListener(new SearchLayout.setSearchCallBackListener() {
			@Override
			public void Search(String keyword) {
////				//进行或联网搜索
//				if(StringUtil.isEmpty(keyword)){
//					showShortToast("请输入搜索内容");
//					return;
//				}
				showSearchResu(keyword);
			}
			@Override
			public void Back() {
				finish();
			}
		});
	}

	@Override
	public void initEvent() {//必须调用
		if(cateGoryId !=0){
			findView(R.id.search_back).setVisibility(View.VISIBLE);
			findView(R.id.search_back).setOnClickListener(view -> finish());
		}
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	private void showSearchResu(String keyword){
		SearchFragment fragment = SearchFragment.createInstance(keyword,cateGoryId);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.search_result_fg, fragment);
		transaction.commit();
	}

	private void showSearchZuiXin(){
		SearchFragment fragment = SearchFragment.createInstance(null,124);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.search_result_fg, fragment);
		transaction.commit();
	}

	private void showSearchResu(String keyword,String defulLike){
		SearchFragment fragment = SearchFragment.createInstance(keyword,cateGoryId,true);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.search_result_fg, fragment);
		transaction.commit();
	}
}