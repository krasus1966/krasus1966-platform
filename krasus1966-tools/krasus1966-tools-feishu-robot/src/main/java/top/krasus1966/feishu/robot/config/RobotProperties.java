package top.krasus1966.feishu.robot.config;

/**
 * @author Krasus1966
 * @date 2021/10/9 21:39
 **/
//@ConfigurationProperties(prefix = "krasus1966.robot")
public class RobotProperties {

    private String projectName;

    private String url;

    private String sign;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
