package top.krasus1966.core.event;

/**
 * @author Krasus1966
 * @date 2022/4/11 21:24
 **/
public enum BaseEventEnum {
    /**
     * 接口地址保存
     */
    URL_SAVE("url_save", "接口地址保存事件"),
    OPLOG_SAVE("oplog_save", "接口操作日志保存事件"),
    ;

    private final String key;
    private final String value;


    BaseEventEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static BaseEventEnum findEnumByKey(String key) {
        for (BaseEventEnum statusEnum : BaseEventEnum.values()) {
            if (statusEnum.getKey().equals(key)) {
                //如果需要直接返回name则更改返回类型为String,return statusEnum.name;
                return statusEnum;
            }
        }
        return null;
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }

    BaseEventEnum[] getAll() {
        return BaseEventEnum.values();
    }
}
