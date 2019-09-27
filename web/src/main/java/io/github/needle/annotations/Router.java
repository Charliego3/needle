package io.github.needle.annotations;

import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.protocol.HttpContentType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Router {

	String value() default "/";

	HttpMethod method() default HttpMethod.GET;

	HttpContentType contentType() default HttpContentType.TEXT;

}
