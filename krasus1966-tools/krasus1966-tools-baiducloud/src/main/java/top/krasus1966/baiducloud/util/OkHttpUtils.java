package top.krasus1966.baiducloud.util;


import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2021/10/9 23:43
 **/
public class OkHttpUtils {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/form-data");

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private OkHttpUtils() {
    }

    public static String postJson(String url, Headers headers, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();
        Response response = CLIENT.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static String postForm(String url, Headers headers, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        Response response = CLIENT.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static String get(String url, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        Response response = CLIENT.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static InputStream getStream(String url, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        Response response = CLIENT.newCall(request).execute();
        return Objects.requireNonNull(response.body()).byteStream();
    }
}
