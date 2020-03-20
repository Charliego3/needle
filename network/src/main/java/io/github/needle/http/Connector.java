package io.github.needle.http;

import java.nio.channels.Selector;

public class Connector {

	private Selector selector;

	public Connector(Selector selector) {
		this.selector = selector;
	}

}
