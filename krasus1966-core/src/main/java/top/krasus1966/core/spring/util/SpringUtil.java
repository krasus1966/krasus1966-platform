package top.krasus1966.core.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Krasus1966
 * @date 2022/10/30 16:17
 **/
@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;

    public static ListableBeanFactory getBeanFactory() {
        return null == beanFactory ? applicationContext : beanFactory;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取bean
     *
     * @param bean bean类型
     * @return T
     * @method getBean
     * @author krasus1966
     * @date 2022/10/30 16:43
     * @description 获取bean
     */
    public static <T> T getBean(Class<T> bean) {
        return getBeanFactory().getBean(bean);
    }

    /**
     * 获取bean
     *
     * @param name bean名称
     * @return T
     * @method getBean
     * @author krasus1966
     * @date 2022/10/30 16:43
     * @description 获取bean
     */
    public static <T> T getBean(String name) {
        return (T) getBeanFactory().getBean(name);
    }

    /**
     * 获取bean
     *
     * @param name bean名称
     * @param bean bean类型
     * @return T
     * @method getBean
     * @author krasus1966
     * @date 2022/10/30 16:44
     * @description 获取bean
     */
    public static <T> T getBean(String name, Class<T> bean) {
        return getBeanFactory().getBean(name, bean);
    }

    /**
     * 获取配置文件值
     *
     * @param property 配置名
     * @return java.lang.String
     * @method getProperties
     * @author krasus1966
     * @date 2022/10/30 16:44
     * @description 获取配置文件值
     */
    public static String getProperty(String property) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getEnvironment().getProperty(property);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtil.beanFactory = configurableListableBeanFactory;
    }
}
