package top.krasus1966.core.web.convert;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;
import top.krasus1966.core.base.exception.BizException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2023/6/16 23:51
 **/
public interface IConverter2<Source,Target> {

    default Target beanCopy(Source source) {
        Class<Target> clazz = CastUtils.cast(
                ReflectionKit.getSuperClassGenericType(this.getClass(), IConverter2.class, 0)
        );
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class));
            Target cast = CastUtils.cast(constructor.invoke());
            BeanUtils.copyProperties(source, cast);
            return cast;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BizException(String.format("获取实体【%s】无参构造方法失败:%s", clazz.getSimpleName(), e.getMessage()));
        } catch (Throwable e) {
            throw new BizException(String.format("创建实体【%s】对象失败:%s", clazz.getSimpleName(), e.getMessage()));
        }
    }

    default List<Target> beansCopy(List<Source> responseList) {
        return responseList.stream().map(this::beanCopy).collect(Collectors.toList());
    }
}
