package top.krasus1966.system.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.sys_user.SysUser;
import top.krasus1966.system.domain.sys_user.SysUserRole;
import top.krasus1966.system.service.SysUserService;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-user")
public class SysUserFacade extends AbstractAdminFacade<SysUserService, SysUser> {

    public SysUserFacade(HttpServletRequest request, HttpServletResponse response, SysUserService service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service, ruleExecuteService);
    }

    /**
     * 分页查询角色下用户
     *
     * @param role 角色对象
     * @param user 用户对象
     * @param page 分页对象
     * @method queryPageRoleUser
     * @author krasus1966
     * @date 2022/4/18 15:09
     * @description 分页查询角色下用户
     */
    @ApiOperation(value = "分页查询角色下用户")
    @GetMapping("/queryPageRoleUser")
    public R<Page<SysUser>> queryPageRoleUser(SysUserRole role, SysUser user, Page<SysUser> page) {
        return R.success(service.queryPageRoleUser(role, user, page));
    }
}
