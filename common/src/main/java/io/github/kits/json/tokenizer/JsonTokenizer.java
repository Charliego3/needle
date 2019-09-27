package io.github.kits.json.tokenizer;

import io.github.kits.Assert;
import io.github.kits.Envs;
import io.github.kits.Strings;
import io.github.kits.exception.JsonParseException;
import io.github.kits.json.Json;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;

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
	private boolean           isAllValue;
	private String[]          paths;
	private boolean           isValue = false;
	private Object[]          kv = new Object[2];
	private Object            value = null;
	private boolean           isFirstObject = true, isFirstArray = true;

	private JsonTokenizer() {}

	public static JsonTokenizer newInstance() {
		return new JsonTokenizer();
	}

	public Object tokenize(String json) throws IOException {
		return tokenize(json, "/");
	}

	public Object tokenize(String json, String path) throws IOException {
		Assert.isTrue(Json.isJson(json), new JsonParseException("Json String is invalid! Json: " + json));
		if (Strings.isNullString(json)) {
			return null;
		}

		if (Strings.isBlack(path) || Arrays.asList(".", "/").contains(path)) {
			isAllValue = true;
		} else {
			Assert.isTrue(Json.isJsonObject(json), new JsonParseException("Json Array does not support path acquisition! Json: " + json));
			if (path.startsWith(".") || path.startsWith("/")) {
				path = path.substring(1);
			}
			paths = path.split("[./]");
		}

		json = readJsonWithoutComment(json, false);
		this.charReader = new CharReader(json);

		Object tokenize = jsonTokenize(Json.isJsonObject(json));
		if (!isAllValue) {
			if (tokenize instanceof List) {
				return null;
			}
			return getPathValue(tokenize);
		}
		return tokenize;
	}

	private Object getPathValue(Object token) {
		for (int i = 0; i < paths.length; i++) {
			if (Objects.nonNull(token)) {
				if (token instanceof Map) {
					token = ((Map) token).get(paths[i]);
				}
			} else {
				return null;
			}
			if (i == paths.length - 1) {
				return token;
			}
		}
		return null;
	}

	private Object jsonTokenize(boolean isJsonObject) throws IOException {
		Map<Object, Object> map = null;
		List<Object> list = null;
		if (isJsonObject) {
			map = new HashMap<>();
		} else {
			list = new ArrayList<>();
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
						map.put(kv[0], kv[1]);
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
						map.put(kv[0], kv[1]);
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
				list.add(value);
			}
		}

		if (isJsonObject) {
			return map;
		} else {
			return list;
		}
	}

	private String subArrayJson() throws IOException {
		return subJson(LEFT_BRACKET);
	}

	private String subObjectJson() throws IOException {
		return subJson(LEFT_BIG_PARANTHESES);
	}

	private String subJson(byte parantheses) throws IOException {
		int leftParanthesesCount = 1, rightParanthesesCount = 0;
		boolean isString = false;
		StringBuilder json = new StringBuilder();
		byte rightParantheses = RIGHT_BIG_PARANTHESES;
		if (parantheses == LEFT_BRACKET) {
			rightParantheses = RIGHT_BRACKET;
		}
		while (this.charReader.hasMore()) {
			char next = this.charReader.next();
			if (next == DOUBLE_QUOTE && this.charReader.peek(2) != '\\') {
				isString = !isString;
			}
			if (next == '\\' && (this.charReader.peek(2) == '\\' || this.charReader.prep() == '\\')) {
				json.append("\\");
				this.charReader.next();
			} else {
				json.append(next);
			}
			if (!isString) {
				if (next == parantheses) {
					leftParanthesesCount++;
				}
				if (next == rightParantheses) {
					if (leftParanthesesCount == ++rightParanthesesCount) {
						break;
					}
				}
			}
		}
		if (json.length() > 0) {
			json.insert(0, (char) parantheses);
		}
		return json.toString();
	}

	private byte next() throws IOException {
		if (this.charReader.hasMore()) {
			return (byte) (this.charReader.next() & 0xFF);
		}
		return -1;
	}

	public static void main(String[] args) throws IOException {
//		Map<String, String> map = new HashMap<String, String>() {{
//			put("key1", "value1");
//			put("key2", "value2");
//			put("key3", "value3");
//			put("key4", "value4");
//			put("key5", "value5");
//		}};
//
//		String json = Json.toJson(map, true);
		String json = "		// sdfkhksjfkj\r" +
						  "/** json\n \t\t * sfdsfdfds \n\t\t */\n  //sdfsdfsdfdsf\n" +
						  "{\n\t/** \n\t * hahhahahha \n\t */\n" +
						  "\t\"key1\": \"value1\",\n" +
						  "\t\"key2\": /** \n" +
						  "\t * zhushi \n" +
						  "\t */\"value2\" , \n" +
						  "\t\"key5\": \"val,   ,  ue5\",\n" +
						  "// comment\n" +
						  "\t\"key3\": \"value3\",\n" +
						  "\t\"key4\": \"value4\"\n" +
						  "}";

		json = "{\n \"bbb\": [\"a , \\\" : [ ] \", \"b   :{ ]  \", \"c  : { }   \"], \"datas\": {\n  \"merchantInfo\": {\n   \"authFailReason\": null,\n   \"autoCancelQuota\": 1000000.00000,\n   \"canTrade\": true,\n   \"closeFailReason\": \"\",\n   \"closeStatus\": 0,\n   \"closeStatusName\": \"初始    {   }   状态\",\n   \"currentCancelTimes\": 0,\n   \"currentTimestamp\": 0,\n   \"hasAdvancedAuthen\": true,\n   \"hasAuthen\": true,\n   \"hasEmailAuthen\": true,\n   \"hasMobileAuthen\": true,\n   \"hasSetContact\": false,\n   \"hasSetPay\": false,\n   \"hasVideoAuthen\": false,\n   \"headUrl\": \"http://zb-testw.   [  ]   oss-cn-     :   shenzh     en.ali    yuncs.com/user/head-pic/4e2d9852-bec5-41b6-90c1-24d199a738e0.png?OSSAccessKeyId=LTAIuKoIx0jeiW4p&Expires=1569367582&Signature=XGtVTh1NTixkBmU%2F27Qjr4sdi1M%3D\",\n   \"id\": 1028073,\n   \"last24hAnswerRate\": 1.00000,\n   \"last24hAnswerRatio\": \"100%\",\n   \"last24hAnswerTimes\": 0,\n   \"last24hRefuseTimes\": 0,\n   \"last24hTimeoutTimes\": 0,\n   \"last30dAppealTimes\": 0,\n   \"last30dFailTradeTimes\": 0,\n   \"last30dTradeRatio\": \"0%\",\n   \"last30dTradeTimes\": 0,\n   \"last30dWinOverTimes\": 0,\n   \"mobileNo\": \"+86 15200000000\",\n   \"nickName\": \"nickname\",\n   \"openRemarkCode\": true,\n   \"orderReceive\": false,\n   \"priceProtectType\": 0,\n   \"realName\": \"whimthen\",\n   \"registerTime\": 1560853303369,\n   \"status\": 0,\n   \"statusName\": \"初始状态\",\n   \"stopAcceptOrder\": false,\n   \"totalAppealTimes\": 0,\n   \"totalCancelTimes\": 0,\n   \"totalTradeTimes\": 0,\n   \"totalWinOverTimes\": 0,\n   \"type\": 1,\n   \"typeName\": \"普通用户\",\n   \"userId\": 359810,\n   \"userName\": \"whimthen@gmail.com\",\n   \"videoHint\": false\n  , \"number\": 234234324     }\n },\n \"resMsg\": {\n  \"code\": 1000,\n  \"method\": \"getMerchantInfo\",\n  \"message\": \"操作成功。\"\n }\n}";

//		json = "{\"datas\":{\"totalAsset\":\"8729000000000.00000000\",\"balances\":[{\"total\":\"1000000000000.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"15\",\"available\":\"1000000000000.00000000\",\"key\":\"QC\",\"unitDecimal\":\"8\"},{\"total\":\"1000000000000.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"13\",\"available\":\"1000000000000.00000000\",\"key\":\"USDT\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"51\",\"available\":\"0.00000000\",\"key\":\"ZB\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"2\",\"available\":\"0.00000000\",\"key\":\"BTC\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"5\",\"available\":\"0.00000000\",\"key\":\"ETH\",\"unitDecimal\":\"8\"}]},\"resMsg\":{\"code\":1000,\"method\":\"getFundAsset\",\"message\":\"操作成功。\"}}";

//		json = "[{\"total\":\"1000000000000.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"15\",\"available\":\"1000000000000.00000000\",\"key\":\"QC\",\"unitDecimal\":\"8\"},{\"total\":\"1000000000000.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"13\",\"available\":\"1000000000000.00000000\",\"key\":\"USDT\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"51\",\"available\":\"0.00000000\",\"key\":\"ZB\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"2\",\"available\":\"0.00000000\",\"key\":\"BTC\",\"unitDecimal\":\"8\"},{\"total\":\"0.00000000\",\"freeze\":\"0.00000000\",\"fundsType\":\"5\",\"available\":\"0.00000000\",\"key\":\"ETH\",\"unitDecimal\":\"8\"}]";

//		json = "  [\"total\", \"free  ] ze\", \"fundsType\"  ]   ";

//		System.out.println(json);
		System.out.println("PrettyJson: \n" + Json.prettyJson(json));
		JsonTokenizer jsonTokenizer = new JsonTokenizer();
		System.out.println("\nJsonTokenizer.readJsonWithoutComment: " + jsonTokenizer.readJsonWithoutComment(json, false));
		Object tokenize = jsonTokenizer.tokenize(json, "/datas/merchantInfo/headUrl");
		System.out.println("\nJsonTokenizer.tokenize: " + tokenize);
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
