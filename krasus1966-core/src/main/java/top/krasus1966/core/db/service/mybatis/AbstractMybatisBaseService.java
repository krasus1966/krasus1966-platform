package top.krasus1966.core.db.service.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.krasus1966.core.db.service.IBaseService;

/**
 * @author Krasus1966
 * @date 2023/6/19 00:03
 **/
public class AbstractMybatisBaseService<Mapper extends BaseMapper<Persistent>, Persistent> extends ServiceImpl<Mapper,Persistent> implements IBaseService<Persistent> {

}
