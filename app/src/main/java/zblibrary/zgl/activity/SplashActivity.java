
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		splash = findViewById(R.id.splash);
		splash_tv = findViewById(R.id.splash_tv);
		HttpRequest.getAppInitInfo(APP_INIT_CODE,new OnHttpResponseListenerImpl(this) );
		ArrayList<String> images = new ArrayList<>();
		images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20." +
				"com%2Fup%2Fallimg%2F1111%2F0Q91Q50307%2F1PQ9150307-8.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size" +
				"=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651980651&t=2416f01e6b8a91d45fd89cffb089871f");
		images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F911%2F0R415123342%2F150R4123342-6-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size" +
				"=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651980651&t=d94a3a34bfc37c48854c10a670ca40fd");
		images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F911%2F111G5133543%2F15111G33543-1.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651980651&t=2607f905437db329cab1f145aad61db1");
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				startActivity(MainTabActivity.createIntent(SplashActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//				finish();
//			}
//		}, images.size()*2000);

		splashCount = new SplashCount(this,splash_tv,splash,images,images.size()*2000,1000);
		splashCount.start();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (splashCount != null) {
			splashCount.cancel();
		}
	}
}