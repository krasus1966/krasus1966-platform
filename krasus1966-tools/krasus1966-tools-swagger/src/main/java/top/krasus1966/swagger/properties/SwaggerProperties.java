package top.krasus1966.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import top.krasus1966.swagger.anno.SwaggerDoc;
import top.krasus1966.swagger.entity.Contact;
import top.krasus1966.swagger.entity.DocketInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2021/9/23 23:53
 **/
@ConfigurationProperties(prefix = "krasus1966.swagger")
public class SwaggerProperties {

    /**
     * 是否启用在线文档
     */
    private boolean enabled = false;

    /**
     * 标题
     */
    private String title = "在线接口文档";
    /**
     * 自定义分组名
     */
    private String group = "";
    /**
     * 描述
     */
    private String description = "接口文档";
    /**
     * 版本
     */
    private String version = "0.0.1-SNAPSHOT";
    /**
     * 联系人
     */
    private Contact contact = new Contact();
    /**
     * 要被扫描的注解，默认SwaggerDoc.class
     */
    private Class<? extends Annotation> scanClazz = SwaggerDoc.class;
    /**
     * 包扫描路径
     */
    private String basePackage = "";
    /**
     * url规则
     */
    private List<String> basePath = new ArrayList<>();
    /**
     * 需要过滤的url
     */
    private List<String> excludePath = new ArrayList<>();
    /**
     * 分组文档
     */
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        if (group == null || "".equals(group)) {
            return title;
        }
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }


    public Class<? extends Annotation> getScanClazz() {
        return scanClazz;
    }

    public void setScanClazz(Class<? extends Annotation> scanClazz) {
        this.scanClazz = scanClazz;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<String> getBasePath() {
        return basePath;
    }

    public void setBasePath(List<String> basePath) {
        this.basePath = basePath;
    }

    public List<String> getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
        this.excludePath = excludePath;
    }

    public Map<String, DocketInfo> getDocket() {
        return docket;
    }

    public void setDocket(Map<String, DocketInfo> docket) {
        this.docket = docket;
    }
}
