package io.github.kits.json;

import io.github.kits.Strings;
import io.github.kits.Types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonPath extends HashMap<Object, Object> implements JsonKind {

	private JsonPath() {}

	public static JsonPath newInstance(String json) {
		return new JsonDecoder<>().jsonPath(json);
	}

	public static JsonPath newInstance() {
		return new JsonPath();
	}

	public <T> T get(String key, Class<T> target) {
		if (JsonPath.class.isAssignableFrom(target)) {
			@SuppressWarnings("unchecked")
			T t = (T) this;
			return t;
		}
		Object   value;
		boolean  isAllValue = false;
		String[] paths      = new String[0];
		if (Strings.isBlack(key) || Arrays.asList(".", "/").contains(key)) {
			isAllValue = true;
		} else {
			if (key.startsWith(".") || key.startsWith("/")) {
				key = key.substring(1);
			}
			paths = key.split("[./]");
		}
		if (!isAllValue) {
			value = getPathValue(paths);
		} else {
			value = this;
		}
		return Types.type(value, target);
	}

	private Object getPathValue(String[] paths) {
		Object tempJson = this;
		int i = 0;
		do {
			if (Objects.nonNull(tempJson)) {
				if (tempJson instanceof Map) {
					@SuppressWarnings("unchecked")
					Object tj = ((Map<Object, Object>) tempJson).get(paths[i]);
					tempJson = tj;
				}
			}
			if (i == paths.length - 1) {
				return tempJson;
			}
			i++;
		} while (i < paths.length);
		return null;
	}

}
