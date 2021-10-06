package top.krasus1966.dozer.utils;

import com.github.dozermapper.core.Mapper;
import top.krasus1966.dozer.exception.DozerException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2021/9/25 00:18
 **/
public class DozerUtils {

    private final Mapper mapper;

    public DozerUtils(Mapper mapper) {
        this.mapper = mapper;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
           return null;
        }
        return mapper.map(source, destinationClass);
    }

    public <T> T map2(Object source, Class<T> destinationClass) {
        if (source == null) {
            try {
                return destinationClass.newInstance();
            } catch (Exception e) {

            }
        }
        return mapper.map(source, destinationClass);
    }

    public <T> T map(Object source, Class<T> destinationClass, String mapId) {
        if (source == null) {
            try {
                return destinationClass.newInstance();
            } catch (Exception e) {
                throw new DozerException("属性拷贝失败");
            }
        }
        return mapper.map(source, destinationClass, mapId);
    }

    public void map(Object source, Object destinationClass) {
        if (source == null) {
            return;
        }
        mapper.map(source, destinationClass);
    }

    public void map(Object source, Object destinationClass, String mapId) {
        if (source == null) {
            return;
        }
        mapper.map(source, destinationClass, mapId);
    }

    public <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
        return mapPage(sourceList, destinationClass);
    }

    public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
            return Collections.emptyList();
        }
        return sourceList.stream()
                .filter(Objects::nonNull)
                .map(sourceObject -> mapper.map(sourceObject, destinationClass))
                .collect(Collectors.toList());
    }

    public <T, E> Set<T> mapSet(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
            return Collections.emptySet();
        }
        return sourceList.stream().map(sourceObject -> mapper.map(sourceObject, destinationClass)).collect(Collectors.toSet());
    }
}
