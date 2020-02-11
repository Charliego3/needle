package io.github.kits;

import io.github.kits.exception.ConvertException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Map tool class
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Maps {

    /**
     * 判断map是否为空
     *
     * @param map Map
     * @return Boolean
     */
    public static<K, V> boolean isNullOrEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否不为空
     *
     * @param map Map
     * @return Boolean
     */
    public static<K, V> boolean isNotNullOrEmpty(Map<K, V> map) {
        return !isNullOrEmpty(map);
    }

    /**
     * 将多个参数组合为map
     *
     * @param objs String
     * @return Map
     */
    public static Map<Object, Object> asMap(Object... objs) {
        Map<Object, Object> map = new LinkedHashMap<>();
        for(int i = 1; i < objs.length; i += 2) {
            map.put(objs[i - 1], objs[i]);
        }
        return map;
    }

    /**
     * 将对象字段封装为Map
     *
     * @param object 需要转换的对象
     * @return 转换后的Map
     */
    public static Map<Object, Object> toMap(Object object) {
        Assert.isNotNull(object, new ConvertException("Object is null"));
        Assert.isTrue(!(Envs.isBasicType(object.getClass()) && Envs.isSystemType(object.getClass())),
            new ConvertException(object.getClass().getName() + " is not support convert to Map. "));
        if (object instanceof Map)
            return (Map<Object, Object>) object;
        HashMap<Object, Object> map = new HashMap<>();
        List<Field> fields = Reflective.getFields(object.getClass());
        if (Lists.isNotNullOrEmpty(fields)) {
            fields.forEach(field -> {
                Object value = Reflective.getFieldValue(object, field);
                map.put(field.getName(), value);
            });
        }
        return map;
    }

    /**
     * 移除Map中Key或Value为null的节点
     *
     * @param nullNodeMap 有键值为null的Map
     */
    public static<K, V> void removeNullNode(Map<K, V> nullNodeMap) {
        removeNode(nullNodeMap, entry -> Envs.isNullEmptyBlack(entry.getKey(), entry.getValue()));
    }

    /**
     * 移除Map中Key为null的节点
     *
     * @param nullKeyMap 有键值为null的Map
     */
    public static<K, V> void removeNullKeyNode(Map<K, V> nullKeyMap) {
        removeNode(nullKeyMap, null);
    }

    /**
     * 移除Map中Value为null的节点
     *
     * @param nullValueMap 有键值为null的Map
     */
    public static<K, V> void removeNullValueNode(Map<K, V> nullValueMap) {
        removeNode(nullValueMap, entry -> Envs.isNullEmptyBlack(entry.getValue()));
    }

    /**
     * 根据断言移除Map中的节点
     *
     * @param map Map
     * @param predicate 断言
     */
    private static<K, V> void removeNode(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        if (Objects.nonNull(map)) {
            if (Objects.isNull(predicate)) {
                map.remove(null);
            } else {
                map.entrySet().removeIf(predicate);
            }
        }
    }

    /**
     * 根据Key排序, Key必须实现Comparable接口
     *
     * @param map 待排序的Map
     * @param <K> Key
     * @param <V> Value
     * @return 排序后的Map
     */
    public static<K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        if (Objects.nonNull(map)) {
            return map.entrySet().stream()
               .sorted(Map.Entry.comparingByKey())
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                   (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        return null;
    }

    /**
     * 根据Value排序, Value必须实现Comparable接口
     *
     * @param map 待排序的Map
     * @param <K> Key
     * @param <V> Value
     * @return 排序后的Map
     */
    public static<K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        if (Objects.nonNull(map)) {
           return map.entrySet().stream()
               .sorted(Map.Entry.comparingByValue())
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                   (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        }
        return null;
    }

}
