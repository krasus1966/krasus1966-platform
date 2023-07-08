package top.krasus1966.liteflow.entity.response;

import lombok.Data;
import top.krasus1966.core.web.entity.AbstractResponse;

/**
 * @author Krasus1966
 * @date 2023/7/8 23:01
 **/
@Data
public class LiteflowScriptResponse extends AbstractResponse {
    private String applicationName;
    private String scriptId;
    private String scriptName;
    private String scriptData;
    private String scriptType;
    private String scriptLanguage;
}
