package io.github.needle.http.listener;

import io.github.kits.log.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class WriteableListener extends HttpListener {

	public WriteableListener(Selector selector) {
		super(selector);
	}

	@Override
	public void onReceive(SelectionKey key) throws IOException {
		key.interestOps(key.readyOps() & ~SelectionKey.OP_WRITE);
		SocketChannel socket = (SocketChannel) key.channel();
		StringBuilder ret    = new StringBuilder();
		ret.append("HTTP/1.1 200 OK\r\n");
		ret.append("Server: Needle\r\n");
		ret.append("Accept: application/json\r\n\r\n");
		ret.append("{\"success\":true, \"message\":\"Successful!\"}");
		ByteBuffer buffer = ByteBuffer.wrap(ret.toString().getBytes());
		socket.write(buffer);
		socket.shutdownOutput();
		Logger.debugf("Write Message: \n{}", ret);
	}

}
