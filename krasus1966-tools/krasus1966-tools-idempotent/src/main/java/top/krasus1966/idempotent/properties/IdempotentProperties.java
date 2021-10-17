package top.krasus1966.idempotent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Krasus1966
 * @date 2021/10/17 21:21
 **/
@ConfigurationProperties(prefix = "krasus1966.idempotent")
public class IdempotentProperties {
    /**
     * 是否启用接口幂等性验证
     */
    private boolean enabled = false;
    private String tokenName;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
