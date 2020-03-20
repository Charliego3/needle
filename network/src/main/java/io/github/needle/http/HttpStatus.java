package io.github.needle.http;

import java.util.HashMap;
import java.util.Map;

public class HttpStatus {

	public static final int Continue           = 100; // RFC 7231, 6.2.1
	public static final int SwitchingProtocols = 101; // RFC 7231, 6.2.2
	public static final int Processing         = 102; // RFC 2518, 10.1
	public static final int EarlyHints         = 103; // RFC 8297

	public static final int OK                   = 200; // RFC 7231, 6.3.1
	public static final int Created              = 201; // RFC 7231, 6.3.2
	public static final int Accepted             = 202; // RFC 7231, 6.3.3
	public static final int NonAuthoritativeInfo = 203; // RFC 7231, 6.3.4
	public static final int NoContent            = 204; // RFC 7231, 6.3.5
	public static final int ResetContent         = 205; // RFC 7231, 6.3.6
	public static final int PartialContent       = 206; // RFC 7233, 4.1
	public static final int MultiStatus          = 207; // RFC 4918, 11.1
	public static final int AlreadyReported      = 208; // RFC 5842, 7.1
	public static final int IMUsed               = 226; // RFC 3229, 10.4.1

	public static final int MultipleChoices   = 300; // RFC 7231, 6.4.1
	public static final int MovedPermanently  = 301; // RFC 7231, 6.4.2
	public static final int Found             = 302; // RFC 7231, 6.4.3
	public static final int SeeOther          = 303; // RFC 7231, 6.4.4
	public static final int NotModified        = 304; // RFC 7232, 4.1
	public static final int UseProxy          = 305; // RFC 7231, 6.4.5
	public static final int TemporaryRedirect = 307; // RFC 7231, 6.4.7
	public static final int PermanentRedirect = 308; // RFC 7538, 3
	// _ = 306; // RFC 7231, 6.4.6 (Unused)

	public static final int BadRequest                   = 400; // RFC 7231, 6.5.1
	public static final int Unauthorized                 = 401; // RFC 7235, 3.1
	public static final int PaymentRequired              = 402; // RFC 7231, 6.5.2
	public static final int Forbidden                    = 403; // RFC 7231, 6.5.3
	public static final int NotFound                     = 404; // RFC 7231, 6.5.4
	public static final int MethodNotAllowed             = 405; // RFC 7231, 6.5.5
	public static final int NotAcceptable                = 406; // RFC 7231, 6.5.6
	public static final int ProxyAuthRequired            = 407; // RFC 7235, 3.2
	public static final int RequestTimeout               = 408; // RFC 7231, 6.5.7
	public static final int Conflict                      = 409; // RFC 7231, 6.5.8
	public static final int Gone                         = 410; // RFC 7231, 6.5.9
	public static final int LengthRequired               = 411; // RFC 7231, 6.5.10
	public static final int PreconditionFailed           = 412; // RFC 7232, 4.2
	public static final int RequestEntityTooLarge        = 413; // RFC 7231, 6.5.11
	public static final int RequestURITooLong            = 414; // RFC 7231, 6.5.12
	public static final int UnsupportedMediaType         = 415; // RFC 7231, 6.5.13
	public static final int RequestedRangeNotSatisfiable  = 416; // RFC 7233, 4.4
	public static final int ExpectationFailed            = 417; // RFC 7231, 6.5.14
	public static final int Teapot                       = 418; // RFC 7168, 2.3.3
	public static final int MisdirectedRequest           = 421; // RFC 7540, 9.1.2
	public static final int UnprocessableEntity          = 422; // RFC 4918, 11.2
	public static final int Locked                       = 423; // RFC 4918, 11.3
	public static final int FailedDependency             = 424; // RFC 4918, 11.4
	public static final int TooEarly                     = 425; // RFC 8470, 5.2.
	public static final int UpgradeRequired              = 426; // RFC 7231, 6.5.15
	public static final int PreconditionRequired         = 428; // RFC 6585, 3
	public static final int TooManyRequests              = 429; // RFC 6585, 4
	public static final int RequestHeaderFieldsTooLarge  = 431; // RFC 6585, 5
	public static final int UnavailableForLegalReasons   = 451; // RFC 7725, 3

	public static final int InternalServerError           = 500; // RFC 7231, 6.6.1
	public static final int NotImplemented                = 501; // RFC 7231, 6.6.2
	public static final int BadGateway                    = 502; // RFC 7231, 6.6.3
	public static final int ServiceUnavailable            = 503; // RFC 7231, 6.6.4
	public static final int GatewayTimeout                = 504; // RFC 7231, 6.6.5
	public static final int HTTPVersionNotSupported       = 505; // RFC 7231, 6.6.6
	public static final int VariantAlsoNegotiates         = 506; // RFC 2295, 8.1
	public static final int InsufficientStorage            = 507; // RFC 4918, 11.5
	public static final int LoopDetected                  = 508; // RFC 5842, 7.2
	public static final int NotExtended                   = 510; // RFC 2774, 7
	public static final int NetworkAuthenticationRequired = 511; // RFC 6585, 6

	private static final Map<Integer, String> statusTextMap = new HashMap<Integer, String>() {{
		put(Continue, "Continue");
		put(SwitchingProtocols, "Switching Protocols");
		put(Processing, "Processing");
		put(EarlyHints, "Early Hints");

		put(OK, "OK");
		put(Created, "Created");
		put(Accepted, "Accepted");
		put(NonAuthoritativeInfo, "Non-Authoritative Information");
		put(NoContent, "No Content");
		put(ResetContent, "Reset Content");
		put(PartialContent, "Partial Content");
		put(MultiStatus, "Multi-Status");
		put(AlreadyReported, "Already Reported");
		put(IMUsed, "IM Used");

		put(MultipleChoices, "Multiple Choices");
		put(MovedPermanently, "Moved Permanently");
		put(Found, "Found");
		put(SeeOther, "See Other");
		put(NotModified, "Not Modified");
		put(UseProxy, "Use Proxy");
		put(TemporaryRedirect, "Temporary Redirect");
		put(PermanentRedirect, "Permanent Redirect");

		put(BadRequest, "Bad Request");
		put(Unauthorized, "Unauthorized");
		put(PaymentRequired, "Payment Required");
		put(Forbidden, "Forbidden");
		put(NotFound, "Not Found");
		put(MethodNotAllowed, "Method Not Allowed");
		put(NotAcceptable, "Not Acceptable");
		put(ProxyAuthRequired, "Proxy Authentication Required");
		put(RequestTimeout, "Request Timeout");
		put(Conflict, "Conflict");
		put(Gone, "Gone");
		put(LengthRequired, "Length Required");
		put(PreconditionFailed, "Precondition Failed");
		put(RequestEntityTooLarge, "Request Entity Too Large");
		put(RequestURITooLong, "Request URI Too Long");
		put(UnsupportedMediaType, "Unsupported Media Type");
		put(RequestedRangeNotSatisfiable, "Requested Range Not Satisfiable");
		put(ExpectationFailed, "Expectation Failed");
		put(Teapot, "I'm a teapot");
		put(MisdirectedRequest, "Misdirected Request");
		put(UnprocessableEntity, "Unprocessable Entity");
		put(Locked, "Locked");
		put(FailedDependency, "Failed Dependency");
		put(TooEarly, "Too Early");
		put(UpgradeRequired, "Upgrade Required");
		put(PreconditionRequired, "Precondition Required");
		put(TooManyRequests, "Too Many Requests");
		put(RequestHeaderFieldsTooLarge, "Request Header Fields Too Large");
		put(UnavailableForLegalReasons, "Unavailable For Legal Reasons");

		put(InternalServerError, "Internal Server Error");
		put(NotImplemented, "Not Implemented");
		put(BadGateway, "Bad Gateway");
		put(ServiceUnavailable, "Service Unavailable");
		put(GatewayTimeout, "Gateway Timeout");
		put(HTTPVersionNotSupported, "HTTP Version Not Supported");
		put(VariantAlsoNegotiates, "Variant Also Negotiates");
		put(InsufficientStorage, "Insufficient Storage");
		put(LoopDetected, "Loop Detected");
		put(NotExtended, "Not Extended");
		put(NetworkAuthenticationRequired, "Network Authentication Required");
	}};

	public static String text(int status) {
		return statusTextMap.get(status);
	}

}
