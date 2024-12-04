package top.krasus1966.common.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.krasus1966.common.core.constant.Constants;
import top.krasus1966.common.core.util.SpringUtil;
import top.krasus1966.common.db.plugins.history_record.plugins.HistoryRecorderInnerInterceptor;

@Configuration
public class MyBatisPlusInterceptorConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(() -> {
            try {
                String header = SpringUtil.getBean(HttpServletRequest.class).getHeader(Constants.RequestHeader.TENANT_ID_NAME);
                return new StringValue(header);
            } catch (Exception e) {
                return new StringValue("ttwl");
            }
        }));
        // 数据权限
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataPermissionHandler()));
        // 防止全表修改和删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 数据变动记录和批量操作数量限制
        HistoryRecorderInnerInterceptor dataChangeRecorderInnerInterceptor = new HistoryRecorderInnerInterceptor();
        dataChangeRecorderInnerInterceptor.addIgnoredTable("sys_history_record");
        dataChangeRecorderInnerInterceptor.setBatchUpdateLimit(10000);
        interceptor.addInnerInterceptor(dataChangeRecorderInnerInterceptor);
        // 分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
