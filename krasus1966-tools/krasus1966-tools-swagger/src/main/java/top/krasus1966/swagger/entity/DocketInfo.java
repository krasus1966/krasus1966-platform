package top.krasus1966.swagger.entity;

import io.swagger.models.Contact;
import top.krasus1966.swagger.anno.SwaggerDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2021/9/24 00:04
 **/
public class DocketInfo {
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
    private Class<?> scanClazz = SwaggerDoc.class;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        if (null == group || "".equals(group)) {
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

    public Class<?> getScanClazz() {
        return scanClazz;
    }

    public void setScanClazz(Class<?> scanClazz) {
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
}
