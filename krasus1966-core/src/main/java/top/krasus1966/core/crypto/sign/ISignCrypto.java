package top.krasus1966.core.crypto.sign;

/**
 * @author Krasus1966
 * @date 2022/10/31 22:57
 **/
public interface ISignCrypto {

    /**
     * 加密
     *
     * @param content 需加密内容
     * @return java.lang.String
     * @method encrypt
     * @author krasus1966
     * @date 2022/10/31 23:41
     * @description 加密
     */
    String encrypt(String content);
}
