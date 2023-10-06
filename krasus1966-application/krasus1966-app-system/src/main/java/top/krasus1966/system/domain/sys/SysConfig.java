package top.krasus1966.system.domain.sys;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_config")
@ApiModel(value = "SysConfigEntity对象", description = "系统配置表")
public class SysConfig extends AbstractPersistent {

    @ApiModelProperty("模块")
    @TableField("config_module")
    @NotBlank(message = "系统模块不能为空", groups = {Insert.class, Update.class})
    private String configModule;

    @ApiModelProperty("配置编码")
    @TableField("config_code")
    @NotBlank(message = "配置编码不能为空", groups = {Insert.class, Update.class})
    private String configCode;

    @ApiModelProperty("配置名")
    @TableField("config_name")
    @NotBlank(message = "配置名不能为空", groups = {Insert.class, Update.class})
    private String configName;

    @ApiModelProperty("配置值")
    @TableField("config_value")
    @NotBlank(message = "配置值不能为空", groups = {Insert.class, Update.class})
    private String configValue;

    @ApiModelProperty("配置说明")
    @TableField("config_remark")
    private String configRemark;

    @ApiModelProperty("排序")
    @TableField("sort")
    @NotNull(message = "排序不能为空", groups = {Insert.class, Update.class})
    private Integer sort;

    @ApiModelProperty("状态 字典 ZT")
    @TableField("status")
    @NotNull(message = "状态不能为空", groups = {Insert.class, Update.class})
    private String status;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private String deleted;


    public static final String CONFIG_MODULE = "config_module";

    public static final String CONFIG_CODE = "config_code";

    public static final String CONFIG_NAME = "config_name";

    public static final String CONFIG_VALUE = "config_value";


    /**
     * 通用BaseWrapper
     *
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.krasus1966.base.domain.SysConfig>
     * @method getBaseWrapper
     * @author krasus1966
     * @date 2022/3/3 21:58
     * @description 通用BaseWrapper
     */
    public QueryWrapper<SysConfig> createWrapper() {
        return new QueryWrapper<SysConfig>()
                .eq(CharSequenceUtil.isNotBlank(configModule), CONFIG_MODULE, configModule)
                .eq(CharSequenceUtil.isNotBlank(configCode), CONFIG_CODE, configCode)
                .like(CharSequenceUtil.isNotBlank(configName), CONFIG_NAME, configName)
                .eq(CharSequenceUtil.isNotBlank(configValue), CONFIG_VALUE, configValue)
                .eq(Constants.Deleted.DELETED, DictConst.DELETE_TYPE.DELETE_TYPE_NO);
    }
}
