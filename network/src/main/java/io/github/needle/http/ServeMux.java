package io.github.needle.http;

import io.github.kits.Maps;
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
	private Map<HttpMethod, Map<String, HttpHandler>> entries;

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

	public Map<HttpMethod, Map<String, HttpHandler>> getEntries() {
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
		Map<String, HttpHandler> handlerMap = this.entries.get(method);
		handlerMap = Objs.nullDefault(handlerMap, () -> {
			Map<String, HttpHandler> map = new ConcurrentHashMap<>();
			this.entries.put(method, map);
			return map;
		});
		handlerMap.put(pattern, handler);
	}

	@Override
	public HttpHandler search(HttpMethod method, String pattern) {
		if (this.entries.containsKey(method)) {
			Map<String, HttpHandler> muxEntry = this.entries.get(method);
			if (Maps.isNotNullOrEmpty(muxEntry) && muxEntry.containsKey(pattern)) {
				return muxEntry.get(pattern);
			}
		}
		throw new NotFundException(Strings.concat("Not find handler with pattern: [", pattern, "]"));
	}

}
