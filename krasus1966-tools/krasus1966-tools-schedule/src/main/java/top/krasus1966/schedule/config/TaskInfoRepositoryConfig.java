package top.krasus1966.schedule.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.krasus1966.schedule.repo.TaskInfoRepo;
import top.krasus1966.schedule.repo.impl.TaskInfoRepositoryImpl;
import top.krasus1966.schedule.repository.ITaskInfoRepository;


/**
 * @author Krasus1966
 * @date 2023/4/20 00:03
 **/
@Configuration
public class TaskInfoRepositoryConfig {

    @Bean
    @ConditionalOnMissingBean(ITaskInfoRepository.class)
    public ITaskInfoRepository taskInfoRepository(TaskInfoRepo repo){
        return new TaskInfoRepositoryImpl(repo);
    }
}
