package zuo.biao.library.util;

import android.app.Activity;

import java.util.Stack;

public class AppManger {
    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    private static Stack<Activity> activityStack;
    private static AppManger instance;

    private AppManger() {
    }

    /**
     * 单一实例
     */
    public static AppManger getInstance() {
        if (instance == null) {
            instance = new AppManger();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
        }
    }


    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        return activityStack.lastElement();
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 1) {
            if (activityStack.lastElement().getClass().equals(cls)) {
                int j = 0;
                for (int i = 0; i < activityStack.size() - 1; i++) {
                    if (activityStack.get(i).getClass().equals(cls)) {
                        activityStack.get((i)).finish();
//                        j = i;
//                        break;
                    }

                }
//                while (j < activityStack.size() - 1) {
//                    activityStack.get((j += 1)).finish();
//                }
            }
           // activityStack.clear();
        }
    }

    /**
     * 结束所有Activity
     */
    @SuppressWarnings("WeakerAccess")
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
        }
    }


    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }


}
