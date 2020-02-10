package io.github.kits;

import io.github.kits.configuration.TypeFunctionConfig;
import io.github.kits.enums.FunctionType;
import io.github.kits.exception.TypeException;
import io.github.kits.json.Json;
import io.github.kits.log.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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
		R r = null;
		if (Collection.class.isAssignableFrom(target)) {
			if (List.class.isAssignableFrom(target)) {
				ArrayList<Object> objects = new ArrayList<>();
				objects.add(object);
				r = (R) objects;
			} else if (Set.class.isAssignableFrom(target)) {
				HashSet<Object> objects = new HashSet<>();
				objects.add(object);
				r = (R) objects;
			}
		} else if (Map.class.isAssignableFrom(target)) {
			HashMap<Object, Object> maps = new HashMap<>();
			maps.put("", object);
			r = (R) maps;
		} else if (Date.class.isAssignableFrom(target)) {
			if (object instanceof Date) {
				r = (R) object;
			} else {
				r = (R) DateTimes.get(object.toString());
			}
		} else if (BigDecimal.class.isAssignableFrom(target)) {
			if (object instanceof BigDecimal) {
				r = (R) object;
			} else {
				r = (R) new BigDecimal(object.toString());
			}
		} else if (target.isArray()) {
			if (object.getClass().isArray()) {
				r = array(object, target);
			} else {
				if (target.getComponentType().isPrimitive()) {
					Object arr = Array.newInstance(target.getComponentType(), 1);
					Array.set(arr, 0, object);
					r = (R) arr;
				} else {
					String name = target.getName();
					if (Strings.isNotBlack(name)) {
						name = name.substring(2);
						name = name.substring(0, name.length() - 1);
					}
					try {
						Object arr = Array.newInstance(Envs.forName(name), 1);
						Array.set(arr, 0, object);
						r = (R) arr;
					} catch (ClassNotFoundException e) {
						Logger.error("Cast Object Array is error", e);
					}
				}
			}
		} else if (Class.class.isAssignableFrom(target)) {

		} else if (Boolean.class.isAssignableFrom(target)) {
			if (object instanceof Boolean) {
				r = (R) object;
			} else {
				boolean b = false;
				if (Strings.isBoolean(object.toString())) {
					b = Boolean.parseBoolean(object.toString());
				}
				r = (R) Boolean.valueOf(b);
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
			r = (R) object.toString();
		} else {
			r = (R) object;
		}
		return r;
	}

	private static <T> T array(Object object, Class<T> tClass) {
		int length = Array.getLength(object);
		Object result = null;
//		switch (tClass.getName()) {
//			case "[S":
//				result = new short[length];
//				break;
//			case "[Ljava.lang.Short;":
//				result = new Short[length];
//				break;
//			case "[I":
//				result = new int[length];
//				break;
//			case "[Ljava.lang.Integer;":
//				result = new Integer[length];
//				break;
//			case "[J":
//				result = new long[length];
//				break;
//			case "[Ljava.lang.Long;":
//				result = new Long[length];
//				break;
//			case "[F":
//				result = new float[length];
//				break;
//			case "[Ljava.lang.Float;":
//				result = new Float[length];
//				break;
//			case "[D":
//				result = new double[length];
//				break;
//			case "[Ljava.lang.Double;":
//				result = new Double[length];
//				break;
//			case "[Z":
//				result = new boolean[length];
//				break;
//			case "[Ljava.lang.Boolean;":
//				result = new Boolean[length];
//				break;
//			case "[C":
//				result = new char[length];
//				break;
//			case "[Ljava.lang.Character;":
//				result = new Character[length];
//				break;
//			case "[B":
//				result = new byte[length];
//				break;
//			case "[Ljava.lang.Byte;":
//				result = new Byte[length];
//				break;
//		}
		result = Array.newInstance(tClass.getComponentType(), length);
//		if (Objects.nonNull(result)) {
			for (int i = 0; i < length; i++) {
				Array.set(result, i, Array.get(object, i));
			}
//		}
		return (T) result;
	}

	private static<T> T object(Object object, Class<T> tClass) {
		return null;
	}

}
