package top.krasus1966.swagger.entity;

/**
 * @author Krasus1966
 * @date 2021/9/24 00:07
 **/
public class Contact {
    /**
     * 联系人
     */
    private String name = "krasus1966";
    /**
     * 联系人url
     */
    private String url = "http://krasus1966.top";
    /**
     * 联系人Email
     */
    private String email = "2134123985@qq.com";

    public Contact() {
    }

    public Contact(String name, String url, String email) {
        this.name = name;
        this.url = url;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
