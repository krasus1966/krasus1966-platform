package top.krasus1966.core.base.constant;

/**
 * @author Krasus1966
 * @date 2021/6/9 17:48
 **/
public interface Constants {

    /**
     * 环境配置
     */
    interface ENV {
        String DEV = "dev";
        String TEST = "test";
        String PROD = "prod";
    }

    interface Result {
        String ACTION_OK = "操作成功";
        String ACTION_FAILED = "操作失败";
    }

    /**
     * 通用状态
     */
    interface Status {
        String ON = "ZT01";
        String OFF = "ZT02";
    }

    /**
     * 实体相关常量
     *
     * @author krasus1966
     * @date 2022/1/8 19:46
     * @description 实体相关常量
     */
    interface Entity {
        /**
         * 根路径id
         */
        String ROOT = "ROOT";

        String SPLIT = ",";
    }

    /**
     * 逻辑删除常量
     *
     * @author krasus1966
     * @date 2022/1/8 19:37
     * @description 逻辑删除常量
     */
    interface Deleted {
        /**
         * 逻辑删除字段
         */
        String DELETED = "deleted";
        /**
         * 未删除
         * 数字
         */
        Integer DELETE_NO_INT = 0;
        /**
         * 已删除
         * 数字
         */
        Integer DELETE_YES_INT = 1;
    }

    /**
     * 异常相关常量
     *
     * @author krasus1966
     * @date 2022/1/8 19:45
     * @description 异常相关常量
     */
    interface Exception {
        /**
         * 404
         */
        String NOT_FOUND = "数据不存在";
        /**
         * 401
         */
        String UNAUTHORIZED = "无权限";

        /**
         * 403
         */
        String NO_LOGIN = "未登录";
    }
}
