package io.github.kits.json;

import io.github.kits.Strings;
import io.github.kits.exception.JsonParseException;

import java.util.List;

/**
 * JSON 解析器接口
 * 实现将任意类型的对象转换为Json字符串
 * 将Json字符串进行格式化
 * 将Json字符串转换为对象
 * 如果自己想实现Json解析器, 只需继承{@link JsonDecoder}和{@link JsonEncoder}, 再调用
 * 即可使用Json中的任意方法<br>
 *
 * The JSON parser interface
 * implements converting any type of object to a Json string
 * Formatting a Json string
 * Converting a Json string to an object
 * If you want to implement a Json parser,
 * just inherit {@link JsonDecoder} and {@link JsonEncoder}, call
 * ready to use Any method in Json
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface JsonSupport<T> {

	/**
	 * 将任意类型的对象转换为Json字符串。<br>
	 * 当且仅当object == null || object.equals("null")时则返回{@code null},<br>
	 * 通过调用{@link JsonSupport#toJson(Object, boolean)}转换,<br>
	 * 默认{@code isPretty = false}<br><br>
	 * <p>
	 * Convert any type of object to a Json string.<br>
	 * Returns {@code null} if and only if {@code object == null || object.equals("null") },<br>
	 * By calling {@link JsonSupport#toJson(Object, boolean)},<br>
	 * the default {@code isPretty = false}<br>
	 *
	 * <pre>
	 * {@code
	 * User user = new User();
	 * user.setName("whim then");
	 * user.setAge(1);
	 * String json = Json.toJson(user);
	 * Result: {"name": "whim then", "age": 1}
	 * }
	 * </pre>
	 *
	 * @param object 任意类型的对象
	 *               Any type of object
	 * @return json字符串
	 * Json String
	 */
	String toJson(Object object);

	/**
	 * 将任意类型的对象转换为Json字符串, 可选是否格式化<br>
	 * 当且仅当object == null || object.equals("null")时则返回{@code null}<br>
	 * 当且仅当{@code isPretty == true}时, 才会格式化<br>
	 * 如果调用{@link JsonSupport#toJson(Object)}默认{@code isPretty = false}<br><br>
	 * <p>
	 * Convert any type of object to a Json string, optionally formatted<br>
	 * Returns {@code null} if and only if {@code object == null || object.equals("null") }<br>
	 * Formatted if and only if {@code isPretty == true}<br>
	 * If you call {@link JsonSupport#toJson(Object)} default {@code isPretty = false}<br>
	 *
	 * <pre>
	 * {@code
	 * User user = new User();
	 * user.setName("whim then");
	 * user.setAge(1);
	 *
	 * String json = Json.toJson(user, true);
	 * Result:
	 * {
	 *     "name": "whim then",
	 *     "age": 1
	 * }
	 * }
	 * </pre>
	 *
	 * <pre>
	 * {@code
	 * String json = Json.toJson(user, false);
	 * Result: {"name": "whim then", "age": 1}
	 * }
	 * </pre>
	 *
	 * @param object   任意类型的对象
	 *                 Any type of object
	 * @param isPretty 是否格式化
	 *                 Whether to format
	 * @return Json字符串
	 * Json String
	 */
	String toJson(Object object, boolean isPretty);

	/**
	 * 格式化Json字符串, 当{@code json == null || isJson(json) == false}时<br>
	 * 抛出{@link IllegalArgumentException}异常 Json String is invalid<br>
	 * 否则将Json字符串格式化<br><br>
	 * <p>
	 * Format the Json string, when {@code json == null || isJson(json) == false}<br>
	 * Throws a {@link IllegalArgumentException} exception Json String is invalid<br>
	 * Otherwise formats the Json string<br>
	 *
	 * <pre>
	 * {@code
	 * String json = "{\"name\": \"whim then\", \"age\": 1}";
	 * String prettyJson = Json.prettyJson(json);
	 *
	 * Result:
	 * {
	 *     "name": "whim then",
	 *     "age": 1
	 * }
	 * }
	 * </pre>
	 *
	 * @param json Json字符串
	 *             Json String
	 * @return 格式化Json字符串
	 * Pretty Json String
	 */
	String prettyJson(String json);

	/**
	 * 将Json字符串转换为对象<br>
	 * 当json为空时返回null, 当target为空时抛出{@link NullPointerException}<br>
	 * json必须符合{@code Json.isJson(json)}否则抛出{@link io.github.kits.exception.JsonParseException}异常<br>
	 * 转换时利用反射构造器进行创建对象<br><br>
	 * <p>
	 * Converts a Json string to an object
	 * Returns null when json is empty, {@link NullPointerException} when target is empty<br>
	 * json must match {@code Json.isJson(json)} otherwise<br>
	 * throw {@link io.github.kits.exception.JsonParseException}Exception<br>
	 * Create object with reflection constructor when converting<br>
	 *
	 * <pre>
	 * {@code
	 * String json = "{\"name\": \"whim then\", \"age\": 1}";
	 * User user = Json.toObject(json, User.class);
	 * }
	 * </pre>
	 *
	 * @param json        json字符串
	 *                    Json String
	 * @param target 目标类型
	 *                    Target Class
	 * @return 范型对象
	 * Generic Object
	 */
	T toObject(String json, Class<T> target);

	/**
	 * 将Json字符串转换为集合<br>
	 * 当{@code json == null || json.equals("null")}时返回{@code Collections.emptyList()}<br>
	 * 当{@code Json.isJsonObject(json) == true}时构造只有一个元素的集合<br>
	 * 当{@code Json.isJsonArray(json) == true}时返回集合对象<br><br>
	 * <p>
	 * Convert a Json string to a collection<br>
	 * Returns {@code Collections.emptyList()} when {@code json == null || json.equals("null")}<br>
	 * when {@code Json.isJsonObject(json) = = true} when constructing a collection with only one element<br>
	 * returning a collection object when {@code Json.isJsonArray(json) == true}<br>
	 *
	 * <pre>
	 * {@code
	 * String json = "{\"name\": \"whim then\", \"age\": 1}";
	 * List<User> singleUser = Json.toList(json, User.class);
	 * singleUser.size() == 1
	 *
	 * String json = "[{\"name\": \"whim then\", \"age\": 1}, {\"name\": \"albert\", \"age\": 2}]";
	 * List<User> userList = Json.toList(json, User.class);
	 * userList.size() == 2
	 * }
	 * </pre>
	 *
	 * @param json        json字符串
	 *                    Json String
	 * @param target 	  目标类型
	 *                    Target Class
	 * @return 范型集合
	 * Generic List
	 */
	List<T> toList(String json, Class<T> target);

	/**
	 * 将Json字符串转换为任意类型的对象<br>
	 * 可选只解析部分内容<br>
	 * 当{@code json == null || "null".equals(json)}时返回null<br>
	 * 当{@code target == null}时抛出{@link NullPointerException}异常<br>
	 * 当{@code Json.isJson(json) == false}时<br>
	 * 抛出{@link io.github.kits.exception.JsonParseException}异常<br>
	 * 当{@code path == null || "".equals(path)}时解析全部内容<br>
	 * 相当于调用{@link JsonSupport#toObject(String, Class)}<br>
	 * 否则只解析指定的path部分<br><br>
	 * <p>
	 * Convert a Json string to an object of any type<br>
	 * Optional parsing only partial content<br>
	 * Returns null when {@code json == null || "null".equals(json)}<br>
	 * When {@code target == null} Throws {@link NullPointerException} exception<br>
	 * when {@code Json.isJson(json) == false}<br>
	 * throws {@link io.github.kits.exception.JsonParseException} exception<br>
	 * when {@code path == Null || "".equals(path)} parses all content<br>
	 * Equivalent to calling {@link JsonSupport#toObject(String, Class)}<br>
	 * otherwise parses only the specified path part<br>
	 *
	 * <pre>
	 * {@code
	 * String json = "{\"name\": \"whim then\", \"age\": 1}";
	 * User user = Json.jsonPath(json, "", User.class);
	 * Or
	 * User user = Json.jsonPath(json, null, User.class);
	 *
	 * user.getName().equals("whim then");
	 * user.getAge == 1;
	 *
	 * String name = Json.jsonPath(json, "name", String.class);
	 * name.equals("whim then");
	 * }
	 * </pre>
	 *
	 * @param json        json字符串
	 *                    Json String
	 * @param path        需要解析的键
	 *                    Key to parse
	 * @param target 	  目标类型
	 *                    Target Class
	 * @return 范型对象
	 * Generic Object
	 */
	<V> V jsonPath(String json, String path, Class<V> target);

	/**
	 * 将Json字符串转换为{@link JsonPath} 对象<br>
	 * 当且仅当{@code isJsonObject(json) == true}时<br>
	 * 否则抛出{@link JsonParseException}<br><br>
	 *
	 * Convert a Json string to a {@link JsonPath} object<br>
	 * if and only if {@code isJsonObject(json) == true}<br>
	 * Otherwise throw {@link JsonParseException}<br><br>
	 *
	 * <pre>
	 * {@code
	 * JsonPath jsonPath = Json.jsonPath("{\"name\": \"whim then\", \"age\": 1, \"operation\": {\"func\": \"get\"}}");
	 * System.out.println(jsonPath.get("name", String.class)); // whim then
	 * System.out.println(jsonPath.get("/age", int.class)); // 1
	 * System.out.println(jsonPath.get("/operation/func", String.class)); // get
	 * }
	 * </pre>
	 *
	 * @param json Json字符串
	 * @return JsonPath对象
	 */
	JsonPath jsonPath(String json);

    /**
     * 判断是否是对象类型的Json字符串<br>
     * 当{@code json == null || "null".equals(json)}时, 返回false<br>
     * 并且满足{@code ^\s*\{[\s\S]*\}\s*$}正则时, 返回true<br><br>
     *
     * Determine if it is a Json string of the object type<br>
     * When {@code json == null || "null".equals(json)}, return false<br>
     * and satisfy {@code ^\s*\{[\s\S]*\}\s*$} return true if regular<br>
     *
     * @param json Json字符串
     *             Json String
     * @return true: 是对象类型的Json字符串 | false: 反之
     * True: is the Json string of the object type | false: otherwise
     */
	default boolean isJsonObject(String json) {
		return Strings.isNotBlack(json) &&
				   Strings.regexFind(json, "^\\s*(/[/*][\\s\\S]*[\\r\\n]?(\\*/)?)?\\s*\\{[\\s\\S]*}\\s*$");
	}

    /**
     * 判断是否是数组或集合类型的Json字符串<br>
     * 当{@code json == null || "null".equals(json)}时, 返回false<br>
     * 并且满足{@code ^\s*\[[\s\S]*\]\s*$}正则时, 返回true<br><br>
     *
     * Determine if it is a Json string of array or collection type<br>
     * When {@code json == null || "null".equals(json)}, return false<br>
     * and satisfy {@code ^\s*\[[\s\S]*\]\s*$} return true if regular<br><br>
     *
     * @param json json字符串
     * @return true: 是集合类型的Json字符串 | false: 反之
     * True: is the Json string of the collection type | false: otherwise
     */
	default boolean isJsonArray(String json) {
		return Strings.isNotBlack(json) &&
				   Strings.regexFind(json, "^\\s*(/[/*][\\s\\S]*[\\r\\n]?(\\*/)?)?\\s*\\[[\\s\\S]*]\\s*$");
	}

    /**
     * 判断是否是Json字符串<br>
     * 当且仅当满足{@code isJsonObject(json) == true ||<br>
     * isJsonArray(json) == true}时返回true<br><br>
     *
     * Determine if it is a Json string<br>
     * Returns true if and only if<br>
     * {@code isJsonObject(json) == true || isJsonArray(json) == true} is satisfied<br><br>
     *
     * @param json json字符串
     *             Json String
     * @return true: 符合json格式 | false: 反之
     * True: conforms to json format | false: vice versa
     */
	default boolean isJson(String json) {
		return isJsonObject(json) || isJsonArray(json);
	}

}
