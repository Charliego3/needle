package io.github.kits.json.tokenizer;

import io.github.kits.Assert;
import io.github.kits.Envs;
import io.github.kits.Strings;
import io.github.kits.exception.JsonParseException;
import io.github.kits.json.Json;
import io.github.kits.json.JsonList;
import io.github.kits.json.JsonPath;
import io.github.kits.json.JsonKind;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;

import static io.github.kits.json.tokenizer.JsonTokenKind.BACKSLASH;
import static io.github.kits.json.tokenizer.JsonTokenKind.COLON;
import static io.github.kits.json.tokenizer.JsonTokenKind.COMMA;
import static io.github.kits.json.tokenizer.JsonTokenKind.DOUBLE_QUOTE;
import static io.github.kits.json.tokenizer.JsonTokenKind.EIGHT;
import static io.github.kits.json.tokenizer.JsonTokenKind.F;
import static io.github.kits.json.tokenizer.JsonTokenKind.FIVE;
import static io.github.kits.json.tokenizer.JsonTokenKind.FOUR;
import static io.github.kits.json.tokenizer.JsonTokenKind.LEFT_BIG_PARANTHESES;
import static io.github.kits.json.tokenizer.JsonTokenKind.LEFT_BRACKET;
import static io.github.kits.json.tokenizer.JsonTokenKind.N;
import static io.github.kits.json.tokenizer.JsonTokenKind.NEGATIVE;
import static io.github.kits.json.tokenizer.JsonTokenKind.NINE;
import static io.github.kits.json.tokenizer.JsonTokenKind.ONE;
import static io.github.kits.json.tokenizer.JsonTokenKind.RIGHT_BIG_PARANTHESES;
import static io.github.kits.json.tokenizer.JsonTokenKind.RIGHT_BRACKET;
import static io.github.kits.json.tokenizer.JsonTokenKind.SEVEN;
import static io.github.kits.json.tokenizer.JsonTokenKind.SIX;
import static io.github.kits.json.tokenizer.JsonTokenKind.SP;
import static io.github.kits.json.tokenizer.JsonTokenKind.T;
import static io.github.kits.json.tokenizer.JsonTokenKind.THREE;
import static io.github.kits.json.tokenizer.JsonTokenKind.TWO;
import static io.github.kits.json.tokenizer.JsonTokenKind.ZERO;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonTokenizer implements Serializable {

	private static final long serialVersionUID = -2019857734532143284L;
	private CharReader        charReader;
	private boolean           isValue = false;
	private final Object[]    kv = new Object[2];
	private Object            value = null;
	private boolean           isFirstObject = true, isFirstArray = true;

	private JsonTokenizer() {}

	public static JsonTokenizer newInstance() {
		return new JsonTokenizer();
	}

	public JsonKind tokenize(String json) throws IOException {
		Assert.isTrue(Json.isJson(json), new JsonParseException("Json String is invalid! Json: " + json));
		if (Strings.isNullString(json)) {
			return null;
		}

		json = readJsonWithoutComment(json, false);
		this.charReader = new CharReader(json);

		return jsonTokenize(Json.isJsonObject(json));
	}

	private JsonKind jsonTokenize(boolean isJsonObject) throws IOException {
		JsonPath jsonPath  = null;
		JsonList<Object> jsonList = null;
		if (isJsonObject) {
			jsonPath = JsonPath.newInstance();
		} else {
			jsonList = new JsonList<>();
		}

		while (this.charReader.hasMore()) {
			byte next = next();
			switch (next) {
				case LEFT_BIG_PARANTHESES:
				case LEFT_BRACKET: {
					String subJson;
					if (next == LEFT_BIG_PARANTHESES && isFirstObject && isJsonObject) {
						isFirstObject = false;
						continue;
					} else if (next == LEFT_BRACKET && isFirstArray && !isJsonObject) {
						isFirstArray = false;
						continue;
					}

					if (next == LEFT_BIG_PARANTHESES) {
						subJson = subObjectJson();
					} else {
						subJson = subArrayJson();
					}
					value = newInstance().tokenize(subJson);
					break;
				}
				case RIGHT_BIG_PARANTHESES:
				case RIGHT_BRACKET: {
					if (Envs.isNotNullEmptyBlack(kv[0]) && isJsonObject) {
						jsonPath.put(kv[0], kv[1]);
						value = kv[0] = kv[1] = null;
					}
					continue;
				}
				case SP: {
					continue;
				}
				case COLON: {
					isValue = true;
					kv[1] = null;
					continue;
				}
				case COMMA: {
					isValue = false;
					if (Envs.isNotNullEmptyBlack(kv[0]) && isJsonObject) {
						jsonPath.put(kv[0], kv[1]);
						value = kv[0] = kv[1] = null;
					}
					continue;
				}
				case DOUBLE_QUOTE: {
					value = readString();
					break;
				}
				case N: {
					value = readNull();
					break;
				}
				case T:
				case F: {
					value = readBoolean();
					break;
				}
				case NEGATIVE:
				case ZERO:
				case ONE:
				case TWO:
				case THREE:
				case FOUR:
				case FIVE:
				case SIX:
				case SEVEN:
				case EIGHT:
				case NINE: {
					value = readNumber();
					break;
				}
			}
			if (isJsonObject) {
				if (Objects.nonNull(value)) {
					if (isValue) {
						kv[1] = value;
					} else {
						kv[0] = value;
					}
				}
			} else {
				jsonList.add(value);
			}
		}

		if (isJsonObject) {
			return jsonPath;
		} else {
			return jsonList;
		}
	}

	private String subArrayJson() throws IOException {
		return subJson(LEFT_BRACKET);
	}

	private String subObjectJson() throws IOException {
		return subJson(LEFT_BIG_PARANTHESES);
	}

	private String subJson(byte parentheses) throws IOException {
		int leftParenthesesCount = 1, rightParenthesesCount = 0;
		boolean isString = false;
		StringBuilder json = new StringBuilder();
		byte rightParentheses = RIGHT_BIG_PARANTHESES;
		if (parentheses == LEFT_BRACKET) {
			rightParentheses = RIGHT_BRACKET;
		}
		while (this.charReader.hasMore()) {
			char next = this.charReader.next();
			if (next == DOUBLE_QUOTE && this.charReader.peek(2) != BACKSLASH) {
				isString = !isString;
			}
			if (next == BACKSLASH && (this.charReader.peek(2) == BACKSLASH || this.charReader.prep() == BACKSLASH)) {
				json.append(BACKSLASH);
				this.charReader.next();
			} else {
				json.append(next);
			}
			if (!isString) {
				if (next == parentheses) {
					leftParenthesesCount++;
				}
				if (next == rightParentheses) {
					if (leftParenthesesCount == ++rightParenthesesCount) {
						break;
					}
				}
			}
		}
		if (json.length() > 0) {
			json.insert(0, (char) parentheses);
		}
		return json.toString();
	}

	private byte next() throws IOException {
		if (this.charReader.hasMore()) {
			return (byte) (this.charReader.next() & 0xFF);
		}
		return -1;
	}

	/**
	 * 移除Json字符串中的注释和换行空格等空白字符
	 *
	 * @param json Json字符串
	 * @param isReservedSpace 是否在键值对中间保留一个空格
	 * @return 格式化后的字符串
	 */
	public String readJsonWithoutComment(String json, boolean isReservedSpace) throws IOException {
		json = Matcher.quoteReplacement(json);
		json = Strings.replaceMultiRegex(json, "", "\\s*(?<!:)//.*|/\\*(\\s|.)*?\\\\*/\\s*", "[\n\r\t]");
		CharReader reader = new CharReader(json);
		StringBuilder jsonBuilder = new StringBuilder();
		boolean isString = false;
		while (reader.hasMore()) {
			char next = reader.next();
			if (next == '\\' && reader.prep() == '\\') {
				jsonBuilder.append("\\");
				reader.next();
				continue;
			}
			if (next == '\"' && (reader.peek(2) == '\\')) {
				isString = true;
			} else if (next == '\"') {
				isString = !isString;
			}
			if (isString) {
				jsonBuilder.append(next);
			} else if (isReservedSpace && next == ':') {
				jsonBuilder.append(next).append(' ');
			} else if (!isWhiteSpace(next)) {
				jsonBuilder.append(next);
			}
		}
		return jsonBuilder.toString().trim();
	}

	/**
	 * Handling spaces or line breaks, etc.
	 *
	 * @param ch Char
	 * @return true-is space | false-not space
	 */
	private boolean isWhiteSpace(char ch) {
		return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
	}

	/**
	 * Read the character as a JsonToken of type String
	 *
	 * @throws IOException IOException
	 * @return JsonToken
	 */
	private String readString() throws IOException {
		StringBuilder sb = new StringBuilder();
		for (; ; ) {
			char ch = charReader.next();
			if (ch == '\\') {
				if (!isEscape()) {
					throw new JsonParseException("Invalid escape character");
				}
				sb.append('\\');
				ch = charReader.peek();
				sb.append(ch);
				if (ch == 'u') {
					for (int i = 0; i < 4; i++) {
						ch = charReader.next();
						if (isHex(ch)) {
							sb.append(ch);
						} else {
							throw new JsonParseException("Invalid character");
						}
					}
				}
			} else if (ch == '"') {
				if (charReader.peek(2) == '\\') {
					sb.append(ch);
					continue;
				}
				return sb.toString();
			} else if (ch == '\r' || ch == '\n') {
				throw new JsonParseException("Invalid character");
			} else {
				sb.append(ch);
			}
		}
	}

	/**
	 * Determine if it is a special character
	 *
	 * @throws IOException IOException
	 * @return true-it's special | false-not special
	 */
	private boolean isEscape() throws IOException {
		char ch = charReader.next();
		return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
					|| ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

	}

	/**
	 * Determine if it is a hex number
	 *
	 * @param ch Char
	 * @return true-is hex | false-not hex
	 */
	private boolean isHex(char ch) {
		return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
					|| ('A' <= ch && ch <= 'F'));
	}

	/**
	 * Read characters as a numeric type of JsonToken
	 *
	 * @throws IOException IOException
	 * @return JsonToken
	 */
	private Number readNumber() throws IOException {
		char          ch = charReader.peek();
		StringBuilder sb = new StringBuilder();
		if (ch == '-') {    // 处理负数
			sb.append(ch);
			ch = charReader.next();
			if (ch == '0') {    // 处理 -0.xxxx
				sb.append(ch);
				sb.append(readFracAndExp());
			} else if (isDigitOne2Nine(ch)) {
				addDigit(ch, sb);
			} else {
				throw new JsonParseException("Invalid minus number");
			}
		} else if (ch == '0') {    // 处理小数
			sb.append(ch);
			sb.append(readFracAndExp());
		} else {
			addDigit(ch, sb);
		}

		return formatNumber(sb.toString(), Short::parseShort, Integer::parseInt, Long::parseLong, Float::parseFloat, Double::parseDouble);
	}

	@SafeVarargs
	private final Number formatNumber(String resource, final Function<String, Number>... functions) {
		if (Strings.isBlack(resource) || functions.length == 0) {
			return 0;
		}
		for (Function<String, Number> function : functions) {
			try {
				return function.apply(resource);
			} catch (NumberFormatException e) {
				// Not operation
			}
		}
		return 0;
	}

	private void addDigit(char ch, StringBuilder sb) throws IOException {
		do {
			sb.append(ch);
			ch = charReader.next();
		} while (isDigitOne2Nine(ch));
		if (ch != (char) -1) {
			charReader.back();
			sb.append(readFracAndExp());
		}
	}

	/**
	 * Determine whether it is a scientific notation
	 *
	 * @param ch Char
	 * @return true-scientific | false-not scientific
	 */
	private boolean isExp(char ch) {
		return ch == 'e' || ch == 'E';
	}

	/**
	 * Determine if it is a number for 0-9
	 *
	 * @param ch Char
	 * @return true-number | false-not number
	 */
	private boolean isDigitOne2Nine(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * Reading floating number as String
	 *
	 * @throws IOException IOException
	 * @return String
	 */
	private String readFracAndExp() throws IOException {
		StringBuilder sb = new StringBuilder();
		char          ch = charReader.next();
		if (ch == '.') {
			sb.append(ch);
			ch = charReader.next();
			if (!isDigitOne2Nine(ch)) {
				throw new JsonParseException("Invalid frac");
			}
			do {
				sb.append(ch);
				ch = charReader.next();
			} while (isDigitOne2Nine(ch));

			if (isExp(ch)) {    // 处理科学计数法
				sb.append(ch);
				sb.append(readExp());
			} else {
				if (ch != (char) -1) {
					charReader.back();
				}
			}
		} else if (isExp(ch)) {
			sb.append(ch);
			sb.append(readExp());
		} else {
			charReader.back();
		}

		return sb.toString();
	}

	/**
	 * Reading scientific notation
	 *
	 * @throws IOException IOException
	 * @return String
	 */
	private String readExp() throws IOException {
		StringBuilder sb = new StringBuilder();
		char          ch = charReader.next();
		if (ch == '+' || ch == '-') {
			sb.append(ch);
			ch = charReader.next();
			if (isDigitOne2Nine(ch)) {
				do {
					sb.append(ch);
					ch = charReader.next();
				} while (isDigitOne2Nine(ch));

				if (ch != (char) -1) {    // 读取结束，不用回退
					charReader.back();
				}
			} else {
				throw new JsonParseException("e or E");
			}
		} else {
			throw new JsonParseException("e or E");
		}

		return sb.toString();
	}

	/**
	 * Reading boolean value as JsonToken
	 *
	 * @throws IOException IOException
	 * @return JsonToken
	 */
	private boolean readBoolean() throws IOException {
		if (charReader.peek() == 't') {
			if (!(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e')) {
				throw new JsonParseException("Invalid json string");
			}

			return true;
		} else {
			if (!(charReader.next() == 'a' && charReader.next() == 'l'
					  && charReader.next() == 's' && charReader.next() == 'e')) {
				throw new JsonParseException("Invalid json string");
			}

			return false;
		}
	}

	/**
	 * Reading null value as JsonToken
	 *
	 * @throws IOException IOException
	 * @return JsonToken
	 */
	private Object readNull() throws IOException {
		if (!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')) {
			throw new JsonParseException("Invalid json string");
		}

		return null;
	}

}
