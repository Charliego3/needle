import io.github.common.model.User;
import io.github.kits.Maps;
import io.github.kits.json.Json;
import io.github.kits.json.JsonPath;
import junit.framework.TestCase;

import java.util.HashMap;
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

	public void testRemoveNullNodeMap() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("10", 1);
		map.put("9", 2);
		map.put("dbdfgdfg", 1);
		map.put("1", 3);
		map.put(null, 1);
		map.put("xcvx", null);
		map.put(null, 3);
		System.out.println(map);

		Maps.removeNullNode(map);
		Maps.removeNullKeyNode(map);
		Maps.removeNullValueNode(map);
		System.out.println(map);

		Map<String, Integer> sortByKey = Maps.sortByKey(map);
		System.out.println(sortByKey);

		Map<String, Integer> sortByValue = Maps.sortByValue(map);
		System.out.println(sortByValue);
	}

}
