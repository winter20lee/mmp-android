
package zblibrary.zgl.activity;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.SplashCount;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.AESUtil;
import zuo.biao.library.util.DeviceIdUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.MD5Utils;
import zuo.biao.library.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 */
public class SplashActivity extends Activity implements OnHttpResponseListener {

	private final int APP_INIT_CODE = 1110;
	private final int DEVICE_LOGIN_CODE = 1120;
	private ImageView splash;
	private TextView splash_tv;
	private SplashCount splashCount;
	private AppInitInfo appInitInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		splash = findViewById(R.id.splash);
		splash_tv = findViewById(R.id.splash_tv);
		HttpRequest.getAppInitInfo(APP_INIT_CODE,new OnHttpResponseListenerImpl(this) );

		splashCount = new SplashCount(this,splash_tv,5000,1000);
		splashCount.start();

		splash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(appInitInfo.ads!=null && appInitInfo.ads.size()>0){
					Intent it =  MainTabActivity.createIntent(SplashActivity.this,appInitInfo.ads.get(0).link);
					startActivity(it);
					finish();
				}
			}
		});

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}


	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case APP_INIT_CODE:
				appInitInfo = GsonUtil.GsonToBean(resultData,AppInitInfo.class);
				MApplication.getInstance().setAppInitInfo(appInitInfo);
				if(StringUtil.isEmpty(HttpManager.getInstance().getToken())){
					HttpRequest.loginByDeviceId(getDeviceToken(),DEVICE_LOGIN_CODE,new OnHttpResponseListenerImpl(this) );
				}
				if(appInitInfo.ads!=null && appInitInfo.ads.size()>0){
					GlideUtil.load(this,appInitInfo.ads.get(0).imgUrl,splash);
				}
				break;
			case DEVICE_LOGIN_CODE:
				User user = GsonUtil.GsonToBean(resultData,User.class);
				MApplication.getInstance().saveCurrentUser(user);
				break;
		}

	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {

	}

	private String getDeviceToken(){
		String orgDeviceId = DeviceIdUtil.getDeviceId(this);
		String agentCode = "huawei";
		String md5Salt = "mmp2022";
		String md5DeviceId = MD5Utils.getLowerMD5Code(orgDeviceId+agentCode+md5Salt);
		String andDeviceId = md5DeviceId + "|" + orgDeviceId;
		String deviceToken = AESUtil.encryptDeviceToken(andDeviceId);
		return deviceToken;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (splashCount != null) {
			splashCount.cancel();
		}
	}
}