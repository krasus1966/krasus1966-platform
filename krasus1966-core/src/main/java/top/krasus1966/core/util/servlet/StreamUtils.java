package top.krasus1966.core.util.servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author Krasus1966
 * @date 2021/6/25 22:30
 **/
public class StreamUtils {

    private StreamUtils() {
    }

    public static void download(InputStream inputStream, String fileName, String contentType,
                                Long fileLength, HttpServletRequest req, HttpServletResponse resp
            , int bufferSize) throws IOException {
        resp.setContentType(contentType);
        resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName
                , "UTF-8"));
        long skip = -1;
        long length = -1;
        long end = fileLength - 1;
        // 断点续传
        String range = (req.getHeader("Range"));
        if (range != null && range.length() > 0) {
            int idx = range.indexOf("-");
            skip = Long.parseLong(range.substring(6, idx));
            if ((idx + 1) < range.length()) {
                end = Long.parseLong(range.substring(idx + 1));
            }
            length = end - skip + 1;
        }
        if (range == null || range.length() <= 0) {
            resp.setHeader("Content-Length", "" + fileLength);
            resp.setStatus(200);
        } else {
            resp.setHeader("Content-Length", "" + length);
            resp.setHeader("Content-Range", "bytes " + skip + "-" + end + "/" + fileLength);
            resp.setStatus(206);
        }
        resp.flushBuffer();
        download(inputStream, resp.getOutputStream(), bufferSize, skip, length);
    }

    public static void download(InputStream in, OutputStream out, int bufferSize, long skip,
                                long length) throws IOException {
        int len;
        byte[] bs = new byte[bufferSize];
        if (skip > 0) {
            in.skip(skip);
        }
        while ((len = in.read(bs)) != -1) {
            if (length > 0) {
                if (length > len) {
                    out.write(bs, 0, len);
                    length -= len;
                } else {
                    out.write(bs, 0, (int) length);
                }
            } else {
                out.write(bs, 0, len);
            }
        }
    }
}
