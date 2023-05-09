package top.krasus1966.core.crypto.util;

/**
 * 加密算法类型
 *
 * @author Krasus1966
 * @date 2022/10/31 23:03
 **/
public class CryptoType {

    /**
     * 签名/哈希算法加密类型
     */
    public enum SignCryptoType {
        /**
         * 签名/哈希算法加密类型
         */
        SHA_256, SM3, MD5
    }

    /**
     * 对称加密算法类型
     */
    public enum DESType {
        /**
         * 对称加密算法类型
         */
        AES, SM4
    }

    /**
     * 非对称加密算法类型
     */
    public enum RSAType {
        /**
         * 非对称加密算法类型
         */
        RSA, SM2
    }
}
