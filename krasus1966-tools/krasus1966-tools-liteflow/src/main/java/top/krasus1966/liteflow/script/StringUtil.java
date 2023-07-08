package top.krasus1966.liteflow.script;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.script.annotation.ScriptBean;

/**
 * @author Krasus1966
 * @date 2023/7/8 22:25
 **/
@ScriptBean(name = "StringUtil")
public class StringUtil {

    public boolean isEmpty(String value) {
        return StrUtil.isEmpty(value);
    }
    public boolean isNotEmpty(String value) {
        return StrUtil.isNotEmpty(value);
    }
}
