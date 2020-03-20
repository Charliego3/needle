package io.github.needle.http;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class HttpRequest extends HttpProtocol {

	private HttpHeader header;

	private InputStream body;

	// ContentLength records the length of the associated content.
	// The value -1 indicates that the length is unknown.
	// Values >= 0 indicate that the given number of bytes may
	// be read from Body.
	//
	// For client requests, a value of 0 with a non-nil Body is
	// also treated as unknown.
	private long contentLength = -1;

	// TransferEncoding lists the transfer encodings from outermost to
	// innermost. An empty list denotes the "identity" encoding.
	// TransferEncoding can usually be ignored; chunked encoding is
	// automatically added and removed as necessary when sending and
	// receiving requests.
	private List<String> transferEncoding;

	// Close indicates whether to close the connection after
	// replying to this request (for servers) or after sending this
	// request and reading its response (for clients).
	//
	// For server requests, the HTTP server handles this automatically
	// and this field is not needed by Handlers.
	//
	// For client requests, setting this field prevents re-use of
	// TCP connections between requests to the same hosts, as if
	// Transport.DisableKeepAlives were set.
	private boolean close;

	private String host;
	private String remoteAddr;
	private String path;
	private HttpCookie[] cookies;
	private HttpSession session;

	public String getBodyString() {
		return "";
	}

	public Optional<String> getContentType() {
		return Optional.ofNullable(header.get(MimeType.CONTENT_TYPE));
	}

}
