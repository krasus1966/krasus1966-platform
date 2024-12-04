package top.krasus1966.common.db.plugins.noPersistence;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import top.krasus1966.common.core.exception.BizException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoPoRepositoryImpl {

    protected final Log log = LogFactory.getLog(getClass());

    private final Map<String, SqlSessionFactory> sqlSessionFactoryMap;
    private final Map<String, PlatformTransactionManager> transactionManagerMap;


    public NoPoRepositoryImpl(Map<String, SqlSessionFactory> sqlSessionFactoryMap, Map<String,
            PlatformTransactionManager> transactionManagerMap) {
        this.sqlSessionFactoryMap = sqlSessionFactoryMap;
        this.transactionManagerMap = transactionManagerMap;
    }

    public static <T> List<List<T>> splitList(List<T> sourceList, int chunkSize) {
        List<List<T>> resultLists = new ArrayList<>();
        int size = sourceList.size();
        for (int i = 0; i < size; i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, size);
            resultLists.add(sourceList.subList(i, endIndex));
        }
        return resultLists;
    }

    /**
     * 无实体新增
     *
     * @param dataSource
     * @param tableName
     * @param data
     * @return
     * @throws BizException
     */
    public int insert(String dataSource, String tableName, Map<String, Object> data) throws BizException {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        for (String key : data.keySet()) {
            if (SqlInjectionUtils.check(key)) {
                throw new BizException("参数格式错误！");
            }
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper noPoMapper = sqlSession.getMapper(NoPoMapper.class);
            return noPoMapper.insertNoPO(tableName, data);
        }
    }

    /**
     * 无实体批量新增
     *
     * @param dataSource    数据源
     * @param tableName     表名
     * @param fieldSet      字段
     * @param datas         数据
     * @param isTransaction 是否开启事务，不开启则需要外部控制事务
     * @return
     * @throws BizException
     */
    public int insertBatch(String dataSource, String tableName, Set<String> fieldSet, List<Map<String, Object>> datas
            , boolean isTransaction) {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        if (fieldSet.isEmpty()) {
            throw new BizException("参数格式错误！");
        }
        for (String field : fieldSet) {
            if (SqlInjectionUtils.check(field)) {
                throw new BizException("参数格式错误！");
            }
        }

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");

        // 事务管理
        PlatformTransactionManager platformTransactionManager = null;
        TransactionStatus status = null;

        // 开启事务管理
        if (isTransaction) {
            platformTransactionManager = transactionManagerMap.get(dataSource + "TransactionManager");
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
            definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = platformTransactionManager.getTransaction(definition);
        }


        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper noPoMapper = sqlSession.getMapper(NoPoMapper.class);
            int i = 0;
            for (List<Map<String, Object>> dataList : splitList(datas, 1000)) {
                i += noPoMapper.batchInsertNoPO(tableName, fieldSet, dataList);
            }
            if (isTransaction) {
                platformTransactionManager.commit(status);
            }
            return i;
        } catch (Exception e) {
            if (isTransaction) {
                platformTransactionManager.rollback(status);
            }
            throw e;
        }
    }

    /**
     * 无实体修改
     *
     * @param dataSource
     * @param tableName
     * @param id
     * @param data
     * @return
     * @throws BizException
     */
    public int updateById(String dataSource, String tableName, String id, Map<String, Object> data) {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (CharSequenceUtil.isEmpty(id)) {
            throw new BizException("ID不能为空！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        for (String key : data.keySet()) {
            if (SqlInjectionUtils.check(key)) {
                throw new BizException("参数格式错误！");
            }
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper noPoMapper = sqlSession.getMapper(NoPoMapper.class);
            return noPoMapper.updateNoPOById(tableName, id, data);
        }
    }


    public int deleteByIds(String dataSource, String tableName, String ids) throws BizException {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        for (String key : ids.split(",")) {
            if (SqlInjectionUtils.check(key)) {
                throw new BizException("参数格式错误！");
            }
        }
        int i = 0;
        Map<String, Object> data = new HashMap<>();
        data.put("deleted", 1);
        for (String id : ids.split(",")) {
            i += updateById(dataSource, tableName, id, data);
        }
        return i;
    }

    public List<Map<String, Object>> query(String dataSource, String tableName,
                                           QueryWrapper<Map<String, Object>> wrapper) throws BizException {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper userMapper = sqlSession.getMapper(NoPoMapper.class);
            List<Map<String, Object>> maps = userMapper.selectByNoPo(tableName, wrapper);
            return maps.stream().map(item -> item.entrySet().stream().collect(Collectors.toMap((key) -> StringUtils.underlineToCamel(key.getKey()), Map.Entry::getValue))).toList();
        }
    }

    public Integer count(String dataSource, String tableName,
                         QueryWrapper<Map<String, Object>> wrapper) throws BizException {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper userMapper = sqlSession.getMapper(NoPoMapper.class);
            return userMapper.countByNoPo(tableName, wrapper);
        }
    }

    public Page<Map<String, Object>> queryPage(String dataSource, String tableName, IPage<Map<String, Object>> page,
                                               QueryWrapper<Map<String, Object>> wrapper) throws BizException {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper noPoMapper = sqlSession.getMapper(NoPoMapper.class);
            Page<Map<String, Object>> pageData = noPoMapper.selectPageByNoPo(tableName, page, wrapper);
            pageData.setRecords(pageData.getRecords().stream().map(item -> item.entrySet().stream().collect(Collectors.toMap((key) -> StringUtils.underlineToCamel(key.getKey()), Map.Entry::getValue))).toList());
            return pageData;
        }
    }

    public List<TempDict> options(String dataSource, String tableName, String key, String label) {
        if (!sqlSessionFactoryMap.containsKey(dataSource + "SqlSessionFactory")) {
            throw new BizException("数据源无法获取！");
        }
        if (SqlInjectionUtils.check(tableName)) {
            throw new BizException("参数格式错误！");
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSource + "SqlSessionFactory");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            NoPoMapper noPoMapper = sqlSession.getMapper(NoPoMapper.class);
            key = StringUtils.camelToUnderline(key);
            label = StringUtils.camelToUnderline(label);
            QueryWrapper<Map<String, Object>> wrapper = new QueryWrapper<>();
            wrapper.checkSqlInjection();
            wrapper.select(key + " AS `dictKey`", label + " AS `dictValue`").groupBy(key);
            List<Map<String, Object>> options = noPoMapper.selectByNoPo(tableName, wrapper);
            return options.stream().map(item -> new TempDict(item.get("dictKey").toString(),
                    item.get("dictValue").toString())).toList();
        }
    }

    public record TempDict(String dictKey, String dictValue) {
    }
}
