package top.krasus1966.core.util.sign;


import top.krasus1966.core.util.crypto.CryptoType;
import top.krasus1966.core.util.crypto.MD5Util;
import top.krasus1966.core.util.crypto.SHAUtil;
import top.krasus1966.core.util.crypto.SM3Util;
import top.krasus1966.core.util.sign.exception.NotFoundCryptoException;

/**
 * @author Krasus1966
 * @date 2022/10/31 22:57
 **/
public class SignCryptoFactory {

    public static ISignCrypto createCrypto(CryptoType.SignCryptoType type) throws NotFoundCryptoException {
        if (type == CryptoType.SignCryptoType.SHA_256) {
            return SHAUtil.getInstance();
        } else if (type == CryptoType.SignCryptoType.MD5) {
            return MD5Util.getInstance();
        } else if (type == CryptoType.SignCryptoType.SM3) {
            return SM3Util.getInstance();
        } else {
            throw new NotFoundCryptoException("未知类型的签名加密算法");
        }
    }
}
