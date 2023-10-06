package top.krasus1966.system.service;

import org.springframework.stereotype.Service;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.system.domain.sys.SysAppVersion;
import top.krasus1966.system.mapper.sys.ISysAppVersionMapper;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysAppVersionService extends AbstractMybatisBaseService<ISysAppVersionMapper, SysAppVersion>{

}
