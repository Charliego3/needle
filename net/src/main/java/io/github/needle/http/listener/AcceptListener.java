package io.github.needle.http.listener;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptListener extends HttpListener {

	public AcceptListener(Selector selector) {
		super(selector);
	}

	@Override
	public void onServe() throws IOException {
//		super.setListener(new ReadableListener(Selector.open()));
//		super.serveListener();
		super.onServe();
	}

	@Override
	public void onReceive(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		// 非阻塞状态拿到客户端
		SocketChannel client = server.accept();
		client.configureBlocking(false);
		super.notification(client, SelectionKey.OP_READ);
	}

}
