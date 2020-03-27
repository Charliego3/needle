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
					  .handle("/test", this::tHandler, HttpMethod.GET, HttpMethod.POST)
					  .handle("/test1", this::t1Handler)
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
