package top.krasus1966.common.db.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import top.krasus1966.common.core.entity.AbstractEntity;
import top.krasus1966.common.core.entity.CommonQuery;
import top.krasus1966.common.core.entity.PageDTO;
import top.krasus1966.common.core.exception.BizException;
import top.krasus1966.common.core.func.BaseFunction;
import top.krasus1966.common.db.convert.BaseConvert;
import top.krasus1966.common.db.convert.BaseConvertFactory;
import top.krasus1966.common.db.entity.AbstractPersistent;
import top.krasus1966.common.db.enums.CompareTypeEnum;
import top.krasus1966.common.db.enums.DataTypeEnum;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 通用查询
 *
 * @param <Persistent> PO
 * @param <Entity>     Entity
 */
@Slf4j
public abstract class CommonServiceImpl<Persistent extends AbstractPersistent, Entity extends AbstractEntity> extends BaseFunction implements ICommonService<Entity, Persistent> {

    protected final IService<Persistent> repository;
    protected BaseConvert<Persistent, Entity> convert;

    protected CommonServiceImpl(IService<Persistent> repository) {
        this.repository = repository;
        this.setBaseConvert(null);
    }

    /**
     * 添加查询条件
     *
     * @param commonQuery
     * @param wrapper
     * @return
     */
    public static <POJO> QueryWrapper<POJO> addQueryWrapper(CommonQuery commonQuery, QueryWrapper<POJO> wrapper) {
        if (null == wrapper) {
            wrapper = new QueryWrapper<>();
        }
        // 开启SQL注入检查
        wrapper.checkSqlInjection();
        if (commonQuery != null) {
            for (CommonQuery.CommonQueryDTO param : commonQuery.getParams()) {
                String[] valueStrs = param.getValue();
                Object[] values = DataTypeEnum.valueOf(param.getDataType()).execute(valueStrs, param.getDateFormat());
                if ("IN".equals(param.getCompareType()) && valueStrs.length == 1 && "S".equals(param.getDataType())) {
                    values = valueStrs[0].split(",");
                }
                CompareTypeEnum.valueOf(param.getCompareType()).execute(wrapper, param.getColumn(), values);
            }
            List<CommonQuery.CommonSortableDTO> sortableDTOS =
                    commonQuery.getSorts().stream().sorted(Comparator.comparing(CommonQuery.CommonSortableDTO::getNum)).toList();
            for (CommonQuery.CommonSortableDTO sort : sortableDTOS) {
                wrapper.orderBy(true, sort.getAsc(), StringUtils.camelToUnderline(sort.getColumn()));
            }
        }
        return wrapper;
    }

    private void setBaseConvert() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<Persistent> sourceClass = (Class<Persistent>) actualTypeArguments[0];
        Class<Entity> targetClass = (Class<Entity>) actualTypeArguments[1];
        this.convert = BaseConvertFactory.getConvert(sourceClass, targetClass);
    }

    protected void setBaseConvert(BaseConvert<Persistent, Entity> convert) {
        if (null == convert) {
            this.setBaseConvert();
        } else {
            this.convert = convert;
        }
    }

    /**
     * 通用保存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public boolean save(Entity entity, Map<String, Object> context) throws Exception {
        String msg = check(entity);
        if (CharSequenceUtil.isNotEmpty(msg)) {
            throw new BizException(msg);
        }
        Persistent persistent = convert.target2Source(entity);
        beforeSave(entity, persistent, context);
        boolean save = repository.save(persistent);
        if (save) {
            entity.setId(persistent.getId());
        }
        afterSave(entity, persistent, save, context);
        return save;
    }

    /**
     * 通用更新
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public boolean update(Entity entity, Map<String, Object> context) throws Exception {
        if (CharSequenceUtil.isEmpty(entity.getId())) {
            throw new BizException("id不能为空");
        }
        String msg = check(entity);
        if (CharSequenceUtil.isNotEmpty(msg)) {
            throw new BizException(msg);
        }
        Persistent persistent = convert.target2Source(entity);
        beforeUpdate(entity, persistent, context);
        boolean success = repository.updateById(persistent);
        afterUpdate(entity, persistent, success, context);
        return success;
    }

    /**
     * 通用删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    public boolean deleteByIds(String ids, Map<String, Object> context) throws Exception {
        if (CharSequenceUtil.isEmpty(ids)) {
            throw new BizException("ids不能为空");
        }
        beforeDeleteByIds(ids, context);
        boolean success = repository.removeByIds(Arrays.asList(ids.split(",")));
        afterDeleteByIds(ids, success, context);
        return success;
    }

    /**
     * 通用获取详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Entity view(String id, Map<String, Object> context) throws Exception {
        if (CharSequenceUtil.isEmpty(id)) {
            throw new BizException("id不能为空");
        }
        beforeView(id, context);
        Entity entity = convert.source2Target(repository.getById(id));
        afterView(entity, context);
        return entity;
    }

    /**
     * 通用分页查询
     *
     * @return
     */
    public PageDTO<Entity> queryPage(Map<String, Object> context) throws Exception {
        CommonQuery query = getQuery();
        return queryPage(addQueryWrapper(query, null), Page.of(query.getPageNum(), query.getPageSize()), context);
    }

    /***
     * 通用分页查询
     * @param queryWrapper
     * @return
     */
    public PageDTO<Entity> queryPage(QueryWrapper<Persistent> queryWrapper, Map<String, Object> context) throws Exception {
        CommonQuery query = getQuery();
        return queryPage(addQueryWrapper(query, queryWrapper), Page.of(query.getPageNum(), query.getPageSize()),
                context);
    }

    /***
     * 通用分页查询
     * @param queryWrapper
     * @return
     */
    public PageDTO<Entity> queryPage(QueryWrapper<Persistent> queryWrapper, Page<Persistent> page,
                                     Map<String, Object> context) throws Exception {
        beforeQueryPage(queryWrapper, page, context);
        PageDTO<Entity> dPr = convert.sourcePage2TargetPage(repository.page(page, queryWrapper));
        afterQueryPage(dPr, context);
        return dPr;
    }

    /**
     * 通用列表查询
     *
     * @return
     */
    public List<Entity> query(Map<String, Object> context) throws Exception {
        return query(addQueryWrapper(null), context);
    }

    /**
     * 通用列表查询
     *
     * @param queryWrapper
     * @return
     */
    public List<Entity> query(QueryWrapper<Persistent> queryWrapper, Map<String, Object> context) throws Exception {
        beforeQuery(queryWrapper, context);
        List<Entity> entityList = convert.sources2Targets(repository.list(queryWrapper));
        afterQuery(entityList, context);
        return entityList;
    }

    public void addCustomQueryWrapper(QueryWrapper<Persistent> wrapper) {

    }

    /**
     * 添加查询条件
     *
     * @param wrapper
     * @return
     */
    public QueryWrapper<Persistent> addQueryWrapper(QueryWrapper<Persistent> wrapper) {
        CommonQuery commonQuery = getQuery();
        if (null == wrapper) {
            wrapper = new QueryWrapper<>();
        }
        addCustomQueryWrapper(wrapper);
        return addQueryWrapper(commonQuery, wrapper);
    }

    public List<Map<String, Object>> options(String key, String label, String keyName, String labelName) {
        key = StringUtils.camelToUnderline(key);
        label = StringUtils.camelToUnderline(label);
        QueryWrapper<Persistent> wrapper = addQueryWrapper(new QueryWrapper<>());
        wrapper.select(key + " AS `" + keyName + "`", label + " AS `" + labelName + "`").groupBy(key);
        return repository.getBaseMapper().selectMaps(wrapper);
    }
}
