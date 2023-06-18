package top.krasus1966.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.krasus1966.core.db.service.IBaseService;
import top.krasus1966.system.domain.persistent.SysUser;
import top.krasus1966.system.mapper.SysUserMapper;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:43
 **/
@Service
public class SysUserBaseServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IBaseService<SysUser> {
}
