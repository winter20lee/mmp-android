package zuo.biao.library.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public MD5Utils() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf (iRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer ();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append (byteToArrayString (bByte[i]));
        }
        return sBuffer.toString ();
    }

    /**
     * 小写md5
     * @param strObj
     * @return
     */
    public static String getLowerMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String (strObj);
            MessageDigest md = MessageDigest.getInstance ("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString (md.digest (strObj.getBytes ()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace ();
        }
        return resultString;
    }

    /**
     * 大写的md5
     */
    public static String getHugeMD5Cod(String str) {
        try {
            if (str == null || str.trim ().length () == 0) {
                return "";
            }
            byte[] bytes = str.getBytes ();
            MessageDigest messageDigest = MessageDigest.getInstance ("MD5");
            messageDigest.update (bytes);
            bytes = messageDigest.digest ();
            StringBuilder sb = new StringBuilder ();
            for (int i = 0; i < bytes.length; i++) {
                sb.append (HEX_DIGITS[(bytes[i] & 0xf0) >> 4] + ""
                        + HEX_DIGITS[bytes[i] & 0xf]);
            }
            return sb.toString ();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }
        return "";
    }
}
