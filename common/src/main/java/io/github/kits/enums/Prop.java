package io.github.kits.enums;

import java.util.Arrays;

/**
 * 枚举类型
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Prop {

	/**
	 * 默认配置文件
	 */
	DEFAULT_LOGGER_PROPERTIES("logger", "prop", 0),
	DEFAULT_CONFIG_PROPERTIES("application", "prop", 0),

	;

	private String prop;
	private String type;
	private int    value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	Prop(String prop, String type, int value) {
		this.prop = prop;
		this.type = type;
		this.value = value;
	}

}
