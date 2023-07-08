package top.krasus1966.liteflow.executor;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import org.springframework.context.ApplicationEventPublisher;
import top.krasus1966.core.spring.util.SpringUtil;

/**
 * @author Krasus1966
 * @date 2023/7/8 21:39
 **/
public class CustomExecutor extends NodeExecutor {

    private final ApplicationEventPublisher applicationEventPublisher = SpringUtil.getBean(ApplicationEventPublisher.class);

    @Override
    public void execute(NodeComponent instance) throws Exception {
        super.execute(instance);
    }

    @Override
    protected void retry(NodeComponent instance, int currentRetryCount) throws Exception {
        super.retry(instance, currentRetryCount);
    }
}
