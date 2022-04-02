package zblibrary.zgl.application;

import zblibrary.zgl.manager.DataManager;
import zblibrary.zgl.model.AppInitInfo;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.StringUtil;

import android.util.Log;

import java.util.Random;

/**Application
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

	}

	
	/**获取当前用户id
	 * @return
	 */
	public String getCurrentUserId() {
		currentUser = getCurrentUser();
		Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.userInfo.userId));
		return currentUser == null ? "" : currentUser.userInfo.userId;
	}
//	/**获取当前用户phone
//	 * @return
//	 */
//	public String getCurrentUserPhone() {
//		currentUser = getCurrentUser();
//		return currentUser == null ? null : currentUser.userInfo.mobile;
//	}

//	/**获取当前用户头像
//	 * @return
//	 */
//	public String getCurrentUserAvatar() {
//		currentUser = getCurrentUser();
//		return currentUser == null ? null : currentUser.userInfo.avatar;
//	}
//
//	/**设置当前用户头像
//	 * @return
//	 */
//	public void  setCurrentUserAvatar(String avatar) {
//		currentUser = getCurrentUser();
//		currentUser.userInfo.avatar = avatar;
//		saveCurrentUser(currentUser);
//	}

	/**获取当前用户昵称
	 * @return
	 */
	public String getCurrentUserNickName() {
		currentUser = getCurrentUser();
		return currentUser == null ? null : currentUser.userInfo.nickName;
	}


	/**获取当前用户生日
	 * @return
	 */
	public String getCurrentUserBirthday() {
		currentUser = getCurrentUser();
		return currentUser == null ? null : currentUser.userInfo.birthday;
	}


	/**保存当前用户昵称
	 * @return
	 */
	public void saveCurrentUserNickName(String nickName) {
		currentUser = getCurrentUser();
		currentUser.userInfo.nickName = nickName;
		saveCurrentUser(currentUser);
	}


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

	private void setDomains(){
		if(appInitInfo!=null && appInitInfo.domains!=null &&  appInitInfo.domains.size()>0){
			Random r = new Random();
			int pos = r.nextInt(appInitInfo.domains.size());
			HttpRequest.URL_BASE = appInitInfo.domains.get(pos);
		}
	}
}
