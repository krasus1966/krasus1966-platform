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
public class LiteflowScriptUpdateForm extends AbstractUpdateForm {
    @NotBlank(message = "服务名称不能为空", groups = {Insert.class, Update.class})
    private String applicationName;

    @NotBlank(message = "脚本key不能为空", groups = {Insert.class, Update.class})
    private String scriptId;

    @NotBlank(message = "脚本名称不能为空", groups = {Insert.class, Update.class})
    private String scriptName;

    @NotBlank(message = "脚本内容不能为空", groups = {Insert.class, Update.class})
    private String scriptData;

    @NotBlank(message = "脚本类型不能为空", groups = {Insert.class, Update.class})
    private String scriptType;

    @NotBlank(message = "脚本语言不能为空", groups = {Insert.class, Update.class})
    private String scriptLanguage;
}
