package io.github.needle.http.listener;

@FunctionalInterface
public interface ShutdownListener {

	void shutdown();

}
