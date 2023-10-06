package top.krasus1966.system.domain.sys;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.db.entity.AbstractPersistent;


/**
 * <p>
 * app版本管理
 * </p>
 *
 * @author krasus1966
 * @since 2022-03-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_app_version")
@ApiModel(value = "SysAppVersion", description = "app版本管理")
public class SysAppVersion extends AbstractPersistent {

    interface ClientType {
        String iOS = "iOS";
        String Android = "Android";
    }

    @ApiModelProperty("版本名称")
    @TableField("name")
    @NotBlank(message = "版本名称不能为空")
    private String name;

    @ApiModelProperty("软件类型")
    @TableField("client_type")
    @NotBlank(message = "软件类型不能为空")
    private String clientType;

    @ApiModelProperty("软件版本号")
    @TableField("client_version")
    @NotNull(message = "软件版本号不能为空")
    private Integer clientVersion;

    @ApiModelProperty("市场版本号")
    @TableField("market_version")
    @NotNull(message = "市场版本号不能为空")
    private String marketVersion;

    @ApiModelProperty("版本更新描述")
    @TableField(value = "remark")
    @NotNull(message = "版本更新信息不能为空")
    private String remark;

    @ApiModelProperty("第三方链接/文件id")
    @TableField("link")
    @NotBlank(message = "第三方链接/文件id不能为空")
    private String link;

    @ApiModelProperty("发布状态")
    @TableField("status")
    @NotBlank(message = "状态不能为空")
    private String status;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private String deleted;

    @ApiModelProperty("乐观锁")
    @TableField(value = "version", update = "%s+1")
    @Version
    private Integer version;


    public static final String NAME = "name";

    public static final String CLIENT_TYPE = "client_type";

    public static final String CLIENT_VERSION = "clientVersion";

    public static final String LINK = "link";

    public static final String STATUS = "status";

    
    public QueryWrapper<SysAppVersion> createWrapper() {
        return new QueryWrapper<SysAppVersion>()
                .like(CharSequenceUtil.isNotBlank(name), NAME, name)
                .eq(CharSequenceUtil.isNotBlank(clientType), CLIENT_TYPE, clientType)
                .eq(CharSequenceUtil.isNotBlank(status), STATUS, status)
                .eq(Constants.Deleted.DELETED, DictConst.DELETE_TYPE.DELETE_TYPE_NO)
                ;
    }
}
