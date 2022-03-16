
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import zblibrary.zgl.R;
import zblibrary.zgl.adapter.MyOrderViewPageAdapter;
import zblibrary.zgl.application.MApplication;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.StringUtil;


/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements OnBottomDragListener {

	private int pos;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	public static Intent createIntent(Context context, int pos) {
		return new Intent(context, MyOrderActivity.class).putExtra(INTENT_TYPE, pos);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_activity, this);
		intent = getIntent();
		pos = intent.getIntExtra(INTENT_TYPE,pos);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {//必须调用
		tabLayout = findView(R.id.myorder_tablayout);
		viewPager = findView(R.id.myorder_view_pager);
	}


	@Override
	public void initData() {//必须调用
		MyOrderViewPageAdapter adapter = new MyOrderViewPageAdapter(getSupportFragmentManager(),this);
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(pos).select();
		viewPager.setOffscreenPageLimit(6);
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