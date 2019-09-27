package io.github.kits.configuration;

import io.github.kits.enums.FunctionType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class TypeFunctionConfig<T, R> {

	private Map<FunctionType, Function> functionMap;

	public static<T, R> Builder<T, R> builder() {
		return new Builder<>(newInstance());
	}

	public static class Builder<T, R> {

		private TypeFunctionConfig<T, R> typeFunctionConfig;

		private Builder(TypeFunctionConfig<T, R> config) {
			this.typeFunctionConfig = config;
		}

		public Builder<T, R> string(Function<CharSequence, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.STRING, function);
			return this;
		}

		public Builder<T, R> collection(Function<Collection<T>, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.COLLECTION, function);
			return this;
		}

		public Builder<T, R> map(Function<Map<Object, Object>, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.MAP, function);
			return this;
		}

		public Builder<T, R> date(Function<Date, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.DATE, function);
			return this;
		}

		public Builder<T, R> bigDecimal(Function<BigDecimal, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.BIG_DECIMAL, function);
			return this;
		}

		public Builder<T, R> array(Function<T, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.ARRAY, function);
			return this;
		}

		public Builder<T, R> classes(Function<Class<T>, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.CLASS, function);
			return this;
		}

		public Builder<T, R> bool(Function<Boolean, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.BOOLEAN, function);
			return this;
		}

		public Builder<T, R> number(Function<Number, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.NUMBER, function);
			return this;
		}

		public Builder<T, R> basicType(Function<T, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.BASIC, function);
			return this;
		}

		public Builder<T, R> systemType(Function<T, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.SYSTEM, function);
			return this;
		}

		public Builder<T, R> enums(Function<Enum, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.ENUM, function);
			return this;
		}

		public Builder<T, R> object(Function<Object, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.OBJECT, function);
			return this;
		}

		public Builder<T, R> orElse(Function<Object, R> function) {
			this.typeFunctionConfig.addFunction(FunctionType.ELSE, function);
			return this;
		}

		public TypeFunctionConfig<T, R> build() {
			return typeFunctionConfig;
		}
	}

	private TypeFunctionConfig() {
		this.functionMap = new HashMap<>();
	}

	private TypeFunctionConfig(FunctionType type, Function<T, R> function) {
		this.functionMap = new HashMap<>();
		this.functionMap.put(type, function);
	}

	private static <T, R> TypeFunctionConfig<T, R> newInstance() {
		return new TypeFunctionConfig<>();
	}

	private static <T, R> TypeFunctionConfig<T, R> newInstance(FunctionType type, Function<T, R> function) {
		return new TypeFunctionConfig<>(type, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromString(Function<T, R> function) {
		return newInstance(FunctionType.STRING, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromCollection(Function<T, R> function) {
		return newInstance(FunctionType.COLLECTION, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromMap(Function<T, R> function) {
		return newInstance(FunctionType.MAP, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromDate(Function<T, R> function) {
		return newInstance(FunctionType.DATE, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromBigDecimal(Function<T, R> function) {
		return newInstance(FunctionType.BIG_DECIMAL, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromArray(Function<T, R> function) {
		return newInstance(FunctionType.ARRAY, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromClass(Function<T, R> function) {
		return newInstance(FunctionType.CLASS, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromBoolean(Function<T, R> function) {
		return newInstance(FunctionType.BOOLEAN, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromNumber(Function<T, R> function) {
		return newInstance(FunctionType.NUMBER, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromBasicType(Function<T, R> function) {
		return newInstance(FunctionType.BASIC, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromSystemType(Function<T, R> function) {
		return newInstance(FunctionType.SYSTEM, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromEnum(Function<T, R> function) {
		return newInstance(FunctionType.ENUM, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromObject(Function<T, R> function) {
		return newInstance(FunctionType.OBJECT, function);
	}

	public static <T, R> TypeFunctionConfig<T, R> fromElse(Function<T, R> function) {
		return newInstance(FunctionType.ELSE, function);
	}

	public Function<T, R> getStringFunction() {
		return getFunction(FunctionType.STRING);
	}

	public Function<T, R> getCollectionFunction() {
		return getFunction(FunctionType.COLLECTION);
	}

	public Function<T, R> getMapFunction() {
		return getFunction(FunctionType.MAP);
	}

	public Function<T, R> getDateFunction() {
		return getFunction(FunctionType.DATE);
	}

	public Function<T, R> getBigDecimalFunction() {
		return getFunction(FunctionType.BIG_DECIMAL);
	}

	public Function<T, R> getArrayFunction() {
		return getFunction(FunctionType.ARRAY);
	}

	public Function<T, R> getClassFunction() {
		return getFunction(FunctionType.CLASS);
	}

	public Function<T, R> getBooleanFunction() {
		return getFunction(FunctionType.BOOLEAN);
	}

	public Function<T, R> getNumberFunction() {
		return getFunction(FunctionType.NUMBER);
	}

	public Function<T, R> getBasicTypeFunction() {
		return getFunction(FunctionType.BASIC);
	}

	public Function<T, R> getSystemTypeFunction() {
		return getFunction(FunctionType.SYSTEM);
	}

	public Function<T, R> getEnumFunction() {
		return getFunction(FunctionType.ENUM);
	}

	public Function<T, R> getObjectFunction() {
		return getFunction(FunctionType.OBJECT);
	}

	public Function<T, R> getElseFunction() {
		return getFunction(FunctionType.ELSE);
	}

	private void addFunction(FunctionType type, Function function) {
		this.functionMap.put(type, function);
	}

	private Function<T, R> getFunction(FunctionType type) {
		return this.functionMap.get(type);
	}

}
