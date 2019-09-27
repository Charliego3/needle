package io.github.kits.annotations;

import java.lang.annotation.*;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    String prop() default "application";

    String name();

    String defaultValue() default "";

}
