
package zblibrary.zgl.model;

/**用户类
 * @author Lemon
 */
public class User {


	public RespUserVoModel respUserVo;
	public String token;

	public static class RespUserVoModel {
		public int id;
		public String avatar;
		public String nickname;
		public String username;
		public String mobile;
	}
}
