package io.github.kits;

import io.github.kits.configuration.TypeFunctionConfig;
import io.github.kits.enums.FunctionType;
import io.github.kits.exception.TypeException;
import io.github.kits.json.annotations.IgnoreJsonSerialize;
import io.github.kits.json.annotations.JsonCamelCase;
import io.github.kits.json.annotations.JsonSerializeName;
import io.github.kits.log.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.kits.enums.FunctionType.ARRAY;
import static io.github.kits.enums.FunctionType.BASIC;
import static io.github.kits.enums.FunctionType.BIG_DECIMAL;
import static io.github.kits.enums.FunctionType.BOOLEAN;
import static io.github.kits.enums.FunctionType.CLASS;
import static io.github.kits.enums.FunctionType.COLLECTION;
import static io.github.kits.enums.FunctionType.DATE;
import static io.github.kits.enums.FunctionType.ENUM;
import static io.github.kits.enums.FunctionType.MAP;
import static io.github.kits.enums.FunctionType.NUMBER;
import static io.github.kits.enums.FunctionType.OBJECT;
import static io.github.kits.enums.FunctionType.STRING;
import static io.github.kits.enums.FunctionType.SYSTEM;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Types {

	public static final String[] basicArrName = {"[S", "[I", "[J", "[F", "[D", "[Z"};

	public static <T, R> R function(T object, TypeFunctionConfig<T, R> config) {
		if (Objects.isNull(object)) {
			throw new TypeException("Class Type is null!");
		}
		FunctionType functionType = OBJECT;
		if (object instanceof Collection) {
			functionType = COLLECTION;
		} else if (object instanceof Map) {
			functionType = MAP;
		} else if (object instanceof Date) {
			functionType = DATE;
		} else if (object instanceof BigDecimal) {
			functionType = BIG_DECIMAL;
		} else if (object.getClass().isArray()) {
			functionType = ARRAY;
		} else if (object instanceof Class) {
			functionType = CLASS;
		} else if (object instanceof Boolean) {
			functionType = BOOLEAN;
		} else if (object instanceof Number && !(object instanceof Byte)) {
			functionType = NUMBER;
		} else if (Envs.isBasicType(object.getClass())) {
			functionType = BASIC;
		} else if (Envs.isSystemType(object.getClass())) {
			functionType = SYSTEM;
		} else if (object instanceof Enum) {
			functionType = ENUM;
		} else if (object instanceof CharSequence || object instanceof Character) {
			functionType = STRING;
		}
		return apply(functionType, config, object);
	}

	private static <T, R> R apply(FunctionType type, TypeFunctionConfig<T, R> config, T object) {
		return FunctionType.getFunction(type, config)
						   .map(function -> function.apply(object))
						   .orElse(null);
	}

	public static <R> R type(Object object, Class<R> target) {
		if (Objects.isNull(target)) {
			throw new TypeException("Target class is null!");
		}
		if (Objects.isNull(object)) {
			return null;
		}
		Object instance = null;
		try {
			instance = Reflective.newInstance(target);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			Logger.errorf("Type convert error", e);
		}
		if (Collection.class.isAssignableFrom(target)) {
			if (object instanceof Collection) {
				instance = object;
			} else {
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) instance;
				if (Lists.isNotNullOrEmpty(collection)) collection.add(object);
			}
		} else if (Map.class.isAssignableFrom(target)) {
			if (object instanceof Map) {
				instance = object;
			} else {
				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) instance;
				if (Objects.nonNull(map)) map.put(null, object);
			}
		} else if (Date.class.isAssignableFrom(target)) {
			instance = object instanceof Date ? object : DateTimes.get(object.toString());
		} else if (BigDecimal.class.isAssignableFrom(target)) {
			instance = object instanceof BigDecimal ? object : new BigDecimal(object.toString());
		} else if (target.isArray()) {
			instance = array(object, target);
		} else if (Class.class.isAssignableFrom(target)) {
			instance = target;
		} else if (Boolean.class.isAssignableFrom(target)) {
			if (object instanceof Boolean) {
				instance = object;
			} else {
				boolean b = false;
				if (Strings.isBoolean(object.toString())) {
					b = Boolean.parseBoolean(object.toString());
				}
				instance = b;
			}
		} else if (Number.class.isAssignableFrom(target) && !(Byte.class.isAssignableFrom(target))) {

		}
//		else if (Envs.isBasicType(target)) {
//
//		} else if (Envs.isSystemType(target)) {
//
//		}
		else if (Enum.class.isAssignableFrom(target)) {

		} else if (CharSequence.class.isAssignableFrom(target) || Character.class.isAssignableFrom(target)) {
			instance = object.toString();
		} else {
			object(object, instance);
		}
		@SuppressWarnings("unchecked")
		R r = (R) instance;
		return r;
	}

	private static void object(Object object, Object target) {
		Map<Object, Object> resource = null;
		if (Objects.nonNull(object)) {
			if (object instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) object;
				resource = map;
			} else {
				resource = Maps.toMap(object);
			}
		}
		Map<Object, Object> finalResource = resource;
		if (Envs.isNotNullEmptyBlack(finalResource, target)) {
			List<Field> fields = Reflective.getFields(target.getClass());
			if (Lists.isNotNullOrEmpty(fields)) {
				fields.forEach(field -> {
					IgnoreJsonSerialize annotation = field.getAnnotation(IgnoreJsonSerialize.class);
					if (Objects.nonNull(annotation)) {
						return;
					}
					String fieldName = field.getName();
					JsonSerializeName serializeName = field.getAnnotation(JsonSerializeName.class);
					if (Objects.nonNull(serializeName)) {
						fieldName = serializeName.value();
					} else {
						JsonCamelCase camelCase = field.getAnnotation(JsonCamelCase.class);
						if (Objects.nonNull(camelCase)) {
							fieldName = Strings.unCameCase(fieldName);
						}
					}
					Object value = finalResource.get(fieldName);
					Reflective.setFieldValue(target, field, value);
				});
			}
		}
	}

	/**
	 * 构建新数组，并将object的值放入新数组中
	 *
	 * @param object 对象
	 * @param tClass 需要转换的目标对象类型
	 * @param <T>    范型
	 * @return 新构建的数组
	 */
	private static <T> T array(Object object, Class<T> tClass) {
		Object arr = null;
		if (Objects.isNull(object)) {
			arr = Array.newInstance(tClass.getComponentType(), 0);
		} else if (object.getClass().isArray() || object instanceof Collection) {
			if (object instanceof Collection) {
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) object;
				object = collection.toArray();
			}
			int length = Array.getLength(object);
			arr = Array.newInstance(tClass.getComponentType(), length);
			for (int i = 0; i < length; i++) {
				Array.set(arr, i, Array.get(object, i));
			}
		} else {
			Class<?> singleArrType = tClass.getComponentType();
			if (!tClass.getComponentType().isPrimitive()) {
				String name = tClass.getName();
				if (Strings.isNotBlack(name)) {
					name = name.substring(2);
					name = name.substring(0, name.length() - 1);
				}
				singleArrType = Envs.forName(name);
				if (!singleArrType.isAssignableFrom(object.getClass())) {
					throw new TypeException("array element type mismatch: " + object.getClass().getName()
												+ " for array type: " + tClass.getName());
				}
			}
			arr = Array.newInstance(singleArrType, 1);
			Array.set(arr, 0, object);
		}
		@SuppressWarnings("unchecked")
		T t = (T) arr;
		return t;
	}

}
