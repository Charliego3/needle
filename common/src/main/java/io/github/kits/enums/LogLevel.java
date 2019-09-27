package io.github.kits.enums;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum LogLevel {

	INFO("INFO",  " INFO"),
	WARN("WARN", " WARN"),
	DEBUG("DEBUG",  "DEBUG"),
	ERROR("ERROR",  "ERROR"),
	FATAL("FATAL", "FATAL")

	;

	LogLevel(String level, String content) {
		this.level = level;
		this.content = content;
	}

	private String level;
	private String content;

	public String getLevel() {
		return level;
	}

	public LogLevel setLevel(String level) {
		this.level = level;
		return this;
	}

	public String getContent() {
		return content;
	}

	public LogLevel setContent(String content) {
		this.content = content;
		return this;
	}

	public static String getInfoPrex() {
		return INFO.getContent();
	}

}
