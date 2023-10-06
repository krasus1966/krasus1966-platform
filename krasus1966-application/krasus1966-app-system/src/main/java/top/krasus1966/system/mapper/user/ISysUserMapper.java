package top.krasus1966.system.mapper.user;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.krasus1966.system.domain.sys_user.SysUser;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Mapper
public interface ISysUserMapper extends BaseMapper<SysUser> {
    Page<SysUser> queryPageRoleUser(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper, IPage<SysUser> page);
}
