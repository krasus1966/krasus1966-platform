package top.krasus1966.common.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author krasus1966
 * @date 2024/10/15 11:44
 **/
@Data
public class AbstractEntity implements Serializable {
    /**
     * 主键。主键
     */
    @Schema(description = "主键")
    private String id;
    /**
     * 创建人。创建人。
     */
    @Schema(description = "创建人")
    private String crtId;
    /**
     * 创建时间。创建时间。年-月-日 时:分:秒
     */
    @Schema(description = "创建时间")
    private LocalDateTime crtTime;
    /**
     * 创建IP。创建IP。示例：192.168.100.172
     */
    @Schema(description = "创建IP")
    private String crtIp;
    /**
     * 最后修改人。
     */
    @Schema(description = "最后修改人")
    private String updId;
    /**
     * 最后修改时间。年-月-日 时:分:秒
     */
    @Schema(description = "最后修改时间")
    private LocalDateTime updTime;
    /**
     * 最后修改IP。示例：192.168.100.172
     */
    @Schema(description = "最后修改IP")
    private String updIp;

    /**
     * 业务ID
     */
    @Schema(description = "租户id")
    private String tenantId;

    // 针对数据权限，创建部门
    @Schema(description = "创建部门")
    private String crtDeptId;

    // 针对数据权限，修改部门
    @Schema(description = "修改部门")
    private String updDeptId;
}
