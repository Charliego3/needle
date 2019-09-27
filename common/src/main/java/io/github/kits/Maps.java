package io.github.kits;

import io.github.kits.json.Json;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
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
    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否不为空
     *
     * @param map Map
     * @return Boolean
     */
    public static boolean isNotNullOrEmpty(Map map) {
        return !isNullOrEmpty(map);
    }

    /**
     * 将map转换为json
     *
     * @param map   任意map
     * @return      转换后的结果
     */
    public static String toJSON(Map<?, ?> map) {
        StringBuilder jsonString = new StringBuilder("{");
        if (isNullOrEmpty(map)) {
            return "null";
        }
        map.forEach((key, value) -> {
            if (isStringOrDate(key)) {
                jsonString.append("\"").append(Json.toJson(key)).append("\"");
            } else {
                jsonString.append(Json.toJson(key));
            }
            if (isStringOrDate(value)) {
                jsonString.append(": \"").append(Json.toJson(value)).append("\"");
            } else {
                jsonString.append(": ").append(Json.toJson(value));
            }
            jsonString.append(", ");
        });
        return jsonString.deleteCharAt(jsonString.lastIndexOf(",")).deleteCharAt(jsonString.lastIndexOf(" ")).append("}").toString();
    }

    /**
     * 判断是否是String或Date类型
     *
     * @param object    需要判断的对象
     * @return          是(true)
     */
    private static boolean isStringOrDate(Object object) {
        return object instanceof CharSequence || object instanceof Character || object instanceof Date;
    }

    /**
     * 将多个参数组合为map
     *
     * @param objs String
     * @return Map
     */
    public static Map asMap(String... objs) {
        Map<Object, Object> map = new LinkedHashMap<>();
        for(int i = 1; i < objs.length; i += 2) {
            map.put(objs[i - 1], objs[i]);
        }
        return map;
    }

    public static Map<Object, Object> jsonToMap(String json, String path) {
        Assert.isNotNullEmptyBlack(json, "Json string is null or empty!");
        boolean isAll = false;
        if (Strings.isBlack(path) || Arrays.asList(".", "/").contains(path)) {
            isAll = true;
        }



        return null;
    }

}
