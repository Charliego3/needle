package io.github.kits.json.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <h3>Json序列化和反序列化时, 属性将按照驼峰命名的形式进行解析</h3>
 * <ul>
 *     <li>1: 将{@link JsonCamelCase}作用于类上时, 则所有的属性都按照驼峰命名的方式解析</li>
 *     <li>2: 将{@link JsonCamelCase}作用于某个单独的属性上时,
 *     则只有单独添加了此注解的属性才会按照驼峰命名的方式解析</li>
 *     <li>3: 如果和{@link JsonSerializeName}同时使用时将没有任何效果</li>
 * </ul>
 *
 * <h3>When Json is serialized and deserialize,
 * the properties will be parsed according to the hump name</h3>
 * <ul>
 *     <li>1: When {@link JsonCamelCase} is applied to a class,
 *     all fields are resolved according to the way the camel is named</li>
 *     <li>2: When {@link JsonCamelCase} is applied to a single field,
 *     Only field that have this annotation added separately
 *     will be parsed according to the way the camel is named</li>
 *     <li>3: There will be no effect when used with {@link JsonSerializeName}</li>
 * </ul>
 * </p>
 *
 * <p>
 * <pre>
 *{@code @JsonCamelCase}
 * public class Example {
 *     private String user_name;
 *     private int avg_age;
 *
 *     setter & getter...
 * }
 * </pre>
 * <blockquote>
 * 在这个例子中将{@link JsonCamelCase}作用于类上, 所以在解析时所有的属性都使用驼峰命名<br>
 * In this example, {@link JsonCamelCase} is applied to the class,
 * so all fields are named after the hump when parsing
 * </blockquote>
 * </p>
 *
 * <p>
 * <pre>
 * public class Example {
 * {@code    @JsonCamelCase}
 *     private String user_name;
 *     private int avg_age;
 *
 *     setter & getter...
 * }
 * </pre>
 * <blockquote>
 * 在这个例子中将{@link JsonCamelCase}作用于user_name上, 所以在解析时只有这个属性会使用驼峰命名<br>
 * In this example, {@link JsonCamelCase} is applied to user_name,
 * so only this field will be named with a camel when parsing<br>
 * </blockquote>
 * </p>
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonCamelCase {

}
