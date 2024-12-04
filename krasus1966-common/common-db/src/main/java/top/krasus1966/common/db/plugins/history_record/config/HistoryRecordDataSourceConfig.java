package top.krasus1966.common.db.plugins.history_record.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用户中心数据源配置类
 *
 * @author daijiaqi
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = "com.ttsx.common.mybatis.history_record.mapper", sqlSessionFactoryRef =
        "platformSqlSessionFactory")
public class HistoryRecordDataSourceConfig {
    /*@Value("${spring.jta.enabled:false}")
    boolean jtaEnabled;

    @Bean("historyRecordDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.history-record")
    public DataSourceProperties historyRecordDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("historyRecordDataSource")
    public DataSource historyRecordDataSource(@Qualifier(value = "platformDataSourceProperties") DataSourceProperties platformDataSourceProperties) throws Exception {
        return DataSourceFactory.getDataSource(platformDataSourceProperties, jtaEnabled);
    }

    @Bean("historyRecordSqlSessionFactory")
    public SqlSessionFactory historyRecordSqlSessionFactory(@Qualifier("historyRecordDataSource") DataSource dataSource
            , MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new BaseMetaObjectHandler());
        globalConfig.setBanner(false);
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath*:mapper/platform/*.xml"));
        mybatisSqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean("historyRecordTransactionManager")
    public PlatformTransactionManager historyRecordTransactionManager(@Qualifier("historyRecordDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/
}
