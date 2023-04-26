package top.krasus1966.core.event.enums;

/**
 * @author Krasus1966
 * @date 2022/4/11 21:20
 **/
public enum EventStatusEnum {
    /**
     * 事件的状态
     */
    PENDING(0, "待处理"),
    SUCCESS(1, "处理成功"),
    FAILED(2, "处理失败");
    private final Integer code;
    private final String desc;

    EventStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EventStatusEnum valueOf(Integer code) {
        switch (code) {
            case 0:
                return PENDING;
            case 1:
                return SUCCESS;
            case 2:
                return FAILED;
            default:
                return null;
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
