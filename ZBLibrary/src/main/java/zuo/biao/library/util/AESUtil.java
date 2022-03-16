
package zuo.biao.library.util;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
	private static final String AES = "AES";
	private static final String KEY = "j}aZR+J9D2*ocll3";
	private static final String IV = "la#dLL!~MlJ#AN0)";
	private static final String CHARSETNAME = "UTF-8";
	/**
	 * 加密
	 *
	 * @param cleartext
	 * @return
	 */
	public static String encrypt( String cleartext) {
		try
		{
			byte[] strArr = cleartext.getBytes(CHARSETNAME);
			SecretKey secretKey = new SecretKeySpec(KEY.getBytes(CHARSETNAME), AES);
			IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(CHARSETNAME));
			Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			byte[] arrByte  = cipher.doFinal(strArr);
			String result = Base64.encodeToString(arrByte,Base64.NO_WRAP);
			return result;
		} catch (Exception e) {
			return "";
		}
	}
}
