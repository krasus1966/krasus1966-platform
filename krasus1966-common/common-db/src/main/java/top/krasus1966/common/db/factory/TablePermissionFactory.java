package top.krasus1966.common.db.factory;

import org.springframework.beans.BeanUtils;
import top.krasus1966.common.db.entity.TablePermission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author krasus1966
 * @date 2024/10/12 10:36
 **/
public class TablePermissionFactory {

    public static <T> TablePermission beanToTablePermission(T bean) {
        TablePermission tablePermission = new TablePermission();
        BeanUtils.copyProperties(bean, tablePermission);
        return tablePermission;
    }

    public static <T> List<TablePermission> beanToTablePermissionList(List<T> beans) {
        List<TablePermission> tablePermissionList = new ArrayList<>();
        beans.forEach(item -> tablePermissionList.add(beanToTablePermission(item)));
        return tablePermissionList;
    }
}
