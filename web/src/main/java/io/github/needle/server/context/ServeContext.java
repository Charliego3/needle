package io.github.needle.server.context;

import io.github.kits.Envs;
import io.github.kits.LambdaExes;
import io.github.kits.Lists;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.server.Serve;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServeContext {

	private static final Set<Serve> serveList = new TreeSet<>(Consts.orderComparator());

	public void registerServe(String classpath) throws IOException, ReflectiveOperationException {
		List<Class> serves = Envs.getDirClass(new File(classpath), Serve.class);
		if (Lists.isNotNullOrEmpty(serves)) {
			serves.stream()
				.map(LambdaExes.rethrowFunction(sc -> (Serve) sc.newInstance()))
				.forEach(serve -> {
					serveList.add(serve);
					Logger.infof("Register Serve: {}", serve.getClass().getCanonicalName());
				});
		}
	}

	public void serve() {
		serveList.forEach(Serve::onServe);
	}

	public void stop() {
		serveList.forEach(Serve::onStop);
	}

}
