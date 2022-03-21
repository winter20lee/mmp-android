package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.fragment.ClassificationSearchFragment;
import zblibrary.zgl.fragment.FirstFragment;
import zblibrary.zgl.fragment.MemberCenterFragment;
import zblibrary.zgl.fragment.MineFragment;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.base.BaseEvent;
import zuo.biao.library.manager.SystemBarTintManager;

/**应用主页
 * @use MainTabActivity.createIntent(...)
 */
public class MainTabActivity extends BaseBottomTabActivity {
	private static final String TAG = "MainTabActivity";

	public static Intent createIntent(Context context) {
		return new Intent(context, MainTabActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {// 必须调用
		super.initView();
		exitAnim = R.anim.bottom_push_out;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		selectFragment(4);
	}

	@Override
	protected int[] getTabClickIds() {
		return new int[]{R.id.llBottomTabTab0, R.id.llBottomTabTab1, R.id.llBottomTabTab2};
	}

	@Override
	protected int[][] getTabSelectIds() {
		return new int[][]{
				new int[]{R.id.ivBottomTabTab0, R.id.ivBottomTabTab1, R.id.ivBottomTabTab2},//顶部图标
				new int[]{R.id.tvBottomTabTab0, R.id.tvBottomTabTab1, R.id.tvBottomTabTab2}//底部文字
		};
	}

	@Override
	public int getFragmentContainerResId() {
		return R.id.flMainTabFragmentContainer;
	}

	@Override
	public Fragment getFragment(int position) {
		switch (position) {
		case 1:
			return MemberCenterFragment.createInstance();
		case 2:
			return MineFragment.createInstance();
		default:
			return FirstFragment.createInstance();
		}
	}

	@Override
	protected void selectTab(int position) {
		switch (position)
		{
			case 2:
				SystemBarTintManager.fullScreen(this);
				break;
			default:
				SystemBarTintManager.setStatusBarMode(this,true);
				break;
		}
	}

	@Override
	public void initData() {// 必须调用
		super.initData();

	}

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();
		EventBus.getDefault().register(this);
	}

	@Override
	public void selectFragment(int position) {
		if(!MApplication.getInstance().isLoggedIn()  && (position==4 || position==3) ){
			toActivity(LoginActivity.createIntent(context));
		}else{
			super.selectFragment(position);
		}
	}

	private long firstTime = 0;//第一次返回按钮计时
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if(secondTime - firstTime > 2000){
				showShortToast("Press again to exit");
				firstTime = secondTime;
			} else {//完全退出
				moveTaskToBack(false);//应用退到后台
				System.exit(0);
			}
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	public Fragment getFragments(int pos){
		return fragments[pos];
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMoonEvent(BaseEvent baseEvent){
		toActivity(LoginActivity.createIntent(this));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}