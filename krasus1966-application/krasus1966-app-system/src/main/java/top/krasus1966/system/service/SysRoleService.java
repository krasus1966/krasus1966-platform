package top.krasus1966.system.service;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.menu.SysRole;
import top.krasus1966.system.domain.menu.SysRoleMenu;
import top.krasus1966.system.mapper.menu.ISysRoleMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysRoleService extends AbstractMybatisBaseService<ISysRoleMapper, SysRole> {

    private final SysRoleMenuService sysRoleMenuService;

    public SysRoleService(SysRoleMenuService sysRoleMenuService) {
        this.sysRoleMenuService = sysRoleMenuService;
    }

    @Override
    public void beforeSave(SysRole obj) {
        if (lambdaQuery()
                .ne(CharSequenceUtil.isNotBlank(obj.getId()), SysRole::getId, obj.getId())
                .eq(SysRole::getRoleName, obj.getRoleName())
                .count() > 0) {
            throw new BizException("角色已存在");
        }

    }

    @Override
    public void beforeUpdate(SysRole obj) {
        if (CharSequenceUtil.isBlank(obj.getId())) {
            throw new BizException("id不能为空");
        }
        SysRole sysRole = getById(obj.getId());
        if (DictConst.BOOL_TYPE.BOOL_TYPE_YES.equals(sysRole.getCanChange())) {
            throw new BizException("此角色不可修改");
        }
        this.beforeSave(obj);
    }

    @Override
    public void beforeDelete(String ids) {
        if (CharSequenceUtil.isBlank(ids)) {
            throw new BizException("ids不能为空！");
        }
        long cannotDeleteNum = lambdaQuery().in(SysRole::getId).eq(SysRole::getCanDelete,
                "1").count();
        if (cannotDeleteNum > 0) {
            throw new BizException("要删除的数据中存在不可删除的角色");
        }
    }

    /**
     * 查询角色列表
     *
     * @param obj 角色对象
     * @return java.util.List<top.krasus1966.base.domain.SysRole>
     * @method queryRole
     * @author krasus1966
     * @date 2022/3/3 17:01
     * @description 查询角色列表
     */
    public List<SysRole> queryRole(SysRole obj) {
        List<SysRole> roleList = lambdaQuery().setEntity(obj).list();
        return roleList.stream().map(role -> role.setMenuIds(sysRoleMenuService
                .lambdaQuery()
                .eq(SysRoleMenu::getRoleId, role.getId())
                .list()
                .stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.joining(",")))).collect(Collectors.toList());
    }
}
