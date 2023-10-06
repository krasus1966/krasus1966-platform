package top.krasus1966.system.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.sys_user.SysUser;
import top.krasus1966.system.domain.sys_user.SysUserRole;
import top.krasus1966.system.mapper.user.ISysUserMapper;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysUserService extends AbstractMybatisBaseService<ISysUserMapper, SysUser>{

    public Page<SysUser> queryPageRoleUser(SysUserRole role, SysUser user, Page<SysUser> page) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>(user);
        if (CharSequenceUtil.isNotBlank(role.getRoleId())) {
            wrapper.eq("sur.role_id", role.getRoleId());
        }
        return baseMapper.queryPageRoleUser(wrapper, page);
    }
}
