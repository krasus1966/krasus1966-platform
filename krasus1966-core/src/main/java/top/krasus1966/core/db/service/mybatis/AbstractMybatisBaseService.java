package top.krasus1966.core.db.service.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.jdbc.SQL;
import top.krasus1966.core.db.service.IBaseService;

/**
 * @author Krasus1966
 * @date 2023/6/19 00:03
 **/
public class AbstractMybatisBaseService<Mapper extends BaseMapper<Persistent>, Persistent> extends ServiceImpl<Mapper,Persistent> implements IBaseService<Persistent> {
    public static void main(String[] args) {
        new SQL()
                .SELECT("u.id", "u.username", "r.id AS roles.id", "r.role_name AS roles.roleName")
                .FROM("user u")
                .LEFT_OUTER_JOIN("user_role ur ON u.id = ur.user_id")
                .LEFT_OUTER_JOIN("role r ON ur.role_id = r.id")
                .toString();
    }
}
