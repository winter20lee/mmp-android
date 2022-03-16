
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.WinningGoodsPageAdapter;
import zblibrary.zgl.application.MApplication;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.StringUtil;


/**
 * 获奖商品
 */
public class WinningGoodsActivity extends BaseActivity implements OnBottomDragListener {

	private TabLayout tabLayout;
	private ViewPager viewPager;
	public static Intent createIntent(Context context) {
		return new Intent(context, WinningGoodsActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winning_goods_activity, this);

		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {//必须调用
		tabLayout = findView(R.id.winning_goods_tablayout);
		viewPager = findView(R.id.winning_goods_view_pager);
	}

	@Override
	public void initData() {//必须调用
		WinningGoodsPageAdapter adapter = new WinningGoodsPageAdapter(getSupportFragmentManager(),this);
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.setOffscreenPageLimit(5);
	}


	@Override
	public void initEvent() {//必须调用
		findView(R.id.kefu).setOnClickListener(v -> {
			if(StringUtil.isNotEmpty(MApplication.getInstance().getServiceUrl(),true)){
				toActivity(WebViewActivity.createIntent(context,"Service",MApplication.getInstance().getServiceUrl()));
			}
		});
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

}