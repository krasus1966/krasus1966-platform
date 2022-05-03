package top.krasus1966.baiducloud.facematch;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.krasus1966.baiducloud.common.Constants;
import top.krasus1966.baiducloud.util.OkHttpUtils;

/**
 * @author Krasus1966
 * @date 2022/5/3 15:17
 **/
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private AuthService() {
    }

    /**
     * 获取权限token
     *
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth(String clientId, String clientSecret) {
        return getAuthPost(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuthPost(String ak, String sk) {
        // 获取token地址
        try {
            String result = OkHttpUtils.postForm(Constants.Url.AUTH_TOKEN_URL,
                    new Headers.Builder().build(),
                    new FormBody.Builder()
                            .add("grant_type", "client_credentials")
                            .add("client_id", ak)
                            .add("client_secret", sk)
                            .build());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(result).get("access_token").asText();
        } catch (Exception e) {
            log.error("获取百度人像检测Token失败", e);
        }
        return null;
    }
}
