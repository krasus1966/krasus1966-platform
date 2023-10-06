package top.krasus1966.system.service;

import org.springframework.stereotype.Service;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.sys.SysConfig;
import top.krasus1966.system.mapper.sys.ISysConfigMapper;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysConfigService extends AbstractMybatisBaseService<ISysConfigMapper, SysConfig>{

}
