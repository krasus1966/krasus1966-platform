package top.krasus1966.common.db.plugins.noPersistence;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface NoPoMapper extends BaseMapper<Map<String, Object>> {

    @InsertProvider(type = NoPoSqlProvider.class, method = "insertNoPO")
    int insertNoPO(String tableName, @Param("data") Map<String, Object> data);

    @InsertProvider(type = NoPoSqlProvider.class, method = "batchInsertNoPO")
    int batchInsertNoPO(String tableName, Set<String> fieldSet, @Param("dataList") List<Map<String, Object>> dataList);

    @UpdateProvider(type = NoPoSqlProvider.class, method = "updateNoPOById")
    int updateNoPOById(String tableName, @Param("id") String id, @Param("data") Map<String, Object> data);

    @MapKey("id")
    @SelectProvider(type = NoPoSqlProvider.class, method = "selectByNoPo")
    List<Map<String, Object>> selectByNoPo(String tableName,
                                           @Param("ew") Wrapper<Map<String, Object>> ew);

    @MapKey("id")
    @SelectProvider(type = NoPoSqlProvider.class, method = "selectByNoPo")
    Page<Map<String, Object>> selectPageByNoPo(String tableName,
                                               IPage<Map<String, Object>> page,
                                               @Param("ew") Wrapper<Map<String, Object>> ew);

    @SelectProvider(type = NoPoSqlProvider.class, method = "countByNoPo")
    Integer countByNoPo(String tableName, @Param("ew") Wrapper<Map<String, Object>> ew);
}
