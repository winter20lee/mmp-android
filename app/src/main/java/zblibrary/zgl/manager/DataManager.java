
package zblibrary.zgl.manager;

import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.model.User;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

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
	public final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";


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

	public void saveSearchHistory(List<String> searchHistory){
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "save searchHistory sdf == null  >> return;");
			return;
		}
		if (searchHistory == null) {
			Log.w(TAG, "save searchHistory searchHistory == null >>  searchHistory = new ArrayList();");
			searchHistory = new ArrayList<>();
		}
		SharedPreferences.Editor editor = sdf.edit();
		String Json = GsonUtil.GsonString(searchHistory);
		editor.putString(KEY_SEARCH_HISTORY, Json);
		editor.commit();
	}

	public List<String> getSearchHistory(){
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "get sdf == null >>  return;");
			return new ArrayList<>();
		}
		String json = sdf.getString(KEY_SEARCH_HISTORY, null);
		if(StringUtil.isEmpty(json)){
			return new ArrayList<>();
		}
		List<String> list  = GsonUtil.jsonToList(json,String.class);
		if(list==null){
			return  new ArrayList<>();
		}
		return list;
	}
}
