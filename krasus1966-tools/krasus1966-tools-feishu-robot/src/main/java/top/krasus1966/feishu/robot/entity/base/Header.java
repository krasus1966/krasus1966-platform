package top.krasus1966.feishu.robot.entity.base;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:16
 **/
public class Header {

    private String template;

    private Text title;

    public Header() {
    }

    public String getTemplate() {
        return template;
    }

    public Header setTemplate(String template) {
        this.template = template;
        return this;
    }

    public Text getTitle() {
        return title;
    }

    public Header setTitle(Text title) {
        this.title = title;
        return this;
    }
}
