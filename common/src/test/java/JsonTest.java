import io.github.common.model.People;
import io.github.common.model.User;
import io.github.kits.json.Json;
import io.github.kits.json.JsonPath;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonTest extends TestCase {

	public void testJsonPath() {
//		JsonPath jsonPath = JsonPath.newInstance("{\"user\": \"whimthen\", \"age\": 34}");
		JsonPath jsonPath = Json.jsonPath("{\"name\": \"whim then\", \"age\": 1, \"operation\": {\"func\": \"get\"}}");
		System.out.println(jsonPath.get("/operation/func", String.class));
	}

	public void testJson() {
		System.out.println(Json.toJson(false));
		System.out.println(Json.toJson("false"));
		System.out.println(Json.toJson(User.class));


		User user = new User();
		user.setName("whim then");
		user.setBlance(123456789012345.1234567890);
		user.setCountry("China");
		System.out.println(Json.toJson(user));
	}

	public void testToJson() {
		User user = new User();
		user.setName("whim then");
//		user.setAge(23);
		user.setBlance(123456789012345.1234567890);
		user.setCountry("China");

		User user1 = new User();
		user1.setName("albert");
//		user1.setAge(34);
		user1.setBlance(234234.234);
		user1.setCode(86);

		user.setUser(user1);

		String json = Json.toJson(user);
		System.out.println(json);

		System.out.println("=========================");

		String arrayJson = Json.toJson(new ArrayList<User>(){{add(user);add(user1);}}, false);
		System.out.println(arrayJson);

		System.out.println("=========================");

		System.out.println(Json.toJson(new User(), true));
	}

	public void testBasicArray() {
		System.out.println(Character.isDigit('2'));

		Object arr = new int[]{1, 2, 3, 4, 5, 6, 7};
		System.out.println(arr.getClass());

//		for(int i = 0; i < Array.getLength(arr); ++i) {
//			Object arrayItem = Array.get(arr, i);
//			System.out.println("Index: " + i + ", Value: " + arrayItem);
//		}


//		Class arrayClass = arr.getClass().getComponentType();
//		Object targetArray = Array.newInstance(arrayClass, Array.getLength(arr));
//		System.out.println(targetArray);

		System.out.println(Json.toJson(arr, true));

		String json = Json.toJson(new double[]{1.1, 1.2, 1.3, 1.4, 1.5});
		System.out.println(json);

		System.out.println(Json.prettyJson(json));

		Object iarr = new Integer[]{2, 3, 4, 5, 6, 7};
		System.out.println(iarr.getClass());
		System.out.println(Json.toJson(iarr));

	}

	public void testMapJson() {
		Map<String, String> map = new HashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};

		System.out.println(Json.toJson(map, false));

		Map<String, Map> mainMap = new HashMap<String, Map>() {{
			put("map1", map);
			put("map2", map);
		}};


		User user = new User();
		user.setName("whim then");
//		user.setAge(23);
		user.setBlance(123456789012345.1234567890);

		User user1 = new User();
		user1.setName("albert");
//		user1.setAge(34);
		user1.setBlance(234234.234);

		user.setUser(user1);

		Map<String, User> userMap = new HashMap<String, User>() {{
			put("map", user);
			put("mainMap", user1);
		}};

		Map<String, Map> mapMap = new HashMap<String, Map>() {{
			put("map", map);
			put("mainMap", mainMap);
		}};


		System.out.println("mapMap: \n" + Json.toJson(mapMap, true));

		System.out.println("userMap: \n" + Json.toJson(userMap, true));

		String                 json        = Json.toJson(mainMap, false);

		System.out.println(json);

		System.out.println(Json.isJsonObject(json));

		System.out.println(Json.toJson(2421421));
	}

	public void testArrayJson() {
		List<User> userList = new ArrayList<>();
		User       user     = new User();
		user.setName("whim then");
//		user.setAge(23);
		user.setBlance(123456789012345.1234567890);

		User user1 = new User();
//		user1.setName("albert");
//		user1.setAge(34);
		user1.setBlance(234234.234);

		user.setUser(user1);

		userList.add(user);
		userList.add(user1);

		System.out.println(Json.toJson(userList));

		List<List> lists = new ArrayList<>();
		lists.add(userList);

		List<User> uList = new ArrayList<>();
		uList.add(user1);

		lists.add(uList);

		Map<String, String> map = new HashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		Map<String, Object> mainMap = new HashMap<String, Object>() {{
			put("map1", map);
			put("map2", userList);
			put("map3", 2421312);
			put("map4", true);
			put("map8", "true");
			put("map5", "2342342343");
			put("map6", 'A');
//			put("map9", null);
			put("map7", 23432444.234);
		}};
		lists.add(Arrays.asList(mainMap, map));

		String                  json        = Json.toJson(lists, true);

		System.out.println(json);

//		io.github.common.model.User user2 = Json.toObject("", io.github.common.model.User.class);
//		List<io.github.common.model.User> userList1 = Json.toList("", io.github.common.model.User.class);
//		io.github.common.model.User user3 = Json.jsonPath("", "", io.github.common.model.User.class);
	}

	public void testReflect() throws Exception {
		Class aClass = People.class;
//		Constructor constructor = aClass.getDeclaredConstructor(new Class[0]);
//		System.out.println(constructor);
//		Object instance = constructor.newInstance();
//		Object instance = aClass.newInstance();
		Byte b = 'b';
		System.out.println(Json.toJson(b));
//		Constructor constructor = Stream.of(aClass.getDeclaredConstructors()).min(Comparator.comparingInt(c -> c.getParameterTypes().length)).orElse(null);
//		System.out.println(constructor);
//		if (Objects.nonNull(constructor)) {
//			Object instance = constructor.newInstance();
//			Reflective.invokeSetMethod(instance, aClass.getDeclaredField("country"), "China");
//			System.out.println(Json.toJson(instance));
//		}
	}

	public void testToObject() throws IllegalAccessException, InstantiationException {
		User user = new User();

//		List list = List.class.newInstance();
//		System.out.println(list);

//		Constructor<?>[] constructors = user.getClass().getDeclaredConstructors();
//		System.out.println(Arrays.toString(constructors));

		user.setName("whim then");
//		user.setAge(23);
		user.setBlance(123456789012345.1234567890);
		user.setCountry("China");

		User user1 = new User();
		user1.setName("albert");
//		user1.setAge(34);
		user1.setBlance(234234.234);
		user1.setCode(86);

		user.setUser(user1);

		String json = Json.toJson(user);
		System.out.println(json);

		List user2 = Json.toObject(json, List.class);
		System.out.println(user2);

	}

}

