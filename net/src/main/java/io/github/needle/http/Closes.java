package io.github.needle.http;

import io.github.kits.log.Logger;

import java.io.Closeable;
import java.util.Objects;

public class Closes {

	public static void close(Closeable... closeables) {
		if (Objects.nonNull(closeables) && closeables.length > 0) {
			for (Closeable closeable : closeables) {
				if (Objects.nonNull(closeable)) {
					try {
						closeable.close();
					} catch (Exception err) {
						Logger.error(err);
					}
				}
			}
		}
	}

}
