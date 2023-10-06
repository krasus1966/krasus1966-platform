package top.krasus1966.system.mapper.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.krasus1966.system.domain.sys.SysAppVersion;

/**
 * <p>
 * app版本管理 Mapper 接口
 * </p>
 *
 * @author krasus1966
 * @since 2022-03-03
 */
@Mapper
public interface ISysAppVersionMapper extends BaseMapper<SysAppVersion> {

}
