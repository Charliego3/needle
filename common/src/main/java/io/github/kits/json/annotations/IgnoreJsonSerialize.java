package io.github.kits.json.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <h3>忽略Json序列化和反序列</h3>
 * 如果属性上有{@link IgnoreJsonSerialize}注解, 则在Json序列化时忽略
 * <ul>
 *     <li>注意: 此注解只能作用于属性上</li>
 * </ul>
 * </p>
 *
 * <p>
 * <h3>Ignore Json serialization and inverse sequences</h3>
 * If there is a {@link IgnoreJsonSerialize} annotation on the field,
 * it is ignored when Json is serialized
 * <ul>
 *     <li>Note: This annotation can only be applied to fields</li>
 * </ul>
 * </p>
 *
 * <p>
 * <pre>
 * public class Example {
 *    {@code @IgnoreJsonSerialize}
 *     private String name;
 *     private int age;
 *
 *     setter & getter...
 * }
 * </pre>
 * <blockquote>
 * 在这个例子中序列化输出到JsonString结果为: {"age": 12}, name属性会忽略<br>
 * In this example, the serialized output to JsonString
 * results in: {"age": 12}, the name field is ignored
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
public @interface IgnoreJsonSerialize {

}