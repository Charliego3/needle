package io.github.kits.log;

import io.github.kits.enums.LogLevel;

import java.util.Arrays;
import java.util.Objects;

import static io.github.kits.log.LogBuilder.getLevel;
import static io.github.kits.log.LogBuilder.setLevels;
import static io.github.kits.log.LogThread.addBody;

/**
 * 日志工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Logger {

	static {
		LogThread.start();
	}

	public static void info(Object message) {
		addBody(LogBody.info().setMessage(message));
	}

	public static void infof(String message, Object... args) {
		addBody(LogBody.info().setMessage(message).setArgs(args));
	}

	public static void debug(Object message) {
		addBody(LogBody.debug().setMessage(message));
	}

	public static void debugf(String message, Object... args) {
		addBody(LogBody.debug().setMessage(message).setArgs(args));
	}

	public static void warn(Object message) {
		addBody(LogBody.warn().setMessage(message));
	}

	public static void warnf(String message, Object... args) {
		addBody(LogBody.warn().setMessage(message).setArgs(args));
	}

	public static void error(Object message) {
		addBody(LogBody.error().setMessage(message));
	}

	public static void error(Throwable exception) {
		addBody(LogBody.error().setException(exception));
	}

	public static void error(String message, Throwable exception) {
		addBody(LogBody.error().setMessage(message).setException(exception));
	}

	public static void errorf(String message, Object... args) {
		addBody(LogBody.error().setMessage(message).setArgs(args));
	}

	public static void errorf(String message, Throwable exception, Object... args) {
		addBody(LogBody.error().setMessage(message).setException(exception).setArgs(args));
	}

	public static boolean isDebugEnabled() {
		return getLevel().contains(LogLevel.DEBUG.getLevel());
	}

	public static void setLevel(LogLevel... level) {
		if (Objects.nonNull(level) && level.length > 0) {
			StringBuilder levels = new StringBuilder();
			for (LogLevel logLevelEnum : level) {
				if (Arrays.asList(LogLevel.values()).contains(logLevelEnum)) {
					levels.append(logLevelEnum.getLevel()).append(",");
				}
			}
			if (levels.length() > 0) {
				levels.deleteCharAt(levels.length() - 1);
				setLevels(levels.toString());
			}
		}
	}

}
