
package zblibrary.zgl.activity;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.AESUtil;
import zuo.biao.library.util.DeviceIdUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.MD5Utils;
import zuo.biao.library.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 */
public class SplashActivity extends Activity implements OnHttpResponseListener {

	private final int APP_INIT_CODE = 1110;
	private final int DEVICE_LOGIN_CODE = 1120;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HttpRequest.getAppInitInfo(APP_INIT_CODE,new OnHttpResponseListenerImpl(this) );

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
		switch (requestCode){
			case APP_INIT_CODE:
				AppInitInfo appInitInfo = GsonUtil.GsonToBean(resultData,AppInitInfo.class);
				MApplication.getInstance().setAppInitInfo(appInitInfo);
				if(StringUtil.isEmpty(HttpManager.getInstance().getToken())){
					HttpRequest.loginByDeviceId(getDeviceToken(),DEVICE_LOGIN_CODE,new OnHttpResponseListenerImpl(this) );
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
}