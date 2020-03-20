package io.github.needle.http;

import io.github.kits.Objs;
import io.github.kits.Strings;
import io.github.needle.http.exception.NotFundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerMux default router processor
 */
public class ServeMux implements Mux {

	/**
	 * HttpHandler container
	 */
	private Map<String, MuxEntry> entries;

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
		MuxEntry muxEntry = new MuxEntry(method, handler);
		this.entries.put(pattern, muxEntry);
	}

	@Override
	public HttpHandler search(HttpMethod method, String pattern) {
		if (this.entries.containsKey(pattern)) {
			MuxEntry muxEntry = this.entries.get(pattern);
			if (Objs.nullDefault(muxEntry.getMethod(), method).equals(method)) {
				return muxEntry.getHandler();
			}
		}
		throw new NotFundException(Strings.concat("Not find handler with pattern: [", pattern, "]"));
	}

}
