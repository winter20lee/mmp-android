
package zuo.biao.library.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**通用网页Activity
 * @use toActivity(WebViewActivity.createIntent(...));
 */
public class WebViewActivity extends BaseActivity implements OnBottomDragListener, OnClickListener {
	public static final String TAG = "WebViewActivity";
	public static final int PAY_SUCCESS = 1000;
	public static final int PAY_FAIL = 1001;

	public static final String INTENT_RETURN = "INTENT_RETURN";
	public static final String INTENT_URL = "INTENT_URL";
	public static final String RESULT_TYPE = "RESULT_TYPE";
	/**获取启动这个Activity的Intent
	 * @param title
	 * @param url
	 */
	public static Intent createIntent(Context context, String title, String url) {
		return new Intent(context, WebViewActivity.class).
				putExtra(WebViewActivity.INTENT_TITLE, title).
				putExtra(WebViewActivity.INTENT_URL, url);
	}


	private String url;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity, this);//传this是为了全局滑动返回
		String title = getIntent().getStringExtra(INTENT_TITLE);
		if(StringUtil.isNotEmpty(title,true) && title.equals("Pay")){
			findView(R.id.titleBar).setVisibility(View.GONE);
		}
		url = StringUtil.getCorrectUrl(getIntent().getStringExtra(INTENT_URL));
		if (StringUtil.isNotEmpty(url, true) == false) {
			Log.e(TAG, "initData  StringUtil.isNotEmpty(url, true) == false >> finish(); return;");
			enterAnim = exitAnim = R.anim.null_anim;
			finish();
			return;
		}

		initView();
		initData();
		initEvent();

	}

	private ProgressBar pbWebView;
	private WebView wvWebView;
	@Override
	public void initView() {
		autoSetTitle();

		pbWebView = findView(R.id.pbWebView);
		wvWebView = findView(R.id.wvWebView);
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	public void initData() {

		WebSettings webSettings = wvWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		wvWebView.requestFocus();

		// 设置setWebChromeClient对象
		wvWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tvBaseTitle.setText(StringUtil.getTrimedString(title));
			}
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				pbWebView.setProgress(newProgress);
			}
		});

		wvWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				if(url.contains("https://12shop/order/pay/success")){
					setResults(PAY_SUCCESS);
				}else if(url.contains("https://12shop/order/pay/fail")){
					setResults(PAY_FAIL);
				}
				wvWebView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				tvBaseTitle.setText(StringUtil.getTrimedString(wvWebView.getUrl()));
				pbWebView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				tvBaseTitle.setText(StringUtil.getTrimedString(wvWebView.getTitle()));
				pbWebView.setVisibility(View.GONE);
			}
		});

		wvWebView.loadUrl(url);
	}

	@Override
	public void initEvent() {

		tvBaseTitle.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			if (wvWebView.canGoForward()) {
				wvWebView.goForward();
			}
			return;
		}
		onBackPressed();
	}

	@Override
	public void onReturnClick(View v) {
		finish();
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvBaseTitle) {
			toActivity(EditTextInfoWindow.createIntent(context
					, EditTextInfoWindow.TYPE_WEBSITE
					, StringUtil.getTrimedString(tvBaseTitle)
					, wvWebView.getUrl()),
					REQUEST_TO_EDIT_TEXT_WINDOW, false);
		}
	}


	@Override
	public void onBackPressed() {
		if (wvWebView.canGoBack()) {
			wvWebView.goBack();
			return;
		}

		super.onBackPressed();
	}


	@Override
	protected void onPause() {
		super.onPause();
		wvWebView.onPause();
	}

	@Override
	protected void onResume() {
		wvWebView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (wvWebView != null) {
			wvWebView.destroy();
			wvWebView = null;
		}
		super.onDestroy();
	}

	protected static final int REQUEST_TO_EDIT_TEXT_WINDOW = 1;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_EDIT_TEXT_WINDOW:
			if (data != null) {
				wvWebView.loadUrl(StringUtil.getCorrectUrl(data.getStringExtra(EditTextInfoWindow.RESULT_VALUE)));
			}
			break;
		}
	}

	private void setResults(int type) {
		intent = new Intent();
		intent.putExtra(RESULT_TYPE,type );
		setResult(RESULT_OK, intent);
		finish();
	}
}