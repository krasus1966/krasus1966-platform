package top.krasus1966.baiducloud.facematch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.Headers;
import top.krasus1966.baiducloud.common.Constants;
import top.krasus1966.baiducloud.util.Base64ImageUtil;
import top.krasus1966.baiducloud.util.OkHttpUtils;

import java.io.IOException;

/**
 * 百度人脸对比工具类
 *
 * @author krasus1966
 * @date 2021/10/16 15:32
 */
public class FaceMatchUtils {

    /**
     * 调用百度人脸对比接口
     *
     * @param authToken 百度智能云token，有效期30天
     * @param imgId1    图片1地址
     * @param imgId2    图片2地址
     * @return 人像比对相似度
     * @throws IOException
     */
    public static Float faceMatch(String authToken, String imgId1, String imgId2) throws IOException {
        //百度人脸比对
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(getJsonObject(imgId1));
        arrayNode.add(getJsonObject(imgId2));
        float rate = 0.0f;
        String result = faceMatchByBaidu(authToken, mapper.writeValueAsString(arrayNode));
        JsonNode res = mapper.readTree(result);
        rate = (float) res.get("result").get("score").asDouble();
        return rate;
    }

    /**
     * 构建发送对象
     *
     * @param path 图片地址
     * @return
     * @throws IOException
     */
    private static ObjectNode getJsonObject(String path) throws IOException {
        ObjectMapper obj = new ObjectMapper();
        ObjectNode objectNode = obj.createObjectNode();
        objectNode.put("image", Base64ImageUtil.getBase64Img(path));
        objectNode.put("image_type", "BASE64");
        objectNode.put("face_type", "LIVE");
        objectNode.put("quality_control", "NONE");
        objectNode.put("liveness_control", "NORMAL");
        return objectNode;
    }

    /**
     * 调用百度人脸对比接口
     *
     * @param accessToken token
     * @param param       参数
     * @return
     * @throws IOException
     */
    private static String faceMatchByBaidu(String accessToken, String param) throws IOException {
        // 请求url
        String url = Constants.Url.FACE_MATCH_URL + "?access_token=" + accessToken;
        return OkHttpUtils.postJson(url, new Headers.Builder().add("Content-Type", "application/json").build(), param);
    }
}
