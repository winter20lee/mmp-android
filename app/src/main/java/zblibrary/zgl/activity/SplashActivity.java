
package zblibrary.zgl.activity;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.util.GsonUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 */
public class SplashActivity extends Activity implements OnHttpResponseListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HttpRequest.getAppInitInfo(0,new OnHttpResponseListenerImpl(this) );
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(MainTabActivity.createIntent(SplashActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
		}, 500);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}


	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		AppInitInfo appInitInfo = GsonUtil.GsonToBean(resultData,AppInitInfo.class);
		MApplication.getInstance().setAppInitInfo(appInitInfo);
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {

	}
}