package top.krasus1966.system.facade;

import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.NotFoundException;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.sys.SysConfig;
import top.krasus1966.system.service.SysConfigService;

import java.util.Optional;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-config")
public class SysConfigFacade extends AbstractAdminFacade<SysConfigService, SysConfig> {

    public SysConfigFacade(HttpServletRequest request, HttpServletResponse response, SysConfigService service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service, ruleExecuteService);
    }

    /**
     * 通过ConfigCode查询系统配置项
     *98
     * @param code ConfigCode值
     * @return top.krasus1966.base.result.R<top.krasus1966.base.domain.SysConfig>
     * @method getConfigByCode
     * @author krasus1966
     * @date 2022/1/8 19:15
     * @description 通过ConfigCode查询系统配置项
     */
    @ApiOperation(value = "通过configCode查询系统配置")
    @GetMapping("/getConfigByCode")
    public R<SysConfig> getConfigByCode(String code) {
        return R.success(
                Optional.ofNullable(
                        service.lambdaQuery()
                                .eq(SysConfig::getConfigCode, code)
                                .one()
                ).<NotFoundException>orElseThrow(() -> new NotFoundException("系统配置不存在"))
        );
    }

    /**
     * 通过ConfigCode查询系统配置值
     *
     * @param code ConfigCode值
     * @return top.krasus1966.base.result.R<java.lang.String>
     * @method getConfigValueByCode
     * @author krasus1966
     * @date 2022/1/8 19:09
     * @description 通过ConfigCode查询系统配置值
     */
    @ApiOperation(value = "通过configCode查询系统配置值")
    @GetMapping("/getConfigValueByCode")
    public R<String> getConfigValueByCode(String code) {
        return R.success(
                Optional.ofNullable(
                        service.lambdaQuery()
                                .eq(SysConfig::getConfigCode, code)
                                .one()
                ).<NotFoundException>orElseThrow(() -> new NotFoundException("系统配置不存在")).getConfigValue());
    }
}
