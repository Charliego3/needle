package io.github.kits;

import java.util.Objects;

public class Objs {

	public static <T> T nullDefault(T object, T defaultValue) {
		return Objects.isNull(object) ? defaultValue : object;
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
