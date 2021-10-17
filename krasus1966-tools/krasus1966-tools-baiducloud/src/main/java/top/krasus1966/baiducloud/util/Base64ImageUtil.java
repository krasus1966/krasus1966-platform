package top.krasus1966.baiducloud.util;

import okhttp3.Headers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Krasus1966
 * @date 2021/10/12 14:40
 **/
public class Base64ImageUtil {
    public static String getBase64Img(String path) throws IOException {
        InputStream in = OkHttpUtils.getStream(path, new Headers.Builder().build());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Util.encode(outStream.toByteArray());
    }
}
