package io.github.kits.json.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <h3>Json序列化和反序列化时忽略实体中值为null的字段</h3>
 * <ul>
 *     <li>1: 如果标记在类上, 则所有值为null的字段都将会忽略</li>
 *     <li>2: 如果标记在某个属性上, 则当此属性值为null时才会忽略</li>
 * </ul>
 *
 * <h3>Ignore fields with null values ​​in entities when serializing and deserializing Json</h3>
 * <ul>
 *     <li>1: If the tag is on a class, all fields with a null value will be ignored</li>
 *     <li>2: If the tag is on a field, it will be ignored when the field value is null</li>
 * </ul>
 * </p>
 *
 * <p>
 * <pre>
 * {@code @NoneSerializeNull }
 *  public class Example {
 * 	private String name;
 * 	private int age;
 *
 * 	setter & getter ...
 *  }
 * </pre>
 * <blockquote>
 * 在这个例子中将会忽略所有值为null的属性<br>
 * All field with a value of null will be ignored in this example.
 * </blockquote>
 *
 * <pre>
 *  public class Example {
 *
 * {@code 	@NoneSerializeNull }
 * 	private String name;
 * 	private int age;
 *
 * 	setter & getter ...
 *  }
 * </pre>
 * <blockquote>
 * 在这个例子中如果name的值为null会忽略, 否则不忽略<br>
 * In this example, if the value of name is null,
 * it will be ignored, otherwise it will not be ignored.
 * </blockquote>
 * </p>
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoneSerializeNull {

}
