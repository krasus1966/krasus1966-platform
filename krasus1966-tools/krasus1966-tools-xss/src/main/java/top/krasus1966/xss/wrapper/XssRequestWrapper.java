package top.krasus1966.xss.wrapper;

import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2021/9/26 22:26
 **/
public class XssRequestWrapper extends HttpServletRequestWrapper {
    /*
     * 策略文件 需要将要使用的策略文件放到项目资源文件路径下
     */
    private static final String ANTI_SAMY_PATH = Objects.requireNonNull(XssRequestWrapper.class.getClassLoader()
            .getResource("antisamy.xml")).getFile();

    public static Policy POLICY_INSTANCE = null;

    static {
        // 指定策略文件
        try {
            POLICY_INSTANCE = Policy.getInstance(ANTI_SAMY_PATH);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * AntiSamy过滤数据
     *
     * @param value 需要进行过滤的数据
     * @return 返回过滤后的数据
     */
    private String xssClean(String value) {
        try {
            // 使用AntiSamy进行过滤
            AntiSamy antiSamy = new AntiSamy();
            CleanResults cr = antiSamy.scan(value, POLICY_INSTANCE);
            value = cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int len = values.length;
        String[] newArray = new String[len];
        for (int j = 0; j < len; j++) {
            System.out.println("Antisamy过滤清理，清理之前的参数值：" + values[j]);
            // 过滤清理
            newArray[j] = xssClean(values[j]);
            System.out.println("Antisamy过滤清理，清理之后的参数值：" + newArray[j]);
        }
        return newArray;
    }

    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null) {
            return null;
        }
        return xssClean(str);
    }


    @Override
    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null) {
            return null;
        }
        return xssClean(str);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        for (Map.Entry<String, String[]> me : requestMap.entrySet()) {
            String[] values = me.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssClean(values[i]);
            }
        }
        return requestMap;
    }
}
