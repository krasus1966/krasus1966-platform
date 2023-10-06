package ${package.Controller}.admin;

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import com.ttsx.base_project.common.core.controller.Admin${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 管理端控制器
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
@RequestMapping("/admin/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class Admin${table.controllerName}<#if superControllerClass??> : Admin${superControllerClass}<${table.serviceName}, ${entity}>()</#if>
<#else>
<#if superControllerClass??>
public class Admin${table.controllerName} extends Admin${superControllerClass}<${table.serviceName}, ${entity}> {
<#else>
public class Admin${table.controllerName} {
</#if>

}
</#if>
