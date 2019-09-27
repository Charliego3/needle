package io.github.needle.server;

import java.io.IOException;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Server {

	void start(Class<?> mainClass, String[] args) throws IOException;

	void stop();

}
