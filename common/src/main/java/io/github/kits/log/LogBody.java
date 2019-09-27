package io.github.kits.log;

import io.github.kits.enums.LogLevel;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class LogBody {

	private LogLevel  logLevel;
	private Object    message;
	private Throwable exception;
	private Object[]  args;
	private String    classInfo;
	private String    threadName;
	private String    classLine;
	private String    threadId;

	public String getThreadId() {
		return threadId;
	}

	public LogBody setThreadId(String threadId) {
		this.threadId = threadId;
		return this;
	}

	public String getClassLine() {
		return classLine;
	}

	public LogBody setClassLine(String classLine) {
		this.classLine = classLine;
		return this;
	}

	public String getClassInfo() {
		return classInfo;
	}

	public LogBody setClassInfo(String classInfo) {
		this.classInfo = classInfo;
		return this;
	}

	public String getThreadName() {
		return threadName;
	}

	public LogBody setThreadName(String threadName) {
		this.threadName = threadName;
		return this;
	}

	public static LogBody newInstance() {
		return new LogBody();
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public LogBody setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	public Object getMessage() {
		return message;
	}

	public LogBody setMessage(Object message) {
		this.message = message;
		return this;
	}

	public Throwable getException() {
		return exception;
	}

	public LogBody setException(Throwable exception) {
		this.exception = exception;
		return this;
	}

	public Object[] getArgs() {
		return args;
	}

	public LogBody setArgs(Object... args) {
		this.args = args;
		return this;
	}

	public static LogBody info() {
		LogBody logBody = newInstance().setLogLevel(LogLevel.INFO);
		complexMsg(logBody);
		return logBody;
	}

	public static LogBody warn() {
		LogBody logBody = newInstance().setLogLevel(LogLevel.WARN);
		complexMsg(logBody);
		return logBody;
	}

	public static LogBody debug() {
		LogBody logBody = newInstance().setLogLevel(LogLevel.DEBUG);
		complexMsg(logBody);
		return logBody;
	}

	public static LogBody error() {
		LogBody logBody = newInstance().setLogLevel(LogLevel.ERROR);
		complexMsg(logBody);
		return logBody;
	}

	public static LogBody fatal() {
		LogBody logBody = newInstance().setLogLevel(LogLevel.FATAL);
		complexMsg(logBody);
		return logBody;
	}

	private static void complexMsg(LogBody logBody) {
		logBody.setClassInfo(LogBuilder.classInfo())
			   .setClassLine(LogBuilder.classLine())
			   .setThreadId(LogBuilder.currentThreadId())
			   .setThreadName(LogBuilder.currentThreadName());
	}

}
