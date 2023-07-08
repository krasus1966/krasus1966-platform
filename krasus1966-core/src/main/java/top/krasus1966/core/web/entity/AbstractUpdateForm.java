package top.krasus1966.core.web.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import top.krasus1966.valid.anno.group.Update;

/**
 * @author Krasus1966
 * @date 2023/6/16 23:41
 **/
@Data
public abstract class AbstractUpdateForm {
    @NotBlank(message = "id不能为空", groups = {Update.class})
    private String id;
}
