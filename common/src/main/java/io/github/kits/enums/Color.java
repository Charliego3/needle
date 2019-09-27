package io.github.kits.enums;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Color {

	BASE_PRE("base", "\033["),
	BOLD("bold", "1;"),
	WHITE("white", "30m"),
	RED("red", "31m"),
	CANARY("canary", "32m"),
	YELLOW("yellow", "33m"),
	BLUE("blue", "34m"),
	PURPLE("purple", "35m"),
	GREEN("green", "36m"),
	GRAY("gray", "37m"),
	GRAY_MORE("grayMore", "90m"),
	PINK("pink", "91m"),
	CANARY_MORE("canaryMore", "92m"),
	YELLOW_MORE("yellowMore", "93m"),
	BLUE_LESS("blueLess", "94m"),
	PURPLE_MORE("purpleMore", "95m"),
	CYAN("cyan", "96m"),
	BLACK("black", "97m"),
	UNDER_LINE("underLine", "4m"),
	BACKGROUND("background", "7m"),
	END("end", "\033[0m"),

	;

	Color(String color, String content) {
		this.color = color;
		this.content = content;
	}

	private String color;
	private String content;

	public String getColor() {
		return color;
	}

	public Color setColor(String color) {
		this.color = color;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Color setContent(String content) {
		this.content = content;
		return this;
	}

}
