package io.github.kits.json;

import java.util.List;

/**
 * 自定义Json解析工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Json {

	private static JsonSupport jsonEncoder = JsonEncoder.newInstance();

	public static <T> void setJsonEncoder(JsonEncoder encoder) {
		jsonEncoder = encoder;
	}

	public static <T> T toObject(String json, Class<T> targetClass) {
		return new JsonDecoder<T>().toObject(json, targetClass);
	}

	public static <T> List<T> toList(String json, Class<T> targetClass) {
		return new JsonDecoder<T>().toList(json, targetClass);
	}

	public static <T> T jsonPath(String json, String path, Class<T> targetClass) {
		return new JsonDecoder<T>().jsonPath(json, path, targetClass);
	}

	public static String toJson(Object object) {
		return jsonEncoder.toJson(object);
	}

	public static String toJson(Object object, boolean isPretty) {
		return jsonEncoder.toJson(object, isPretty);
	}

	public static String prettyJson(String json) {
		return jsonEncoder.prettyJson(json);
	}

	public static boolean isJsonObject(String json) {
		return jsonEncoder.isJsonObject(json);
	}

	public static boolean isJsonArray(String json) {
		return jsonEncoder.isJsonArray(json);
	}

	public static boolean isJson(String json) {
		return jsonEncoder.isJson(json);
	}

}
