package io.github.needle.serve;

import io.github.kits.log.Logger;
import io.github.needle.annotations.Order;
import io.github.needle.server.Serve;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Order(value = 3)
public class ServeTest3 implements Serve {

	@Override
	public void onServe() {
		for (int i = 0; i < 2; i++) {
			Logger.infof("{}: {}.onServe is run.", i, getClass().getCanonicalName());
		}
	}

	@Override
	public void onStop() {
		for (int i = 0; i < 2; i++) {
			Logger.infof("{}: {}.onStop is stop.", i, getClass().getCanonicalName());
		}
	}

}
