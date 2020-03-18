package io.github.kits;

import io.github.kits.cache.PatternCache;
import io.github.kits.json.tokenizer.CharReader;
import io.github.kits.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * 字符串操作工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Strings {

	public static final String NULL = "null";

	/**
	 * 常用到的字符
	 * 0-9
	 * A-Z
	 * a-z
	 */
	public static final String[] CHARS = new String[]{
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
	};

	/**
	 * 获取所有字符的字符串
	 *
	 * @return 0-9, A-Z, a-z
	 */
	public static String getChars() {
		return String.join("", CHARS);
	}

	/**
	 * 判断是否为空
	 *
	 * @param resource 需要判断的字符串
	 * @return true-null or empty | false-not null Or empty
	 */
	public static boolean isNullOrEmpty(String... resource) {
		return isNullEmptyBlack(false, resource);
	}

	/**
	 * 判断字符串是否为空 或两端为空白字符
	 *
	 * @param isBlack  是否去除两端空白字符
	 * @param resource 源字符串
	 * @return 结果
	 */
	private static boolean isNullEmptyBlack(boolean isBlack, String... resource) {
		if (Objects.isNull(resource) || resource.length == 0) {
			return true;
		}
		int i = 0;
		do {
			String single = resource[i];
			if (Objects.isNull(single) || (isBlack ? single.trim().isEmpty() : single.isEmpty())) {
				return true;
			}
			i++;
		} while (i < resource.length);
		return false;
	}

	/**
	 * 判断字符串去除两端空白字符后是否为空
	 *
	 * @param resources 源字符串
	 * @return 结果
	 */
	public static boolean isBlack(String... resources) {
		return isNullEmptyBlack(true, resources);
	}

	/**
	 * 判断字符串去除两端空白字符后是否不为空
	 *
	 * @param resources 源字符串
	 * @return 结果
	 */
	public static boolean isNotBlack(String... resources) {
		return !isBlack(resources);
	}

	public static boolean isNullString(String resource) {
		return isNotBlack(resource) && NULL.equalsIgnoreCase(resource.trim());
	}

	public static boolean isNotNullString(String resource) {
		return isNotBlack(resource) && !NULL.equalsIgnoreCase(resource.trim());
	}

	/**
	 * 判断是否不为空
	 *
	 * @param value 需要判断的字符串
	 * @return true-not null or empty | false-null or empty
	 */
	public static boolean isNotNullOrEmpty(String... value) {
		return !isNullOrEmpty(value);
	}

	public static String checkNullOrEmpty(String resource, String defaultValue) {
		return isNullOrEmpty(resource) ? defaultValue : resource;
	}

	public static String checkBlack(String resource, String defaultValue) {
		return isBlack(resource) ? defaultValue : resource;
	}

	/**
	 * 将多个正则匹配到的内容替换为相同的内容
	 *
	 * @param resource    资源内容
	 * @param replacement 替换的目标内容
	 * @param regex       正则
	 * @return 替换后的字符串
	 */
	public static String replaceMultiRegex(String resource, String replacement, String... regex) {
		if (isNullOrEmpty(resource) || regex.length == 0) {
			return resource;
		}
		for (String reg : regex) {
			resource = replace(resource, checkNullOrEmpty(replacement, ""), reg);
		}
		return resource;
	}

	/**
	 * 将正则匹配到的内容替换为相同的内容
	 *
	 * @param resource    资源内容
	 * @param replacement 替换的目标内容
	 * @param regex       正则
	 * @return 替换后的字符串
	 */
	public static String replace(String resource, String replacement, String regex) {
		if (isNullOrEmpty(resource) || isNullOrEmpty(regex)) {
			return resource;
		}
		Matcher matcher = PatternCache.get(regex).matcher(resource);
		if (matcher.find()) {
			return matcher.replaceAll(checkNullOrEmpty(replacement, ""));
		}
		return resource;
	}

	/**
	 * 将多个正则匹配到的内容替换为相同的内容
	 *
	 * @param resource    资源内容
	 * @param replacement 替换的目标内容
	 * @param regex       正则
	 * @return 替换后的字符串
	 */
	public static String replaceFirst(String resource, String replacement, String regex) {
		if (isNullOrEmpty(resource) || isNullOrEmpty(regex)) {
			return resource;
		}
		Matcher matcher = PatternCache.get(regex).matcher(resource);
		if (matcher.find()) {
			return matcher.replaceFirst(checkNullOrEmpty(replacement, ""));
		}
		return resource;
	}

	/**
	 * 判断正则是否至少有一个匹配
	 *
	 * @param resource 资源字符串
	 * @param regex    多正则
	 * @return Boolean
	 */
	public static String replaceByMulti(String resource, String[] regex, String[] replacements) {
		Assert.isNotNullEmptyBlack(regex, "Regex can not be null or empty!");
		Assert.equal(regex.length, replacements.length, "Regex length is not equals Replacement length!");
		for (int i = 0; i < regex.length; i++) {
			if (Objects.nonNull(replacements[i])) {
				resource = replace(resource, replacements[i], regex[i]);
			}
		}
		return resource;
	}

	/**
	 * 缩进字符串
	 *
	 * @param source      待缩进的字符串
	 * @param indentCount 缩进数(空格的数目)
	 * @return 缩进后的字符串
	 */
	public static String indent(String source, int indentCount) {
		if (indentCount > 0 && isNotNullOrEmpty(source)) {
			String indent = repeat(" ", indentCount);
			source = indent + source;
			source = replace(source, "\n" + indent, "\n");
		}
		return source;
	}

	/**
	 * 转换字符串长度, 用空格填充
	 * Convert string length, padded with spaces
	 *
	 * @param source      待转换字符串, String to be converted
	 * @param indentCount 总长度, Total length
	 * @return 转换后的字符串, Converted string
	 */
	public static String indentRight(String source, int indentCount) {
		if (indentCount > 0 && isNotNullOrEmpty(source)) {
			if (source.toCharArray().length < indentCount) {
				String indent = repeat(" ", indentCount - source.toCharArray().length);
				source += indent;
			}
		}
		return source;
	}

	/**
	 * @param num   num
	 * @param radix radix
	 * @return String
	 */
	public static String radixConvert(long num, int radix) {
		if (radix < 2 || radix > 62) {
			return null;
		}
		num = num < 0 ? num * -1 : num;
		StringBuilder result = new StringBuilder();
		long tmpValue = num;
		while (true) {
			long value = (int) (tmpValue % radix);
			result.insert(0, CHARS[(int) value]);
			value = tmpValue / radix;
			if (value >= radix) {
				tmpValue = value;
			} else {
				result.insert(0, CHARS[(int) value]);
				break;
			}
		}
		return result.toString();
	}

	/**
	 * 复制字符串
	 *
	 * @param resource 需要重复的字符串资源
	 * @param count    重复之后共次数
	 * @return 重复之后的字符串
	 */
	public static String repeat(String resource, int count) {
		Assert.isNotNull(resource, "resource is null!");
		int len = resource.length();
		if (len == 0 || count == 0) {
			return "";
		}
		if (count == 1) {
			return resource;
		}
		if (Integer.MAX_VALUE / count < len) {
			throw new OutOfMemoryError("Repeating " + len + " bytes String " + count +
										   " times will produce a String exceeding maximum size.");
		}
		StringBuilder repeatString = new StringBuilder(resource);
		for (int i = 1; i < count; i++) {
			repeatString.append(resource);
		}
		return repeatString.toString();
	}

	/**
	 * 获取字符在字符串中出现的次数
	 *
	 * @param resource 源
	 * @param val      需要计数的字符
	 * @return 次数
	 */
	public static int getCharCount(String resource, char val) {
		int count = 0;
		if (isNotNullOrEmpty(resource)) {
			for (char ch : resource.toCharArray()) {
				if (ch == val) {
					count++;
				}
			}
		}
		return count;
	}

	public static List<String> toCharList(String val) {
		List<String> resp = new ArrayList<>();
		if (isNotNullOrEmpty(val)) {
			for (Character v : val.toCharArray()) {
				resp.add(v.toString());
			}
		}
		return resp;
	}

	/**
	 * 判断正则是否匹配
	 *
	 * @param resource 资源字符串
	 * @param regex    正则
	 * @return Boolean
	 */
	public static boolean regexMatch(String resource, String regex) {
		return PatternCache.get(regex)
						   .matcher(resource)
						   .matches();
	}

	/**
	 * 判断正则是否匹配
	 *
	 * @param resource 资源字符串
	 * @param regex    正则
	 * @return Boolean
	 */
	public static boolean regexFind(String resource, String regex) {
		return PatternCache.get(regex)
						   .matcher(resource)
						   .find();
	}

	/**
	 * 判断正则是否至少有一个匹配
	 *
	 * @param resource 资源字符串
	 * @param regex    多正则
	 * @return Boolean
	 */
	public static boolean findByMultiRegex(String resource, String... regex) {
		Optional<String> any = Stream.of(regex).parallel()
									 .filter(r -> regexFind(resource, r))
									 .findAny();
		return any.isPresent();
	}

	public static int searchByRegex(String source, String regex) {
		if (source == null) {
			return 0;
		}
		int count = 0;
		Matcher matcher = PatternCache.get(regex).matcher(source);
		if (matcher.find()) {
			do {
				count++;
			} while (matcher.find());
		}
		return count;
	}

	/**
	 * 判断字符串是否为数字(十进制)
	 *
	 * @param val 需要判断的字符串
	 * @return true-是数字类型 | false-不是数字类型
	 */
	public static boolean isNumber(String val) {
		return isNumber(val, false);
	}

	/**
	 * 判断字符串是否为数字类型
	 * 整数型、浮点型
	 *
	 * @param val 需要判断的值
	 * @return true-是数字类型 | false-不是数字类型
	 */
	public static boolean isNumber(String val, boolean isFloating) {
		if (isBlack(val)) {
			return false;
		}
		if (!isFloating && val.contains(".")) {
			return false;
		}
		char[] chars = val.toCharArray();
		if (chars[0] == '.' || chars[chars.length - 1] == '.'
				|| chars[0] == 'e' || chars[0] == 'E') {
			throw new IllegalArgumentException("The value first or last char can't be [., e, E]");
		}
		for (char ch : chars) {
			if (ch != '.' && ch != 'e' && ch != 'E' && !Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断源字符串是否为Boolean类型
	 *
	 * @param val 需要判断的字符串
	 * @return true-是Boolean类型 | false-不是Boolean类型
	 */
	public static boolean isBoolean(String val) {
		return isNotNullOrEmpty(val)
				   && ("TRUE".equalsIgnoreCase(val.trim())
						   || "FALSE".equalsIgnoreCase(val.trim()));
	}

	public static boolean parseBoolean(String val) {
		if (isBlack(val))
			return false;
		val = val.trim();
		return "TRUE".equalsIgnoreCase(val) || "1".equalsIgnoreCase(val);
	}

	/**
	 * 首字母大写
	 *
	 * @param val 需要设置的值
	 * @return 设置后的值
	 */
	public static String toUpperPrefix(String val) {
		if (isNullOrEmpty(val)) {
			return null;
		}
		char ch = val.charAt(0);
		if (ch >= 'a' && ch <= 'z') {
			return ((char) (ch - 32)) + val.substring(1);
		} else {
			return val;
		}
	}

	/**
	 * CameCase is converted to underline form
	 *
	 * @param val CameCase String
	 * @return underline String
	 */
	public static String unCameCase(String val) {
		try {
			if (isNotNullOrEmpty(val)) {
				CharReader charReader = new CharReader(val);
				StringBuilder result = new StringBuilder();
				while (charReader.hasMore()) {
					char ch = charReader.next();
					if (ch >= 'A' && ch <= 'Z') {
						result.append('_')
							  .append((char) (ch + 32));
					} else if (ch != '_') {
						result.append(ch);
					}
				}
				return result.toString();
			}
		} catch (IOException e) {
			Logger.error("String unCameCase error", e);
		}
		return "";
	}

	/**
	 * Underline is convertd to CameCase form
	 *
	 * @param val Underline String
	 * @return CameCase String
	 */
	public static String cameCase(String val) {
		try {
			if (isNotNullOrEmpty(val)) {
				CharReader charReader = new CharReader(val);
				StringBuilder result = new StringBuilder(String.valueOf(charReader.next()));
				while (charReader.hasMore()) {
					char ch = charReader.next();
					if (ch == '_') {
						char next = charReader.next();
						if (next != '_') {
							result.append((char) (next - 32));
						}
					} else {
						result.append(ch);
					}
				}
				return result.toString();
			}
		} catch (IOException e) {
			Logger.error("String cameCase error", e);
		}
		return "";
	}

	public static String concat(Object... objs) {
		if (Objects.isNull(objs) || objs.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			sb.append(obj);
		}
		return sb.toString();
	}

}
