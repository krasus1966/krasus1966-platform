package top.krasus1966.core.crypto.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import top.krasus1966.core.web.constant.SecurityConst;

/**
 * RSA加密
 *
 * @author Krasus1966
 * @date 2022/11/17 17:36
 **/
public class RSAUtil {

    private static final RSA RSA_CLIENT;

    static {
        RSA_CLIENT = new RSA(SecurityConst.RSA_PRIVATE_KEY, SecurityConst.RSA_PUBLIC_KEY);
    }

    /**
     * 私钥加密
     *
     * @param content    需加密内容
     * @param privateKey 私钥，不能为空
     * @param publicKey  公钥，可以为空
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/11/17 17:53
     * @description 私钥加密
     */
    public static String encrypt(String content, String privateKey, String publicKey) {
        RSA rsa = new RSA(privateKey, publicKey);
        return rsa.encryptBase64(content, KeyType.PublicKey);
    }

    public static String encrypt(String content) {
        return RSA_CLIENT.encryptBase64(content, KeyType.PublicKey);
    }

    /**
     * 公钥解密
     *
     * @param content    需加密内容
     * @param privateKey 私钥，可以为空
     * @param publicKey  公钥，不能为空
     * @return java.lang.String
     * @method decrypt
     * @author krasus1966
     * @date 2022/11/17 17:54
     * @description 公钥解密
     */
    public static String decrypt(String content, String privateKey, String publicKey) {
        RSA rsa = new RSA(privateKey, publicKey);
        return rsa.decryptStr(content, KeyType.PrivateKey);
    }

    public static String decrypt(String content) {
        return RSA_CLIENT.decryptStr(content, KeyType.PrivateKey);
    }
}
