package io.github.needle;

import io.github.kits.Colors;
import io.github.kits.Envs;
import io.github.kits.Files;
import io.github.kits.Props;
import io.github.kits.Strings;
import io.github.kits.log.LogThread;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.server.ServeConfig;

import java.io.IOException;
import java.util.Objects;

import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
class ServerStartupHandler {

	void printEnvironment() {
		String version = ServeConfig.getInstance().getServerVersion();
		if (Strings.isBlack(version) && version.contains("-")) {
			version = version.substring(0, ServeConfig.getInstance().getServerVersion().indexOf('-'));
		}
		Logger.info(Strings.indentRight("jdk.version", 15).concat("-> ").concat(System.getProperty("java.version")));
		Logger.info(Strings.indentRight("user.dir", 15).concat("-> ").concat(Files.getContentPath()));
		Logger.info(Strings.indentRight("user.timezone", 15).concat("-> ").concat(System.getProperty("user.timezone")));
		Logger.info(Strings.indentRight("file.encoding", 15).concat("-> ").concat(System.getProperty("file.encoding")));
		Logger.info(Strings.indentRight("classpath", 15).concat("-> ").concat(Files.getClassPath()));
		Logger.info(Strings.indentRight("server.version", 15).concat("-> ").concat(version));
		Envs.sleep(10);
	}

	/**
	 * 输出banner
	 */
	void printBanner() throws IOException {
		final String RELEASE =
				"  ____  _____ _     _____    _    ____  _____\n" +
				" |  _ \\| ____| |   | ____|  / \\  / ___|| ____|\n" +
				" | |_) |  _| | |   |  _|   / _ \\ \\___ \\|  _|\n" +
				" |  _ <| |___| |___| |___ / ___ \\ ___) | |___\n" +
				" |_| \\_|_____|_____|_____/_/   \\_|____/|_____|";
		final String SNAPSHOTS =
				"  ____  _   _    _    ____  ____  _   _  ___ _____ \n" +
				" / ___|| \\ | |  / \\  |  _ \\/ ___|| | | |/ _ \\_   _|\n" +
				" \\___ \\|  \\| | / _ \\ | |_) \\___ \\| |_| | | | || | \n" +
				"  ___) | |\\  |/ ___ \\|  __/ ___) |  _  | |_| || | \n" +
				" |____/|_| \\_/_/   \\_|_|   |____/|_| |_|\\___/ |_| ";
		final String threeRelease =
				" +-+-+-+-+-+-+-+\n" +
				" |R|E|L|E|A|S|E|\n" +
				" +-+-+-+-+-+-+-+";
		final String threeSnapshots =
				" +-+-+-+-+-+-+-+-+\n" +
				" |S|N|A|P|S|H|O|T|\n" +
				" +-+-+-+-+-+-+-+-+";
		byte[] bytes = Files.loadResourceFile("banner.txt");
		if (Objects.nonNull(bytes) && bytes.length > 0) {
			String lineSeparator = Files.getLineSeparator();
			String   banner           = new String(bytes);
			String[] banners          = banner.split(lineSeparator);
			boolean  isStart          = true;
			int      bannerLineLength = 0;
			int      maxLineLength    = 0;
			for (String s : banners) {
				if (isStart) {
					if (Strings.isNotNullOrEmpty(s))
						isStart = false;
					bannerLineLength++;
					continue;
				}
				if (Strings.isNotNullOrEmpty(s)) {
					bannerLineLength++;
					if (s.length() > maxLineLength)
						maxLineLength = s.toCharArray().length;
				}
			}
			String  subBanner  = RELEASE;
			String  version    = ServeConfig.getInstance().getServerVersion();
			boolean isSnapshot = version.toUpperCase().contains("SNAPSHOT");
			if (isSnapshot) {
				if (bannerLineLength > 5)
					subBanner = SNAPSHOTS;
				else if (bannerLineLength > 3)
					subBanner = threeSnapshots;
			} else {
				if (bannerLineLength > 5)
					subBanner = RELEASE;
				else if (bannerLineLength > 3)
					subBanner = threeRelease;
			}
			String[]      snaps      = subBanner.split(lineSeparator);
			StringBuilder bannerText = new StringBuilder();
			if (bannerLineLength > snaps.length) {
				for (int i = 0; i < bannerLineLength; i++) {
					if (Strings.isNotNullOrEmpty(banners[i])) {
						StringBuilder b = new StringBuilder(banners[i]);
						if (i >= bannerLineLength - snaps.length) {
							int spaceCount = b.toString().toCharArray().length;
							if (spaceCount < maxLineLength)
								for (int j = 0; j < maxLineLength - spaceCount; j++) {
									b.append(" ");
								}
							b.append("\t\t\t").append(snaps[Math.abs(bannerLineLength - i - snaps.length)]);
						}
						bannerText.append(b).append(lineSeparator);
					}
				}
			}
			if (Strings.isNotNullOrEmpty(bannerText.toString())) {
				bannerText.insert(0, lineSeparator);
				bannerText.append(lineSeparator);
				LogThread.op(Colors.random(bannerText.toString()));
			}
		}
	}

	/**
	 * 从配置文件加载
	 * Load from Properties file
	 */
	void buildServeConfigFromProperties() {
		Props.getProperties(Props.getDefaultConfig()).ifPresent(properties -> {
			// port
			String port = properties.getProperty(Consts.PropArgs.port);
			if (ServeConfig.getInstance().getPort() <= 0 && Strings.isNotNullOrEmpty(port))
				if (Strings.isNumber(port))
					ServeConfig.getInstance().setPort(Integer.parseInt(port));
			// ServerName -> ThreadName
			String serverName = properties.getProperty(Consts.PropArgs.serverName);
			if (Strings.isNullOrEmpty(ServeConfig.getInstance().getServerName()) && Strings.isNotNullOrEmpty(serverName))
				ServeConfig.getInstance().setServerName(serverName);
			// InetAddress
			String address = properties.getProperty(Consts.PropArgs.inetAddress);
			if (Strings.isNullOrEmpty(ServeConfig.getInstance().getInetAddress()) && Strings.isNotNullOrEmpty(address))
				ServeConfig.getInstance().setInetAddress(address);
		});
	}

	/**
	 * 命令行启动参数解析
	 * Command line startup parameter resolution
	 *
	 * @param args 参数 args
	 */
	void buildServeConfigFromCommand(String[] args) {
		if (Strings.isNotNullOrEmpty(args)) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				switch (arg) {
					case Consts.CommandArgs.port:
					case Consts.CommandArgs.port0:
						if (Strings.isNumber(args[i + 1])) {
							ServeConfig.getInstance().setPort(Integer.parseInt(args[i + 1]));
							i++;
						} else
							throw new IllegalArgumentException("The start param --port or -p is not a number!");
						break;
					case Consts.CommandArgs.name:
					case Consts.CommandArgs.name0:
						if (Strings.isNotNullOrEmpty(args[i + 1])) {
							ServeConfig.getInstance().setServerName(args[i + 1]);
							i++;
						}
						break;
					case Consts.CommandArgs.address:
					case Consts.CommandArgs.address0:
						if (Strings.isNotNullOrEmpty(args[i + 1])) {
							ServeConfig.getInstance().setInetAddress(args[i + 1]);
							i++;
						}
				}
			}
		}
	}

}
