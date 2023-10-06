package top.krasus1966.system.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.menu.SysMenu;
import top.krasus1966.system.domain.menu.SysRoleMenu;
import top.krasus1966.system.mapper.menu.ISysMenuMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysMenuService extends AbstractMybatisBaseService<ISysMenuMapper, SysMenu> {

    private final SysRoleMenuService sysRoleMenuService;

    public SysMenuService(SysRoleMenuService sysRoleMenuService) {
        this.sysRoleMenuService = sysRoleMenuService;
    }

    /**
     * 检查数据合法性
     *
     * @param obj 菜单对象
     * @return void
     * @method checkNullable
     * @author krasus1966
     * @date 2022/1/8 19:51
     * @description 检查数据合法性
     */
    @Override
    public void beforeSave(SysMenu obj) {
        if (lambdaQuery()
                .ne(CharSequenceUtil.isNotBlank(obj.getId()), SysMenu::getId, obj.getId())
                .eq(SysMenu::getMenuCode, obj.getMenuCode())
                .count() > 0) {
            throw new BizException("此菜单编码已经存在");
        }
        if (lambdaQuery()
                .ne(CharSequenceUtil.isNotBlank(obj.getId()), SysMenu::getId, obj.getId())
                .eq(SysMenu::getMenuName, obj.getMenuName())
                .count() > 0) {
            throw new BizException("此菜单name已经存在");
        }
        if (lambdaQuery()
                .eq(SysMenu::getParentId, obj.getParentId())
                .eq(SysMenu::getMenuType, DictConst.MENU_TYPE.MENU_TYPE_BUTTON)
                .count() > 0) {
            throw new BizException("按钮下不能添加菜单");
        }
        if (CharSequenceUtil.isBlank(obj.getNoCache())) {
            obj.setNoCache(DictConst.BOOL_TYPE.BOOL_TYPE_YES);
        }
    }

    @Override
    public void beforeDelete(String ids) {
        Long count =
                lambdaQuery().in(SysMenu::getParentId, Arrays.asList(ids.split(Constants.Entity.SPLIT))).count();
        if (count > 0) {
            throw new BizException("菜单包含子菜单，请先删除子菜单");
        }
    }

    /**
     * 通过用户id查询菜单
     *
     * @param userId 用户id
     * @return java.util.List<top.krasus1966.base.domain.SysMenu>
     * @method queryMenuByUserId
     * @author krasus1966
     * @date 2022/1/17 17:14
     * @description 通过用户id查询菜单
     */
    public List<SysMenu> queryMenuByUserId(String userId) {
        return baseMapper.queryMenuByUserId(userId);
    }

    public List<SysMenu> queryMenuByUserIdAndRoleId(String userId, String roleId) {
        return baseMapper.queryMenuByUserIdAndRoleId(userId, roleId);
    }

    public Page<SysMenu> queryPageMenu(Wrapper<SysMenu> wrapper, IPage<SysMenu> page) {
        return baseMapper.queryPageMenu(wrapper, page);
    }

    /**
     * 新增角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id，逗号分割
     * @return java.lang.Boolean
     * @method saveRoleMenus
     * @author krasus1966
     * @date 2022/1/8 20:19
     * @description 新增角色菜单
     */
    public Boolean saveRoleMenus(String roleId, String menuIds) {
        if (CharSequenceUtil.isBlank(roleId)) {
            throw new BizException("角色id不能为空");
        }
        // 先删除当前角色下的所有菜单
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq(SysRoleMenu.ROLE_ID, roleId));
        // 按逗号分割menuId并构建对象
        List<SysRoleMenu> banchList = new ArrayList<>();
        if (CharSequenceUtil.isBlank(menuIds)) {
            return false;
        }
        for (String menuId : menuIds.split(Constants.Entity.SPLIT)) {
            banchList.add(new SysRoleMenu(roleId, menuId));
        }
        // 批量保存角色菜单
        return sysRoleMenuService.saveBatch(banchList);
    }
}
