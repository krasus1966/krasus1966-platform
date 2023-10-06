package top.krasus1966.system.mapper.menu;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.krasus1966.system.domain.menu.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Mapper
public interface ISysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询用户菜单，联合查询
     *
     * @param userId 用户id
     * @return java.util.List<top.krasus1966.base.domain.SysMenu>
     * @method queryMenuByUserId
     * @author krasus1966
     * @date 2022/1/17 17:14
     * @description 查询用户菜单，联合查询
     */
    List<SysMenu> queryMenuByUserId(@Param("userId") String userId);


    List<SysMenu> queryMenuByUserIdAndRoleId(@Param("userId") String userId,@Param("roleId")String roleId);

    Page<SysMenu> queryPageMenu(@Param(Constants.WRAPPER) Wrapper<SysMenu> wrapper, IPage<SysMenu> page);
}
