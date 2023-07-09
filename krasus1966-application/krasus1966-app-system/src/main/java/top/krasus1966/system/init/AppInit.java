package top.krasus1966.system.init;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

/**
 * @author Krasus1966
 * @date 2023/5/3 16:27
 **/
@ComponentScans({@ComponentScan("top.krasus1966")})
@MapperScans({@MapperScan("top.krasus1966.system")})
@Component
public class AppInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }


}
