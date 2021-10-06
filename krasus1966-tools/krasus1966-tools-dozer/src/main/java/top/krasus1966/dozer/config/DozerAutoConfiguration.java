package top.krasus1966.dozer.config;

import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import top.krasus1966.dozer.utils.DozerUtils;

/**
 * @author Krasus1966
 * @date 2021/9/25 00:18
 **/
public class DozerAutoConfiguration {

    @Bean
    public DozerUtils getDozerUtils(Mapper mapper){
        return new DozerUtils(mapper);
    }
}
