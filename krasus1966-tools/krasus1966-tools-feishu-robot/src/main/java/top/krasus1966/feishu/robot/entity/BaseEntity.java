package top.krasus1966.feishu.robot.entity;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:31
 **/
public class BaseEntity {
    private String msgType;
    private Card card;

    public BaseEntity() {
    }

    public String getMsgType() {
        return msgType;
    }

    public BaseEntity setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public Card getCard() {
        return card;
    }

    public BaseEntity setCard(Card card) {
        this.card = card;
        return this;
    }
}
