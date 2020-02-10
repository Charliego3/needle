package io.github.common.model;

public class People {

	private String country;
	private Integer code;

	public People() {}

	public People(String country) {
		this.country = country;
	}

	public People(String country, Integer code) {
		this.country = country;
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
