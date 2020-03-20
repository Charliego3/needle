package io.github.needle.http.listener;

import io.github.kits.log.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ReadableListener extends HttpListener {

	public ReadableListener(Selector selector) {
		super(selector);
	}

	@Override
	public void onServe() throws IOException {
		this.setListener(new WriteableListener(Selector.open()));
		this.serveListener();
		super.onServe();
	}

	@Override
	public void onReceive(SelectionKey key) throws IOException {
		key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
		SocketChannel channel  = (SocketChannel) key.channel();
		String        content = read(channel);
		Logger.debugf("Read Message: \n{}", content);
		super.notification(channel, SelectionKey.OP_WRITE);
	}

	ByteBuffer buffer = ByteBuffer.allocate(100);
	private String read(SocketChannel channel) throws IOException {
		try {
			Logger.debugf("Reading....");
			buffer.clear();
			int           numRead = -1;
			StringBuilder data    = new StringBuilder();
			while ((numRead = channel.read(buffer)) > 0) {
				data.append(new String(buffer.array(), 0, numRead, StandardCharsets.UTF_8));
				buffer.clear();
			}
			return data.toString();
		} catch (IOException e) {
			Logger.info("closed by exception" + channel);
			throw e;
		}
	}

}
