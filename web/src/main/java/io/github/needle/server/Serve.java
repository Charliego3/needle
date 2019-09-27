package io.github.needle.server;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Serve {

	void onServe();

	default void onStop() {};

}
