package io.github.needle;

import io.github.kits.log.Logger;

import java.io.IOException;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Application {

	public static void main(String[] args) throws IOException {
		Needle.me().get("/main/get1", (request, response) -> {
			Logger.infof("Request Parameters: {}", request.getParameters());
			Logger.infof("Request Headers: {}", request.getHeaders());
			Logger.infof("Response Headers: {}", response.getHeader());
			response.json("{\"isSucc\": true, \"message\": \"" + request.getPath() + " execute success\"}");
		}).post("/main/post1", (request, response) -> {
			Logger.infof("Request Parameters: {}", request.getParameters());
			Logger.infof("Request Headers: {}", request.getHeaders());
			Logger.infof("Response Headers: {}", response.getHeader());
			response.json("{\"isSucc\": true, \"message\": \"" + request.getPath() + " execute success\"}");
		}).listen(9001).start(Application.class, args);
	}

}
