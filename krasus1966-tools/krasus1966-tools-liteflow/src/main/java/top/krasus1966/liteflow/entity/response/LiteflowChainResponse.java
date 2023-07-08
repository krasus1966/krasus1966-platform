package top.krasus1966.liteflow.entity.response;

import lombok.Data;
import top.krasus1966.core.web.entity.AbstractResponse;

/**
 * @author Krasus1966
 * @date 2023/7/8 23:01
 **/
@Data
public class LiteflowChainResponse extends AbstractResponse {
    private String applicationName;
    private String chainKey;
    private String chainName;
    private String elData;
}
