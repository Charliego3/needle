package io.github.kits.enums;

import io.github.kits.configuration.TypeFunctionConfig;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum  FunctionType {

	STRING, COLLECTION, MAP, DATE, BIG_DECIMAL, ARRAY, CLASS, BOOLEAN, NUMBER, BASIC, SYSTEM, ENUM, OBJECT, ELSE

	;

	public static <T, R> Optional<Function<T, R>> getFunction(FunctionType type, TypeFunctionConfig<T, R> config) {
		Function<T, R> function = null;
		switch (type) {
			case STRING:
				function = config.getStringFunction();
				break;
			case COLLECTION:
				function = config.getCollectionFunction();
				break;
			case MAP:
				function = config.getMapFunction();
				break;
			case DATE:
				function = config.getDateFunction();
				break;
			case BIG_DECIMAL:
				function = config.getBigDecimalFunction();
				break;
			case ARRAY:
				function = config.getArrayFunction();
				break;
			case CLASS:
				function = config.getClassFunction();
				break;
			case BOOLEAN:
				function = config.getBooleanFunction();
				break;
			case NUMBER:
				function = config.getNumberFunction();
				break;
			case BASIC:
				function = config.getBasicTypeFunction();
				break;
			case SYSTEM:
				function = config.getSystemTypeFunction();
				break;
			case ENUM:
				function = config.getEnumFunction();
				break;
			case OBJECT:
				function = config.getObjectFunction();
				break;
		}

		if (Objects.isNull(function)) {
			function = config.getElseFunction();
		}
		return Optional.ofNullable(function);
	}

}
