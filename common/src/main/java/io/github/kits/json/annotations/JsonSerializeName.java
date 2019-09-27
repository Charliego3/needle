package io.github.kits.json.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <h3>Json序列化和反序列化时为特定属性指定别名</h3>
 * <ul>
 *     <li>只能作用于属性上, 提供
 *     {@link JsonSerializeName#value()}用于指定别名</li>
 * </ul>
 *
 * <h3>Specify aliases for specific fields when Json serializes and deserializes</h3>
 * <ul>
 *     <li>Can only be applied to fields,
 *     providing {@link JsonSerializeName#value()}
 *     for specifying aliases</li>
 * </ul>
 * </p>
 *
 * <p>
 * <pre>
 * public class Example {
 *    {@code @JsonSerializeName(value = "userName")}
 *     private String name;
 *     private int age;
 *
 *     setter & getter...
 * }
 * </pre>
 * <blockquote>
 * 上面的例子中, Json序列化时将按照userName进行解析	<br>
 *
 * In the above example, Json will be parsed according to userName.
 * </blockquote>
 * </p>
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonSerializeName {

	/**
	 * <p>
	 * 为属性提供别名<br>
	 * Provide an alias for the fields
	 * </p>
	 *
	 * @return alias
	 */
	String value();

}
