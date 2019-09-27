package io.github.kits.json.tokenizer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum JsonTokenType {

	BEGIN_OBJECT(1, "{"),
	END_OBJECT(2, "}"),
	BEGIN_ARRAY(4, "["),
	END_ARRAY(8, "]"),
	NULL(16, "null"),
	NUMBER(32, null),
	STRING(64, null),
	BOOLEAN(128, null),
	SEP_COLON(256, ":"),
	SEP_COMMA(512, ","),
	END_DOCUMENT(1024, ""),

	OBJECT(0, null),
	QUOTATION(0, "\""),
	BLANK(0, " "),

	;

	JsonTokenType(int tokenCode, String value) {
		this.code = tokenCode;
		this.value = value;
	}

	private int code;

	private String value;

	public String getValue() {
		return value;
	}

	public int getTokenCode() {
		return code;
	}

}
