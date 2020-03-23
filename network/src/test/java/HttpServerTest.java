import io.github.kits.log.Logger;
import io.github.needle.http.filter.HttpFilter;
import io.github.needle.http.HttpMethod;
import io.github.needle.http.HttpServer;
import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;
import junit.framework.TestCase;

public class HttpServerTest extends TestCase {

	public void testListenAndServe() {
		try {
			Logger.debug("Listen serve the [:2048]");
			HttpServer.newInstance()
					  .filter(new CustomerFilter())
					  .requestFilter(this::reqFilter)
					  .responseFilter(this::resFilter)
					  .handle(HttpMethod.GET, "/test", this::tHandler)
					  .handle(HttpMethod.POST, "/test", this::t1Handler)
					  .handle(HttpMethod.OPTIONS, "/test", this::t1Handler)
					  .handle(HttpMethod.CONNECT, "/test", this::t1Handler)
					  .handle(HttpMethod.DELETE, "/test", this::t1Handler)
					  .handle(HttpMethod.HEAD, "/test", this::t1Handler)
					  .handle(HttpMethod.PUT, "/test", this::t1Handler)
					  .handle(HttpMethod.TRACE, "/test", this::t1Handler)
					  .handle(HttpMethod.PATCH, "/test", this::t1Handler)
//					  .handle("/test11", this::t1Handler)
//					  .handle("/test12", this::t1Handler)
//					  .handle("/test13", this::t1Handler)
//					  .handle("/test14", this::t1Handler)
//					  .handle("/test15", this::t1Handler)
//					  .handle("/test16", this::t1Handler)
//					  .handle("/test17", this::t1Handler)
//					  .handle("/test18", this::t1Handler)
//					  .handle("/test19", this::t1Handler)
//					  .handle("/test20", this::t1Handler)
//					  .handle("/test21", this::t1Handler)
//					  .handle("/test22", this::t1Handler)
//					  .handle("/test23", this::t1Handler)
//					  .handle("/test24", this::t1Handler)
//					  .handle("/test25", this::t1Handler)
//					  .handle("/test26", this::t1Handler)
//					  .handle("/test27", this::t1Handler)
//					  .handle("/test28", this::t1Handler)
//					  .handle("/test29", this::t1Handler)
//					  .handle("/test30", this::t1Handler)
//					  .handle("/test31", this::t1Handler)
//					  .handle("/test32", this::t1Handler)
//					  .handle("/test33", this::t1Handler)
//					  .handle("/test34", this::t1Handler)
//					  .handle("/test35", this::t1Handler)
//					  .handle("/test36", this::t1Handler)
//					  .handle("/test37", this::t1Handler)
//					  .handle("/test38", this::t1Handler)
//					  .handle("/test39", this::t1Handler)
//					  .handle("/test40", this::t1Handler)
//					  .handle("/test41", this::t1Handler)
//					  .handle("/test42", this::t1Handler)
//					  .handle("/test43", this::t1Handler)
//					  .handle("/test44", this::t1Handler)
//					  .handle("/test45", this::t1Handler)
//					  .handle("/test46", this::t1Handler)
//					  .handle("/test47", this::t1Handler)
//					  .handle("/test48", this::t1Handler)
//					  .handle("/test49", this::t1Handler)
//					  .handle("/test50", this::t1Handler)
//					  .handle("/test51", this::t1Handler)
//					  .handle("/test52", this::t1Handler)
//					  .handle("/test53", this::t1Handler)
//					  .handle("/test54", this::t1Handler)
//					  .handle("/test55", this::t1Handler)
//					  .handle("/test56", this::t1Handler)
//					  .handle("/test57", this::t1Handler)
//					  .handle("/test58", this::t1Handler)
//					  .handle("/test59", this::t1Handler)
//					  .handle("/test60", this::t1Handler)
//					  .handle("/test61", this::t1Handler)
//					  .handle("/test62", this::t1Handler)
//					  .handle("/test63", this::t1Handler)
//					  .handle("/test64", this::t1Handler)
//					  .handle("/test65", this::t1Handler)
//					  .handle("/test66", this::t1Handler)
//					  .handle("/test67", this::t1Handler)
//					  .handle("/test68", this::t1Handler)
//					  .handle("/test69", this::t1Handler)
//					  .handle("/test70", this::t1Handler)
//					  .handle("/test71", this::t1Handler)
//					  .handle("/test72", this::t1Handler)
//					  .handle("/test73", this::t1Handler)
//					  .handle("/test74", this::t1Handler)
//					  .handle("/test75", this::t1Handler)
//					  .handle("/test76", this::t1Handler)
//					  .handle("/test77", this::t1Handler)
//					  .handle("/test78", this::t1Handler)
//					  .handle("/test79", this::t1Handler)
//					  .handle("/test80", this::t1Handler)
//					  .handle("/test81", this::t1Handler)
//					  .handle("/test82", this::t1Handler)
//					  .handle("/test83", this::t1Handler)
//					  .handle("/test84", this::t1Handler)
//					  .handle("/test85", this::t1Handler)
//					  .handle("/test86", this::t1Handler)
//					  .handle("/test87", this::t1Handler)
//					  .handle("/test88", this::t1Handler)
//					  .handle("/test89", this::t1Handler)
//					  .handle("/test90", this::t1Handler)
//					  .handle("/test91", this::t1Handler)
//					  .handle("/test92", this::t1Handler)
//					  .handle("/test93", this::t1Handler)
//					  .handle("/test94", this::t1Handler)
//					  .handle("/test95", this::t1Handler)
//					  .handle("/test96", this::t1Handler)
//					  .handle("/test97", this::t1Handler)
//					  .handle("/test98", this::t1Handler)
//					  .handle("/test99", this::t1Handler)
//					  .handle("/test100", this::t1Handler)
//					  .handle("/test101", this::t1Handler)
//					  .handle("/test102", this::t1Handler)
//					  .handle("/test103", this::t1Handler)
//					  .handle("/test104", this::t1Handler)
//					  .handle("/test105", this::t1Handler)
//					  .handle("/test106", this::t1Handler)
//					  .handle("/test107", this::t1Handler)
//					  .handle("/test108", this::t1Handler)
//					  .handle("/test109", this::t1Handler)
//					  .handle("/test110", this::t1Handler)
//					  .handle("/test111", this::t1Handler)
//					  .handle("/test112", this::t1Handler)
//					  .handle("/test113", this::t1Handler)
//					  .handle("/test114", this::t1Handler)
//					  .handle("/test115", this::t1Handler)
//					  .handle("/test116", this::t1Handler)
//					  .handle("/test117", this::t1Handler)
//					  .handle("/test118", this::t1Handler)
//					  .handle("/test119", this::t1Handler)
//					  .handle("/test120", this::t1Handler)
//					  .handle("/test121", this::t1Handler)
//					  .handle("/test122", this::t1Handler)
//					  .handle("/test123", this::t1Handler)
//					  .handle("/test124", this::t1Handler)
//					  .handle("/test125", this::t1Handler)
//					  .handle("/test126", this::t1Handler)
//					  .handle("/test127", this::t1Handler)
//					  .handle("/test128", this::t1Handler)
//					  .handle("/test129", this::t1Handler)
//					  .handle("/test130", this::t1Handler)
//					  .handle("/test131", this::t1Handler)
//					  .handle("/test132", this::t1Handler)
//					  .handle("/test133", this::t1Handler)
//					  .handle("/test134", this::t1Handler)
//					  .handle("/test135", this::t1Handler)
//					  .handle("/test136", this::t1Handler)
//					  .handle("/test137", this::t1Handler)
//					  .handle("/test138", this::t1Handler)
//					  .handle("/test139", this::t1Handler)
//					  .handle("/test140", this::t1Handler)
//					  .handle("/test141", this::t1Handler)
//					  .handle("/test142", this::t1Handler)
//					  .handle("/test143", this::t1Handler)
//					  .handle("/test144", this::t1Handler)
//					  .handle("/test145", this::t1Handler)
//					  .handle("/test146", this::t1Handler)
//					  .handle("/test147", this::t1Handler)
//					  .handle("/test148", this::t1Handler)
//					  .handle("/test149", this::t1Handler)
					  .shuttingDown(this::shutdown)
					  .listenAndServe(":2048");
		} catch (Exception err) {
			Logger.error(err);
		}
	}

	public boolean resFilter(HttpRequest request, HttpResponse response) {
		Logger.debug("Response Filter.....");
		return true;
	}

	public boolean reqFilter(HttpRequest request, HttpResponse response) {
		Logger.debug("Request Filter.....");
		return true;
	}

	public void t1Handler(HttpRequest request, HttpResponse response) {
		Logger.debug("This is /test1 handler....");
	}

	public void shutdown() {
		Logger.debug("Server is shutting down.....");
	}

	public void tHandler(HttpRequest request, HttpResponse response) {
		Logger.debug("This is /test handler....");
	}

	public static class CustomerFilter implements HttpFilter {

		@Override
		public boolean onRequest(HttpRequest request, HttpResponse response) {
			Logger.info("CustomerFilter onRequest.....");
			return true;
		}

		@Override
		public boolean onResponse(HttpRequest request, HttpResponse response) {
			Logger.info("CustomerFilter onResponse.....");
			return true;
		}

	}

	public void testListenAndServe1() {
		try {
			Logger.debug("Listen serve the [192.168.123.105:2048]");
			HttpServer.newInstance().handle("/test", (request, response) -> {

			}).listenAndServe("192.168.123.105", 2048);
		} catch (Exception err) {
			Logger.error(err);
		}
	}

	public static void main(String[] args) {
		for (int i = 1; i < 150; i++) {
			System.out.println(".handle(\"/test" + i + "\", this::t1Handler)");
		}
	}

}
