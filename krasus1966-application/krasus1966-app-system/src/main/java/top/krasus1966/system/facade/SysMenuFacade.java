package top.krasus1966.system.facade;

import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.util.TreeUtil;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.menu.SysMenu;
import top.krasus1966.system.service.SysMenuService;

import java.util.Comparator;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-menu")
public class SysMenuFacade extends AbstractAdminFacade<SysMenuService, SysMenu> {

    public SysMenuFacade(HttpServletRequest request, HttpServletResponse response, SysMenuService service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service, ruleExecuteService);
    }

    /**
     * 查询菜单树
     *
     * @param obj 菜单对象
     * @return top.krasus1966.base.result.R<java.util.List < top.krasus1966.base.domain.SysMenu>>
     * @method getTree
     * @author krasus1966
     * @date 2022/1/8 19:53
     * @description 查询菜单树
     */
    @ApiOperation(value = "获取菜单树")
    @GetMapping("/getTree")
    public R<List<SysMenu>> getTree(SysMenu obj) {
        if (CharSequenceUtil.isBlank(obj.getParentId())) {
            obj.setParentId(Constants.Entity.ROOT);
        }
        List<SysMenu> sysMenus = TreeUtil.parseTreeWithLevel(service.query().list(),
                obj.getParentId(), SysMenu::getId, SysMenu::getParentId, SysMenu::setChildren,SysMenu::setLevel
                , Comparator.comparing(SysMenu::getSort));
        return R.success(sysMenus);
    }

    /**
     * 保存角色菜单信息
     *
     * @param roleId  角色id
     * @param menuIds 菜单ids
     * @return top.krasus1966.base.result.R<java.lang.Boolean>
     * @method saveRoleMenu
     * @author krasus1966
     * @date 2022/1/8 20:20
     * @description 保存角色菜单信息
     */
    @ApiOperation(value = "保存角色菜单")
    @PostMapping("/saveRoleMenu")
    public R<Boolean> saveRoleMenu(String roleId, String menuIds) {
        return R.success(service.saveRoleMenus(roleId, menuIds));
    }
}
