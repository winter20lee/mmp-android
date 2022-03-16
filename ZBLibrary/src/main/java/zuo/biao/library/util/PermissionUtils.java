package zuo.biao.library.util;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.ui.AlertDialog;

public class PermissionUtils {
    public static boolean checkPermission(Context context,final String... permissionGroup)
    {
        return AndPermission.hasPermissions(context,permissionGroup);
    }
    public static void requestPermission(final Context context, final PermissionQuestListener listener, final String... permissionGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AndPermission.hasPermissions(context, permissionGroup)) {
                if (listener != null)
                    listener.onGranted();
                return;
            }
            // 没有权限，申请权限
            AndPermission.with(context)
                    .runtime()
                    .permission(permissionGroup)
                    .onGranted(data -> {
                        if (listener != null)
                            listener.onGranted();
                    })
                    .onDenied(data -> {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissionGroup)) {
                            if(listener==null)
                                return;
                            if(listener.isUserDefultDialog()) {
                                showDefultDialog(listener, context,data);
                            }
                            else {
                                listener.onAlwaysDeniedPermission();
                            }
                        } else {
                            //拒绝
                            if (listener != null)
                                listener.onDenied(data);
                        }
                    })
                    .start();
        } else {
            //api 23以下不需要动态申请
            if (listener != null)
                listener.onGranted();
        }
    }

    private static void showDefultDialog(PermissionQuestListener listener, Context context,List<String> data) {
        PermissionDialogMessage permissionDialogMessage = listener.onAlwaysDeniedData();
        new AlertDialog(context, permissionDialogMessage.title, permissionDialogMessage.message, false, 0, new AlertDialog.OnDialogButtonClickListener() {
            @Override
            public void onDialogButtonClick(int requestCode, boolean isPositive) {
                if(isPositive){
                    if (listener != null)
                        listener.onDefultDialogPrositiveClick();
                }else{
                    toSettingPage(context);
                    if (listener != null)
                        listener.onDefultDialogNegativeClick();
                }
            }
        }).show();
    }

    private static void toSettingPage(Context context)
    {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
    }

    /**
     * 权限申请监听
     */
    public interface PermissionQuestListener{
        /**
         * 允许获得权限后操作
         */
        void onGranted();

        /**
         * 拒绝权限后操作
         * @param data 权限list
         */
        void onDenied(List<String> data);

        /**
         * 拒绝后是否使用默认对话框
         * @return true-使用 false-不适用
         */
        boolean isUserDefultDialog();

        /**
         * 拒绝后使用默认对话框 自定义不再提示信息
         * @return PermissionDialogMessage自定义信息类
         */
        PermissionDialogMessage onAlwaysDeniedData();

        /**
         * 默认弹窗确认按钮
         */
        void onDefultDialogPrositiveClick();

        /**
         * 默认弹窗确认取消按钮
         */
        void onDefultDialogNegativeClick();

        /**
         * 拒绝询问后自定义弹窗
         */
        void onAlwaysDeniedPermission();
    }

    public static class PermissionDialogMessage{
        /**
         * 标题
         */
        public  String title = "权限申请";
        /**
         * 描述
         */
        public  String message ="请开启权限";
        /**
         * 图标
         */
        public  int imgId = R.mipmap.ic_launcher;
        /**
         *  确定按钮文字
         */
        public  String positiveText = "去开启";
        /**
         * 取消按钮文字
         */
        public  String negativeText = "不用了";
    }
}
