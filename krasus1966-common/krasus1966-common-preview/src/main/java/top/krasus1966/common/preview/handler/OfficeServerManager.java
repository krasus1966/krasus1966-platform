package top.krasus1966.common.preview.handler;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.InstalledOfficeManagerHolder;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.local.office.LocalOfficeUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.krasus1966.common.preview.property.OfficeProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Arrays;

/**
 * office转换服务管理
 *
 * @author Krasus1966
 * @date 2023/5/6 23:17
 **/
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(OfficeProperty.class)
public class OfficeServerManager {

    private final OfficeProperty officeProperty;

    public OfficeServerManager(OfficeProperty officeProperty) {
        this.officeProperty = officeProperty;
    }

    @PostConstruct
    public void startOfficeManager() throws OfficeException {
        File officeHome;
        if (null == officeProperty.getOfficeHome()) {
            officeHome = LocalOfficeUtils.getDefaultOfficeHome();
        } else {
            officeHome = new File(officeProperty.getOfficeHome());
            if (!officeHome.exists()) {
                officeHome = LocalOfficeUtils.getDefaultOfficeHome();
                if (null == officeHome || !officeHome.exists()) {
                    log.error("office服务未找到，无法使用转换服务");
                    return;
                }
            }
        }
        try {
            String[] portsString = officeProperty.getServerPorts().split(",");
            int[] ports = Arrays.stream(portsString).mapToInt(Integer::parseInt).toArray();
            long timeout = DurationStyle.detectAndParse(officeProperty.getConvertTimeout()).toMillis();
            LocalOfficeManager officeManager = LocalOfficeManager.builder()
                    .officeHome(officeHome)
                    .portNumbers(ports)
                    .processTimeout(timeout)
                    .maxTasksPerProcess(100)
                    .taskQueueTimeout(30L)
                    .build();
            officeManager.start();
            InstalledOfficeManagerHolder.setInstance(officeManager);
        } catch (Exception e) {
            log.error("启动office组件失败，请检查office组件是否可用");
            throw e;
        }
    }

    @PreDestroy
    public void killProcess() {
        org.jodconverter.core.office.OfficeManager officeManager = InstalledOfficeManagerHolder.getInstance();
        if (null != officeManager && officeManager.isRunning()) {
            OfficeUtils.stopQuietly(officeManager);
        }
    }
}
