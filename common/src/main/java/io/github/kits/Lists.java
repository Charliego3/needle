package io.github.kits;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Lists {

	/**
	 * 判断list是否为null
	 *
	 * @param list @java.util.List
	 * @param <T>  范型
	 * @return Boolean
	 */
	public static <T> boolean isNull(Collection<T> list) {
		return list == null;
	}

	/**
	 * 判断是否不为null
	 *
	 * @param list List
	 * @param <T>  范型
	 * @return Boolean
	 */
	public static <T> boolean isNotNull(Collection<T> list) {
		return !isNull(list);
	}

	/**
	 * 判断是否为空
	 *
	 * @param list List
	 * @return Boolean
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 判断是否不为空
	 *
	 * @param list ObjectList
	 * @return Boolean
	 */
	public static <T> boolean isNotNullOrEmpty(Collection<T> list) {
		return !isNullOrEmpty(list);
	}

	/**
	 * 转换为集合
	 *
	 * @param objects Objectes
	 * @param <T>     范型
	 * @return List
	 */
	@SafeVarargs
	public static <T> List<T> asList(T... objects) {
		if (objects == null || objects.length == 0) {
			return null;
		}
		return Arrays.asList(objects);
	}

	/**
	 * 将数组添加到集合中
	 *
	 * @param <T>        泛型约束
	 * @param targetList 目标集合
	 * @param resource   资源数组
	 */
	@SafeVarargs
	static <T> void add2List(List<T> targetList, T... resource) {
		if (isNotNull(targetList)) {
			List<T> newList = Stream.of(resource)
									.filter(t -> !(t instanceof Field) || !((Field) t).getName().equals("serialVersionUID"))
									.collect(Collectors.toList());
			if (isNotNullOrEmpty(newList)) {
				targetList.addAll(newList);
			}
		}
	}

}
