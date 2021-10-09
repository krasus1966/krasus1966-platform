package top.krasus1966.feishu.robot.config;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2021/10/9 23:43
 **/
public class OkHttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient CLIENT = new OkHttpClient();

    private OkHttpUtils() {
    }

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = CLIENT.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
