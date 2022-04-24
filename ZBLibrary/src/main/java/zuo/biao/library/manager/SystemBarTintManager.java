package zuo.biao.library.manager;
import android.app.Activity;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import zuo.biao.library.R;

public class SystemBarTintManager {
    public static void setStatusBarMode(Activity activity) {
        ImmersionBar.with(activity)
                .statusBarColor("#282828")
                .init();
    }

    public static void setStatusBarFull(Activity activity) {
        ImmersionBar.with(activity).reset()
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init();
    }
}
