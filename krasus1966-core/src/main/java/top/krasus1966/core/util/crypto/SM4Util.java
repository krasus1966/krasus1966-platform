package top.krasus1966.core.util.crypto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import top.krasus1966.core.constant.SecurityConst;


/**
 * @author Krasus1966
 * @date 2022/11/20 20:35
 **/
public class SM4Util {

    private static final SM4 SM4_CLIENT;

    static {
        SM4_CLIENT = new SM4(Mode.CBC, Padding.PKCS5Padding,
                HexUtil.decodeHex(SecurityConst.SM4_KEY), HexUtil.decodeHex(SecurityConst.SM4_KEY));
    }

    /**
     * SM4 加密
     *
     * @param content 需加密内容
     * @param key     密钥，32位字符串
     * @param iv      偏移量，32位字符串
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description SM4 加密
     */
    public static String encrypt(String content, String key, String iv) {
        if (CharSequenceUtil.isBlank(content) || CharSequenceUtil.isBlank(key) || CharSequenceUtil.isBlank(iv)) {
            return null;
        }
        byte[] keyByte = HexUtil.decodeHex(key);
        byte[] ivByte = HexUtil.decodeHex(iv);
        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return sm4.encryptBase64(content);
    }

    /**
     * SM4加密，使用系统密钥
     *
     * @param content 需加密内容
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description SM4加密，使用系统密钥
     */
    public static String encrypt(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        return SM4_CLIENT.encryptBase64(content);
    }

    /**
     * SM4解密
     *
     * @param content 需解密内容
     * @param key     密钥，32位字符串
     * @param iv      偏移量，32位字符串
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description SM4解密
     */
    public static String decrypt(String content, String key, String iv) {
        if (CharSequenceUtil.isBlank(content) || CharSequenceUtil.isBlank(key) || CharSequenceUtil.isBlank(iv)) {
            return null;
        }
        byte[] keyByte = HexUtil.decodeHex(key);
        byte[] ivByte = HexUtil.decodeHex(iv);
        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return sm4.decryptStr(content);
    }

    /**
     * SM4解密，使用系统密钥
     *
     * @param content 需解密内容
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description SM4解密，使用系统密钥
     */
    public static String decrypt(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        return SM4_CLIENT.decryptStr(content);
    }
}
