package top.krasus1966.core.db.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import top.krasus1966.core.base.constant.DateTimeConstants;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Krasus1966
 * @date 2021/10/26 22:41
 **/
@Getter
@Setter
@Accessors(chain = true)
public class AbstractPersistent implements Serializable {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "租户id", position = 1, hidden = true)
    @TableField("tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "创建人", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "crt_id", fill = FieldFill.INSERT)
    private String crtId;

    @ApiModelProperty(value = "创建时间", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "crt_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD)
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD, timezone = "GMT+8")
    @OrderBy
    private LocalDateTime crtTime;

    @ApiModelProperty(value = "创建人ip", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "crt_ip", fill = FieldFill.INSERT)
    private String crtIp;

    @ApiModelProperty(value = "修改人", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "upd_id", fill = FieldFill.UPDATE)
    private String updId;

    @ApiModelProperty(value = "修改时间", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "upd_time", fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD)
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT_STANDARD, timezone = "GMT+8")
    @OrderBy(sort = 32766)
    private LocalDateTime updTime;

    @ApiModelProperty(value = "修改人ip", notes = "正常状态下没用，不要传", hidden = true, position = 127)
    @TableField(value = "upd_ip", fill = FieldFill.UPDATE)
    private String updIp;
}
