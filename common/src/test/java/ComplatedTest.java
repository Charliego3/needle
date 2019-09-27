import io.github.kits.Envs;
import io.github.kits.log.Logger;
import junit.framework.TestCase;

import java.util.concurrent.CompletableFuture;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ComplatedTest extends TestCase {

	public void testCompletedFutureExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
		Logger.info(cf.isDone());
		Logger.info(cf.getNow(null));
	}

	public void testAsyncExample() {
		CompletableFuture cf = CompletableFuture.runAsync(() -> {
			logThreadIsDaemon();
			randomSleep();
		});
		Logger.info(cf.isDone());
		randomSleep();
		Logger.info(cf.isDone());
	}

	public void testThenApplyExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> {
			logThreadIsDaemon();
			return s.toUpperCase();
		});
		Logger.info(cf.getNow(null));
	}

	public void testThenApplyAsyncExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
//			logThreadIsDaemon();
			randomSleep();
			return s.toUpperCase();
		}).whenComplete((m, e) -> {
//			Logger.warn(m);
//			Logger.error(e);
		});
		Logger.warnf("cf.IsDone : {}", cf.isDone());
		Logger.warnf("cf.IsDone : {}", cf.isCompletedExceptionally());
		Logger.infof("getNow = {}", cf.getNow(null));
		Logger.info(cf.getNow(null));
		Logger.infof("getMessage = {}", cf.join());
	}

	private void randomSleep() {
		Envs.sleep(1000);
	}

	private void logThreadIsDaemon() {
		Logger.infof("This Thread is Daemon?: {}", Thread.currentThread().isDaemon());
	}

}
