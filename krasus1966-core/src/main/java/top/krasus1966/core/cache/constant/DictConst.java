package top.krasus1966.core.cache.constant;

/**
 * @author Krasus1966
 * @date 2022/11/21 13:57
 **/
public interface DictConst {

    /**
     * 状态
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface STATUS_TYPE {
        String STATUS_TYPE = "ZT";
        String STATUS_TYPE_ON = "ZT01";
        String STATUS_TYPE_OFF = "ZT02";
    }

    /**
     * 菜单类型
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface MENU_TYPE {
        String MENU_TYPE = "CDLX";
        String MENU_TYPE_PAGE = "CDLX_MENU";
        String MENU_TYPE_BUTTON = "CDLX_BUTTON";
    }

    /**
     * 权限类型
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface AUTH_TYPE {
        String AUTH_TYPE = "QXLX";
        String AUTH_TYPE_ADMIN = "QXLX_ADMIN";
        String AUTH_TYPE_NORMAL = "QXLX_NORMAL";
    }

    /**
     * 删除类型
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface DELETE_TYPE {
        String DELETE_TYPE = "SCZT";
        String DELETE_TYPE_NO = "SCZT02";
        String DELETE_TYPE_YES = "SCZT01";
    }

    /**
     * 是否
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface BOOL_TYPE {
        String BOOL_TYPE = "SF";
        String BOOL_TYPE_YES = "SF01";
        String BOOL_TYPE_NO = "SF02";
    }

    /**
     * 角色类型
     *
     * @author krasus1966
     * @date 2022/11/21 13:59
     * @description
     */
    interface ROLE_TYPE {
        String ROLE_TYPE = "SF";
        String ROLE_TYPE_SUPER = "JSLX_SUPER";
        String ROLE_TYPE_PT = "JSLX_PT";
        String ROLE_TYPE_QY = "JSLX_QY";
        String ROLE_TYPE_OTHER = "JSLX_OTHER";
    }
}
