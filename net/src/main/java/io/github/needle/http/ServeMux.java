package io.github.needle.http;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerMux default router processor
 */
public class ServeMux implements Mux {

	public static final String PATTERN_SEPARATOR = "###";

	/**
	 * HttpHandler container
	 */
	private final Map<String, MuxEntry> entries;

	private ServeMux() {
		entries = new ConcurrentHashMap<>();
	}

	public static final Mux DEFAULT_MUX = getInstance();

	public static ServeMux getInstance() {
		return ServerMuxProvider.INSTANCE;
	}

	private static class ServerMuxProvider {
		private static final ServeMux INSTANCE = new ServeMux();
	}

	public Map<String, MuxEntry> getEntries() {
		return entries;
	}

	/**
	 * register the {@link HttpHandler} to default muxEntry
	 *
	 * @param pattern httpRequest path
	 * @param handler HttpHandler
	 */
	@Override
	public synchronized void handle(HttpMethod method, String pattern, HttpHandler handler) {
		String key = buildKey(method, pattern);
		this.entries.put(key, new MuxEntry(method, pattern, handler));
	}

	@Override
	public HttpHandler search(HttpMethod method, String pattern) {
		String key = buildKey(method, pattern);
		MuxEntry entry = null;
		if (this.entries.containsKey(key)) {
			entry = this.entries.get(key);
		} else {
			entry = this.entries.get(pattern);
		}
		if (Objects.nonNull(entry)) {
			return entry.getHandler();
		}
		return null;
	}

	public static String buildKey(HttpMethod method, String pattern) {
		return pattern + (Objects.isNull(method) ? "" : PATTERN_SEPARATOR + method.name());
	}

}
