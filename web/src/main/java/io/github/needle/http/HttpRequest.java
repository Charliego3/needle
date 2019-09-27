package io.github.needle.http;

import io.github.kits.Strings;
import io.github.kits.json.Json;
import io.github.needle.constants.Consts;
import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.protocol.HttpHeader;
import io.github.needle.http.protocol.HttpProtocol;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpRequest extends HttpProtocol {

	private HttpHeader          headers;
	private String              path;
	private Map<String, String> parameters;
	private InetSocketAddress   remoteAddress;
	private InetSocketAddress   localAddress;
	private Part[]              parts;

	public HttpRequest() {
		super(HttpMethod.GET, VERSION_1_1);
		headers = new HttpHeader();
		parameters = new HashMap<>();
	}

	public HttpRequest(SocketAddress remoteAddress, SocketAddress localAddress) {
		this();
		this.remoteAddress = (InetSocketAddress) remoteAddress;
		this.localAddress = (InetSocketAddress) localAddress;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public Optional<Integer> getParameterAsInt(String key) {
		String iVal = parameters.get(key);
		return Optional.ofNullable(isNullNumber(iVal) ? null : Integer.valueOf(iVal));
	}

	public Optional<Short> getParameterAsShort(String key) {
		String sVal = parameters.get(key);
		return Optional.ofNullable(isNullNumber(sVal) ? null : Short.valueOf(sVal));
	}

	public Optional<Long> getParameterAsLong(String key) {
		String lval = parameters.get(key);
		return Optional.ofNullable(isNullNumber(lval) ? null : Long.valueOf(lval));
	}

	public Optional<Float> getParameterAsFloat(String key) {
		String fval = parameters.get(key);
		return Optional.ofNullable(isNullNumber(fval) ? null : Float.valueOf(fval));
	}

	public Optional<Double> getParameterAsDouble(String key) {
		String dval = parameters.get(key);
		return Optional.ofNullable(isNullNumber(dval) ? null : Double.valueOf(dval));
	}

	public Optional<Boolean> getParameterAsBoolean(String key) {
		String bval = parameters.get(key);
		return Optional.ofNullable(Strings.isNullOrEmpty(bval)
				|| !Strings.isBoolean(bval) ? null : Boolean.valueOf(bval));
	}

	public Optional<String> getParameterAsString(String key) {
		return Optional.ofNullable(parameters.get(key));
	}

	public String getPath() {
		return path;
	}

	HttpRequest setPath(String path) {
		this.path = path;
		return this;
	}

	public Part[] getParts() {
		return parts;
	}

	public Optional<Part> getPart(int index) {
		return Optional.ofNullable(parts[index]);
	}

	void addPart(Part part) {

	}

	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	public String getContentLength() {
		return headers.get(Consts.Http.CONTENT_LENGTH);
	}

	public HttpHeader getHeaders() {
		return headers;
	}

	public String getContentType() {
		return headers.get(Consts.Http.CONTENT_TYPE);
	}

	public String getUserAgent() {
		return headers.get(Consts.Http.USER_AGENT);
	}

	public String getOrigin() {
		return headers.get(Consts.Http.ORIGIN);
	}

	public String getAccept() {
		return headers.get(Consts.Http.ACCEPT);
	}

	public String getAcceptEncoding() {
		return headers.get(Consts.Http.ACCEPT_ENCODING);
	}

	public String getAcceptLanguage() {
		return headers.get(Consts.Http.ACCEPT_LANGUAGE);
	}

	public String getConnection() {
		return headers.get(Consts.Http.CONNECTION);
	}

	public void setQueryString(String queryString) {
		if (Strings.isNotNullOrEmpty(queryString)) {
			super.setQueryString(queryString);
			String[] strs = queryString.split("&");
			for (String str : strs) {
				String[] params = str.split("=");
				if (params.length == 2) {
					String key   = params[0];
					String value = params[1];
					parameters.put(key, value);
				}
			}
		}
	}

	private boolean isNullNumber(String val) {
		return Strings.isNullOrEmpty(val) || !Strings.isNumber(val, true);
	}

	@Override
	public String toString() {
		return Json.toJson(this);
	}

}
