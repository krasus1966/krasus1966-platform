package top.krasus1966.feishu.robot.entity.base;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:14
 **/
public class Content {

    private String text;

    private String imageKey;

    public String getText() {
        return text;
    }

    public Content setText(String text) {
        this.text = text;
        return this;
    }

    public String getImageKey() {
        return imageKey;
    }

    public Content setImageKey(String imageKey) {
        this.imageKey = imageKey;
        return this;
    }
}
