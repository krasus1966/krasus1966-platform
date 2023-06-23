package top.krasus1966.core.db.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;


//@Component
public class ServiceFactory implements ApplicationContextAware {

    private static Map<String, IBaseService> contractWarningServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contractWarningServiceMap = applicationContext.getBeansOfType(IBaseService.class);
    }

    public static IBaseService getContractWarningService(String systemName) {
        return contractWarningServiceMap.getOrDefault(systemName + "ServiceImpl",null);
    }
}
