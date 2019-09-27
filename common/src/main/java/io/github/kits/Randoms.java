package io.github.kits;

import java.util.Random;

/**
 * 随机字符工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Randoms {

	/**
	 * 随机获取字符串(包含0、o、O、1、I、i)
	 *
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String getString(int length) {
		return getRandomStr(length, false, false, null);
	}

	/**
	 * 随机获取字符串(不包含0、o、O、1、I、i)
	 *
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String getStringRemove0oOIiLl(int length) {
		return getRandomStr(length, false, true, null);
	}

	/**
	 * 随机获取字符串
	 *
	 * @param length    长度
	 * @param exceptVal 需要排除的字符
	 * @return 随机字符串
	 */
	public static String getString(int length, String exceptVal) {
		return getRandomStr(length, false, false, exceptVal);
	}

	/**
	 * 获取纯数字的验证码等
	 *
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String getNumber(int length) {
		return getRandomStr(length, true, false, null);
	}

	/**
	 * 获取随机的字符串
	 *
	 * @param length          长度
	 * @param isNumber        是否为数字
	 * @param isExcept0oOIiLl 是否移除0oOIiLl
	 * @param exceptVal       需要移除的字符串
	 * @return 随机字符串
	 */
	private static String getRandomStr(int length, boolean isNumber, boolean isExcept0oOIiLl, String exceptVal) {
		String resource = Strings.getChars();
		if (isNumber) {
			resource = resource.substring(0, 10);
		}
		if (isExcept0oOIiLl) {
			resource = Strings.replace(resource, "", "[0oOIiLl]");
		}
		if (Strings.isNotNullOrEmpty(exceptVal)) {
			resource = Strings.replace(resource, "", '[' + exceptVal + ']');
		}
		StringBuilder randomStr = new StringBuilder();
		Random        random    = new Random(System.currentTimeMillis());
		for (int i = 0; i < length; i++) {
			randomStr.append(resource.charAt(random.nextInt(resource.length())));
		}
		return randomStr.toString();
	}

}
