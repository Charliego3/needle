//package io.github.needle.http;
//
//import io.github.kits.Lists;
//import io.github.kits.Strings;
//import io.github.kits.log.Logger;
//import io.github.needle.http.exception.HandlerException;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.LineNumberReader;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class Server {
//
//	private              Selector                     selector;
//	private static final AtomicBoolean                serverClose = new AtomicBoolean(false);
////	private final        LinkedList<ShutdownListener> shutdownListeners;
//	private              Mux                          mux;
////	private              ServerOption                 serverOption;
//	private              Map<String, HttpHandler>     muxHandler  = new ConcurrentHashMap<>();
//
////	Server() {
////		this.shutdownListeners = new LinkedList<>();
////		this.shutdownListeners.add(() -> {
////			if (Objects.nonNull(selector)) {
////				try {
////					close();
////					selector.wakeup();
////					selector.close();
////					Logger.infof("Closed selector...");
////				} catch (IOException e) {
////					Logger.errorf("ShutDown close selector error", e);
////				}
////			}
////		});
////	}
////
////	void handle(String pattern, HttpHandler handler) {
////		if (this.muxHandler.containsKey(pattern)) {
////			throw new HandlerException(Strings.concat("multiple pattern registrations for [", pattern, "]"));
////		}
////		if (!pattern.startsWith("/")) {
////			pattern = "/" + pattern;
////		}
////		this.muxHandler.put(pattern, handler);
////	}
////
////	public void listenAndServe(InetSocketAddress addr, Mux mux) throws IOException {
////		this.mux = mux;
////		this.muxHandler.forEach((k, v) -> this.mux.handle(k, v));
////		this.muxHandler = null;
////		selector = Selector.open();
////		ServerSocketChannel serverSocket = selector.provider().openServerSocketChannel();
////		serverSocket.configureBlocking(false);
////		serverSocket.socket().bind(addr);
////		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
////		Logger.infof("服务器信息: {}", serverSocket.getLocalAddress());
////		Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "NEEDLE-SHUTDOWN"));
////		serve();
////	}
//
////	private HttpRequest readRequest() throws IOException {
////		HttpRequest request          = new HttpRequest();
////		Duration    wholeReqDeadline = Duration.ZERO, hdrDeadline = Duration.ZERO;
////		Duration    t0               = Duration.ofNanos(System.nanoTime());
////		if (!serverOption.getReadHeaderTimeout().isZero()) {
////			hdrDeadline = t0.plus(serverOption.getReadHeaderTimeout());
////		}
////		if (!serverOption.getReadTimeOut().isZero()) {
////			wholeReqDeadline = t0.plus(serverOption.getReadTimeOut());
////		}
////		SocketChannel socket = null;//(SocketChannel) key.channel();
////		int           limit  = socket.read(buffer);
////		buffer.flip();
////		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
////		stream.write(buffer.array(), 0, limit);
////		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.array());
////		LineNumberReader     reader      = new LineNumberReader(new InputStreamReader(inputStream));
////		reader.readLine();
////		return request;
////	}
////
////	public static void main(String[] args) throws IOException {
////		String content = "POST /test1 HTTP/1.1\n" +
////							 "Accept: application/json\r\n" +
////							 "Content-Type: application/json; charset=utf-8\n" +
////							 "Host: 127.0.0.1:2048\n" +
////							 "Connection: close\n" +
////							 "User-Agent: Paw/3.1.10 (Macintosh; OS X/10.15.3) GCDHTTPRequest\n" +
////							 "Content-Length: 39\n" +
////							 "\n" +
////							 "{\"id\":\"1\",\"name\":\"whimthen\",\"age\":\"20\"}";
////		ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
////		LineNumberReader     reader      = new LineNumberReader(new InputStreamReader(inputStream));
////		reader.lines().forEach(Logger::debug);
////	}
//
//	public void serve() {
//		Logger.debug("HTTP SERVER IS STARTED.");
//		do {
//			try {
//				if (serverClose.get()) {
//					break;
//				}
//				if (selector.select() == 0) {
//					continue;
//				}
//				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//				while (iterator.hasNext()) {
//					if (serverClose.get()) {
//						break;
//					}
//					Logger.warnf("SelectionKey size: {}", selector.selectedKeys().size());
//					SelectionKey key = iterator.next();
//					iterator.remove();
//
//					if (key.isAcceptable()) {
//						ServerSocketChannel server = (ServerSocketChannel) key.channel();
//						// 非阻塞状态拿到客户端
//						SocketChannel client = server.accept();
//						client.configureBlocking(false);
//						client.register(selector, SelectionKey.OP_READ);
//					} else if (key.isReadable()) {
//						SocketChannel socket  = (SocketChannel) key.channel();
//						String        content = read(socket);
//						HttpHandler   handler = this.mux.findHandler("/test1");
//						handler.process(new HttpRequest(), new HttpResponse());
//						Logger.debugf("Read Message: \n{}", content);
//						socket.register(selector, SelectionKey.OP_WRITE);
//					} else if (key.isWritable()) {
//						key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
//						SocketChannel socket = (SocketChannel) key.channel();
//						StringBuilder ret    = new StringBuilder();
//						ret.append("HTTP/1.1 200 OK\r\n");
//						ret.append("Server: Needle\r\n");
//						ret.append("Accept: application/json\r\n\r\n");
//						ret.append("{\"success\":true, \"message\":\"Successful!\"}");
//						ByteBuffer buffer = ByteBuffer.wrap(ret.toString().getBytes());
//						socket.write(buffer);
//						socket.shutdownOutput();
//						Logger.debugf("Write Message: {}", ret);
//					}
//				}
//			} catch (Exception err) {
//				Logger.error(err);
//			}
//		} while (!serverClose.get());
//	}
//
//	ByteBuffer buffer = ByteBuffer.allocate(100);
//
//	private String read(SocketChannel channel) throws IOException {
//		try {
//			buffer.clear();
//			int           numRead = -1;
//			StringBuilder data    = new StringBuilder();
//			while ((numRead = channel.read(buffer)) > 0) {
//				data.append(new String(buffer.array(), 0, numRead, StandardCharsets.UTF_8));
//				buffer.clear();
//			}
//			return data.toString();
//		} catch (IOException e) {
//			Logger.info("closed by exception" + channel);
//			throw e;
//		}
//	}
////
////	public static void close() {
////		Server.serverClose.set(true);
////	}
////
////	public static boolean isClosed() {
////		return Server.serverClose.get();
////	}
////
////	void registerShutdownListener(List<ShutdownListener> shutDownListener) {
////		shutdownListeners.addAll(shutDownListener);
////	}
////
////	private void stop() {
////		if (Lists.isNotNullOrEmpty(shutdownListeners)) {
////			shutdownListeners.forEach(ShutdownListener::shutdown);
////		}
////	}
////
////	public class ServerOption {
////		private TLSConfig tlsConfig;
////		private Duration  readTimeOut       = Duration.ZERO;
////		private Duration  readHeaderTimeout = Duration.ZERO;
////		private Duration  writeTimeout      = Duration.ZERO;
////		private Duration  idleTimeout       = Duration.ZERO;
////		private int       maxHeaderBytes;
////
////		public TLSConfig getTlsConfig() {
////			return tlsConfig;
////		}
////
////		public void setTlsConfig(TLSConfig tlsConfig) {
////			this.tlsConfig = tlsConfig;
////		}
////
////		public Duration getReadTimeOut() {
////			return readTimeOut;
////		}
////
////		public void setReadTimeOut(Duration readTimeOut) {
////			this.readTimeOut = readTimeOut;
////		}
////
////		public Duration getReadHeaderTimeout() {
////			if (readHeaderTimeout.isZero())
////				return readTimeOut;
////			return readHeaderTimeout;
////		}
////
////		public void setReadHeaderTimeout(Duration readHeaderTimeout) {
////			this.readHeaderTimeout = readHeaderTimeout;
////		}
////
////		public Duration getWriteTimeout() {
////			return writeTimeout;
////		}
////
////		public void setWriteTimeout(Duration writeTimeout) {
////			this.writeTimeout = writeTimeout;
////		}
////
////		public Duration getIdleTimeout() {
////			return idleTimeout;
////		}
////
////		public void setIdleTimeout(Duration idleTimeout) {
////			this.idleTimeout = idleTimeout;
////		}
////
////		public int getMaxHeaderBytes() {
////			return maxHeaderBytes;
////		}
////
////		public void setMaxHeaderBytes(int maxHeaderBytes) {
////			this.maxHeaderBytes = maxHeaderBytes;
////		}
////
////	}
////
////
////	private static ExecutorService pool = new ThreadPoolExecutor(5,
////																 200,
////																 0L,
////																 TimeUnit.MILLISECONDS,
////																 new LinkedBlockingQueue<>(1024),
////																 new ClientListener.HttpServerThreadFactory(),
////																 new ThreadPoolExecutor.AbortPolicy());
//
//}
