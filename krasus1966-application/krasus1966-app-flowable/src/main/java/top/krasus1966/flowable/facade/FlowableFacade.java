package top.krasus1966.flowable.facade;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.web.entity.R;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/6/11 16:59
 **/
@RestController
public class FlowableFacade {

    private final ProcessEngine processEngine;

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final IdentityService identityService;

    private final TaskService taskService;
    private final HistoryService historyService;

    private final ManagementService managementService;
    private final FormService formService;
    private final DynamicBpmnService dynamicBpmnService;

    public FlowableFacade(ProcessEngine processEngine, RepositoryService repositoryService, RuntimeService runtimeService, IdentityService identityService, TaskService taskService, HistoryService historyService, ManagementService managementService, FormService formService, DynamicBpmnService dynamicBpmnService) {
        this.processEngine = processEngine;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.identityService = identityService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.managementService = managementService;
        this.formService = formService;
        this.dynamicBpmnService = dynamicBpmnService;
    }

    @RequestMapping(value = "test")
    public R test() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        return R.success(list);
    }
}
