import io.github.kits.enums.LogLevel;
import io.github.kits.log.LogBody;
import io.github.kits.log.Logger;
import io.github.kits.log.LogBuilder;
import junit.framework.TestCase;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class LogTest extends TestCase {

	public void testBuildErrorMsg() {
		String  log     = LogBuilder.buildMsg(getLogBody(LogLevel.ERROR));
		System.out.println(log);
	}

	public void testBuildInfoMsg() {
		String  log     = LogBuilder.buildMsg(getLogBody(LogLevel.INFO));
		System.out.println(log);
	}

	public void testBuildWarnMsg() {
		String  log     = LogBuilder.buildMsg(getLogBody(LogLevel.WARN));
		System.out.println(log);
	}

	public void testBuildDebugMsg() {
		String  log     = LogBuilder.buildMsg(getLogBody(LogLevel.DEBUG));
		System.out.println(log);
	}

	private LogBody getLogBody(LogLevel logLevelEnum) {
		return LogBody.newInstance().setLogLevel(logLevelEnum).setMessage("this is {}.{} method.").setException(new NullPointerException("thi is Null")).setArgs(LogTest.class.getSimpleName(), "testBuildMsg");
	}

	public void testInfo() {
		Thread.currentThread().setName("WINTER-TEST");
//		Logger.setLevel(LogLevelEnum.DEBUG);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 2000; i++) {
			Logger.info("hahahahhaha   " + (i + 1));
//			Logger.warn("hahahahhaha   " + (i + 1));
//			Logger.debug("hahahahhaha   " + (i + 1));
//			Logger.error(new RuntimeException("hahaha 出错了!!"));
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	public void testLog() {
		Logger.info("hahahahhaha   ");
		Logger.warn("hahahahhaha   ");
		Logger.debug("hahahahhaha   ");
		Logger.error("hahahahhaha   ");
		Logger.error(new RuntimeException("hahaha 出错了!!"));
		Logger.error("this is testLog", new RuntimeException("hahaha 出错了!!"));
		Logger.errorf("this is {} over", new RuntimeException("hahaha 出错了!!"), "testLog...");
		Logger.errorf("this is {} over", "testLog...");
	}

}
