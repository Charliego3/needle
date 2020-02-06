package io.github.needle;

import io.github.kits.Assert;
import io.github.kits.Envs;
import io.github.kits.Files;
import io.github.kits.Strings;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.router.HttpRouter;
import io.github.needle.http.router.HttpRouterHandler;
import io.github.needle.server.RunnableFunctional;
import io.github.needle.server.ServeConfig;
import io.github.needle.server.Server;
import io.github.needle.server.context.ServeContext;
import io.github.needle.server.filter.HttpFilterAdapter;
import io.github.needle.server.router.context.RouterContextFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Needle {

	private static Needle               needle;
	private        Server               server;
	private final  Consumer<Exception>  startupExceptionHandler;
	private        ServerStartupHandler startupHandler;
	private        ServeContext         serveContext;
	private        HttpRouterHandler    routerHandler;
	private        HttpFilterAdapter    filterAdapter;

	public static Needle me() {
		if (Objects.isNull(needle)) {
			synchronized (Needle.class) {
				if (Objects.isNull(needle))
					needle = new Needle();
			}
		}
		return needle;
	}

	public Needle listen(String inetAddress, int port) {
		ex(() -> {
			Assert.isNotNullOrEmpty(inetAddress, "The Server InetAddress is null!");
			ServeConfig.getInstance().setInetAddress(inetAddress);
			assertPort(port);
			ServeConfig.getInstance().setPort(port);
		});
		return this;
	}

	public Needle listen(int port) {
		ex(() -> {
			assertPort(port);
			ServeConfig.getInstance().setPort(port);
		});
		return this;
	}

	public Needle get(String path, HttpRouter router) {
		ex(() -> RouterContextFactory.addRouter(HttpMethod.GET, path, router));
		return this;
	}

	public Needle post(String path, HttpRouter router) {
		ex(() -> RouterContextFactory.addRouter(HttpMethod.POST, path, router));
		return this;
	}

	private Needle() {
		this.startupExceptionHandler = (e) -> {
			Logger.error("Start server failed!", e);
			Envs.sleep(500);
			System.exit(-1);
		};
		ex(() -> {
			this.startupHandler = new ServerStartupHandler();
			this.serveContext = new ServeContext();
			this.filterAdapter = new HttpFilterAdapter();
			this.routerHandler = new HttpRouterHandler();
		});
	}

	public void start(Class<?> mainClass, String[] args) {
		ex(() -> {
			this.startupHandler.buildServeConfigFromCommand(args);
			this.startupHandler.buildServeConfigFromProperties();
			String serverName = ServeConfig.getInstance().getServerName() + "-MAIN";;
			Thread.currentThread().setName(serverName);
			this.startupHandler.printEnvironment();
			this.startupHandler.printBanner();
			String mainPackage = mainClass.getPackage().getName();
			String classpath   = Files.getClassPath() + File.separator + mainPackage.replace('.', '/');
			serveContext.registerServe(classpath);
			serveContext.serve();
			filterAdapter.registerFilter(classpath);
			routerHandler.registerRouter(classpath);
			Thread serverThread = new Thread(() -> {
				try {
					this.server = new NeedleServe();
					this.server.start(mainClass, args);
				} catch (IOException e) {
					this.startupExceptionHandler.accept(e);
				}
			});
			serverThread.setName(ServeConfig.getInstance().getServerName().toUpperCase() + "-SERVER");
			serverThread.start();
			Logger.infof("Current process ID: {}", Envs.getCurrentPID());
			String address = ServeConfig.getInstance().getInetAddress();
			if (Strings.isNullOrEmpty(address))
				address = Consts.DEFAULT_HOST;
			Logger.infof("Server started. You can visit http://{}:{}", address, ServeConfig.getInstance().getPort());
		});
	}

	static {
		needle = new Needle();
	}

	private void ex(RunnableFunctional runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			startupExceptionHandler.accept(e);
		}
	}

	/**
	 * 断言port是否小于0, 小于0时抛出异常
	 * Assert whether the port is less than 0, throw an exception when less than 0
	 *
	 * @param port Port
	 */
	private void assertPort(int port) {
		Assert.greaterThan0(port, "The Server port is less then 0! Port: " + port);
	}

}
