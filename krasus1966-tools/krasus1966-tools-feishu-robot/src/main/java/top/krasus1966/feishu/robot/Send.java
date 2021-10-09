package top.krasus1966.feishu.robot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import top.krasus1966.feishu.robot.config.OkHttpUtils;
import top.krasus1966.feishu.robot.entity.BaseEntity;
import top.krasus1966.feishu.robot.entity.Card;
import top.krasus1966.feishu.robot.entity.base.Config;
import top.krasus1966.feishu.robot.entity.base.Header;
import top.krasus1966.feishu.robot.entity.base.Text;

import java.io.IOException;
import java.util.Arrays;

/**
 * 向飞书机器人发送消息卡片信息
 *
 * @author Krasus1966
 * @date 2021/10/9 21:44
 **/
public class Send {

    private final String url;

    /**
     * @param url webhook 地址
     */
    public Send(String url) {
        this.url = url;
    }

    /**
     * <h1>使用url向飞书机器人发送消息</h1>
     * <h2>请使用者自行实现锁，否则每次异常都会发送消息</h2>
     *
     * @param title      卡片标题
     * @param message    卡片消息
     * @param stackTrace 堆栈信息
     * @throws IOException 发送Post请求或转Json失败异常
     */
    public String sendMessageToRobotByUrl(String title, String message, String stackTrace) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String param = objectMapper.writeValueAsString(new BaseEntity()
                    .setMsgType("interactive")
                    .setCard(new Card()
                            .setConfig(new Config().setWideScreenMode(true).setEnableForward(false))
                            .setHeader(new Header().setTemplate("red").setTitle(new Text().setTag("plain_text").setContent(title)))
                            .setElements(Arrays.asList(new Text().setTag("hr"),
                                    new Text().setTag("markdown").setContent(message),
                                    new Text().setTag("hr"),
                                    new Text().setTag("markdown").setContent(stackTrace)))));
            return OkHttpUtils.post(url, param);
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
    }
}
