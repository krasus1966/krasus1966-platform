package top.krasus1966.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import top.krasus1966.quartz.repo.impl.TaskInfoRepositoryImpl;
import top.krasus1966.quartz.repository.ITaskInfoRepository;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:08
 **/
@Configuration
@ComponentScan("top.krasus1966.quartz.*")
public class QuartzConfig {

    private final SpringJobFactory jobFactory;

    public QuartzConfig(SpringJobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setStartupDelay(5);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() throws SchedulerException {
        return schedulerFactoryBean().getScheduler();
    }

    @ConditionalOnMissingBean(ITaskInfoRepository.class)
    @Bean
    public ITaskInfoRepository taskInfoRepository() {
        return new TaskInfoRepositoryImpl();
    }
}
