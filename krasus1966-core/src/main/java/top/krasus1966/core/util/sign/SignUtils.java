package top.krasus1966.core.util.sign;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import top.krasus1966.core.constant.Cache;
import top.krasus1966.core.constant.PropertiesConstants;
import top.krasus1966.core.entity.auth.UserLoginInfo;
import top.krasus1966.core.entity.web.R;
import top.krasus1966.core.util.JsonUtils;
import top.krasus1966.core.util.login.LoginUtils;
import top.krasus1966.core.util.redis_util.CacheUtil;
import top.krasus1966.core.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 使用国密的验签工具
 *
 * @author Krasus1966
 * @date 2022/9/13 17:32
 **/
@Slf4j
public class SignUtils {

    public static int sign(PropertiesConstants propertiesConstants) {
        ISignCrypto crypto =
                SignCryptoFactory.createCrypto(propertiesConstants.getSignCryptoType());
        //获取接口访问路径
        HttpServletRequest request = ServletUtils.getRequest();
        // 获取前端签名
        String webSign =
                CharSequenceUtil.trim(request.getHeader(propertiesConstants.getHeaderSignName()));
        String random =
                CharSequenceUtil.trim(request.getHeader(propertiesConstants.getHeaderSignRandomName()));
        String token =
                CharSequenceUtil.trim(request.getHeader(propertiesConstants.getHeaderUserToken()));
        if (null == webSign || "".equals(webSign)) {
            return 0;
        }
        //获取访问参数
        Map<String, String[]> params = request.getParameterMap();

        String[] webSignArray = webSign.split(",");
        String webSign1 = webSignArray[0];
        String webSign2 = webSignArray[1];
        log.debug("前端签名=" + webSign);
        log.debug("随机字符串=" + random);
        try {
            String key = Cache.Prefix.CACHE_STR + "sign:" + random;
            if (CacheUtil.keyIsExist(key)) {
                return 2;
            }
            CacheUtil.set(key, System.currentTimeMillis() + "", 180);
        } catch (Exception e) {

        }
        UserLoginInfo info = LoginUtils.getUserLoginInfo(token);
        if (info != null && CharSequenceUtil.isNotBlank(info.getInfos().get("NEWKEY"))) {
            //获取参数拼接字符串
            String paramsJoint1 = transformMapToString(params,
                    token + ":" + random + ":" + info.getInfos().get("NEWKEY"));
            //计算参数摘要
            String sign1 = crypto.encrypt(paramsJoint1);
            //对比两个签名是否一致
            if (webSign1.equalsIgnoreCase(sign1)) {
                return 1;
            }
        } else {
            String paramsJoint2 = transformMapToString(params, token + ":" + random + ":");
            String sign2 = crypto.encrypt(paramsJoint2);

            // 对比签名二是否一致
            if (webSign2.equalsIgnoreCase(sign2)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 设置签名
     *
     * @param result
     */
    public static void resSetSign(PropertiesConstants propertiesConstants, Object result) {
        ISignCrypto crypto =
                SignCryptoFactory.createCrypto(propertiesConstants.getSignCryptoType());
        HttpServletRequest request = ServletUtils.getRequest();
        if (result instanceof R) {
            try {
                HttpServletResponse response = ServletUtils.getResponse();
                String random = UUID.randomUUID().toString(true);
                response.setHeader(propertiesConstants.getHeaderSignRandomName(), random);
                String token = request.getHeader(propertiesConstants.getHeaderUserToken());
                UserLoginInfo info = LoginUtils.getUserLoginInfo(token);
                if (null == info || "".equals(info.getInfos().get("NEWKEY"))) {
                    String aesKey =
                            UUID.randomUUID().toString(true) + "," + UUID.randomUUID().toString(true);
                    response.setHeader(propertiesConstants.getHeaderSignAesKey(), aesKey);
                }
                String res = crypto.encrypt(JsonUtils.objectToJson(result) + "|" + random);
                response.setHeader(propertiesConstants.getHeaderSignName(), res);
            } catch (Exception e) {
                System.out.println("返回报文签名失败！" + request.getRequestURI());
                e.printStackTrace();
            }
        }
    }


    public static String transformMapToString(Map<String, String[]> params, String key) {
        //给参数名排序
        List<String> paramNames = new ArrayList<>(params.keySet());
        Collections.sort(paramNames);
        //按照顺序拼接字符串
        StringBuffer stringBuffer = new StringBuffer();
        for (String paramName : paramNames) {
            if (stringBuffer != null && stringBuffer.length() > 1) {
                stringBuffer.append("|");
            }
            String[] str = params.get(paramName);
            String pa = "";
            if (str != null && str.length > 0) {
                pa = str[0];
            }
            stringBuffer.append(paramName + "=" + pa);
        }
        stringBuffer.append(":" + key);
        return stringBuffer.toString();
    }
}
