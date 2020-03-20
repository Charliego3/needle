package io.github.needle.http.listener;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;

public interface Listener {

	void onServe() throws IOException;

	void onReceive(SelectionKey key) throws IOException;

	void onDestroy();

	void notification(AbstractSelectableChannel channel, int ops) throws ClosedChannelException;

}
