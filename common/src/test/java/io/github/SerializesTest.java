package io.github;

import io.github.common.model.User;
import io.github.kits.Serializes;
import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SerializesTest extends TestCase {

	public void testJsonSerializes() {
		System.out.println(new String(jsonSerializes(), StandardCharsets.UTF_8));
	}

	public void testJsonUnSerializes() {
		Object unSerialize = Serializes.jsonUnSerialize(jsonSerializes());
		if (Objects.nonNull(unSerialize)) {
			System.out.println(unSerialize.getClass().getName());
		} else {
			System.out.println("Serialize result is null!");
		}
	}

	private byte[] jsonSerializes() {
		User user = new User();
		user.setName("WHIMTHEN");
		user.setBlance(33.44);
		user.setCode(86);
		user.setCountry("China");
		return Serializes.jsonSerialize(user);
	}

}
