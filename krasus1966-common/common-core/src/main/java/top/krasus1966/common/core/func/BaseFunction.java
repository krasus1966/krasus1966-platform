package top.krasus1966.common.core.func;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.krasus1966.common.core.constant.Constants;
import top.krasus1966.common.core.entity.CommonQuery;
import top.krasus1966.common.core.entity.R;
import top.krasus1966.common.core.enums.ActionEnum;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基类Function
 *
 * @author krasus1966
 * @date 2024/9/23 09:09
 **/
@Slf4j
public abstract class BaseFunction {

    private static final Set<String> SEARCH_TYPE = Set.of("WHERE", "ORDER", "PAGE");

    @Autowired(required = false)
    protected HttpServletRequest request;
    @Autowired(required = false)
    protected HttpServletResponse response;

    public String getTenantId() {
        return getHeader(Constants.RequestHeader.TENANT_ID_NAME);
    }

    /***
     * 获取Header参数
     * @param name
     * @return java.lang.String
     * @throws
     * @method getHeader
     * @author krasus1966
     * @date 2024/9/23
     * @description 获取Header参数
     **/
    public String getHeader(String name) {
        return this.request.getHeader(name);
    }

    /***
     * 获取Param参数
     * @param name
     * @return java.lang.String
     * @method getParameter
     * @author krasus1966
     * @date 2024/9/23
     * @description 获取Param参数
     **/
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    /***
     * 获取Param参数
     * @param name
     * @param defaultValue
     * @return java.lang.String
     * @throws
     * @method getParameter
     * @author krasus1966
     * @date 2024/9/23
     * @description 获取Param参数
     **/
    public String getParameter(String name, String defaultValue) {
        return isEmpty(this.request.getParameter(name)) ? defaultValue : this.request.getParameter(name);
    }

    /***
     * 获取Param参数Map
     * @param
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @throws
     * @method getParameterMap
     * @author krasus1966
     * @date 2024/9/23
     * @description 获取Param参数Map
     **/
    public Map<String, String> getParameterMap() {
        Map<String, String[]> map = getParameterMaps();
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (item) -> item.getValue()[0]));
    }

    /***
     * 获取Param参数Map数组
     * @param
     * @return java.util.Map<java.lang.String, java.lang.String [ ]>
     * @throws
     * @method getParameterMaps
     * @author krasus1966
     * @date 2024/9/23
     * @description 获取Param参数Map数组
     **/
    public Map<String, String[]> getParameterMaps() {
        return this.request.getParameterMap();
    }

    /***
     * 判断是否为空
     * @param obj
     * @return boolean
     * @throws
     * @method isEmpty
     * @author krasus1966
     * @date 2024/9/23
     * @description 判断是否为空
     **/
    public boolean isEmpty(Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    /***
     * 判断是否不为空
     * @param obj
     * @return boolean
     * @throws
     * @method isNotEmpty
     * @author krasus1966
     * @date 2024/9/23
     * @description 判断是否不为空
     **/
    public boolean isNotEmpty(Object obj) {
        return ObjectUtil.isNotEmpty(obj);
    }

    /***
     * 获取通用查询，由对应框架去实现具体的查询
     * @return
     */
    public CommonQuery getQuery() {
        CommonQuery query = new CommonQuery();
        for (Map.Entry<String, String[]> paramMap : this.request.getParameterMap().entrySet()) {
            if (isNotEmpty(paramMap.getKey())) {
                String[] queryKeys = paramMap.getKey().split("\\.");
                if (queryKeys.length == 0 || isEmpty(queryKeys[0]) || !SEARCH_TYPE.contains(queryKeys[0])) {
                    continue;
                }
                String[] params = paramMap.getValue();
                ActionEnum.valueOf(queryKeys[0]).execute(query, queryKeys, params);
            }
        }
        return query;
    }

    /***
     * 下载文件流请求
     * @param inputStream
     * @param fileName
     * @param fileLength
     * @return void
     * @throws
     * @method downloadFile
     * @author krasus1966
     * @date 2024/9/23
     * @description 下载文件流请求
     **/
    public void downloadFile(InputStream inputStream, String fileName, Long fileLength) {
        try {
            String range = getHeader("Range");
            long length = -1;
            long skip = -1;
            long end = fileLength - 1;
            if (range != null && range.length() > 0) {
                int idx = range.indexOf("-");
                skip = Long.parseLong(range.substring(6, idx));
                if ((idx + 1) < range.length()) {
                    end = Long.parseLong(range.substring(idx + 1));
                }
                length = end - skip + 1;
            }
            fileName = CharSequenceUtil.trim(fileName);
            if (range == null || range.length() <= 0) {//bytes=32523-32523
                response.setHeader("Content-Length", "" + fileLength);
                response.setStatus(200);
            } else {
                response.setHeader("Content-Length", "" + length);
                response.setHeader("Content-Range", "bytes " + skip + "-" + end + "/" + fileLength);
                response.setStatus(206);

            }
            if (fileName.toLowerCase().endsWith(".mp4")) {
                response.setHeader("Accept-Ranges", "bytes");
                response.setContentType("video/mp4");
            } else if (fileName.toLowerCase().endsWith(".mov")) {
                response.setHeader("Accept-Ranges", "bytes");
                response.setContentType("video/quicktime");
            } else {
                try {
                    int idx = fileName.lastIndexOf(".");
                    if (idx >= 0) {
                        String type = fileName.substring(idx + 1).toLowerCase();
                        java.util.HashMap<String, String> hash = new java.util.HashMap<String, String>();
                        hash.put("txt", "text/plain");
                        hash.put("pdf", "application/pdf");
                        hash.put("doc", "application/msword");
                        hash.put("docx", "application/msword");
                        hash.put("xls", "application/vnd.ms-excel");
                        hash.put("xlsx", "application/vnd.ms-excel");
                        hash.put("ppt", "application/vnd.ms-powerpoint");
                        hash.put("pptx", "application/vnd.ms-powerpoint");
                        hash.put("xml", "text/xml");
                        hash.put("png", "image/png");
                        hash.put("jpg", "image/jpeg");
                        hash.put("jpeg", "image/jpeg");
                        hash.put("zip", "application/x-zip-compressed");
                        if (hash.containsKey(type)) {
                            response.setContentType(hash.get(type));
                            response.setHeader("Content-Disposition",
                                    "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
                        }

                    }
                } catch (Exception e) {
                    log.error("content-type", e);
                }
            }
            if (skip > 0) {
                inputStream.skip(skip);
            }
            byte[] bs = new byte[10240];
            int len;

            while ((len = inputStream.read(bs)) != -1) {
                if (length > 0) {
                    if (length > len) {
                        response.getOutputStream().write(bs, 0, len);
                        response.getOutputStream().flush();
                        length -= len;
                    } else {
                        response.getOutputStream().write(bs, 0, (int) length);
                        break;
                    }
                } else {
                    response.getOutputStream().write(bs, 0, len);
                }
            }
            response.flushBuffer();
        } catch (Exception e) {
            setDownloadFileError(e);
        }
    }

    /***
     * 设置下载失败响应
     * @param e
     * @return void
     * @throws
     * @method setDownloadFileError
     * @author krasus1966
     * @date 2024/9/23
     * @description 设置下载失败响应
     **/
    public void setDownloadFileError(Exception e) {
        try {
            OutputStream ops = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ops.write(mapper.writeValueAsString(R.parse(500, "下载失败:" + e.getMessage(), "")).getBytes(StandardCharsets.UTF_8));
            ops.close();
        } catch (Exception ignored) {
        }
    }
}
