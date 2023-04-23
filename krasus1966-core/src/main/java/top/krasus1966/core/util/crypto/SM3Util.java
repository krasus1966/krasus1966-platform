package top.krasus1966.core.util.crypto;

import cn.hutool.crypto.digest.SM3;
import top.krasus1966.core.util.sign.ISignCrypto;

/**
 * @author Krasus1966
 * @date 2022/10/31 23:53
 **/
public class SM3Util implements ISignCrypto {
    private volatile static SM3Util sm3Util;

    private SM3Util() {
    }

    public static SM3Util getInstance() {
        if (null == sm3Util) {
            synchronized (MD5Util.class) {
                if (null == sm3Util) {
                    sm3Util = new SM3Util();
                }
            }
        }
        return sm3Util;
    }

    @Override
    public String encrypt(String content) {
        return SM3.create().digestHex(content, "UTF-8");
    }
}
