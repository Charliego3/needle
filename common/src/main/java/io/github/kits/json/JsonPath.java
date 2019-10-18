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
public class JsonPath extends HashMap implements JsonKind {

	private JsonPath() {}

	public static JsonPath newInstance(String json) {
		return new JsonDecoder<>().jsonPath(json);
	}

	public static JsonPath newInstance() {
		return new JsonPath();
	}

	public <T> T get(String key, Class<T> target) {
		if (JsonPath.class.isAssignableFrom(target)) {
			return (T) this;
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
		for (int i = 0; i < paths.length; i++) {
			if (Objects.nonNull(tempJson)) {
				if (tempJson instanceof Map) {
					tempJson = ((Map) tempJson).get(paths[i]);
				}
			}
			if (i == paths.length - 1) {
				return tempJson;
			}
		}
		return null;
	}

}
