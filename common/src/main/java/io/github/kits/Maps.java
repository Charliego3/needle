package io.github.kits;

import io.github.kits.exception.ConvertException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public static<K,V> boolean isNullOrEmpty(Map<K,V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否不为空
     *
     * @param map Map
     * @return Boolean
     */
    public static<K,V> boolean isNotNullOrEmpty(Map<K,V> map) {
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

}
