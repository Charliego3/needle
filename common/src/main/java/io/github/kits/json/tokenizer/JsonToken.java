package io.github.kits.json.tokenizer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonToken {

	private JsonTokenType tokenType;

	private String value;

	public JsonToken(JsonTokenType tokenType, String value) {
		this.tokenType = tokenType;
		this.value = value;
	}

	public JsonToken(JsonTokenType jsonTokenType) {
		this.tokenType = jsonTokenType;
		this.value = jsonTokenType.getValue();
	}

	public JsonTokenType getTokenType() {
		return tokenType;
	}

	public JsonToken setTokenType(JsonTokenType tokenType) {
		this.tokenType = tokenType;
		return this;
	}

	public String getValue() {
		return value;
	}

	public JsonToken setValue(String value) {
		this.value = value;
		return this;
	}

}
