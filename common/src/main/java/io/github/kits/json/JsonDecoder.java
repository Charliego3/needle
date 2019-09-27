package io.github.kits.json;

import io.github.kits.Assert;
import io.github.kits.Strings;
import io.github.kits.exception.JsonNotSupportedException;
import io.github.kits.exception.JsonParseException;
import io.github.kits.json.tokenizer.JsonTokenizer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonDecoder<T> implements JsonSupport<T> {

	private JsonTokenizer tokenizer = JsonTokenizer.newInstance();

	@Override
	public T toObject(String json, Class<T> targetClass) {
		return jsonPath(json, "/", targetClass);
	}

	@Override
	public List<T> toList(String json, Class<T> targetClass) {
		if (Strings.isNullOrEmpty(json))
			return Collections.emptyList();
		return null;
	}

	@Override
	public T jsonPath(String json, String path, Class<T> targetClass) {
		Assert.isNotNull(targetClass, "Target Class is null!");

		T object = null;

		try {
			Object tokenize = tokenizer.tokenize(json, path);
//			JsonTokenList tokenList = tokenizer.tokenize(json, path);
//			object = Reflective.instance(targetClass);
//			while (tokenList.hasMore()) {
//				JsonToken jsonToken = tokenList.next();
//				switch (jsonToken.getTokenType()) {
//					case BEGIN_OBJECT:
//						System.out.print(";;");
//					case END_DOCUMENT:
//						break;
//				}
//			}
		} catch (IOException ie) {
			throw new JsonParseException(ie);
		}
		return object;
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
