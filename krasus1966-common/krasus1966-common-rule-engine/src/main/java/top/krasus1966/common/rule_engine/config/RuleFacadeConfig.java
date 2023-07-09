package top.krasus1966.common.rule_engine.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.krasus1966.common.rule_engine.facade.RuleFacade;
import top.krasus1966.common.rule_engine.service.IRuleStoreService;
import top.krasus1966.core.rule_engine.IRuleExecuteService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Krasus1966
 * @date 2023/7/9 17:07
 **/
//@Configuration
//@ConditionalOnProperty(prefix = "rule.facade", name = "enabled", havingValue = "true")
public class RuleFacadeConfig {

    @Bean
    public RuleFacade ruleFacadeBean(IRuleStoreService ruleStoreService,
                                     IRuleExecuteService ruleExecuteService,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     RequestMappingHandlerMapping requestMappingHandlerMapping) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RuleFacade ruleFacade = new RuleFacade(ruleStoreService, ruleExecuteService, request, response);
        //注册Controller
        Method method = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().
                getDeclaredMethod("detectHandlerMethods", Object.class);
        //将private改为可使用
        method.setAccessible(true);
        method.invoke(requestMappingHandlerMapping, ruleFacade);
        return ruleFacade;
    }
}
