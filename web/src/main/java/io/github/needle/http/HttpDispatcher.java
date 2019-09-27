package io.github.needle.http;

import io.github.kits.Strings;
import io.github.kits.cache.PatternCache;
import io.github.kits.json.Json;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.server.filter.HttpFilterAdapter;
import io.github.needle.server.router.context.RouterContextFactory;
import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.router.HttpRouter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpDispatcher {

	private final ByteBuffer buffer = ByteBuffer.allocate(1024);

	public void dispatcher(SelectionKey selectionKey) throws IOException {
		SocketChannel channel  = (SocketChannel) selectionKey.channel();
		HttpResponse  response = new HttpResponse(selectionKey);
		String        data     = read(channel);
		if (Strings.isNullOrEmpty(data)) {
			response.badRequest();
			return;
		}
		HttpRequest request = new HttpRequest(channel.getRemoteAddress(), channel.getLocalAddress());
		System.out.println(data);
		analysisMessage(data, request, response);

		Logger.infof("SelectionKeys: {}, SocketChannel: {}", selectionKey.selector().selectedKeys().size(), channel.toString());
		Logger.debugf("HttpRequest: \n{}", Json.toJson(request, true));

		Optional<HttpRouter> router = RouterContextFactory.getRouter(request);

		// 404 路由不匹配
		if (!router.isPresent()) {
			response.notFund();
			return;
		}

		// Filter -> onRequest
		HttpFilterAdapter filterAdapter = new HttpFilterAdapter(request, response);
		boolean           onRequest     = filterAdapter.onRequest();
		Logger.warnf("OnRequest Returned: {}", onRequest);
		if (!onRequest) {
			response.send();
			return;
		}

		// Router -> process
		router.ifPresent(routerHandler ->
				routerHandler.process(request, response));

		// Filter -> onResponse
		filterAdapter.onResponse();

		response.send();
	}

	/**
	 * 解析请求数据, 并封装{@code HttpRequest, HttpResponse}对象
	 *
	 * @param data     请求数据
	 * @param request  HttpRequest
	 * @param response HttpResponse
	 */
	private void analysisMessage(String data, HttpRequest request, HttpResponse response) throws IOException {
		String[] requestDatas = data.split(Consts.CRLF);
		if (requestDatas.length <= 0) {
			response.badRequest();
			return;
		}

		int index = 0;
		// 请求行: 解析Http协议相关参数
		// GET 请求时获取参数, 问号 ? 分隔
		String   httpProtocol = requestDatas[index++];
		if (Strings.isNotNullOrEmpty(httpProtocol)) {
			String[] protocol        = httpProtocol.split(" ");
			String[] pathQueryString = protocol[1].split("\\?");
			request.setPath(pathQueryString[0].trim());
			request.setMethod(HttpMethod.getByString(protocol[0].trim().toUpperCase()));
			request.setVersion(protocol[2].trim());
			if (pathQueryString.length > 1) {
				request.setQueryString(pathQueryString[1].trim());
			}
		}

		// 请求头: Header, 以空行结束
		while (index < requestDatas.length) {
			String header = requestDatas[index++];
			if (Strings.isNullOrEmpty(header))
				break;
			Logger.infof("Header: {}", header);
			String key, value;
			if (header.contains(":")) {
				key = header.substring(0, header.indexOf(":"));
				value = header.substring(header.indexOf(":") + 1).trim();
				request.getHeaders().put(key.trim(), "null".equals(value) ? null : value);
			}
			if (Consts.Http.KEEP_ALIVE.equalsIgnoreCase(request.getConnection())) {
				response.setConnection(Consts.Http.KEEP_ALIVE);
			}
		}

		// 请求正文: Body
		if (Strings.isNotNullOrEmpty(request.getContentType())) {
			if (request.getContentType().contains(Consts.ContentType.APPLICATION_X_WWW_FORM_URLENCODED)) {
				String queryString = requestDatas[index];
				if (Strings.isNotNullOrEmpty(queryString)) {
					request.setQueryString(URLDecoder.decode(queryString, "UTF-8"));
				}
			} else if (request.getContentType().contains(Consts.ContentType.MULTIPART_FORM_DATA)) {
				// multipart/form-data boundary
				String contentType = request.getContentType();
				String boundary = null, endBoundary = null;
				if (Strings.isNotNullOrEmpty(contentType) && contentType.contains("boundary")) {
					String boundaryKey = "boundary=";
					boundary = "--" + contentType.substring(contentType.indexOf(boundaryKey) + boundaryKey.length());
					endBoundary = boundary + "--";
				}

				String key = null, fileName = null;
				while (index < requestDatas.length) {
					String part = requestDatas[index++];
					if (part.equals(endBoundary))
						break;
					// 每个 part 以 boundary 开头
					if (part.equals(boundary)) {
						while (index < requestDatas.length) {
							String singlePart = requestDatas[index++];
							if (Strings.isNullOrEmpty(singlePart))
								break;
							else if (singlePart.startsWith("Content-Disposition")) {
								String  keyFileNameRegex = "^Content-Disposition: form-data; name=\"(\\S+)\"(; filename=\"(.+)\")?$";
								Matcher matcher          = PatternCache.get(keyFileNameRegex).matcher(singlePart);
								if (matcher.matches()) {
									key = matcher.group(1);
									fileName = matcher.group(3);
								}
							} else {
								Logger.infof("Single Part: {}", singlePart);
							}
						}

						// fileName 不为空表示上传文件
						if (Strings.isNotNullOrEmpty(fileName)) {
							String body = "";
							while (index < requestDatas.length) {
								String p = requestDatas[index++];
								if (p.equals(boundary) || p.equals(endBoundary)) {
									index--;
									break;
								}
								if (Strings.isNullOrEmpty(body))
									body += new String(p.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
								else
									body += new String((Consts.CRLF + p).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
							}
							if (Strings.isNotNullOrEmpty(body)) {
								System.out.println(body);
								File file = Files.createFile(Paths.get(System.currentTimeMillis() + "_" + fileName)).toFile();
								writeBytesToFile(body.getBytes(StandardCharsets.ISO_8859_1), file.getAbsolutePath());
								System.out.println(file);
							}
						} else {
							String value = requestDatas[index++];
							if (Strings.isNotNullOrEmpty(key, value))
								request.getParameters().put(key, value);
						}
					}
				}
			}
		}


//		// ==============
//		BufferedReader reader = new BufferedReader(new StringReader(data));
//		String line;
//		// 请求行: 解析Http协议相关参数
//		if (StringKit.isNotNullOrEmpty((line = reader.readLine()))) {
//			String[] protocol        = line.split(" ");
//			String[] pathQueryString = protocol[1].split("\\?");
//			request.setPath(pathQueryString[0].trim());
//			request.setMethod(HttpMethod.getByString(protocol[0].trim().toUpperCase()));
//			request.setVersion(protocol[2].trim());
//			if (pathQueryString.length > 1) {
//				request.setQueryString(pathQueryString[1].trim());
//			}
//		}
//
//		// 解析请求头, 以空行结束
//		while (StringKit.isNotNullOrEmpty(line = reader.readLine())) {
//			Logger.infof("Header: {}", line);
//			String key, value;
//			if (line.contains(":")) {
//				key = line.substring(0, line.indexOf(":"));
//				value = line.substring(line.indexOf(":") + 1);
//				request.getHeader().put(key.trim(), value.trim());
//			}
//			if (Consts.Http.KEEP_ALIVE.equalsIgnoreCase(request.getConnection())) {
//				response.setConnection(Consts.Http.KEEP_ALIVE);
//			}
//		}
//
//		// multipart/form-data boundary
////		String contentType = request.getContentType();
////		String boundary = null;
//		if (StringKit.isNotNullOrEmpty(contentType) && contentType.contains("boundary")) {
//			String boundaryKey = "boundary=";
//			boundary = "--" + contentType.substring(contentType.indexOf(boundaryKey) + boundaryKey.length());
//		}
//
//		// 请求主体
//		if (StringKit.isNotNullOrEmpty(request.getContentType())) {
//			if (request.getContentType().contains(Consts.ContentType.APPLICATION_X_WWW_FORM_URLENCODED)) {
//				String queryString = reader.readLine();
//				if (StringKit.isNotNullOrEmpty(queryString)) {
//					request.setQueryString(URLDecoder.decode(queryString, "UTF-8"));
//				}
//			} else if (request.getContentType().contains(Consts.ContentType.MULTIPART_FORM_DATA)) {
//				String key = null, fileName = null;
//				while (Objects.nonNull(line = reader.readLine())) {
//					if (StringKit.isNotNullOrEmpty(boundary) && line.equals(boundary)) {
//						String disposition = reader.readLine();
//						String keyFileNameRegex = "^Content-Disposition: form-data; name=\"(\\S+)\"(; filename=\"(\\S+)\")?";
//						Matcher matcher = PatternCache.get(keyFileNameRegex).matcher(disposition);
//						if (matcher.matches()) {
//							key = matcher.group(1);
//							fileName = matcher.group(3);
//						}
//
//						if (StringKit.isNotNullOrEmpty(fileName)) {
//							StringBuilder body = new StringBuilder();
//							String bodyStr = reader.readLine();
//							if (StringKit.isNotNullOrEmpty(bodyStr) && bodyStr.startsWith(Consts.Http.CONTENT_TYPE)) {
//
//							} else
//								body.append(new String(bodyStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
//							while (true) {
//								bodyStr = reader.readLine();
//								if (Objects.isNull(bodyStr))
//									break;
//								if (bodyStr.startsWith(boundary) && bodyStr.endsWith("-"))
//									continue;
//								body.append(new String(bodyStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
//								if (bodyStr.isEmpty() || bodyStr.equals("\u001A"))
//									body.append("\n");
//							}
////							FileChannel channel = FileChannel.open(Paths.get(fileName));
////							channel.write()
//							System.out.println(body.toString());
//							File file = Files.createFile(Paths.get("fileName_" + System.currentTimeMillis() + ".png")).toFile();
//							writeBytesToFile(body.toString().getBytes(StandardCharsets.ISO_8859_1), file.getAbsolutePath());
//							System.out.println(file);
//						} else {
//							reader.readLine();
//							String value = reader.readLine();
//							if (StringKit.isNotNullOrEmpty(key, value))
//								request.getParamters().put(key, value);
//						}
//					}
//				}
//			}
//		}
	}

	//Since JDK 7 - try resources
	private static void writeBytesToFile(byte[] bFile, String fileDest) {
		try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest);
			 BufferedOutputStream bf = new BufferedOutputStream(fileOuputStream)) {
			bf.write(bFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从请求中读取数据(Header、body...)
	 *
//	 * @param key seleectionKey -> socketChannel
	 * @return conetnt
	 * @throws IOException read exception
	 */
	private String read(SocketChannel channel) throws IOException {
		try {
			buffer.clear();
			int           numRead   = -1;
			StringBuilder data      = new StringBuilder();
			while ((numRead = channel.read(buffer)) > 0) {
				data.append(new String(buffer.array(), 0, numRead, StandardCharsets.UTF_8));
				buffer.clear();
			}
			return data.toString();
		} catch (IOException e) {
			Logger.info("closed by exception" + channel);
			throw e;
		}
	}

}
