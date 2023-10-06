package top.krasus1966.core.crypto.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import top.krasus1966.core.web.constant.LoginConst;
import top.krasus1966.core.web.constant.SecurityConst;
import top.krasus1966.core.web.util.login.LoginUtils;

import java.util.UUID;

/**
 * AES加密
 *
 * @author Krasus1966
 * @date 2022/11/17 17:36
 **/
public class AESUtil {

    private static final AES AES_CLIENT;

    static {
        byte[] keyByte = HexUtil.decodeHex(SecurityConst.AES_KEY);
        byte[] ivByte = HexUtil.decodeHex(SecurityConst.AES_IV);
        AES_CLIENT = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
    }

    /**
     * AES加密
     *
     * @param content 需加密内容
     * @param key     密钥，32位字符串
     * @param iv      偏移量，32位字符串
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES加密
     */
    public static String encrypt(String content, String key, String iv) {
        if (CharSequenceUtil.isBlank(content) || CharSequenceUtil.isBlank(key) || CharSequenceUtil.isBlank(iv)) {
            return null;
        }
        byte[] keyByte = HexUtil.decodeHex(key);
        byte[] ivByte = HexUtil.decodeHex(iv);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return aes.encryptBase64(content);
    }

    /**
     * AES解密
     *
     * @param content 需解密内容
     * @param key     密钥，32位字符串
     * @param iv      偏移量，32位字符串
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES解密
     */
    public static String decrypt(String content, String key, String iv) {
        if (CharSequenceUtil.isBlank(content) || CharSequenceUtil.isBlank(key) || CharSequenceUtil.isBlank(iv)) {
            return null;
        }
        byte[] keyByte = HexUtil.decodeHex(key);
        byte[] ivByte = HexUtil.decodeHex(iv);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return aes.decryptStr(content);
    }

    /**
     * AES加密返回给前端
     *
     * @param content 需加密内容
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES加密
     */
    public static String encryptToWeb(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        String aesKey = LoginUtils.getOneInfo(LoginConst.INFO_USER_AES_KEY);
        if (CharSequenceUtil.isBlank(aesKey)) {
            return null;
        }
        String[] keys = aesKey.split(",");
        byte[] keyByte = HexUtil.decodeHex(keys[0]);
        byte[] ivByte = HexUtil.decodeHex(keys[1]);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return aes.encryptBase64(content);
    }

    /**
     * AES解密 从前端数据
     *
     * @param content 需解密内容
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES解密
     */
    public static String decryptFromWeb(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        String aesKey = LoginUtils.getOneInfo(LoginConst.INFO_USER_AES_KEY);
        if (CharSequenceUtil.isBlank(aesKey)) {
            return null;
        }
        String[] keys = aesKey.split(",");
        byte[] keyByte = HexUtil.decodeHex(keys[0]);
        byte[] ivByte = HexUtil.decodeHex(keys[1]);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);
        return aes.decryptStr(content);
    }

    /**
     * AES加密 传递给数据库
     *
     * @param content 需加密内容
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES加密
     */
    public static String encryptToDatabase(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        return AES_CLIENT.encryptBase64(content);
    }

    /**
     * AES解密 从数据库
     *
     * @param content 需解密内容
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES解密
     */
    public static String decryptFromDatabase(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        return AES_CLIENT.decryptStr(content);
    }

    /**
     * AES加密 从缓存Key解密后加密传给数据库
     *
     * @param content 需加密内容
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES加密
     */
    public static String newKeyGetToAes(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        String newKeyDecrypt = decryptFromWeb(content);
        if (CharSequenceUtil.isBlank(newKeyDecrypt)) {
            return null;
        }
        return encryptToDatabase(newKeyDecrypt);
    }

    /**
     * AES解密 从数据库解密后用缓存Key加密返回前端
     *
     * @param content 需解密内容
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:46
     * @description AES解密
     */
    public static String aesGetToNewKey(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return null;
        }
        String clientDecrypt = decryptFromDatabase(content);
        if (CharSequenceUtil.isBlank(clientDecrypt)) {
            return null;
        }
        return encryptToWeb(clientDecrypt);
    }

    /**
     * 生成AES对称密钥
     *
     * @return java.lang.String[]
     * @method generateKey
     * @author krasus1966
     * @date 2022/11/17 20:15
     * @description 生成AES对称密钥
     */
    public static String[] generateKey() {
        String uuid1 = UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
        String uuid2 = UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
        return new String[]{uuid1, uuid2};
    }
}
