package top.krasus1966.core.constant;

/**
 * @author Krasus1966
 * @date 2021/11/15 00:32
 **/
public interface Cache {

    interface Prefix {
        // 对象
        String CACHE_OBJ = "OBJ:";
        // 登录
        String CACHE_LOGIN = "LOGIN:";
        // 字符串
        String CACHE_STR = "STR:";
        // 树型结构
        String CACHE_TREE = "TREE:";
    }

    interface Entity {
        // 基础名称
        String MENU = "menu";
        String DICT = "dict";
    }

    interface Property {
        String ID = "_id";
        // 扩展名称
        String PARENT_ID = "_parent_id";
        String TREE = "_tree";
        String DICT_CODE = "_dict_code";
    }

    interface Tree {
        String DICT_PARENT_MAP = "CACHE:DICT_PARENT_MAP:";
        String MENU_PARENT_MAP = "CACHE:MENU_PARENT_MAP:";
    }
}
