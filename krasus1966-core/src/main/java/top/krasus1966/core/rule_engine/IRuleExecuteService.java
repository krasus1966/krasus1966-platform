package top.krasus1966.core.rule_engine;

/**
 * @author Krasus1966
 * @date 2023/7/8 23:44
 **/
public interface IRuleExecuteService {

    /**
     * 检查规则是否存在
     *
     * @param key 规则标识
     * @return boolean
     * @throws
     * @method hasChain
     * @author krasus1966
     * @date 2023/7/8 23:46
     * @description 检查规则是否存在
     */
    default boolean hasRule(String key) {
        return false;
    }

    /**
     * 无返回值执行规则
     *
     * @param key     规则key
     * @param param   规则参数
     * @param context 上下文
     * @return void
     * @throws Exception
     * @method doExec
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 无返回值执行规则
     */
    default void doExec(String key, Object param, Object... context) throws Exception {
        if (null == key || "".equals(key) || !hasRule(key)) {
            return;
        }
        execute(key,param,context);
    }

    /**
     * 带返回值执行规则
     *
     * @param key         规则key
     * @param param       流程参数
     * @param returnClazz 返回值类型，这个返回值会从上下文中获取，所以返回值一定在上下文中
     * @param context     上下文
     * @return T
     * @throws Exception
     * @method doExec
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 带返回值执行规则
     */
    private  <T> T doExec(String key, Object param, Class<T> returnClazz, Object... context) throws Exception {
        if (null == key || "".equals(key) || !hasRule(key)) {
            return null;
        }
        return execute(key,param,returnClazz,context);
    }

    /**
     * 无返回值执行规则
     *
     * @param key     规则key
     * @param param   规则参数
     * @param context 上下文
     * @return void
     * @throws Exception
     * @method execute
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 无返回值执行规则
     */
    default void execute(String key, Object param, Object... context) throws Exception {

    }

    /**
     * 带返回值执行规则
     *
     * @param key         规则key
     * @param param       流程参数
     * @param returnClazz 返回值类型，这个返回值会从上下文中获取，所以返回值一定在上下文中
     * @param context     上下文
     * @return T
     * @throws Exception
     * @method execute
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 带返回值执行规则
     */
    default <T> T execute(String key, Object param, Class<T> returnClazz, Object... context) throws Exception {
        return null;
    }

    /**
     * 添加自定义Java对象，添加后可全局使用对象内的方法
     * Liteflow：需要Java对象标注@ScriptBean
     * 不可以使用静态方法
     *
     * @param key  名称
     * @param bean 注入对象
     * @return void
     * @throws
     * @method addScriptBean
     * @author krasus1966
     * @date 2023/7/8 22:27
     * @description 添加自定义Java对象，添加后可全局使用对象内的方法
     */
    default void addScriptBean(String key, Object bean) {

    }

    default boolean validRule(String chain) {
        return true;
    }

    /**
     * 刷新所有规则和脚本
     *
     * @return void
     * @method reloadRule
     * @author krasus1966
     * @date 2023/7/8 22:06
     * @description 刷新所有规则和脚本
     */
    default void reloadRule() {

    }

    /**
     * 刷新特定规则
     *
     * @param key   key
     * @param value 规则内容
     * @return void
     * @throws
     * @method reloadChain
     * @author krasus1966
     * @date 2023/7/8 22:38
     * @description 刷新特定规则
     */
    default void reloadRule(String key, String value) {

    }

    /**
     * 重新刷新脚本
     *
     * @param language 语言
     * @param key      key
     * @param script   脚本内容
     * @return void
     * @throws
     * @method reloadScript
     * @author krasus1966
     * @date 2023/7/8 22:38
     * @description 重新刷新脚本
     */
    default void reloadScript(String language, String key, String script) {

    }
}
