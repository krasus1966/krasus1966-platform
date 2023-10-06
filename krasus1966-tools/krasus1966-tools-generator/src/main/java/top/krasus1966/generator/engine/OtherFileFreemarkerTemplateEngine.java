package top.krasus1966.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/11/19 22:36
 **/
public class OtherFileFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    /**
     * 重写管理端Facade生成位置
     *
     * @param tableInfo
     * @param objectMap
     * @method outputController
     * @author krasus1966
     * @date 2022/11/19 23:00
     * @description 重写管理端Facade生成位置
     */
    @Override
    protected void outputController(TableInfo tableInfo,
                                    Map<String, Object> objectMap) {
        String controllerPath = getPathInfo(OutputFile.controller);
        if (StringUtils.isNotBlank(tableInfo.getControllerName()) && StringUtils.isNotBlank(controllerPath)) {
            getTemplateFilePath(TemplateConfig::getController).ifPresent(controller -> {
                String entityName = tableInfo.getEntityName();
                String controllerFile = String.format((controllerPath + File.separator + "Admin" + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                outputFile(new File(controllerFile), objectMap, controller, getConfigBuilder().getStrategyConfig().controller().isFileOverride());
            });
        }
    }

    /**
     * 重写其他文件生成位置 用户端Facade VO DTO
     *
     * @param customFiles
     * @param tableInfo
     * @param objectMap
     * @method outputCustomFile
     * @author krasus1966
     * @date 2022/11/19 23:00
     * @description 重写其他文件生成位置
     */
    @Override
    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        String otherPath = getPathInfo(OutputFile.other);
        customFiles.forEach((value) -> {
            String fileName = String.format((otherPath + File.separator + "%s"), value.getFileName());
            outputFile(new File(fileName), objectMap, value.getFilePath(),
                    getConfigBuilder().getInjectionConfig().isFileOverride());
        });
    }
}
