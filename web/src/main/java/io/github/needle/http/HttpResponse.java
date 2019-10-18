package io.github.needle.http;

import io.github.kits.DateTimes;
import io.github.kits.Strings;
import io.github.kits.json.Json;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.http.protocol.HttpContentType;
import io.github.needle.server.ServeConfig;
import io.github.needle.http.enums.HttpStatus;
import io.github.needle.http.protocol.HttpHeader;
import io.github.needle.http.protocol.HttpProtocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpResponse extends HttpProtocol {

	private HttpHeader   header;
	private SelectionKey key;
	private int          status;
	private String       statusCode;
	private ByteBuffer   buffer = ByteBuffer.allocate(1024);

	public HttpResponse(SelectionKey selectionKey) {
		this.header = new HttpHeader();
		this.key = selectionKey;
		this.status = HttpStatus.OK.getStatus();
		this.statusCode = HttpStatus.OK.name();

		initHeader(HttpContentType.TEXT);
	}

	public void initHeader(HttpContentType contentType) {
		String serverVersion = ServeConfig.getInstance().getServerVersion();
		if (serverVersion.contains("-")) {
			serverVersion = serverVersion.substring(0, serverVersion.indexOf("-"));
		}
		this.header.put("Server", "Winter " + (Strings.isNullOrEmpty(serverVersion) ? "" : serverVersion));
		this.header.put("Date", DateTimes.nowGMT());
		setContentType(contentType);
	}

	public void setContentType(HttpContentType contentType) {
		this.header.put(Consts.Http.CONTENT_TYPE, contentType.getHttpContentType());
	}

	public void setConnection(String connection) {
		this.header.put(Consts.Http.CONNECTION, connection);
	}

	public void badRequest() {
		setContentType(HttpContentType.TEXT);
		setStatus(HttpStatus.BadRequest);
		write("Bad Request!");
		send();
	}

	public void notFund() {
		setContentType(HttpContentType.TEXT);
		setStatus(HttpStatus.NotFund);
		write("Router Not Fund!");
		send();
	}

	public void json(Object object) {
		setContentType(HttpContentType.JSON);
		write(Json.toJson(object));
	}

	public void xml(String xml) {
		setContentType(HttpContentType.XML);
		write(xml);
	}

	public void gif(byte[] bytes) {
		setContentType(HttpContentType.IMAGE_GIF);
		write(new String(bytes));
	}

	public void jpg(byte[] bytes) {
		setContentType(HttpContentType.IMAGE_JPG);
		write(new String(bytes));
	}

	public void png(byte[] bytes) {
		setContentType(HttpContentType.IMAGE_PNG);
		write(new String(bytes));
	}

	public void write(String msg) {
		buffer.clear();
		String res = statusLine();
		res += headerString() + msg;
		buffer.put(res.getBytes());
	}

	public void write(String msg, HttpContentType contentType) {
		setContentType(contentType);
		write(msg);
	}

	public void send() {
		try {
			if (buffer.hasArray()) {
				buffer.flip();
				key.channel().register(key.selector(), SelectionKey.OP_WRITE);
				key.attach(buffer);
			}
		} catch (ClosedChannelException e) {
			closeChannel(key, e);
		}
	}

	public HttpHeader getHeader() {
		return header;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatus(HttpStatus status) {
		setStatus(status.getStatus());
		setStatusCode(status.getStatusCode());
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	private String statusLine() {
		return getVersion() + " " + status + " " + statusCode + Consts.CRLF;
	}

	private String headerString() {
		StringBuilder header = new StringBuilder();
		getHeader().forEach((k, v) ->
				header.append(k).append(": ").append(v).append(Consts.CRLF)
		);
		header.append(Consts.CRLF);
		return header.toString();
	}

	private void closeChannel(SelectionKey key, ClosedChannelException e) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		key.cancel();
		try {
			socketChannel.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Logger.error("SocketChannel is closed.", e);
	}

}
