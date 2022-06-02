package zuo.biao.library.base;

import okhttp3.OkHttpClient;
import zuo.biao.library.R;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.DownUtils;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.SmartRefreshUtils;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**基础Application
 * @author Lemon
 * @see #init
 * @use extends BaseApplication 或 在你的Application的onCreate方法中BaseApplication.init(this);
 */
public class BaseApplication extends Application {
	private static final String TAG = "BaseApplication";

	public BaseApplication() {
	}
	
	private static Application instance;
	public static Application getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "项目启动 >>>>>>>>>>>>>>>>>>>> \n\n");
		
		init(this);
		ViewTarget.setTagId(R.id.glide_tag);
	}

	/**初始化方法
	 * @param application
	 * @must 调用init方法且只能调用一次，如果extends BaseApplication会自动调用
	 */
	public static void init(Application application) {
		instance = application;
		if (instance == null) {
			Log.e(TAG, "\n\n\n\n\n !!!!!! 调用BaseApplication中的init方法，instance不能为null !!!" +
					"\n <<<<<< init  instance == null ！！！ >>>>>>>> \n\n\n\n");
		}
		
		DataKeeper.init(instance);
		SettingUtil.init(instance);
		SmartRefreshUtils.init(instance);
		DownUtils.init(instance);
		Glide.get(instance).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(getNoCheckOkHttpClient()));
	}

	/**获取应用名
	 * @return
	 */
	public String getAppName() {
		return getResources().getString(R.string.app_name);
	}
	/**获取应用版本名(显示给用户看的)
	 * @return
	 */
	public String getAppVersion() {
		return getResources().getString(R.string.app_version);
	}


	public static OkHttpClient getNoCheckOkHttpClient() {
		SSLSocketFactory ssl = getNoCheckSSLSocketFactory();
		X509TrustManager trustManager = getTrustManager();
		return new OkHttpClient.Builder()
				.connectTimeout(TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS)
				.readTimeout(TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS)
				.writeTimeout(TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS)
				.sslSocketFactory(ssl, trustManager)
				.hostnameVerifier((hostname, session) -> true)
				.retryOnConnectionFailure(true)
				.build();
	}

	public static SSLSocketFactory getNoCheckSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] {getTrustManager()}, new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得信任管理器TrustManager,不做任何校验
	 *
	 * @return X509TrustManager
	 */
	public static X509TrustManager getTrustManager() {
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] serverX509Certificates, String s) {}

			/**
			 * 只支持正序或者逆序存放的证书链，如果证书链顺序打乱的将不支持 我们以下认定x509Certificates数组里从0-end如果是设备证书到ca root证书是正序的
			 * 反之是倒序的
			 */
			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

}
