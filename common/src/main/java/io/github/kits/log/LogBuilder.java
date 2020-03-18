package io.github.kits.log;

import io.github.kits.DateTimes;
import io.github.kits.Envs;
import io.github.kits.Files;
import io.github.kits.Props;
import io.github.kits.Strings;
import io.github.kits.cache.PatternCache;
import io.github.kits.enums.Color;
import io.github.kits.enums.LogLevel;
import io.github.kits.enums.Prop;
import io.github.kits.json.Json;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

/**
 * 日志组装类
 * Log assembly tool
 *
 * <p>
 * template: {B?F[0-9]{1,2}} 如果存在B则为粗体, 按照后两位数字定义颜色值
 * {FE} 颜色结束标识
 * {L} 日志级别
 * {D} 时间日期, 格式为: yyyy-MM-dd HH:mm:ss:SSS
 * {T} 当前线程信息
 * {SC} 缩写的类名及行数
 * {C} 完整的类名及行数
 * {I} 日志主体内容
 * </p>
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class LogBuilder {

	private static final String        template;
	private static      String        level;
	private static final boolean       isTemplateColorful;
	private static final String        LEVEL_COLOR_REGEX         = "(\\{B?F\\d{1,2}})?([\\s|\\S])?\\{L}([\\s|\\S])?\\{FE}?";
	private static final String        COLOR_REGEX               = "(\\{B?F(\\d{1,2})})";
	private static final String        CLASS_REGEX               = "\\{(C|SC)([R|L]?)(\\d*)}";
	private static final String        CLASS_LINE_REGEX          = "\\{CL([R|L]?)(\\d*)}";
	private static final String        THREAD_REGEX              = "\\{T([R|L]?)(\\d*)}";
	private static final String        THREAD_ID_REGEX           = "\\{TI([R|L]?)(\\d*)}";
	private static final String        LEVEL                     = "\\{L}";
	private static final String        DATE_TIME                 = "\\{D}";
	private static final String        COLOR_END                 = "\\{FE}";
	private static final String        INFORMATION               = "\\{I}";
	private static final String        SHORT_CLASS               = "SC";
	private static final String        LEFT                      = "L";
	private static final String        BOLD                      = "B";
	private static final AtomicInteger CLASS_INDENT_LENGTH       = new AtomicInteger(0);
	private static final AtomicBoolean IS_SHORT_CLASS            = new AtomicBoolean(false);
	private static final AtomicBoolean CLASS_IS_LEFT_INDENT      = new AtomicBoolean(true);
	private static final AtomicBoolean CLASS_LINE_IS_LEFT_INDENT = new AtomicBoolean(true);
	private static final AtomicInteger CLASS_LINE_INDENT_LENGTH  = new AtomicInteger(0);
	private static final AtomicBoolean THREAD_IS_LEFT_INDENT     = new AtomicBoolean(true);
	private static final AtomicInteger THREAD_INDENT_LENGTH      = new AtomicInteger(0);
	private static final AtomicBoolean THREAD_ID_IS_LEFT_INDENT  = new AtomicBoolean(true);
	private static final AtomicInteger THREAD_ID_INDENT_LENGTH   = new AtomicInteger(0);
	private static final AtomicBoolean IS_SET_LEVEL              = new AtomicBoolean(false);

	static {
		template = Props.getString(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), "template").orElse("{BF30}[{L}]{FE} [{F4}{D}{FE}] {F32}[{TL15}:{TIL2}]{FE} {F36}[{SCL33}:{CLL3}]{FE}{F91}:{FE} {I}");
		isTemplateColorful = Strings.regexFind(template, COLOR_REGEX);
		Matcher classMatcher = PatternCache.get(CLASS_REGEX).matcher(template);
		if (classMatcher.find()) {
			String shortClass   = classMatcher.group(1);
			String rightLeft    = classMatcher.group(2);
			String indentLength = classMatcher.group(3);
			if (Strings.isNotNullOrEmpty(shortClass))
				IS_SHORT_CLASS.set(SHORT_CLASS.equals(shortClass));
			if (Strings.isNotNullOrEmpty(rightLeft))
				CLASS_IS_LEFT_INDENT.set(LEFT.equals(rightLeft));
			if (Strings.isNumber(indentLength))
				CLASS_INDENT_LENGTH.set(Integer.parseInt(indentLength));
		}
		setIndentAndDirection(CLASS_LINE_REGEX, CLASS_LINE_INDENT_LENGTH, CLASS_LINE_IS_LEFT_INDENT);
		setIndentAndDirection(THREAD_REGEX, THREAD_INDENT_LENGTH, THREAD_IS_LEFT_INDENT);
		setIndentAndDirection(THREAD_ID_REGEX, THREAD_ID_INDENT_LENGTH, THREAD_ID_IS_LEFT_INDENT);
	}

	public static void addBody(LogBody body) {
		// 配置的日志级别才输出, level 放在这里可以实现不重启修改日志级别
		if (!IS_SET_LEVEL.get())
			level = Props.getString(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), "level").orElse("INFO,WARN,ERROR");
		// The configured log level is output.
		if (level.contains(body.getLogLevel().getLevel())) {
			LogThread.addBody(body);
		}
	}

	/**
	 * 设置缩进长度和方向
	 * Set the indentation length and direction
	 *
	 * @param regex       正则
	 *                    Regex
	 * @param indent      缩进
	 *                    Indent
	 * @param isRightLeft 方向
	 *                    Direction
	 */
	private static void setIndentAndDirection(String regex, AtomicInteger indent, AtomicBoolean isRightLeft) {
		Matcher classLineMatcher = PatternCache.get(regex).matcher(template);
		if (classLineMatcher.find()) {
			String rightLeft    = classLineMatcher.group(1);
			String indentLength = classLineMatcher.group(2);
			if (Strings.isNotNullOrEmpty(rightLeft))
				isRightLeft.set(LEFT.equals(rightLeft));
			if (Strings.isNumber(indentLength))
				indent.set(Integer.parseInt(indentLength));

		}
	}

	/**
	 * 获取日志级别
	 * Get logger level
	 *
	 * @return 级别
	 * Level
	 */
	static String getLevel() {
		return level;
	}

	/**
	 * 设置日级别
	 * Set logger level
	 *
	 * @param logLevel 日志级别字符串, 用逗号分隔
	 *                 Log level string, separated by commas
	 */
	static void setLevels(String logLevel) {
		if (Strings.isNotNullOrEmpty(logLevel)) {
			level = logLevel;
			IS_SET_LEVEL.set(true);
		}
	}

	/**
	 * 日志构建
	 * Log construction
	 *
	 * @param logBody 日志主体
	 *                Log body
	 * @return 日志完整内容
	 * Log full content
	 */
	static String buildMsg(LogBody logBody) {
		LogLevel      logLevel = logBody.getLogLevel();
		Object        message  = logBody.getMessage();
		Object[]      args     = logBody.getArgs();
		StringBuilder msg      = new StringBuilder();
		// 彩色日志处理
		// Color log processing
		String colorToken = replaceColorToken(logLevel);
		if (message instanceof String)
			msg.append(message);
		else
			msg.append(Json.toJson(message));
		if (Strings.regexFind(msg.toString(), "(\\{\\})")) {
			if (Objects.nonNull(args) && args.length > 0) {
				for (Object arg : args) {
					String m = msg.toString();
					msg.delete(0, msg.toString().length());
					String json = Json.toJson(arg);
					// 对特殊字符加转译符号, 如$等做替换操作会抛异常: java.lang.IllegalArgumentException: Illegal group reference
					String replacement = Strings.isNullOrEmpty(json) ? Strings.NULL : Matcher.quoteReplacement(json);
					msg.append(Strings.replaceFirst(m, replacement, "\\{\\}"));
				}
			} else {
				String m = msg.toString();
				msg.delete(0, msg.toString().length());
				msg.append(Strings.replace(m, Strings.NULL, "\\{\\}"));
			}
		}
		if (logLevel == LogLevel.ERROR && Objects.nonNull(logBody.getException())) {
			if (Objects.isNull(message))
				msg.delete(0, msg.toString().length());
			if (Strings.isNotNullOrEmpty(msg.toString()))
				msg.append(" => ");
			msg.append(appendStackTrace(logBody.getException()));
		}
		return replaceMsgToken(colorToken, logLevel, msg.toString(), logBody);
	}

	/**
	 * 处理日志消息
	 * Processing log messages
	 *
	 * @param logLevel 日志级别
	 *                 Log level
	 * @param message  内容
	 *                 content
	 */
	private static String replaceMsgToken(String token, LogLevel logLevel, String message, LogBody logBody) {
		String classInfo  = logBody.getClassInfo();
		String classLine  = logBody.getClassLine();
		String threadInfo = logBody.getThreadName();
		String threadId   = logBody.getThreadId();
		token = Strings.replace(token, logLevel.getContent(), LEVEL);
		token = Strings.replace(token, DateTimes.now(DateTimes.YYYY_MM_DD_MM_HH_MM_SS_SSS), DATE_TIME);
		token = Strings.replace(token, Matcher.quoteReplacement(message), INFORMATION);
		token = getToken(token, classInfo, classLine, CLASS_INDENT_LENGTH, CLASS_IS_LEFT_INDENT,
						 CLASS_REGEX, CLASS_LINE_INDENT_LENGTH, CLASS_LINE_IS_LEFT_INDENT, CLASS_LINE_REGEX);
		token = getToken(token, threadInfo, threadId, THREAD_INDENT_LENGTH, THREAD_IS_LEFT_INDENT,
						 THREAD_REGEX, THREAD_ID_INDENT_LENGTH, THREAD_ID_IS_LEFT_INDENT, THREAD_ID_REGEX);
		return token + "\r\n";
	}

	/**
	 * regex替换token
	 * Regex replace token
	 *
	 * @param token            结果字符串
	 *                         Result string
	 * @param info             信息
	 *                         information
	 * @param id               id
	 * @param infoIndentLength 缩进长度
	 *                         Info Indent length
	 * @param isLeftIndent     缩进方向
	 *                         Indent Direction
	 * @param regex            正则
	 *                         Regex
	 * @param idIndentLength   ID缩进长度
	 *                         ID Indent length
	 * @param idIsLeftIndent   ID缩进方向
	 *                         Id Indent Direction
	 * @param idRegex          ID正则
	 *                         Id Regex
	 * @return 替换结果
	 * Replacement result
	 */
	private static String getToken(String token, String info, String id, AtomicInteger infoIndentLength,
								   AtomicBoolean isLeftIndent, String regex, AtomicInteger idIndentLength,
								   AtomicBoolean idIsLeftIndent, String idRegex) {
		if (infoIndentLength.get() > 0) {
			if (isLeftIndent.get())
				info = Strings.indent(info, infoIndentLength.get() - info.length());
			else
				info = Strings.indentRight(info, infoIndentLength.get());
		}
		token = Strings.replace(token, info, regex);
		if (idIndentLength.get() > 0) {
			if (idIsLeftIndent.get())
				id = Strings.indent(id, idIndentLength.get() - id.length());
			else
				id = Strings.indentRight(id, idIndentLength.get());
		}
		return Strings.replace(token, id, idRegex);
	}

	/**
	 * 获取日志中的class信息
	 * Get the class information in the log
	 * eg: <em>[io.github.kits.PropertiesKit.java: 698]</em>
	 *
	 * @return class信息
	 * Class info
	 */
	static String classInfo() {
		String        classInfo = Envs.getStaceTraceE(5).getClassName();
		StringBuilder className = new StringBuilder();
		if (IS_SHORT_CLASS.get() && (CLASS_INDENT_LENGTH.get() > 0 && classInfo.length() + 5 > CLASS_INDENT_LENGTH.get())) {
			String[] split = classInfo.split("\\.");
			for (int i = 0; i < split.length - 1; i++) {
				className.append(split[i].charAt(0)).append(".");
			}
			className.append(split[split.length - 1]);
		} else {
			className.append(classInfo);
		}
		className.append(".java");
		if (className.length() > CLASS_INDENT_LENGTH.get() && CLASS_INDENT_LENGTH.get() > 0)
			className.delete(0, className.length() - CLASS_INDENT_LENGTH.get());
		if (className.length() > 0 && className.charAt(0) == '.')
			className.setCharAt(0, ' ');
		return className.toString();
	}

	/**
	 * 获取日志中class调用行数
	 * Get the number of class call lines in the log
	 *
	 * @return 行数
	 * Line number
	 */
	static String classLine() {
		int lineNumber = Envs.getStaceTraceE(5).getLineNumber();
		return String.valueOf(lineNumber);
	}

	/**
	 * 获取当前线程的名称
	 * Get the name of the current thread
	 *
	 * @return 线程名称
	 * Thread name
	 */
	static String currentThreadName() {
		Thread currentThread = Thread.currentThread();
		String threadName    = currentThread.getName();
		if (threadName.length() > THREAD_INDENT_LENGTH.get()) {
			threadName = threadName.substring(threadName.length() - THREAD_INDENT_LENGTH.get());
		}
		if (threadName.startsWith(".")) {
			threadName = threadName.substring(1);
		}
		return threadName;
	}

	/**
	 * 获取当前线程的id
	 * Get the id of the current thread
	 *
	 * @return 当前线程id
	 * current thread id
	 */
	static String currentThreadId() {
		return String.valueOf(Thread.currentThread().getId());
	}

	/**
	 * 处理彩色日志输出
	 * Handling color log output
	 */
	private static String replaceColorToken(LogLevel logLevel) {
		String  tempTemplate = template;
		Matcher group;
		if (isTemplateColorful && Envs.isNotWin()) {
			// 如果是error级别, 则全部为红色显示
			// If it is error level, all are displayed in red
			if (logLevel == LogLevel.ERROR) {
				tempTemplate = Strings.replaceMultiRegex(tempTemplate, "", COLOR_END, COLOR_REGEX);
//				tempTemplate = Strings.replace(tempTemplate, "", COLOR_REGEX);
				tempTemplate = Color.BASE_PRE.getContent() + Color.BOLD.getContent() +
								   Color.RED.getContent() + tempTemplate + Color.END.getContent();
				return tempTemplate;
			}
			if (logLevel == LogLevel.WARN && Strings.regexFind(tempTemplate, LEVEL)) {
				tempTemplate = replaceTemplateLevel(Color.YELLOW, tempTemplate);
			} else if (logLevel == LogLevel.DEBUG && Strings.regexFind(tempTemplate, LEVEL)) {
				tempTemplate = replaceTemplateLevel(Color.BLUE, tempTemplate);
			}
			group = PatternCache.get(COLOR_REGEX).matcher(tempTemplate);
			while (group.find()) {
				String r           = group.group(1);
				String c           = group.group(2);
				String replacement = Color.BASE_PRE.getContent();
				if (r.contains(BOLD))
					replacement += Color.BOLD.getContent();
				replacement += c + "m";
				r = Strings.replace(r, "\\\\{", "\\{");
				r = Strings.replace(r, "\\\\}", "\\}");
				tempTemplate = Strings.replace(tempTemplate, replacement, r);
			}
			tempTemplate = Strings.replace(tempTemplate, Color.END.getContent(), COLOR_END);
		} else {
			group = PatternCache.get(COLOR_REGEX).matcher(tempTemplate);
			tempTemplate = group.replaceAll("");
			tempTemplate = Strings.replaceMultiRegex(tempTemplate, "", "\033\\[(1;)?\\d{1,2}m", COLOR_END);
		}
		return tempTemplate;
	}

	/**
	 * 处理日志级别的颜色
	 * Handling log level colors
	 *
	 * @param color        颜色值
	 *                     Color value
	 * @param tempTemplate 模板
	 *                     Template
	 * @return 替换级别后的模板
	 * Replace the template after the level
	 */
	private static String replaceTemplateLevel(Color color, String tempTemplate) {
		Matcher matcher = PatternCache.get(LEVEL_COLOR_REGEX).matcher(tempTemplate);
		String  debug   = "";
		if (matcher.find()) {
			String startColor = matcher.group(1);
			String startSpace = matcher.group(2);
			String endSpace   = matcher.group(3);
			if (Strings.isNotNullOrEmpty(startColor)) {
				debug += Color.BASE_PRE.getContent();
				if (startColor.contains(BOLD))
					debug += Color.BOLD.getContent();
				debug += color.getContent();
			}
			if (Strings.isNotNullOrEmpty(startSpace))
				debug += startSpace;
			debug += LEVEL;
			if (Strings.isNotNullOrEmpty(endSpace))
				debug += endSpace;
			if (Strings.isNotNullOrEmpty(startColor))
				debug += COLOR_END;
		}
		return Strings.replace(tempTemplate, debug, LEVEL_COLOR_REGEX);
	}

	/**
	 * 向日志中写入异常信息
	 * Write exception information to the log
	 *
	 * @param exception 异常
	 *                  exception
	 */
	private static String appendStackTrace(Throwable exception) {
		StringBuilder msg = new StringBuilder();
		while (Objects.nonNull(exception)) {
			msg.append(exception.getClass().getCanonicalName())
			   .append(": ")
			   .append(exception.getMessage())
			   .append(Files.getLineSeparator())
			   .append(Strings.indent(Envs.getStackElementsMessage(exception.getStackTrace()), 6));
			exception = exception.getCause();
		}
		return msg.toString();
	}

}
