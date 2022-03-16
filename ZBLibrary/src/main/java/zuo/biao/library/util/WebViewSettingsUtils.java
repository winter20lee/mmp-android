package zuo.biao.library.util;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewSettingsUtils {
    public static void  setWebView(WebView webView){
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        }else{
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }
}
