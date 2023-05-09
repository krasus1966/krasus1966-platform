package top.krasus1966.core.crypto.util;


import cn.hutool.crypto.SecureUtil;
import top.krasus1966.core.crypto.sign.ISignCrypto;

/**
 * @author Krasus1966
 * @date 2022/10/31 23:40
 **/
public class SHAUtil implements ISignCrypto {

    private volatile static SHAUtil SHAUtil;

    private SHAUtil() {
    }

    public static SHAUtil getInstance() {
        if (null == SHAUtil) {
            synchronized (SHAUtil.class) {
                if (null == SHAUtil) {
                    SHAUtil = new SHAUtil();
                }
            }
        }
        return SHAUtil;
    }

    @Override
    public String encrypt(String content) {
        return SecureUtil.sha256(content);
    }
}
