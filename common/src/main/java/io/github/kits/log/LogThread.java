package io.github.kits.log;

import io.github.kits.Colors;
import io.github.kits.DateTimes;
import io.github.kits.Envs;
import io.github.kits.Props;
import io.github.kits.Strings;
import io.github.kits.enums.Prop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class LogThread implements Runnable {

	private static OutputStream[] outputStreams;
	private static final LinkedBlockingDeque<LogBody> queue;
	private static final LogThread logThread;
	private static Thread mainThread;
	private static String logPath;
	private static final String type;

	static {
		logThread = new LogThread();
		queue = new LinkedBlockingDeque<>();
		logPath = Props.getString(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), "path").orElse("{WORKDIR}/sysout.{D}.log");
		type = Props.getString(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), "type").orElse("STDOUT");
		setOutputStreams(getOutputStreams());
	}

	private static void setOutputStreams(OutputStream[] outputStream) {
		outputStreams = outputStream;
	}

	public static void addBody(LogBody logBody) {
		queue.add(logBody);
	}

	@Override
	public void run() {
		try {
			Thread mainThread = Envs.getMainThread();
			while (true) {
				try {
					//优化日志输出事件
					if (queue.isEmpty()) {
						continue;
					}
					LogBody logBody = queue.take();
					print(logBody);
					//如果主线程结束,则日志线程也退出
					if (mainThread == null && queue.isEmpty()) {
						Envs.sleep(1000);
						break;
					}
				} catch (Exception e) {
					try {
						op(LogBuilder.buildMsg(LogBody.error().setException(e)));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		} finally {
			try {
				if (Objects.nonNull(outputStreams) && outputStreams.length > 0) {
					for (OutputStream outputStream : outputStreams) {
						if (outputStream != null) {
							outputStream.close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static void print(LogBody logBody) throws IOException {
		String message = LogBuilder.buildMsg(logBody);
		if (Strings.isNullOrEmpty(message)) {
			return;
		}
		op(message);
	}

	public static void op(String message) throws IOException {
		if (outputStreams != null && outputStreams.length > 0) {
			for (OutputStream outputStream : outputStreams) {
				if (outputStream != null) {
					if (outputStream != System.out) {
						//文件写入剔除着色部分
						String fileMessage = Strings.replace(message, "", "\033\\[(1;)?\\d{1,2}m");
						output(outputStream, fileMessage);
					} else {
						output(outputStream, message);
					}
				}
			}
		}
	}

	/**
	 * log output to file or console
	 *
	 * @param outputStream stream
	 * @param message      log message
	 * @throws IOException IoException
	 */
	private static void output(OutputStream outputStream, String message) throws IOException {
		outputStream.write(message.getBytes());
		outputStream.flush();
	}

	private static OutputStream[] getOutputStreams() {
		String[] logTypes = type.split(",");
		OutputStream[] outputStreams = new OutputStream[logTypes.length];
		for (int i = 0; i < logTypes.length; i++) {
			String logType = logTypes[i].trim();
			switch (logType) {
				case "STDOUT":
					outputStreams[i] = System.out;
					break;
				case "FILE":
					File logFile = null;
					try {
						String logDirPath = Paths.get("logs").toAbsolutePath().toString();
						logPath = logPath.replaceFirst("\\{WORKDIR}", logDirPath);
						String dir = logPath.substring(0, logPath.lastIndexOf("/"));
						File logDir = new File(dir);
						if (!logDir.exists()) logDir.mkdirs();
						String now = DateTimes.now(DateTimes.YYYYMMDD);
						logPath = Strings.replace(logPath, now, "\\{D\\}");
						logFile = new File(logPath);
						outputStreams[i] = new FileOutputStream(logFile, true);
					} catch (FileNotFoundException e) {
						String message = "[ " + logFile + " ]";
						CompletableFuture.runAsync(() -> Logger.warn(Colors.toRedBold(message) + " file is not found."));
					}
					break;
				default:
					break;
			}
		}
		return outputStreams;
	}

	/**
	 * start this thread do output log
	 */
	static void start() {
		if (Objects.isNull(mainThread)) {
			synchronized (LogThread.class) {
				if (Objects.isNull(mainThread)) {
					mainThread = new Thread(logThread);
					mainThread.setDaemon(true);
					mainThread.setName("NEEDLE-LOGGER");
					mainThread.start();
				}
			}
		}
	}

}
