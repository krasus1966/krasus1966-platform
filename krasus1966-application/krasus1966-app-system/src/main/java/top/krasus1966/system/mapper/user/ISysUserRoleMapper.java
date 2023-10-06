package top.krasus1966.system.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.krasus1966.system.domain.sys_user.SysUserRole;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Mapper
public interface ISysUserRoleMapper extends BaseMapper<SysUserRole> {

}
