package io.github.kits.json;

import io.github.kits.Assert;
import io.github.kits.Strings;
import io.github.kits.exception.JsonNotSupportedException;
import io.github.kits.exception.JsonParseException;
import io.github.kits.json.tokenizer.JsonTokenizer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonDecoder<T> implements JsonSupport<T> {

	private final JsonTokenizer tokenizer = JsonTokenizer.newInstance();

	@Override
	public T toObject(String json, Class<T> target) {
		return jsonPath(json, "/", target);
	}

	@Override
	public List<T> toList(String json, Class<T> target) {
		if (Strings.isNullOrEmpty(json))
			return Collections.emptyList();
		return null;
	}

	@Override
	public <V> V jsonPath(String json, String path, Class<V> target) {
		Assert.isNotNull(target, "Target Class is null!");
		Assert.isTrue(Json.isJsonObject(json),
			new JsonParseException("JsonString does not support path acquisition! Json: " + json));
		try {
			JsonKind tokenize = tokenizer.tokenize(json);
			if (tokenize instanceof JsonPath) {
				JsonPath kvJsonPath = (JsonPath) tokenize;
				return kvJsonPath.get(path, target);
			}
		} catch (IOException ie) {
			throw new JsonParseException(ie);
		}
		return null;
	}

	@Override
	public JsonPath jsonPath(String json) {
		return jsonPath(json, "/", JsonPath.class);
	}

	@Override
	public final String toJson(Object object) {
		throw new JsonNotSupportedException();
	}

	@Override
	public final String toJson(Object object, boolean isPretty) {
		throw new JsonNotSupportedException();
	}

	@Override
	public final String prettyJson(String json) {
		throw new JsonNotSupportedException();
	}

}
