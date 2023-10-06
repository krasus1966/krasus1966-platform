package ${package.Controller}.front;

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import com.ttsx.base_project.common.core.controller.Front${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 用户端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : Front${superControllerClass}<${table.serviceName}, ${entity}>()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends Front${superControllerClass}<${table.serviceName}, ${entity}> {
<#else>
public class ${table.controllerName} {
</#if>

}
</#if>
