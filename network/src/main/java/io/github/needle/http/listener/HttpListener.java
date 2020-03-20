package io.github.needle.http.listener;

import io.github.kits.Envs;
import io.github.kits.Strings;
import io.github.kits.enums.LogLevel;
import io.github.kits.log.Logger;
import io.github.needle.http.Closes;
import io.github.needle.http.HttpServer;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class HttpListener implements Listener {

	private Selector selector;
	private HttpListener listener;
	private ExecutorService service = Executors.newSingleThreadExecutor();

	public HttpListener(Selector selector) {
		this.selector = selector;
	}

	@Override
	public void onServe() throws IOException {
		Logger.debugf("{} onServed...", this.getClass().getName());
		Logger.warn("Test logger.....ssssss");
		int i = 0;
		do {
			try {
				i++;
				if (i == 20) {
					Logger.setLevel(LogLevel.DEBUG, LogLevel.INFO);
				}
				Logger.warnf("Test logger warn {} .....", i);
				Logger.debugf("Test logger debug {} .....", i);
				Envs.sleep(500);
//				if (HttpServer.isClosed()) {
//					break;
//				}
//				if (this.selector.select() == 0) {
//					continue;
//				}
//				Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
//				while (iterator.hasNext()) {
//					if (HttpServer.isClosed()) {
//						break;
//					}
//					final SelectionKey key = iterator.next();
//					iterator.remove();
//					onReceive(key);
//				}
			} catch (Throwable err) {
				Logger.error(err);
			}
		} while (!HttpServer.isClosed());
		Logger.error(Strings.concat(this.getClass().getName(), " serve end...."));
	}

	@Override
	public void onDestroy() {
		if (Objects.nonNull(this.listener))
			this.listener.onDestroy();
		this.selector.wakeup();
		Closes.close(this.selector);
		Logger.debugf("{} is destroy....", this.getClass().getName());
	}

	@Override
	public void notification(AbstractSelectableChannel channel, int ops) throws ClosedChannelException {
		if (this.listener.selector.isOpen()) {
			channel.register(this.listener.selector, ops);
		}
		Logger.debugf("{} Notification listener...", this.getClass().getName());
	}

	public void serveListener() {
		this.service.execute(() -> {
			try {
				this.listener.onServe();
			} catch (IOException err) {
				Logger.error(err);
			}
		});
	}

	public void setListener(HttpListener listener) {
		this.listener = listener;
	}

}
