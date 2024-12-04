package top.krasus1966.common.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author krasus1966
 * @date 2024/11/4 14:15
 **/
@Data
public class AbstractDTO implements Serializable {
    /**
     * 主键。主键
     */
    @Schema(description = "主键")
    private String id;
}
