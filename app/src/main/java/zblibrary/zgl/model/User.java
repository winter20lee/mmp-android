
package zblibrary.zgl.model;

/**用户类
 */
public class User {


		public String token;
		public UserInfoBean userInfo;

		public static class UserInfoBean {
			public String userId;
			public String headImg;
			public String nickName;
			public int status;
			public String birthday;
			public int gender;
			public String personal;
			public int isBindPhone;
			public String memberLevelCode;
			public String gmtMemberExpired;
			public String memberLevelName;
			public int isPushOpen = 0;
	}
}
