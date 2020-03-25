package io.github.kits;

import java.util.Objects;
import java.util.function.Supplier;

public class Objs {

	public static <T> T nullDefault(T object, T defaultValue) {
		return Objects.isNull(object) ? defaultValue : object;
	}

	public static <T> T nullDefault(T object, Supplier<T> supplier) {
		return Objects.isNull(object) ? supplier.get() : object;
	}

	public static boolean isAllNull(Object... objects) {
		if (Objects.isNull(objects) || objects.length <= 0) {
			return true;
		}
		for (Object object : objects) {
			if (Objects.nonNull(object)) {
				return false;
			}
		}
		return true;
	}

}
