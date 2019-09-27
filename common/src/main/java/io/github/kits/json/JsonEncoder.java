package io.github.kits.json;

import io.github.kits.Assert;
import io.github.kits.DateTimes;
import io.github.kits.Envs;
import io.github.kits.Lists;
import io.github.kits.Reflective;
import io.github.kits.Strings;
import io.github.kits.Types;
import io.github.kits.configuration.TypeFunctionConfig;
import io.github.kits.constants.Consts;
import io.github.kits.exception.JsonNotSupportedException;
import io.github.kits.exception.JsonParseException;
import io.github.kits.json.annotations.IgnoreJsonSerialize;
import io.github.kits.json.annotations.JsonCamelCase;
import io.github.kits.json.annotations.JsonSerializeName;
import io.github.kits.json.annotations.NoneSerializeNull;
import io.github.kits.json.tokenizer.CharReader;
import io.github.kits.json.tokenizer.JsonTokenType;
import io.github.kits.json.tokenizer.JsonTokenizer;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.kits.json.tokenizer.JsonTokenType.BEGIN_ARRAY;
import static io.github.kits.json.tokenizer.JsonTokenType.BEGIN_OBJECT;
import static io.github.kits.json.tokenizer.JsonTokenType.BLANK;
import static io.github.kits.json.tokenizer.JsonTokenType.END_ARRAY;
import static io.github.kits.json.tokenizer.JsonTokenType.END_OBJECT;
import static io.github.kits.json.tokenizer.JsonTokenType.NULL;
import static io.github.kits.json.tokenizer.JsonTokenType.QUOTATION;
import static io.github.kits.json.tokenizer.JsonTokenType.SEP_COLON;
import static io.github.kits.json.tokenizer.JsonTokenType.SEP_COMMA;

/**
 * Json解析器: 将Object对象转换为Json字符串
 * Json parser: Convert an Object object to a Json string
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonEncoder<T> implements JsonSupport<T> {

	private ThreadLocal<Boolean>       isPretty = ThreadLocal.withInitial(() -> false);
	private ThreadLocal<AtomicInteger> indent   = ThreadLocal.withInitial(() -> new AtomicInteger(0));

	@Override
	public String toJson(Object object) {
		return toJson(object, false);
	}

	@Override
	public String toJson(Object object, boolean isPretty) {
		if (Objects.isNull(object) || "null".equals(object))
			return null;
		this.isPretty.set(isPretty);
		String result = null;
		TypeFunctionConfig.Builder<Object, String> builder = TypeFunctionConfig.builder();
		builder.string(Objects::toString)
			   .array(this::arrayToJson)
			   .collection(this::arrayToJson)
			   .orElse(this::objectToJson);
//		if (isString(object, false))
//			result = object.toString();
//		else if (object instanceof Collection || object.getClass().isArray()) {
//			result = arrayToJson(object);
//		} else {
//			result = objectToJson(object);
//		}
		result = Types.function(object, builder.build());
		this.isPretty.set(false);
		this.indent.get().set(0);
		return result;
	}

	@Override
	public final String prettyJson(String json) {
		Assert.isTrue(isJson(json), new IllegalArgumentException("Json String is invalid"));
		StringBuilder prettyJson = new StringBuilder();
		try {
			boolean isString = false;
			json = JsonTokenizer.newInstance().readJsonWithoutComment(json, true);
			CharReader reader = new CharReader(json);
			while (reader.hasMore()) {
				char next = reader.next();
				boolean isAppend = false;
				if (next == '"' && reader.peek(2) != '\\') {
					isString = !isString;
				}
				if (isString) {
					prettyJson.append(next);
				} else {
					switch (next) {
						case '{':
						case '[':
							prettyJson.append(next);
							isAppend = true;
							prettyJson.append(Consts.CRLF);
							prettyJson.append(Strings.repeat(Consts.TAB, indent.get().incrementAndGet()));
							break;
						case ',':
							prettyJson.append(next);
							isAppend = true;
							prettyJson.append(Consts.CRLF);
							prettyJson.append(Strings.repeat(Consts.TAB, indent.get().get()));
							while (true) {
								if (reader.next() != ' ') {
									reader.back();
									break;
								}
							}
							break;
						case '}':
						case ']':
							prettyJson.append(Consts.CRLF);
							prettyJson.append(Strings.repeat(Consts.TAB, indent.get().decrementAndGet()));
							break;
					}
					if (!isAppend) {
						prettyJson.append(next);
					}
				}
			}
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
		indent.get().set(0);
		return prettyJson.toString();
	}

	/**
	 * 将数组或集合转换为JSON字符串
	 * Convert an array or collection to a JSON string
	 *
	 * @param object 数组或集合对象
	 *               Array or List
	 * @return JSON字符串
	 * JSON String
	 */
	private String arrayToJson(Object object) {
		if (Objects.isNull(object))
			return null;
		Collection<Object> collection = null;
		if (object instanceof Collection) {
			collection = (Collection<Object>) object;
		} else if (object.getClass().isArray()) {
			collection = new ArrayList<>();
			for (int i = 0; i < Array.getLength(object); i++) {
				collection.add(Array.get(object, i));
			}
		}
		StringBuilder json = new StringBuilder(BEGIN_ARRAY.getValue());
		if (isPretty.get()) {
			json.append(Consts.CRLF);
			indent.get().getAndIncrement();
		}
		if (Lists.isNotNullOrEmpty(collection)) {
			collection.forEach(c -> {
				indentTab(json);

				if (c instanceof Collection || c.getClass().isArray()) {
					String array = arrayToJson(c);
					json.append(array);
				} else {
					String objectJson = objectToJson(c);
					boolean isAddQuotation = Strings.isNotNullOrEmpty(objectJson)
												 && !isBool(objectJson)
												 && !isJsonObject(objectJson)
												 && !Strings.isNumber(objectJson, true);
					if (isAddQuotation)
						json.append(QUOTATION.getValue());
					json.append(objectJson);
					if (isAddQuotation)
						json.append(QUOTATION.getValue());
				}

				addCRLF(json);
			});
		}

		deleteUnnecessary(json, END_ARRAY);
		return json.toString();
	}

	/**
	 * 对象转Json字符串
	 * Object to Json string
	 *
	 * @param object 任意类型的对象
	 *               Any type of object
	 * @return JSON字符串
	 * JSON String
	 */
	private String objectToJson(Object object) {
		if (Objects.isNull(object))
			return null;
//		if (object instanceof Map) {
//			Map<Object, Object> map     = (Map) object;
//			StringBuilder       mapJson = new StringBuilder(BEGIN_OBJECT.getValue());
//			if (isPretty.get()) {
//				mapJson.append(Consts.CRLF);
//				indent.get().getAndIncrement();
//			}
//			map.forEach((k, v) -> {
//				addTab(mapJson);
//				objectJson(mapJson, k);
//				mapJson.append(SEP_COLON.getValue()).append(BLANK.getValue());
//				objectJson(mapJson, v);
//
//				addCrlfOrBlank(mapJson);
//			});
//			deleteUnnecessary(mapJson, END_OBJECT);
//			return mapJson.toString();
//		} else if (object instanceof Date) {
//			return DateTimes.format((Date) object, DateTimes.YYYY_MM_DD_MM_HH_MM_SS);
//		} else if (object instanceof BigDecimal) {
//			if (BigDecimal.ZERO.compareTo((BigDecimal) object) == 0) {
//				object = BigDecimal.ZERO;
//			}
//			return ((BigDecimal) object).toPlainString();
//		} else if (object instanceof Collection || object.getClass().isArray()) {
//			return arrayToJson(object);
//		} else if (object instanceof Class) {
//			return object.toString();
//		} else if (isBool(object)) {
//			return object.toString();
//		} else if (object instanceof Float || object instanceof Double) {
//			return new BigDecimal(object.toString()).toPlainString();
//		} else if (Envs.isBasicType(object.getClass()) || Envs.isSystemType(object.getClass())) {
//			return object.toString();
//		} else if (object instanceof Enum) {
//			return ((Enum) object).name();
//		}
//		return customObjectToJson(object);

		TypeFunctionConfig.Builder<Object, String> build = TypeFunctionConfig.builder();
		build.collection(this::arrayToJson)
			 .array(this::arrayToJson)
			 .map(map -> {
				 StringBuilder mapJson = new StringBuilder(BEGIN_OBJECT.getValue());
				 if (isPretty.get()) {
					 mapJson.append(Consts.CRLF);
					 indent.get().getAndIncrement();
				 }
				 map.forEach((k, v) -> {
					 indentTab(mapJson);
					 objectJson(mapJson, k);
					 mapJson.append(SEP_COLON.getValue());
					 if (isPretty.get()) {
					 	mapJson.append(BLANK.getValue());
					 }
					 objectJson(mapJson, v);

					 addCRLF(mapJson);
				 });
				 deleteUnnecessary(mapJson, END_OBJECT);
				 return mapJson.toString();
			 })
			 .bigDecimal(bigDecimal -> {
				 if (BigDecimal.ZERO.compareTo(bigDecimal) == 0) {
					 bigDecimal = BigDecimal.ZERO;
				 }
				 return bigDecimal.toPlainString();
			 })
			 .number(number -> new BigDecimal(number.toString()).toPlainString())
			 .enums(Enum::name)
			 .object(this::customObjectToJson)
			 .date(date -> DateTimes.format(date, DateTimes.YYYY_MM_DD_MM_HH_MM_SS))
			 .orElse(Objects::toString);

		return Types.function(object, build.build());
	}

	/**
	 * 删除结尾处多余的字符
	 * Remove extra characters at the end
	 *
	 * @param json 结果字符串(StringBuilder)
	 *             Result string(StringBuilder)
	 * @param end  添加结尾字符到Json字符串中 } | ]
	 *             Add end character to Json string
	 */
	private void deleteUnnecessary(StringBuilder json, JsonTokenType end) {
		if (json.length() > 1)
			if (isPretty.get() && json.length() > 2)
				json.delete(json.length() - 3, json.length());
			else
				json.delete(json.length() - 1, json.length());
		if (isPretty.get()) {
			json.append(Consts.CRLF);
			json.append(Strings.repeat(Consts.TAB, indent.get().decrementAndGet()));
		}
		json.append(end.getValue());
	}

	/**
	 * 自定义(非Java自带类型)类型的对象转JSON字符串
	 * Custom (non-Java comes with type) type of object to JSON string
	 *
	 * @param object 自定义对象
	 *               Custom object
	 * @return JSON字符串
	 * JSON String
	 */
	private String customObjectToJson(Object object) {
		StringBuilder json                = new StringBuilder(BEGIN_OBJECT.getValue());
		AtomicBoolean isCameCase          = new AtomicBoolean(false);
		AtomicBoolean isNoneSerializeNull = new AtomicBoolean(false);
		Class<?>      aClass              = object.getClass();

		// 如果类上标注了@NoneSerializeNull则属性值为null时忽略
		// Ignore if the attribute value is null if @NoneSerializeNull is marked on the class
		NoneSerializeNull noneSerializeNull = aClass.getAnnotation(NoneSerializeNull.class);
		if (Objects.nonNull(noneSerializeNull))
			isNoneSerializeNull.set(true);
		// 如果类上标注了@JsonCamelCase则所有的属性都以驼峰命名的形式转换为JSON字符串
		// If @JsonCamelCase is marked on the class, all properties are converted to JSON strings in the form of camel names.
		JsonCamelCase jsonCamelCase = aClass.getAnnotation(JsonCamelCase.class);
		if (Objects.nonNull(jsonCamelCase))
			isCameCase.set(true);

		if (isPretty.get()) {
			json.append(Consts.CRLF);
			indent.get().incrementAndGet();
		}

		Reflective.getFields(aClass).stream()
				  .filter(field -> {
					  // 过滤掉不转换为JSON的属性
					  // Filter out attributes that are not converted to JSON
					  IgnoreJsonSerialize ignore = field.getAnnotation(IgnoreJsonSerialize.class);
					  return Objects.isNull(ignore);
				  })
				  .forEach(field -> {
					  String fieldName = field.getName();

					  // 如果类上没有标注@NoneSerializeNull, 则以字段上的此注解为准, 字段上也未标注时不忽略属性值为null的字段
					  // If @NoneSerializeNull is not marked on the class, the annotation on the field shall prevail,
					  // and the field with the attribute value null will not be ignored when the field is not marked.
					  // 如果类上已经标注了@NoneSerializeNull注解时, 则不管字段是否标注此注解, 都会忽略属性值为null的字段
					  // If the @NoneSerializeNull annotation is already marked on the class,
					  // the field with the attribute value null will be ignored regardless of
					  // whether the field is annotated with this annotation.
					  if (!isNoneSerializeNull.get()) {
						  NoneSerializeNull serializeNull = field.getAnnotation(NoneSerializeNull.class);
						  if (Objects.nonNull(serializeNull))
							  isNoneSerializeNull.set(true);
					  }

					  // 反射获取字段值
					  // Reflect get field value
					  Object value = Reflective.getFieldValue(object, field);

					  // 如果字段上有@NoneSerializeNull标记, 则当value值为null时跳过
					  // If the @NoneSerializeNull annotation is on the field, skip when the value is null
					  if (Objects.isNull(value) && isNoneSerializeNull.get())
						  return;

					  // 如果类上没有标注@JsonCamelCase, 则以字段上是否标注了此注解为准, 字段上也未标注时不会以驼峰命名的形式解析
					  // If @JsonCamelCase is not marked on the class, the annotation is subject to the annotation on the field.
					  // If the field is not marked, it will not be resolved in the form of a camel name.
					  // 如果类上标注了@JsonCamelCase注解, 则不管此字段上是否标注了此注解, 所有的字段都将按照驼峰命名的形式解析
					  // If the @JsonCamelCase annotation is marked on the class, regardless of whether this
					  // annotation is marked on this field, all fields will be parsed according to the hump name.
					  if (!isCameCase.get()) {
						  JsonCamelCase camelCase = field.getAnnotation(JsonCamelCase.class);
						  if (Objects.nonNull(camelCase))
							  isCameCase.set(true);
					  }
					  if (isCameCase.get())
						  fieldName = Strings.cameCase(fieldName);

					  // 如果字段上标注了@JsonSerializeName, 则以此注解的value作为JSON的key
					  // If @JsonSerializeName annotation on the field, the value of this annotation is used as the JSON key.
					  JsonSerializeName serializeName = field.getAnnotation(JsonSerializeName.class);
					  if (Objects.nonNull(serializeName))
						  fieldName = serializeName.value();

					  if (Strings.isNotNullOrEmpty(fieldName)) {

						  indentTab(json);

						  json.append(QUOTATION.getValue()).append(fieldName)
							  .append(QUOTATION.getValue()).append(SEP_COLON.getValue());

						  if (isPretty.get()) {
							  json.append(BLANK.getValue());
						  }

						  if (Objects.nonNull(value)) {
							  if (value instanceof Collection || value.getClass().isArray()) {
								  String array = arrayToJson(value);
								  json.append(array);
							  } else if (!Envs.isBasicType(value.getClass()))
								  complexValue(objectToJson(value), json);
							  else
								  complexValue(value, json);
						  } else
							  json.append(NULL.getValue());

						  addCRLF(json);
					  }
				  });

		deleteUnnecessary(json, END_OBJECT);
		return json.toString();
	}

	/**
	 * 在字段之前添加缩进
	 * Add indentation before the field
	 *
	 * @param json 结果字符串
	 *             Result String
	 */
	private void indentTab(StringBuilder json) {
		if (json.lastIndexOf(Consts.CRLF) != -1 && isPretty.get())
			json.append(Strings.repeat(Consts.TAB, indent.get().get()));
	}

	/**
	 * 在字段后添加换行符或者空格
	 * Add a line break or a space after the field
	 *
	 * @param json 结果字符串(StringBuilder)
	 *             Result String
	 */
	private void addCRLF(StringBuilder json) {
		json.append(SEP_COMMA.getValue());
		if (isPretty.get())
			json.append(Consts.CRLF).append(BLANK.getValue());
	}

	/**
	 * 将属性内的类型转换为JSON字符串, 目前只作用于{@code Map<K, V>}键值
	 * Convert a type inside a property to a JSON string, currently only for {@code Map<K, V>} key value
	 *
	 * @param json   StringBuilder
	 * @param object 对象
	 */
	private void objectJson(StringBuilder json, Object object) {
		String  value          = objectToJson(object);
		boolean isAddQuotation = true;
		// 当object为数字、Object Json字符串、集合|数组Json字符串、Boolean类型时不添加引号
		// Do not add quotes when object is a number, an Object Json string,
		// a collection | an array Json string, or a Boolean type
		if (Strings.isNullOrEmpty(value) ||
				isNumber(object) || isJsonObject(value) ||
				isJsonArray(value) || isBool(object))
			isAddQuotation = false;

		if (isAddQuotation)
			json.append(QUOTATION.getValue());
		if (Objects.isNull(value)) {
			json.append("null");
		} else if ("".equals(value)) {
			json.append("\"\"");
		} else {
			json.append(value);
		}
		if (isAddQuotation)
			json.append(QUOTATION.getValue());
	}

	/**
	 * 判断对象是否是String类型
	 * Determine if the object is of type String
	 *
	 * @param object 需要判断的对象
	 *               Object to be judged
	 * @param isBool true: 如果字符串为true或false则判定不为String类型 | false: 反之
	 *               true: if the string is true or false then the decision is not a String type | false: vice versa
	 * @return true: 是String类型 | false: 不是String类型
	 * true: is a String type | false: not a String type
	 */
	private boolean isString(Object object, boolean isBool) {
		if (Objects.isNull(object))
			return false;
		if (isBool && isBool(object))
			return false;
		return object instanceof CharSequence || object instanceof Character;
	}

	/**
	 * 判断对象是否为Boolean类型, 如果为字符串类型, 则
	 * {@code
	 * "true".equals(object) ||
	 * "false".equals(object) ||
	 * "TRUE".equals(object) ||
	 * "FALSE".equals(object)
	 * }
	 * 成立时表示为Boolean类型
	 * <p>
	 * Determine whether the object is of type Boolean, if it is a string type,
	 * {@code
	 * "true".equals(object) ||
	 * "false".equals(object) ||
	 * "TRUE".equals(object) ||
	 * "FALSE".equals(object)
	 * }
	 * When expressed, it is expressed as Boolean type
	 *
	 * @param object 可能为Boolean类型的对象
	 *               May be an object of type Boolean
	 * @return true: 是Boolean类型 | false: 反之
	 * True: is a Boolean type | false: otherwise
	 */
	private boolean isBool(Object object) {
		if (Envs.hasNullOrEmpty(object))
			return false;
		return object instanceof Boolean || Strings.isBoolean(object.toString());
	}

	/**
	 * 判断是否为数字类型
	 * Determine if it is a numeric type
	 *
	 * @param object 可能为数字的对象
	 *               Possible number of objects
	 * @return true: 代表Object为数字 | false: 反之
	 * True: represents Object as a number | false: otherwise
	 */
	private boolean isNumber(Object object) {
		if (Envs.hasNullOrEmpty(object))
			return false;
		return (!(object instanceof Byte) && object instanceof Number);
	}

	/**
	 * 如果object为String类型则在两侧用引号包裹
	 * If the object is of type String, enclose it in quotation marks on both sides
	 *
	 * @param object Value
	 * @param json   StringBuilder
	 */
	private void complexValue(Object object, StringBuilder json) {
		boolean isString = isString(object, true);

		if (object instanceof Double || object instanceof Float) {
			isString = false;
			object = new BigDecimal(object.toString()).toPlainString();
		} else if (isNumber(object) || ((object instanceof CharSequence) &&
											isJsonObject(object.toString()))) {
			isString = false;
		}

		if (isString)
			json.append(QUOTATION.getValue());
		json.append(object);
		if (isString)
			json.append(QUOTATION.getValue());
	}

	@Override
	public final T toObject(String jsonStr, Class<T> targetClass) {
		throw new JsonNotSupportedException();
	}

	@Override
	public final List<T> toList(String jsonStr, Class<T> targetClass) {
		throw new JsonNotSupportedException();
	}

	@Override
	public final T jsonPath(String json, String path, Class<T> targetClass) {
		throw new JsonNotSupportedException();
	}

	public static <T> JsonEncoder<T> newInstance() {
		return new JsonEncoder<T>();
	}

}
