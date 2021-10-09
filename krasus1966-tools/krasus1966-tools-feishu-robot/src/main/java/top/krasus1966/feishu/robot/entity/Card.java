package top.krasus1966.feishu.robot.entity;

import top.krasus1966.feishu.robot.entity.base.Config;
import top.krasus1966.feishu.robot.entity.base.Header;
import top.krasus1966.feishu.robot.entity.base.Text;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:53
 **/
public class Card {
    private Config config;

    private Header header;

    private List<Text> elements;

    public Config getConfig() {
        return config;
    }

    public Card setConfig(Config config) {
        this.config = config;
        return this;
    }

    public Header getHeader() {
        return header;
    }

    public Card setHeader(Header header) {
        this.header = header;
        return this;
    }

    public List<Text> getElements() {
        return elements;
    }

    public Card setElements(List<Text> elements) {
        this.elements = elements;
        return this;
    }
}
