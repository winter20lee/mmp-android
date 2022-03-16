package zuo.biao.library.util;

import android.content.Context;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import zuo.biao.library.R;

public class SmartRefreshUtils {
    public static void  init(Context context){
        ClassicsHeader.REFRESH_HEADER_PULLING = context.getString(R.string.pulling);//"下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = context.getString(R.string.loading);//"正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = context.getString(R.string.loading);//"正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = context.getString(R.string.loading);//"释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = context.getString(R.string.finish);//"刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = context.getString(R.string.failed);//"刷新失败";
        ClassicsHeader.REFRESH_HEADER_UPDATE= context.getString(R.string.header_lasttime);//"上次更新 M-d HH:mm";

        ClassicsFooter.REFRESH_FOOTER_PULLING = context.getString(R.string.loading);//"上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = context.getString(R.string.loading);//"释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = context.getString(R.string.loading);//"正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = context.getString(R.string.loading);//"正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = context.getString(R.string.finish);//"加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = context.getString(R.string.failed);//"加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = context.getString(R.string.footer_nothing);//"没有更多数据了";

    }
}
