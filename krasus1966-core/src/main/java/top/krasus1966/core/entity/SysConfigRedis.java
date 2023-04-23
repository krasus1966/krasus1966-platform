package top.krasus1966.core.entity;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2022/11/18 23:25
 **/
@Data
public class SysConfigRedis {
    private String configCode;
    private String configName;
    private String configValue;
}