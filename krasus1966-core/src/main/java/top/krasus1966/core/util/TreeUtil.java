package top.krasus1966.core.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2022/11/20 18:27
 **/
public class TreeUtil {

    private TreeUtil() {
    }

    /**
     * 生成树型结构，需要原始实体有接收List的能力-不排序
     *
     * @param datas       数据列表
     * @param rootId      根节点id
     * @param getId       获取当前数据id方法
     * @param getParentId 获取当前数据父id方法
     * @param setChildren 设置子列表方法
     * @return java.util.List<T>
     * @method parseTree
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 生成树型结构，需要原始实体有接收List的能力
     */
    public static <T, ID> List<T> parseTree(List<T> datas, ID rootId, Function<T, ID> getId,
                                            Function<T, ID> getParentId,
                                            BiConsumer<T, List<T>> setChildren) {
        return parseTree(datas, rootId, getId, getParentId, setChildren, null);
    }

    /**
     * 生成树型结构，需要原始实体有接收List的能力-排序
     *
     * @param datas       数据列表
     * @param rootId      根节点id
     * @param getId       获取当前数据id方法
     * @param getParentId 获取当前数据父id方法
     * @param setChildren 设置子列表方法
     * @param sort        排序方式
     * @return java.util.List<T>
     * @method parseTree
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 生成树型结构，需要原始实体有接收List的能力
     */
    public static <T, ID> List<T> parseTree(List<T> datas, ID rootId, Function<T, ID> getId,
                                            Function<T, ID> getParentId,
                                            BiConsumer<T, List<T>> setChildren, Comparator<?
            super T> sort) {
        Map<ID, List<T>> map = datas.stream().collect(Collectors.groupingBy(getParentId));
        List<T> rootList = map.get(rootId);
        if (null != rootList && !rootList.isEmpty()) {
            if (null != sort) {
                rootList.sort(sort);
            }
            getAndSetChildList(map, rootList, getId, setChildren, sort);
        }
        return rootList;
    }

    /**
     * 生成树型结构，需要原始实体有接收List的能力-排序
     *
     * @param map         数据列表 map格式
     * @param rootId      根节点id
     * @param getId       获取当前数据id方法
     * @param setChildren 设置子列表方法
     * @param sort        排序方式
     * @return java.util.List<T>
     * @method parseTree
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 生成树型结构，需要原始实体有接收List的能力
     */
    public static <T, ID> List<T> parseTree(Map<ID, List<T>> map, ID rootId,
                                            Function<T, ID> getId,
                                            BiConsumer<T, List<T>> setChildren, Comparator<?
            super T> sort) {
        List<T> rootList = map.get(rootId);
        if (null != rootList && !rootList.isEmpty()) {
            if (null != sort) {
                rootList.sort(sort);
            }
            getAndSetChildList(map, rootList, getId, setChildren, sort);
        }
        return rootList;
    }

    /**
     * 获取列表元素子节点的数据
     *
     * @param parentIdMap 通过父id生成的 String：List Map
     * @param parentList  父列表
     * @param getId       获取当前数据id方法
     * @param setChildren 设置子列表方法
     * @param sort        排序方式，为空则不排序
     * @method getChild
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 获取列表元素子节点的数据
     */
    private static <T, E> void getAndSetChildList(Map<E, List<T>> parentIdMap, List<T> parentList
            , Function<T, E> getId, BiConsumer<T, List<T>> setChildren,
                                                  Comparator<? super T> sort) {
        parentList.forEach(data -> {
            List<T> childList = parentIdMap.get(getId.apply(data));
            if (null != childList && !childList.isEmpty()) {
                if (null != sort) {
                    childList.sort(sort);
                }
                setChildren.accept(data, childList);
                getAndSetChildList(parentIdMap, childList, getId, setChildren, sort);
            }
        });
    }

    /**
     * 生成树型结构，需要原始实体有接收List和层级的能力-不排序
     *
     * @param datas       数据列表
     * @param rootId      根节点id
     * @param getId       获取当前数据id方法
     * @param getParentId 获取当前数据父id方法
     * @param setChildren 设置子列表方法
     * @param setLevel    设置层级的方法
     * @return java.util.List<T>
     * @method parseTree
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 生成树型结构，需要原始实体有接收List和层级的能力-不排序
     */
    public static <T, ID> List<T> parseTreeWithLevel(List<T> datas, ID rootId,
                                                     Function<T, ID> getId,
                                                     Function<T, ID> getParentId,
                                                     BiConsumer<T, List<T>> setChildren,
                                                     BiConsumer<T, Integer> setLevel) {
        return parseTreeWithLevel(datas, rootId, getId, getParentId, setChildren, setLevel, null);
    }

    /**
     * 生成树型结构，需要原始实体有接收List和层级的能力
     *
     * @param datas       数据列表
     * @param rootId      根节点id
     * @param getId       获取当前数据id方法
     * @param getParentId 获取当前数据父id方法
     * @param setChildren 设置子列表方法
     * @param setLevel    设置层级的方法
     * @param sort        排序方式
     * @return java.util.List<T>
     * @method parseTree
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 生成树型结构，需要原始实体有接收List和层级的能力
     */
    public static <T, ID> List<T> parseTreeWithLevel(List<T> datas, ID rootId,
                                                     Function<T, ID> getId,
                                                     Function<T, ID> getParentId,
                                                     BiConsumer<T, List<T>> setChildren,
                                                     BiConsumer<T, Integer> setLevel, Comparator<?
            super T> sort) {
        Map<ID, List<T>> map = datas.stream().collect(Collectors.groupingBy(getParentId));
        List<T> rootList = map.get(rootId);
        if (null != rootList && !rootList.isEmpty()) {
            if (null != sort) {
                rootList.sort(sort);
            }
            getAndSetChildListWithLevel(map, rootList, getId, setChildren, setLevel, 1,
                    sort);
        }
        return rootList;
    }

    /**
     * 获取列表元素子节点的数据
     *
     * @param parentIdMap 通过父id生成的 String：List Map
     * @param parentList  父列表
     * @param getId       获取当前数据id方法
     * @param setChildren 设置子列表方法
     * @param setLevel    设置层级的方法
     * @param sort        排序方式，为空则不排序
     * @method getChild
     * @author krasus1966
     * @date 2022/11/20 19:24
     * @description 获取列表元素子节点的数据
     */
    private static <T, E> void getAndSetChildListWithLevel(Map<E, List<T>> parentIdMap,
                                                           List<T> parentList
            , Function<T, E> getId, BiConsumer<T, List<T>> setChildren,
                                                           BiConsumer<T, Integer> setLevel,
                                                           int level,
                                                           Comparator<? super T> sort) {
        parentList.forEach(data -> {
            setLevel.accept(data, level);
            List<T> childList = parentIdMap.get(getId.apply(data));
            if (null != childList && !childList.isEmpty()) {
                if (null != sort) {
                    childList.sort(sort);
                }
                setChildren.accept(data, childList);
                int nextLevel = level + 1;
                getAndSetChildListWithLevel(parentIdMap, childList, getId, setChildren, setLevel,
                        nextLevel, sort);
            }
        });
    }
}
