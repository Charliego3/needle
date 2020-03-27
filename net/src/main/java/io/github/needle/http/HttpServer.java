package io.github.needle.http;

import io.github.kits.Assert;
import io.github.kits.Colors;
import io.github.kits.Lists;
import io.github.kits.Objs;
import io.github.kits.Props;
import io.github.kits.Strings;
import io.github.kits.constants.Consts;
import io.github.kits.enums.Prop;
import io.github.kits.log.Logger;
import io.github.needle.http.exception.HandlerException;
import io.github.needle.http.exception.ServeException;
import io.github.needle.http.filter.HttpFilter;
import io.github.needle.http.filter.RequestFilter;
import io.github.needle.http.filter.ResponseFilter;
import io.github.needle.http.listener.AcceptListener;
import io.github.needle.http.listener.ShutdownListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpServer {

	private static final AtomicBoolean                CLOSED = new AtomicBoolean(false);
	private final        LinkedList<ShutdownListener> shutdownListeners;
	private final        AtomicInteger                maxMethodLength;

	private Mux                   mux;
	private Map<String, MuxEntry> tempMuxHandler;
	private AcceptListener        listener;

	public HttpServer() {
		this.maxMethodLength = new AtomicInteger(0);
		this.tempMuxHandler = new ConcurrentHashMap<>();
		this.shutdownListeners = new LinkedList<>();
		this.shutdownListeners.add(() -> {
			close();
			this.listener.onDestroy();
		});
	}

	public void close() {
		CLOSED.set(true);
	}

	public static HttpServer newInstance() {
		return new HttpServer();
	}

	public HttpServer handle(String pattern, HttpHandler handler, HttpMethod... methods) {
		Assert.isNotNullEmptyBlack(pattern, new HandlerException("invalid pattern"));
		Assert.isNotNull(handler, new HandlerException("null handler. pattern: " + pattern));
		if (!pattern.startsWith("/")) {
			pattern = "/" + pattern;
		}
		pattern = Strings.replace(pattern, "/", "[/]+");
		methods = Objs.nullDefault(methods, new HttpMethod[]{HttpMethod.ALL});
		for (HttpMethod method : methods) {
			String key = ServeMux.buildKey(method, pattern);
			if (this.tempMuxHandler.containsKey(key)) {
				String err;
				if (HttpMethod.ALL.equals(method)) {
					err = Strings.concat("multiple pattern registrations for [", pattern, "]");
				} else {
					err = Strings.concat("multiple pattern registrations for ", method.name(), " - [", pattern, "]");
				}
				throw new HandlerException(err);
			}
			this.tempMuxHandler.put(key, new MuxEntry(method, pattern, handler));
			int methodLength = method.name().toCharArray().length;
			if (this.maxMethodLength.intValue() < methodLength) {
				this.maxMethodLength.set(methodLength);
			}
		}
		return this;
	}

	public HttpServer filter(HttpFilter filter) {
		return this;
	}

	public HttpServer requestFilter(RequestFilter filter) {
		return this;
	}

	public HttpServer responseFilter(ResponseFilter filter) {
		return this;
	}

	public void listenAndServe() throws IOException {
		listenAndServe(Constants.DEFAULT_PORT);
	}

	public void listenAndServe(int port) throws IOException {
		listenAndServe(port, null);
	}

	public void listenAndServe(int port, Mux mux) throws IOException {
		listenAndServe(null, port, mux);
	}

	public void listenAndServe(String host, int port) throws IOException {
		listenAndServe(host, port, null);
	}

	public void listenAndServe(String addr) throws IOException {
		listenAndServe(addr, null);
	}

	public void listenAndServe(String addr, Mux mux) throws IOException {
		boolean isValid = Strings.isNotBlack(addr) && addr.contains(":");
		Assert.isTrue(isValid, new ServeException("listen addr is invalid, addr: " + addr));
		String portStr = addr.substring(addr.indexOf(":") + 1);
		Assert.isNotNullEmptyBlack(portStr, new ServeException("port is not can be empty"));
		String host = addr.substring(0, addr.indexOf(":"));
		listenAndServe(host, Integer.parseInt(portStr), mux);
	}

	public void listenAndServe(String host, int port, Mux mux) throws IOException {
		Assert.isTrue(Strings.regexMatch(port + "", Constants.PORT_REG),
					  new ServeException("listen error, the port is invalid. port: " + port));
		InetSocketAddress address;
		if (Strings.isBlack(host)) {
			address = new InetSocketAddress(port);
		} else {
			address = new InetSocketAddress(host, port);
		}
		this.mux = Objs.nullDefault(mux, ServeMux.DEFAULT_MUX);
//		this.tempMuxHandler.forEach((m, es) -> {
//			es.forEach((p, h) -> this.mux.handle(m, p, h));
//			int length = m.name().toCharArray().length;
//			if (max.get() < length) {
//				max.set(length);
//			}
//		});
		this.tempMuxHandler.forEach((key, entry) -> {
			this.mux.handle(entry.getMethod(), entry.getPattern(), entry.getHandler());
		});
		this.tempMuxHandler = null;
		Logger.errorf("MaxHandler Method length: {}", maxMethodLength.get());
		printHandler();
		Selector            selector     = Selector.open();
		ServerSocketChannel serverSocket = selector.provider().openServerSocketChannel();
		serverSocket.configureBlocking(false);
		serverSocket.socket().bind(address);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		Logger.infof("服务器信息: {}", serverSocket.getLocalAddress());
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "NEEDLE-SHUTDOWN"));
		this.listener = new AcceptListener(selector);
		Props.put(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), Consts.LOG_ASYNC_PRINT_KEY, "true");
		this.listener.onServe();
	}

	private void printHandler() {
		if (this.mux instanceof ServeMux) {
			final int maxLength = this.maxMethodLength.get();
			((ServeMux) this.mux).getEntries().forEach((key, entry) -> {
				if (maxLength == 0) {
					Logger.infof("Register HttpHandler: {}", entry.getPattern());
				} else {
					String methodName = entry.getMethod().name();
					methodName = Strings.indentRight(methodName, maxLength);
					switch (entry.getMethod()) {
						case GET:
							methodName = Colors.toBlueBold(methodName);
							break;
						case POST:
							methodName = Colors.toRedBold(methodName);
							break;
						case PUT:
							methodName = Colors.toYellowBold(methodName);
							break;
						case DELETE:
							if (maxLength > 6) {
								methodName = Colors.toUnderLineBold(entry.getMethod().name()) + " ";
							} else {
								methodName = Colors.toUnderLineBold(methodName);
							}
							break;
						case OPTIONS:
							methodName = Colors.toGrayBold(methodName);
							break;
						case HEAD:
							methodName = Colors.toCanaryBold(methodName);
							break;
						case PATCH:
							methodName = Colors.toGrayMoreBold(methodName);
							break;
						case TRACE:
							methodName = Colors.toBlueLessBold(methodName);
							break;
						case CONNECT:
							methodName = Colors.toBackgroundBold(methodName);
							break;
						default:
							methodName = Colors.toWhiteBold(methodName);
							break;
					}
					Logger.infof("Register HttpHandler: {} {} {}", methodName, "->", entry.getPattern());
				}
			});
		}
	}

	public static boolean isClosed() {
		return CLOSED.get();
	}

	public HttpServer shuttingDown(ShutdownListener shutdownListeners) {
		this.shutdownListeners.add(shutdownListeners);
		return this;
	}

	private void stop() {
		if (Lists.isNotNullOrEmpty(shutdownListeners)) {
			shutdownListeners.forEach(ShutdownListener::shutdown);
		}
	}

}
