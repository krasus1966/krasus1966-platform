package top.krasus1966.core.web.convert;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.web.entity.AbstractResponse;
import top.krasus1966.core.web.entity.AbstractSearchForm;
import top.krasus1966.core.web.entity.AbstractUpdateForm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2023/6/16 23:51
 **/
public interface IConverter<Persistent extends AbstractPersistent, Response extends AbstractResponse,
        UpdateForm extends AbstractUpdateForm, SearchForm extends AbstractSearchForm> {

    default Persistent responseToPersistent(Response response) {
        Class<Persistent> clazz = CastUtils.cast(
                ReflectionKit.getSuperClassGenericType(this.getClass(), IConverter.class, 0)
        );
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class));
            Persistent cast = CastUtils.cast(constructor.invoke());
            BeanUtils.copyProperties(response, cast);
            return cast;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BizException(String.format("获取实体【%s】无参构造方法失败:%s", clazz.getSimpleName(), e.getMessage()));
        } catch (Throwable e) {
            throw new BizException(String.format("创建实体【%s】对象失败:%s", clazz.getSimpleName(), e.getMessage()));
        }
    }

    default Persistent updateFormToPersistent(UpdateForm updateForm) {
        Class<Persistent> clazz = CastUtils.cast(
                ReflectionKit.getSuperClassGenericType(this.getClass(), IConverter.class, 0)
        );
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class));
            Persistent cast = CastUtils.cast(constructor.invoke());
            BeanUtils.copyProperties(updateForm, cast);
            return cast;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BizException(String.format("获取实体【%s】无参构造方法失败:%s", clazz.getSimpleName(), e.getMessage()));
        } catch (Throwable e) {
            throw new BizException(String.format("创建实体【%s】对象失败:%s", clazz.getSimpleName(), e.getMessage()));
        }
    }

    default Persistent searchFormToPersistent(SearchForm searchForm) {
        Class<Persistent> clazz = CastUtils.cast(
                ReflectionKit.getSuperClassGenericType(this.getClass(), IConverter.class, 0)
        );
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class));
            Persistent cast = CastUtils.cast(constructor.invoke());
            BeanUtils.copyProperties(searchForm, cast);
            return cast;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BizException(String.format("获取实体【%s】无参构造方法失败:%s", clazz.getSimpleName(), e.getMessage()));
        } catch (Throwable e) {
            throw new BizException(String.format("创建实体【%s】对象失败:%s", clazz.getSimpleName(), e.getMessage()));
        }
    }

    default Response toResponse(Persistent persistent) {
        Class<Response> clazz = CastUtils.cast(
                ReflectionKit.getSuperClassGenericType(this.getClass(), IConverter.class, 1)
        );
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class));
            Response cast = CastUtils.cast(constructor.invoke());
            BeanUtils.copyProperties(persistent, cast);
            return cast;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BizException(String.format("获取实体【%s】无参构造方法失败:%s", clazz.getSimpleName(), e.getMessage()));
        } catch (Throwable e) {
            throw new BizException(String.format("创建实体【%s】对象失败:%s", clazz.getSimpleName(), e.getMessage()));
        }
    }

    default List<Persistent> responseToPersistentList(List<Response> responseList) {
        return responseList.stream().map(this::responseToPersistent).collect(Collectors.toList());
    }

    default List<Persistent> updateFormToPersistentList(List<UpdateForm> updateForm) {
        return updateForm.stream().map(this::updateFormToPersistent).collect(Collectors.toList());
    }

    default List<Persistent> searchFormToPersistentList(List<SearchForm> searchForm) {
        return searchForm.stream().map(this::searchFormToPersistent).collect(Collectors.toList());
    }

    default List<Response> toResponseList(List<Persistent> persistent) {
        return persistent.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
