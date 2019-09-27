package io.github.needle;

import io.github.kits.Envs;
import io.github.kits.Strings;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.http.HttpDispatcher;
import io.github.needle.server.ServeConfig;
import io.github.needle.server.Server;
import io.github.needle.server.context.ServeContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
class NeedleServe implements Server {

	private ServerSocketChannel serverSocketChannel;
	private Selector            selector;
	private ByteBuffer          readBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer          writeBuf   = ByteBuffer.allocate(1024);
	private String              inetAddress;
	private int                 port;
	private HttpDispatcher      dispatcher;

	NeedleServe() {
		inetAddress = ServeConfig.getInstance().getInetAddress();
		port = ServeConfig.getInstance().getPort();
		dispatcher = new HttpDispatcher();
	}

	@Override
	public void start(Class<?> mainClass, String[] args) throws IOException {
		serve();
		SelectionKey key = null;
		while (true) {
			try {
				// 已经准备就绪的事件个数
				if (selector.isOpen() && selector.select() > 0) {
					Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
					while (selectedKeys.hasNext()) {
						key = selectedKeys.next();
						if (!key.isValid()) {
							continue;
						}
						if (key.isAcceptable()) {
							accept();
						} else if (key.isReadable()) {
							dispatcher.dispatcher(key);
						} else if (key.isWritable()) {
							write(key);
						}
						selectedKeys.remove();
					}
				}
			} catch (Throwable e) {
				if (Objects.nonNull(key)) {
					key.cancel();
					SelectableChannel channel = key.channel();
					if (Objects.nonNull(channel)) {
						try {
							channel.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
				Logger.error(e);
			}
		}
	}

	@Override
	public void stop() {
		Logger.info("Shutdown....");
		try {
			new ServeContext().stop();
			if (Objects.nonNull(readBuffer)) {
				readBuffer.clear();
			}
			if (Objects.nonNull(serverSocketChannel)) {
				serverSocketChannel.close();
			}
			if (Objects.nonNull(selector)) {
				selector.close();
			}
			Logger.info("Server stop successful!");
		} catch (Exception e) {
			Logger.error("Server stop error", e);
		}
		Envs.sleep(500);
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		Logger.infof("WriteThread: {}, Channel: {}", Thread.currentThread().toString(), socketChannel);
//		writeBuf.clear();
//		writeBuf.put("Hello, You good!".getBytes());
//		writeBuf.flip();
		ByteBuffer attachment = (ByteBuffer) key.attachment();
		socketChannel.write(attachment);
		socketChannel.shutdownOutput();
		socketChannel.close();
	}

	private void serve() throws IOException {
		if (Strings.isNullOrEmpty(inetAddress))
			inetAddress = Consts.DEFAULT_HOST;
		if (port <= 0)
			port = Consts.DEFAULT_PORT;
		selector = Selector.open();
		SelectorProvider provider = selector.provider();
		serverSocketChannel = provider.openServerSocketChannel();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName(inetAddress), port));
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		registerShutdownHook();
	}

	private void accept() throws IOException {
		SocketChannel socketChannel = serverSocketChannel.accept();
		if (Objects.nonNull(socketChannel)) {
			Logger.infof("AcceptThread: {}, Accept: {}", Thread.currentThread().toString(), socketChannel);
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
		}
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "WINTER-SHUTDOWN"));
	}

}
