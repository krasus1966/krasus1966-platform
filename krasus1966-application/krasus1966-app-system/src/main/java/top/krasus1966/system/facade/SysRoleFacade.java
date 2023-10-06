package top.krasus1966.system.facade;

import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.menu.SysRole;
import top.krasus1966.system.domain.sys_user.SysUserRole;
import top.krasus1966.system.service.SysRoleService;
import top.krasus1966.system.service.SysUserRoleService;
import top.krasus1966.valid.anno.group.Update;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-role")
public class SysRoleFacade extends AbstractAdminFacade<SysRoleService, SysRole> {

    private final SysUserRoleService sysUserRoleService;

    public SysRoleFacade(HttpServletRequest request, HttpServletResponse response, SysRoleService service, IRuleExecuteService ruleExecuteService, SysUserRoleService sysUserRoleService) {
        super(request, response, service, ruleExecuteService);
        this.sysUserRoleService = sysUserRoleService;
    }

    /**
     * 查询角色列表和拥有的菜单权限
     *
     * @param obj 角色对象
     * @return top.krasus1966.base.result.R<java.util.List < top.krasus1966.base.domain.SysRole>>
     * @method queryRole
     * @author krasus1966
     * @date 2022/3/3 17:03
     * @description 查询角色列表和拥有的菜单权限
     */
    @GetMapping(value = "/queryRole")
    @ApiOperation(value = "查询角色列表")
    public R<List<SysRole>> queryRole(SysRole obj) {
        return R.success(service.queryRole(obj));
    }

    /**
     * 保存用户角色信息
     *
     * @param obj 用户角色对象
     * @return com.ttsx.base.common.entity.R<java.lang.Boolean>
     * @throws
     * @method saveUserRole
     * @author krasus1966
     * @date 2022/4/18 15:10
     * @description 保存用户角色信息
     */
    @ApiOperation(value = "保存用户角色")
    @PostMapping("/saveUserRole")
    public R<Boolean> saveUserRole(@Validated(value = {Update.class}) SysUserRole obj) {
        return R.success(sysUserRoleService.saveUserRole(obj));
    }
}
