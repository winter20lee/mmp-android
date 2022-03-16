
package zblibrary.zgl.fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import zblibrary.zgl.R;
import zuo.biao.library.base.BaseTabFragment;
import zuo.biao.library.util.StringUtil;

/**分类搜索
 */
public class ClassificationSearchFragment extends BaseTabFragment implements OnClickListener {
	private String keyword;
	public static ClassificationSearchFragment createInstance(String keyword) {
		ClassificationSearchFragment fragment = new ClassificationSearchFragment();
		Bundle bundle = new Bundle();
		bundle.putString(CLASSIFICATION_SEARCH_KEYWORD, keyword);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState, R.layout.classification_search_fragment);
		argument = getArguments();
		if (argument != null) {
			keyword = argument.getString(CLASSIFICATION_SEARCH_KEYWORD, "");
		}
		initView();
		initData();
		initEvent();
		return view;
	}

	@Override
	public int getTopTabViewResId() {
		if(StringUtil.isNotEmpty(keyword,true)){
			findView(R.id.top_bar).setVisibility(View.GONE);
		}

		return super.getTopTabViewResId();
	}

	@Override
	public void initView() {//必须在onCreate方法内调用
		super.initView();
		if(StringUtil.isNotEmpty(keyword,true)){
			findView(R.id.rlBaseTab).setBackgroundColor(Color.parseColor("#ffffff"));
		}
	}

	@Override
	public void initData() {//必须在onCreate方法内调用
		super.initData();
	}


	@Override
	protected String[] getTabNames() {
		return new String[] {"Raffle", "Hot sale"};
	}

	@Override
	protected Fragment getFragment(int position) {
		if(position==0){
			return ClassificationSearchItemFragment.createInstance(CLASSIFICATION_SEARCH_TYPE_LOOT,keyword);
		}else{
			return ClassificationSearchItemFragment.createInstance(CLASSIFICATION_SEARCH_TYPE_MALL,keyword);
		}
	}

	@Override
	public void initEvent() {//必须在onCreate方法内调用
		super.initEvent();
	}



	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		default:
			break;
		}
	}

}