package top.krasus1966.liteflow.entity.form.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import top.krasus1966.core.web.entity.AbstractUpdateForm;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;

/**
 * @author Krasus1966
 * @date 2023/7/8 23:01
 **/
@Data
public class LiteflowChainUpdateForm extends AbstractUpdateForm {
    @NotBlank(message = "服务名称不能为空",groups = {Insert.class, Update.class})
    private String applicationName;

    @NotBlank(message = "key不能为空",groups = {Insert.class, Update.class})
    private String chainKey;

    @NotBlank(message = "编排名称不能为空",groups = {Insert.class, Update.class})
    private String chainName;

    @NotBlank(message = "编排脚本不能为空",groups = {Insert.class, Update.class})
    private String elData;
}
