package io.github.needle.constants;

import io.github.kits.PropertiesKit;
import io.github.needle.annotations.Order;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Consts {

	String DEFAULT_HOST        = "0.0.0.0";
	int    DEFAULT_PORT        = 9000;
	String DEFAULT_SERVER_NAME = PropertiesKit.getString("server.name").orElse("NEEDLE");
	String CRLF                = "\r\n";

	interface PropArgs {
		String port        = "server.port";
		String inetAddress = "server.address";
		String serverName  = "server.name";
	}

	interface CommandArgs {
		String port0 = "-p";
		String port  = "--port";

		String name0 = "-n";
		String name  = "--name";

		String address0 = "-addr";
		String address  = "--address";
	}

	interface Http {
		String CONTENT_TYPE    = "Content-Type";
		String CONNECTION      = "Connection";
		String KEEP_ALIVE      = "Keep-Alive";
		String CONTENT_LENGTH  = "Content-Length";
		String USER_AGENT      = "User-Agent";
		String ORIGIN          = "Origin";
		String ACCEPT          = "Accept";
		String ACCEPT_ENCODING = "Accept-Encoding";
		String ACCEPT_LANGUAGE = "Accept-Language";
	}

	interface ContentType {
		String MULTIPART_FORM_DATA               = "multipart/form-data";
		String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	}

	static <T> Comparator<T> orderComparator() {
		return (f1, f2) -> {
			Order o1 = f1.getClass().getAnnotation(Order.class);
			Order o2 = f2.getClass().getAnnotation(Order.class);
			if (Objects.nonNull(o1)) {
				if (Objects.nonNull(o2))
					return o1.value() - o2.value();
				return -1;
			} else if (Objects.nonNull(o2)) {
				return 1;
			}
			return -1;
		};
	}

}
