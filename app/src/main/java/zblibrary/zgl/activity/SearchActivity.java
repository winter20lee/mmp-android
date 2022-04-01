
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;

import zblibrary.zgl.R;
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
	private SearchLayout msearchlayout;
	public static Intent createIntent(Context context, int range) {
		return new Intent(context, SearchActivity.class).putExtra(INTENT_RANGE, range);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity, this);
		intent = getIntent();
		initView();
		initData();
		initEvent();
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
//				//进行或联网搜索
				if(StringUtil.isEmpty(keyword)){
					showShortToast("Please enter the search content");
					return;
				}
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

	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	private void showSearchResu(String keyword){
		SearchFragment fragment = SearchFragment.createInstance(keyword);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.search_result_fg, fragment);
		transaction.commit();
	}
}