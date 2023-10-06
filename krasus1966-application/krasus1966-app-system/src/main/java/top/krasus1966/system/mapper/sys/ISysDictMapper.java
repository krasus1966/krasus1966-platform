package top.krasus1966.system.mapper.sys;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.krasus1966.system.domain.sys.SysDict;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Mapper
public interface ISysDictMapper extends BaseMapper<SysDict> {

    /**
     * 字典树查询
     *
     * @param wrapper 查询条件封装
     * @return java.util.List<top.krasus1966.base.domain.SysDict>
     * @method getTree
     * @author krasus1966
     * @date 2022/1/8 17:11
     * @description 字典树查询
     */
    List<SysDict> getTree(@Param(Constants.WRAPPER) Wrapper<SysDict> wrapper);

    /**
     * 查询子字典
     *
     * @param parentId 父id
     * @return java.util.List<top.krasus1966.base.domain.SysDict>
     * @method getChild
     * @author krasus1966
     * @date 2022/1/8 17:12
     * @description 查询子字典
     */
    List<SysDict> getChild(@Param("parentId") String parentId);
}
