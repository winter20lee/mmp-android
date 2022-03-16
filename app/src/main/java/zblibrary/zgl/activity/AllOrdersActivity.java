
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.AllOrderViewPageAdapter;
import zblibrary.zgl.application.MApplication;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.StringUtil;


/**
 * 全部订单
 */
public class AllOrdersActivity extends BaseActivity implements OnBottomDragListener {

	private TabLayout tabLayout;
	private ViewPager viewPager;
	public static Intent createIntent(Context context) {
		return new Intent(context, AllOrdersActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_orders_activity, this);

		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {//必须调用
		tabLayout = findView(R.id.allorder_tablayout);
		viewPager = findView(R.id.allorder_view_pager);
	}

	@Override
	public void initData() {//必须调用
		AllOrderViewPageAdapter adapter = new AllOrderViewPageAdapter(getSupportFragmentManager(),this);
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

		toActivity(MainTabActivity.createIntent(context));
	}

}