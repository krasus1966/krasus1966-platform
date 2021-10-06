package top.krasus1966.swagger.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.krasus1966.swagger.entity.DocketInfo;
import top.krasus1966.swagger.properties.SwaggerProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2021/9/23 23:29
 **/
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
@ConditionalOnProperty(name = "krasus1966.swagger.enabled", havingValue = "true", matchIfMissing = false)
public class SwaggerAutoConfiguration implements BeanFactoryAware {

    private final SwaggerProperties swaggerProperties;

    private BeanFactory beanFactory;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();
        // 没有分组
        if (swaggerProperties.getDocket().isEmpty()) {
            Docket docket = createDocket(swaggerProperties);
            configurableBeanFactory.registerSingleton(swaggerProperties.getTitle(), docket);
            docketList.add(docket);
            return docketList;
        }
        // 分组创建
        for (String groupName : swaggerProperties.getDocket().keySet()) {
            DocketInfo docketInfo =
                    swaggerProperties.getDocket().get(groupName);
            ApiInfo apiInfo = new ApiInfoBuilder()
                    //页面标题
                    .title(docketInfo.getTitle())
                    //创建人
                    .contact(new Contact(docketInfo.getContact().getName(),
                            docketInfo.getContact().getUrl(),
                            docketInfo.getContact().getEmail()))
                    //版本号
                    .version(docketInfo.getVersion())
                    //描述
                    .description(docketInfo.getDescription())
                    .build();

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            if (docketInfo.getBasePath().isEmpty()) {
                docketInfo.getBasePath().add("/**");
            }
            List<Predicate<String>> basePath = new ArrayList<>();
            for (String path : docketInfo.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }

            // exclude-path处理
            List<Predicate<String>> excludePath = new ArrayList<>();
            for (String path : docketInfo.getExcludePath()) {
                excludePath.add(PathSelectors.ant(path));
            }
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo)
                    .groupName(docketInfo.getGroup())
                    .select()
                    //为当前包路径
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                    .apis(RequestHandlerSelectors.withClassAnnotation(swaggerProperties.getScanClazz()))
                    .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                    .build();
            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    /**
     * 创建接口文档对象
     *
     * @param swaggerProperties
     * @return
     */
    private Docket createDocket(SwaggerProperties swaggerProperties) {
        /**
         * API 基础信息
         */
        ApiInfo apiInfo = apiInfo(swaggerProperties);

        /**
         * base-path处理
         * 当没有配置任何path的时候，解析/**
         */
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        /**
         * exclude-path处理
         */
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName(swaggerProperties.getGroup())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .apis(RequestHandlerSelectors.withClassAnnotation(swaggerProperties.getScanClazz()))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                .build();
    }

    //构建 api文档的详细信息
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .description(swaggerProperties.getDescription())
                .build();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
