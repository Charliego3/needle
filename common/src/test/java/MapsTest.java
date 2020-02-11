import io.github.common.model.User;
import io.github.kits.Maps;
import io.github.kits.json.Json;
import io.github.kits.json.JsonPath;
import junit.framework.TestCase;

import java.util.Map;

public class MapsTest extends TestCase {

	public void testToMap() {
//		Map<Object, Object> objectMap = Maps.toMap(345345);

		User user = new User();
		user.setName("WHIMTHEN");
		user.setBlance(33.44);
		user.setCode(86);
		user.setCountry("China");
		user.setUser(user);

		Map<Object, Object> map = Maps.toMap(user);
		System.out.println(map);


		JsonPath jsonPath = Json.jsonPath("{\"name\": \"whim then\", \"age\": 1, \"operation\": {\"func\": \"get\"}}");
		Map<Object, Object> map1 = Maps.toMap(jsonPath);
		System.out.println(map1);
	}

}
