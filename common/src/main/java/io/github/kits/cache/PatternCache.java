package io.github.kits.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 正则缓存
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class PatternCache {

	private static final ConcurrentHashMap<String, Pattern> PATTERN_MAP = new ConcurrentHashMap<>();

	public static void put(Pattern pattern) {
		PATTERN_MAP.put(pattern.pattern(), pattern);
	}

	public static Pattern get(String regex) {
		Pattern pattern = PATTERN_MAP.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex);
			put(pattern);
		}
		return pattern;
	}

}
