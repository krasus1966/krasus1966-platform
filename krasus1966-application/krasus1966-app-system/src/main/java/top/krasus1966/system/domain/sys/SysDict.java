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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.db.entity.AbstractPersistent;

import java.util.List;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "sys_dict")
@ApiModel(value = "SysDictEntity对象", description = "字典表")
public class SysDict extends AbstractPersistent {

    @ApiModelProperty("父id")
    @TableField("parent_id")
    @NotBlank(message = "父id不能为空")
    private String parentId;

    @ApiModelProperty("字典编码")
    @TableField("dict_code")
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @ApiModelProperty("字典名称")
    @TableField("dict_name")
    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @ApiModelProperty("字典值")
    @TableField("dict_value")
    @NotBlank(message = "字典值不能为空")
    private String dictValue;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("状态 字典 ZT")
    @TableField("status")
    private String status;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("逻辑删除 字典 SCZT")
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private String deleted;

    @ApiModelProperty("子字典列表")
    @TableField(exist = false)
    private List<SysDict> children;

    public static final String PARENT_ID = "parent_id";

    public static final String DICT_CODE = "dict_code";

    public static final String DICT_NAME = "dict_name";

    public static final String DICT_VALUE = "dict_value";

    public static final String REMARK = "remark";


    /**
     * 通用QueryWrapper
     *
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.krasus1966.base.domain.SysDict>
     * @method getBaseWrapper
     * @author krasus1966
     * @date 2022/1/7 23:34
     * @description 通用QueryWrapper
     */
    public QueryWrapper<SysDict> createWrapper() {
        return new QueryWrapper<SysDict>()
                .eq(CharSequenceUtil.isNotBlank(parentId), PARENT_ID, parentId)
                .eq(CharSequenceUtil.isNotBlank(dictCode), DICT_CODE, dictCode)
                .like(CharSequenceUtil.isNotBlank(dictName), PARENT_ID, dictName)
                .eq(CharSequenceUtil.isNotBlank(dictValue), DICT_VALUE, dictValue)
                .eq(Constants.Deleted.DELETED, DictConst.DELETE_TYPE.DELETE_TYPE_NO);
    }
}
