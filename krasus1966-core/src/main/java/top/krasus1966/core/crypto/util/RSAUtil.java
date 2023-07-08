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


    public static void main(String[] args) {
    }

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG1YLQE75BCZDowcKAa1uQez5AIE0vHNDxE81Dh5TlLDYdxS/htFsoEuaoDCmYJqiTTrJ1lrvMMmvCA5byatNrpRxBHcVZQzGM3j7HgjG3yk+3CU845tlFrG7k4O51Bn+m5VcbGDBqPnuafC4V3hOnQ/u7r5vyxY9xbmc/GX3orQIDAQAB";
    public static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIbVgtATvkEJkOjBwoBrW5B7PkAgTS8c0PETzUOHlOUsNh3FL+G0WygS5qgMKZgmqJNOsnWWu8wya8IDlvJq02ulHEEdxVlDMYzePseCMbfKT7cJTzjm2UWsbuTg7nUGf6blVxsYMGo+e5p8LhXeE6dD+7uvm/LFj3FuZz8ZfeitAgMBAAECgYAGTipHokC5qGGdLOB0YnDd/RFwbMJHQ3z25SLqd0bsf6iJNF00nBtOMLIWJjl0uswT1XCNhK7EEhCNel7+96Vq1/y5B+x8WmrunMlk7Rp+aj/m/dapUIUQglyT5UdGmglhJ20CVnuv//TYtbPDGU1v38o67nXmj2TPi9EXucySEQJBAKI9U/4P92zxysSUWW8kFqIzfzSH7DE1IrI76oHM371KCiYA2g9/6OPjS7M3hPCvzPQAoCrMLTeOa5wm8TjFQ9ECQQDUwaW2xaBox4jSZI9R2pya1W0/6hkinePrApz/HpYIVCXLn4euM/Z480IwUEYJbrJttYIUCeLkHSLBAu04ohodAkAGT2ZV6l8QQLNHRBHabcfnlSMscuDv5QYtcoEjfUlyfk+4vK2+jpYLTwrhtJYaGmxZTTDtmTVlBMgQsb8OGzYRAkAMfscFwfuWy8k8Wg2UyBsUeN5ut4f8YXuGTOP9k8VJ/e8uU1M7pu+d0OOdRMmoOikeil92X9JEhXWKEmruPY79AkBa+53iN0NlWPboUZjwZqos3sJIaHyR9o+YR6/nzYakREs3MUVrgNJq5QcellUHm4YMkpTni+YdoZrX7kqbCER6";
}
