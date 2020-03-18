package io.github.kits;

import java.util.Objects;

public class Objs {

	public static <T> T nullDefault(T object, T defaultValue) {
		return Objects.isNull(object) ? defaultValue : object;
	}

}
