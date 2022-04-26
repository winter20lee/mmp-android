package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.fragment.FirstFragment;
import zblibrary.zgl.fragment.MemberCenterFragment;
import zblibrary.zgl.fragment.MineFragment;
import zblibrary.zgl.fragment.MyDownFilesFragment;
import zblibrary.zgl.model.RefreshDownEvent;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.base.BaseEvent;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.StringUtil;

/**应用主页
 * @use MainTabActivity.createIntent(...)
 */
public class MainTabActivity extends BaseBottomTabActivity {
	private static final String TAG = "MainTabActivity";
	private View myDownFilesFragment;
	private String url;
	private Timer timer = new Timer(true);
	public static Intent createIntent(Context context) {
		return new Intent(context, MainTabActivity.class);
	}

	public static Intent createIntent(Context context,String url) {
		return new Intent(context, MainTabActivity.class).putExtra(INTENT_ID,url);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity);
		intent = getIntent();
		if(intent!=null){
			url = intent.getStringExtra(INTENT_ID);
			if(StringUtil.isNotEmpty(url,true)){
				toActivity(WebViewActivity.createIntent(this,"",url));
			}
		}
		initView();
		initData();
		initEvent();
		timer.schedule(task, 0, 10*60*1000);
	}

	private final Handler handler  = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.arg1 == 1){
				HttpRequest.reportActive(0,null);
			}
		}
	};

	//任务
	private TimerTask task = new TimerTask() {
		public void run() {
			Message msg = new Message();
			msg.arg1 = 1;
			handler.sendMessage(msg);
		}
	};



	@Override
	public void initView() {// 必须调用
		super.initView();
		myDownFilesFragment =findViewById(R.id.fg_title);
		myDownFilesFragment.setVisibility(View.GONE);
		exitAnim = R.anim.bottom_push_out;
	}

	@Override
	protected void selectTab(int position) {
		if(position==2 ){
			try {
				MineFragment fragment = (MineFragment) fragments[2];
				fragment.onResume();
			}catch (Exception e){

			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		selectFragment(1);
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
			if(myDownFilesFragment.getVisibility() == View.VISIBLE){
				myDownFilesFragment.setVisibility(View.GONE);
				MineFragment fragment = (MineFragment) fragments[2];
				fragment.onResume();
				return true;
			}
			long secondTime = System.currentTimeMillis();
			if(secondTime - firstTime > 2000){
				showShortToast("再按一次退出应用");
				firstTime = secondTime;
			} else {//完全退出
				MyDownFilesFragment.TasksManager.getImpl().onDestroy();
				FileDownloader.getImpl().pauseAll();
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

	public void showDownFile(){
		myDownFilesFragment.setVisibility(View.VISIBLE);
		EventBus.getDefault().post(new RefreshDownEvent(false));
	}

	public void hideDownFile(){
		myDownFilesFragment.setVisibility(View.GONE);
		MineFragment fragment = (MineFragment) fragments[2];
		fragment.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}