package io.github.kits;

import io.github.kits.exception.ReflectiveException;
import io.github.kits.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Reflective {

	private static final ConcurrentHashMap<Class<?>, List<Field>> FIELDS = new ConcurrentHashMap<>();

	/**
	 * 获取class的字段
	 * 包括父类的
	 *
	 * @param tClass 资源类
	 * @return 字段集合
	 */
	public static List<Field> getFields(Class<?> tClass) {
		List<Field> fieldList = FIELDS.get(tClass);
		if (Lists.isNull(fieldList)) {
			fieldList = new ArrayList<>();
			Lists.add2List(fieldList, tClass.getDeclaredFields());
			while (Objects.nonNull(tClass.getSuperclass()) && !Envs.isBasicType(tClass.getSuperclass())
					&& !Envs.isSystemType(tClass.getSuperclass())) {
				Lists.add2List(fieldList, tClass.getSuperclass().getDeclaredFields());
				tClass = tClass.getSuperclass();
			}
			FIELDS.put(tClass, fieldList);
		}
		return fieldList;
	}

	/**
	 * Execute the setter method
	 *
	 * @param object Object
	 * @param field  Field
	 * @param params Parameter
	 */
	public static void setFieldValue(Object object, Field field, Object params) {
		String   methodName = complexSetMethodName(field.getName());
		Class<?> tClass     = null;
		try {
			tClass = object.getClass();
			Method  method     = tClass.getDeclaredMethod(methodName, field.getType());
			boolean accessible = method.isAccessible();
			method.setAccessible(true);
			method.invoke(object, params);
			method.setAccessible(accessible);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			if (Objects.nonNull(tClass.getSuperclass()) && !Envs.isBasicType(tClass.getSuperclass())
					&& !Envs.isSystemType(tClass.getSuperclass())) {
				setFieldValue(object, field, params);
			} else {
				throw new ReflectiveException(e);
			}
		}
	}

	/**
	 * 完善字段setter方法名
	 *
	 * @param fieldName 字段名称
	 * @return 修改后的值
	 */
	public static String complexSetMethodName(String fieldName) {
		return "set" + Strings.toUpperPrefix(fieldName);
	}

	/**
	 * 根据构造器反射初始化对象, 构造器最小参数原则
	 * Initialize the object according to the constructor reflection
	 * Constructor minimum parameter principle
	 *
	 * @param target 目标对象类型
	 *                    Target object type
	 * @return 初始化的对象
	 * Initialized object
	 *
	 * @throws IllegalAccessException 非法访问异常
	 * @throws InstantiationException 实例化异常
	 * @throws InvocationTargetException 调用目标异常
	 */
	public static <T> T newInstance(Class<T> target) throws IllegalAccessException, InstantiationException, InvocationTargetException {
		T instance = null;
		if (Envs.isBasicType(target) || Envs.isSystemType(target)) {
			if (target.isInterface()) {
				Object t = null;
				if (List.class.isAssignableFrom(target)) {
					t = new ArrayList<>();
				} else if (Map.class.isAssignableFrom(target)) {
					t = new HashMap<>();
				} else if (Set.class.isAssignableFrom(target)) {
					t = new HashSet<>();
				}
				@SuppressWarnings("unchecked")
				T it = (T) t;
				instance = it;
			} else {
				instance = target.newInstance();
			}
		}

		if (Objects.nonNull(instance)) {
			return instance;
		}

		Constructor<?> constructor =
			Arrays.stream(target.getDeclaredConstructors())
				  .min(Comparator.comparingInt(v -> v.getParameterTypes().length))
				  .orElseThrow(() -> new NullPointerException("No Constructor, Class: "
																  + target.getCanonicalName()));

		Class<?>[] parameterTypes = constructor.getParameterTypes();
		Object[] params = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> type = parameterTypes[i];
			params[i] = newInstance(type);
		}

		boolean accessible = constructor.isAccessible();
		constructor.setAccessible(true);
		@SuppressWarnings("unchecked")
		T t = (T) constructor.newInstance(params);
		instance = t;
		constructor.setAccessible(accessible);
		return instance;
	}

	/**
	 * 根据Field获取值
	 *
	 * @param object 对象
	 * @param field  属性
	 * @return value
	 */
	public static Object getFieldValue(Object object, Field field) {
		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			Object value = field.get(object);
			field.setAccessible(accessible);
			return value;
		} catch (IllegalAccessException e) {
			Logger.error("Reflect get field value error", e);
		}
		return null;
	}

}
