
package zblibrary.zgl.manager;

import zblibrary.zgl.MApplication;
import zblibrary.zgl.model.User;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;

import android.content.Context;
import android.content.SharedPreferences;

/**数据工具类
 */
public class DataManager {
	private final String TAG = "DataManager";

	private Context context;
	private DataManager(Context context) {
		this.context = context;
	}

	private static DataManager instance;
	public static DataManager getInstance() {
		if (instance == null) {
			synchronized (DataManager.class) {
				if (instance == null) {
					instance = new DataManager(MApplication.getInstance());
				}
			}
		}
		return instance;
	}


	private String PATH_USER = "PATH_USER";
	public final String KEY_USER = "KEY_USER";
	public final String KEY_AUTO_PLAY = "KEY_AUTO_PLAY";


	/**获取当前用户id
	 * @return
	 */
	public String getUserId() {
		User user = getUser();
		return user == null ? "" : user.userInfo.userId;
	}

//	/**获取当前用户的手机号
//	 * @return
//	 */
//	public String getUserPhone() {
//		User user = getUser();
//		return user == null ? "" : user.userInfo.mobile;
//	}

	/**获取用户
	 * @return
	 */
	public User getUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "get sdf == null >>  return;");
			return null;
		}
		return GsonUtil.GsonToBean(sdf.getString(KEY_USER, null), User.class);
	}

	/**保存用户
	 * @param user
	 */
	public void saveUser(User user) {
		saveUser(context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE), user);
	}
	/**保存用户
	 * @param sdf
	 * @param user
	 */
	private void saveUser(SharedPreferences sdf, User user) {
		if (sdf == null || user == null) {
			Log.e(TAG, "saveUser sdf == null || user == null >> return;");
			return;
		}
		sdf.edit().remove(KEY_USER).putString(KEY_USER, GsonUtil.GsonString(user)).commit();
	}

	/**删除用户
	 */
	public void removeUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "removeUser sdf == null  >> return;");
			return;
		}
		sdf.edit().remove(KEY_USER).commit();
	}

	/**保存自动播放
	 */
	public void saveAutoPlayState(boolean  isAutoPlay) {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "saveUser sdf == null || user == null >> return;");
			return;
		}
		sdf.edit().remove(KEY_AUTO_PLAY).putBoolean(KEY_AUTO_PLAY, isAutoPlay).commit();
	}

	/**获取自动播放
	 */
	public boolean getAutoPlayState() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "saveUser sdf == null || user == null >> return;");
			return true;
		}
		return sdf.getBoolean(KEY_AUTO_PLAY, true);
	}

//	/**设置当前用户手机号
//	 * @param phone
//	 */
//	public void setUserPhone(String phone) {
//		User user = getUser();
//		if (user == null) {
//			user = new User();
//		}
//		user.userInfo.mobile = phone;
//		saveUser(user);
//	}

	/**设置当前用户姓名
	 * @param name
	 */
	public void setUserName(String name) {
		User user = getUser();
		if (user == null) {
			user = new User();
		}
		user.userInfo.nickName = name;
		saveUser(user);
	}
}
