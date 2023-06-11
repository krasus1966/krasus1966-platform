package top.krasus1966.flowable.init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author Krasus1966
 * @date 2023/5/3 16:27
 **/
@ComponentScan("top.krasus1966.core")
@MapperScan("top.krasus1966.flowable")
@Component
public class AppInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
