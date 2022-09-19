
package zblibrary.zgl.activity;

import zblibrary.zgl.R;
import zblibrary.zgl.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.SplashCount;
import zuo.biao.library.base.BaseEvent;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.AESUtil;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.DeviceIdUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.MD5Utils;
import zuo.biao.library.util.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.greenrobot.eventbus.EventBus;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class SplashActivity extends Activity implements OnHttpResponseListener {

	private final int APP_INIT_CODE = 1110;
	private final int DEVICE_LOGIN_CODE = 1120;
	private ImageView splash;
	private ImageView splash2;
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
		getAndroiodScreenProperty();

		splashCount = new SplashCount(this,splash_tv,5000,1000);
		splashCount.start();

		splash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(null!=appInitInfo && appInitInfo.ads!=null && appInitInfo.ads.size()>0){
					Intent it =  MainTabActivity.createIntent(SplashActivity.this,appInitInfo.ads.get(0).link);
					startActivity(it);
					finish();
				}
			}
		});

		splash_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent it =  MainTabActivity.createIntent(SplashActivity.this);
				startActivity(it);
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		HttpRequest.getAppInitInfo(APP_INIT_CODE,new OnHttpResponseListenerImpl(new OnHttpResponseListener() {
			@Override
			public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
				switch (requestCode){
					case APP_INIT_CODE:
						Log.d("h_bl", "requestCode开始：" + resultData);
						appInitInfo = GsonUtil.GsonToBean(resultData,AppInitInfo.class);
						MApplication.getInstance().setAppInitInfo(appInitInfo);
						HttpRequest.loginByDeviceId(getDeviceToken(),DEVICE_LOGIN_CODE,new OnHttpResponseListenerImpl(this) );
						if(appInitInfo.ads!=null && appInitInfo.ads.size()>0){
							GlideUtil.load(SplashActivity.this,appInitInfo.ads.get(0).imgUrl,splash);
						}
						break;
					case DEVICE_LOGIN_CODE:
						Log.d("h_bl", "requestCode设备登录：" + resultData);
						User user = GsonUtil.GsonToBean(resultData,User.class);
						MApplication.getInstance().saveNewToken(user.token);
						MApplication.getInstance().saveCurrentUser(user);
						break;
				}
			}

			@Override
			public void onHttpError(int requestCode, Exception e, String message) {

			}
		}) );
	}

	public void getAndroiodScreenProperty() {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;         // 屏幕宽度（像素）
		int height = dm.heightPixels;       // 屏幕高度（像素）
		float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
		// 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
		int heightPixels =  dm.widthPixels;
		int widthPixels =  dm.heightPixels;
		int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
		int screenHeight = (int) (height / density);// 屏幕高度(dp)
		float density2 = dm.density;
		float heightDP = heightPixels / density;
		float widthDP = widthPixels / density;
		float smallestWidthDP;
		if(widthDP < heightDP) {
			smallestWidthDP = widthDP;
		}else {
			smallestWidthDP = heightDP;
		}


		Log.d("h_bl", "屏幕宽度（像素）：" + width);
		Log.d("h_bl", "屏幕高度（像素）：" + height);
		Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
		Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
		Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
		Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
		Log.d("h_bl", "smallestWidthDP：" + smallestWidthDP);
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
				Log.d("h_bl", "requestCode开始：" + resultData);
				appInitInfo = GsonUtil.GsonToBean(resultData,AppInitInfo.class);
				MApplication.getInstance().setAppInitInfo(appInitInfo);
//				if(StringUtil.isEmpty(HttpManager.getInstance().getToken())){
				HttpRequest.loginByDeviceId(getDeviceToken(),DEVICE_LOGIN_CODE,new OnHttpResponseListenerImpl(this) );
//				}
				if(appInitInfo.ads!=null && appInitInfo.ads.size()>0){
					GlideUtil.load(this,appInitInfo.ads.get(0).imgUrl,splash);
				}
				break;
			case DEVICE_LOGIN_CODE:
				Log.d("h_bl", "requestCode设备登录：" + resultData);
				User user = GsonUtil.GsonToBean(resultData,User.class);
				MApplication.getInstance().saveNewToken(user.token);
				MApplication.getInstance().saveCurrentUser(user);

				break;
		}

	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		Log.d("h_bl", "requestCode：" + requestCode);
	}

	private String getDeviceToken(){
		String orgDeviceId = DeviceIdUtil.getDeviceId(this);
		String agentCode = HttpManager.AC_KEY;
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