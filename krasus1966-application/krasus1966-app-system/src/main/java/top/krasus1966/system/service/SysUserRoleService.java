package top.krasus1966.system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.sys_user.SysUserRole;
import top.krasus1966.system.mapper.user.ISysUserRoleMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysUserRoleService extends AbstractMybatisBaseService<ISysUserRoleMapper, SysUserRole>{

    /**
     * 保存用户角色信息
     *
     * @param obj 用户角色对象
     * @return java.lang.Boolean
     * @method saveUserRole
     * @author krasus1966
     * @date 2022/1/8 20:43
     * @description 保存用户角色信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserRole(SysUserRole obj) {
        List<SysUserRole> banchList = new ArrayList<>();
        for (String userId : obj.getUserId().split(Constants.Entity.SPLIT)) {
            SysUserRole userRole = new SysUserRole(userId, obj.getRoleId());
            beforeSave(userRole);
            banchList.add(userRole);
        }
        return saveBatch(banchList);
    }

    @Override
    public void beforeSave(SysUserRole obj) {
        if (lambdaQuery()
                .eq(SysUserRole::getUserId, obj.getUserId())
                .eq(SysUserRole::getRoleId, obj.getRoleId())
                .count() > 0) {
            throw new BizException("此用户已经拥有此角色身份");
        }
    }
}
