package top.krasus1966.liteflow.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Krasus1966
 * @date 2023/7/9 19:23
 **/
@Configuration
@ComponentScan("top.krasus1966.liteflow")
@MapperScan("top.krasus1966.liteflow")
public class LiteflowAutoConfiguation {
}
