package top.krasus1966.core.db.mybatis_plus.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Krasus1966
 * @date 2021/4/18 22:56
 **/
@Configuration
@MapperScan("top.krasus1966.*.service.*.mapper")
public class MybatisPlusConfig {
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     * (该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 租户插件
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
//
//            /**
//             * 获取租户id
//             * @return
//             */
//            @Override
//            public Expression getTenantId() {
//                return new StringValue("tenantId");
//            }
//
//            /**
//             * 忽略租户条件
//             * @param tableName 表名
//             * @return
//             */
//            @Override
//            public boolean ignoreTable(String tableName) {
//                return TenantLineHandler.super.ignoreTable(tableName);
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                return TenantLineHandler.super.getTenantIdColumn();
//            }
//        }));

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}