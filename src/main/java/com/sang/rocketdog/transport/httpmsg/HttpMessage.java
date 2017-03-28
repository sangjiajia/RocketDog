package com.sang.rocketdog.transport.httpmsg;

import java.util.Map;

public class HttpMessage {

	private String method;

	private String requestPath;

	private String version;

	private Map<String, Object> headers;

	private Object context;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}

}
