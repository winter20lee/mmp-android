package zuo.biao.library.manager;
import android.app.Activity;
import com.gyf.immersionbar.ImmersionBar;
import zuo.biao.library.R;

public class SystemBarTintManager {
    /**
     *
     * @param activity
     * @param isDarkMode true 深色 false 浅色
     */
    public static void setStatusBarMode(Activity activity,boolean isDarkMode) {
        if(isDarkMode){
            ImmersionBar.with(activity)
                    .statusBarDarkFont(true, 0.2f)
                    .statusBarColor(R.color.white)
                    .init();
        }else{
            ImmersionBar.with(activity)
                    .statusBarDarkFont(true, 0.2f)
                    .statusBarColor(R.color.white)
                    .init();
        }
    }

    public static void fullScreen(Activity activity){
        ImmersionBar.with(activity).reset()
                .fullScreen(true)
                .init();
    }
}
