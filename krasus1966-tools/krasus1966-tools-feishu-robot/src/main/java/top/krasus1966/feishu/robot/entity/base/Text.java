package top.krasus1966.feishu.robot.entity.base;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:13
 **/
public class Text {

    private String content;
    private String tag;

    public Text() {
    }

    public String getContent() {
        return content;
    }

    public Text setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public Text setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
