package zblibrary.zgl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.bugly.Bugly;

import java.util.Random;

import im.crisp.client.Crisp;
import zblibrary.zgl.manager.DataManager;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.AppManger;
import zuo.biao.library.util.StringUtil;

/**
 * Application
 */
public class MApplication extends BaseApplication {
    private static final String TAG = "DemoApplication";

    private static MApplication context;

    public static MApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Crisp.configure(getApplicationContext(), "77a88c56-94bc-4f29-abee-f2724df4b0ac");
        //Bugly.init(getApplicationContext(), "第2步获取的AppID", false);
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                AppManger.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                AppManger.getInstance().removeActivity(activity);
            }
        });
    }


    /**
     * 获取当前用户id
     *
     * @return
     */
    public String getCurrentUserId() {
        currentUser = getCurrentUser();
        Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.userInfo.userId));
        return currentUser == null ? "" : currentUser.userInfo.userId;
    }

    /**
     * 获取当前用户phone
     *
     * @return
     */
    public boolean isBindUserPhone() {
        currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUser.userInfo.isBindPhone == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前用户头像
     *
     * @return
     */
    public String getCurrentUserAvatar() {
        currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.userInfo.headImg;
    }

    /**
     * 设置当前用户头像
     *
     * @return
     */
    public void setCurrentUserAvatar(String avatar) {
        currentUser = getCurrentUser();
        currentUser.userInfo.headImg = avatar;
        saveCurrentUser(currentUser);
    }

    /**
     * 获取当前用户昵称
     *
     * @return
     */
    public String getCurrentUserNickName() {
        currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.userInfo.nickName;
    }

    /**
     * 获取当前用户昵称
     *
     * @return
     */
    public void setCurrentUserNickName(String nickName) {
        currentUser = getCurrentUser();
        currentUser.userInfo.nickName = nickName;
        saveCurrentUser(currentUser);
    }


    /**
     * 获取当前用户简介
     *
     * @return
     */
    public String getCurrentUserPersonal() {
        currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.userInfo.personal;
    }

    /**
     * 获取当前用户简介
     *
     * @return
     */
    public void setCurrentUserPersonal(String personal) {
        currentUser = getCurrentUser();
        currentUser.userInfo.personal = personal;
        saveCurrentUser(currentUser);
    }


    /**
     * 获取当前用户生日
     *
     * @return
     */
    public String getCurrentUserBirthday() {
        currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.userInfo.birthday;
    }

    /**
     * 获取当前用户性别
     *
     * @return
     */
    public String getCurrentUserSex() {
        currentUser = getCurrentUser();
        if (currentUser != null && currentUser.userInfo.gender == 2) {
            return "女";
        }
        return "男";
    }

    /**
     * 获取当前用户Push
     *
     * @return
     */
    public int getCurrentUserPush() {
        currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.userInfo.isPushOpen;
        }
        return 0;
    }


    /**
     * 获取当前用户性别
     *
     * @return
     */
    public void setCurrentUserSex(String gender) {
        currentUser = getCurrentUser();
        if (gender.equals("女")) {
            currentUser.userInfo.gender = 2;
        } else {
            currentUser.userInfo.gender = 1;
        }
        saveCurrentUser(currentUser);
    }


    /**
     * 获取当前用户生日
     *
     * @return
     */
    public void setCurrentUserBirthday(String birthday) {
        currentUser = getCurrentUser();
        currentUser.userInfo.birthday = birthday;
        saveCurrentUser(currentUser);
    }

    /**
     * push
     *
     * @return
     */
    public void setCurrentUserPush(int state) {
        currentUser = getCurrentUser();
        currentUser.userInfo.isPushOpen = state;
        saveCurrentUser(currentUser);
    }

    /**
     * 是否是会员
     */
    public boolean isVip() {
        currentUser = getCurrentUser();
        if (currentUser == null || currentUser.userInfo == null) {
            return false;
        }
        if (currentUser.userInfo.memberLevelCode.equals("guest")) {
            return false;
        }
        return true;
    }

    public int downloadCount;
    public int playCount;
    public boolean isEditFav;
    private static User currentUser = null;
    private static AppInitInfo appInitInfo = null;

    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = DataManager.getInstance().getUser();
        }
        return currentUser;
    }

    public AppInitInfo getAppInitInfo() {
        return appInitInfo;
    }

    public void setAppInitInfo(AppInitInfo appInitInfo) {
        this.appInitInfo = appInitInfo;
        setDomains();
    }

    public void saveCurrentUser(User user) {
        if (user == null) {
            Log.e(TAG, "saveCurrentUser  currentUser == null >> return;");
            return;
        }
        if (StringUtil.isEmpty(user.userInfo.userId)) {
            Log.e(TAG, "saveCurrentUser  user.getId() <= 0" +
                    " && StringUtil.isNotEmpty(user.getName(), true) == false >> return;");
            return;
        }

        currentUser = user;
        DataManager.getInstance().saveUser(currentUser);
        HttpManager.resetHeaderToken();
    }

    public void logout() {
        currentUser = null;
        DataManager.getInstance().removeUser();
        HttpManager.resetHeaderToken();
    }


    public boolean isLoggedIn() {
        return !StringUtil.isEmpty(getCurrentUserId());
    }

    private void setDomains() {
        if (appInitInfo != null && appInitInfo.domains != null && appInitInfo.domains.size() > 0) {
            Random r = new Random();
            int pos = r.nextInt(appInitInfo.domains.size());
            HttpRequest.URL_BASE = appInitInfo.domains.get(pos);
        }
    }
}
