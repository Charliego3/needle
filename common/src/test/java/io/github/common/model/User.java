package io.github.common.model;

import io.github.kits.json.annotations.JsonSerializeName;

//@NoneSerializeNull
public class User extends People {

	@JsonSerializeName(value = "userName")
//	@NoneSerializeNull
	private String name;
//	private int    age;
	private Double blance;
//	@NoneSerializeNull
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getBlance() {
		return blance;
	}

	public void setBlance(Double blance) {
		this.blance = blance;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public static User newInstance() {
		User user = new User();
		user.setName("WHIMTHEN");
		user.setBlance(33.44);
		user.setCode(86);
		user.setCountry("China");
		return user;
	}

//	public int getAge() {
//		return age;
//	}
//
//	public User setAge(int age) {
//		this.age = age;
//		return this;
//	}

}
