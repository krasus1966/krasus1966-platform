package top.krasus1966.core.util.crypto;


import cn.hutool.crypto.digest.MD5;
import top.krasus1966.core.util.sign.ISignCrypto;

import java.nio.charset.StandardCharsets;

/**
 * @author Krasus1966
 * @date 2022/10/31 23:44
 **/
public class MD5Util implements ISignCrypto {

    private volatile static MD5Util md5Util;

    private MD5Util() {
    }

    public static MD5Util getInstance() {
        if (null == md5Util) {
            synchronized (MD5Util.class) {
                if (null == md5Util) {
                    md5Util = new MD5Util();
                }
            }
        }
        return md5Util;
    }

    @Override
    public String encrypt(String content) {
        return MD5.create().digestHex(content, StandardCharsets.UTF_8);
    }
}
