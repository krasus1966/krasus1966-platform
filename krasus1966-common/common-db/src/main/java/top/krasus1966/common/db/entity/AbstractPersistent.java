package top.krasus1966.common.db.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;
import top.krasus1966.common.core.constant.DateTimeConstants;
import top.krasus1966.common.db.plugins.auto_create_table.annonation.AutoTableField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Krasus1966
 * @date 2021/10/26 22:41
 **/
@Data
@Accessors(chain = true)
@FieldNameConstants
public abstract class AbstractPersistent implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "主键")
    @AutoTableField(isPrimaryKey = true, comment = "id")
    private String id;

    @TableField(value = "crt_id", fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    @AutoTableField(comment = "创建人id")
    private String crtId;

    @TableField(value = "crt_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD)
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD, timezone = "GMT+8")
    @Schema(description = "创建时间")
    @AutoTableField(comment = "创建时间")
    private LocalDateTime crtTime;

    @TableField(value = "crt_ip", fill = FieldFill.INSERT)
    @Schema(description = "创建人ip")
    @AutoTableField(length = 200, comment = "创建人ip")
    private String crtIp;

    @TableField(value = "crt_dept_id", fill = FieldFill.UPDATE)
    @Schema(description = "创建人部门")
    @AutoTableField(comment = "创建部门id")
    private String crtDeptId;

    @TableField(value = "upd_id", fill = FieldFill.UPDATE)
    @Schema(description = "修改人")
    @AutoTableField(comment = "修改人id")
    private String updId;

    @TableField(value = "upd_time", fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD)
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD, timezone = "GMT+8")
    @Schema(description = "修改时间")
    @AutoTableField(comment = "修改时间")
    private LocalDateTime updTime;

    @TableField(value = "upd_ip", fill = FieldFill.UPDATE)
    @Schema(description = "修改人ip")
    @AutoTableField(length = 200, comment = "修改人ip")
    private String updIp;

    @TableField(value = "upd_dept_id", fill = FieldFill.UPDATE)
    @Schema(description = "修改人部门")
    @AutoTableField(comment = "修改部门id")
    private String updDeptId;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "逻辑删除，1已删除0未删除")
    @AutoTableField(length = 10, comment = "逻辑删除，1已删除0未删除", nullable = false)
    private Integer deleted;

    @TableField("tenant_id")
    @Schema(description = "租户id", hidden = true)
    @AutoTableField(comment = "租户id", nullable = false)
    private String tenantId;
}
