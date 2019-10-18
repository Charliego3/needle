package io.github.kits;

import io.github.kits.configuration.TypeFunctionConfig;
import io.github.kits.enums.FunctionType;
import io.github.kits.exception.TypeException;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	public static <T, R> R function(T object, TypeFunctionConfig<T, R> config) {
		if (Objects.isNull(object)) {
			throw new TypeException("Class Type is null!");
		}
		R r = null;
		if (object instanceof Collection) {
			r = apply(COLLECTION, config, object);
		} else if (object instanceof Map) {
			r = apply(MAP, config, object);
		} else if (object instanceof Date) {
			r = apply(DATE, config, object);
		} else if (object instanceof BigDecimal) {
			r = apply(BIG_DECIMAL, config, object);
		} else if (object.getClass().isArray()) {
			r = apply(ARRAY, config, object);
		} else if (object instanceof Class) {
			r = apply(CLASS, config, object);
		} else if (object instanceof Boolean) {
			r = apply(BOOLEAN, config, object);
		} else if (object instanceof Number && !(object instanceof Byte)) {
			r = apply(NUMBER, config, object);
		} else if (Envs.isBasicType(object.getClass())) {
			r = apply(BASIC, config, object);
		} else if (Envs.isSystemType(object.getClass())) {
			r = apply(SYSTEM, config, object);
		} else if (object instanceof Enum) {
			r = apply(ENUM, config, object);
		} else if (object instanceof CharSequence || object instanceof Character) {
			r = apply(STRING, config, object);
		} else {
			r = apply(OBJECT, config, object);
		}
		return r;
	}

	private static <T, R> R apply(FunctionType type, TypeFunctionConfig<T, R> config, T object) {
		return FunctionType.getFunction(type, config)
						   .map(function -> function.apply(object))
						   .orElse(null);
	}

	public static <T, R> R type(Object object, Class<R> target) {
		if (Objects.isNull(target)) {
			throw new TypeException("Target class is null!");
		}
		R r = null;
		if (Collection.class.isAssignableFrom(target)) {
			if (object instanceof List || object instanceof Set) {
				return (R) object;
			}
			if (List.class.isAssignableFrom(target)) {
				ArrayList<Object> objects = new ArrayList<>();
				objects.add(object);
				return (R) objects;
			} else if (Set.class.isAssignableFrom(target)) {
				HashSet<Object> objects = new HashSet<>();
				objects.add(object);
				return (R) objects;
			}
		} else if (Map.class.isAssignableFrom(target)) {

		} else if (Date.class.isAssignableFrom(target)) {

		} else if (BigDecimal.class.isAssignableFrom(target)) {

		} else if (target.isArray()) {

		} else if (Class.class.isAssignableFrom(target)) {

		} else if (Boolean.class.isAssignableFrom(target)) {

		} else if (Number.class.isAssignableFrom(target) && !(Byte.class.isAssignableFrom(target))) {

		} else if (Envs.isBasicType(target)) {

		} else if (Envs.isSystemType(target)) {

		} else if (Enum.class.isAssignableFrom(target)) {

		} else if (CharSequence.class.isAssignableFrom(target) || Character.class.isAssignableFrom(target)) {

		} else {

		}
		return r;
	}

}
