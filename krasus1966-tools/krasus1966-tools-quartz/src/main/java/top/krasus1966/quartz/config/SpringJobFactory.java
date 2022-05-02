package top.krasus1966.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author Krasus1966
 * @date 2022/5/2 18:56
 **/
@Component
public class SpringJobFactory extends AdaptableJobFactory {

    private final AutowireCapableBeanFactory capableBeanFactory;

    public SpringJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
        this.capableBeanFactory = capableBeanFactory;
    }

    @Override
    protected Object createJobInstance(@NonNull TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
