# HTTP 实现

```java
public class HttpServerTest extends TestCase {

	public void testListenAndServe() {
		try {
			Logger.debug("Listen serve the [:2048]");
			HttpServer.newInstance()
					  .filter(new CustomerFilter())
					  .requestFilter(this::reqFilter)
					  .responseFilter(this::resFilter)
					  .handle(HttpMethod.GET, "/test", this::tHandler)
					  .handle(HttpMethod.POST, "/test1", this::t1Handler)
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

	public void tHandler(HttpRequest request, HttpResponse response) {
		Logger.debug("This is /test handler....");
	}

	public void t1Handler(HttpRequest request, HttpResponse response) {
		Logger.debug("This is /test1 handler....");
	}

	public void shutdown() {
		Logger.debug("Server is shutting down.....");
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

}
```