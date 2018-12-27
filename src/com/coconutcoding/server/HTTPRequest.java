package com.coconutcoding.server;

import java.io.BufferedReader;

/**
 * HTTP Request class.
 * @author Tom Sprudzans
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class HTTPRequest {
	
	/**
	 * Enum which maps the HTTP/1.1 available request methods 
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
	 */
	public enum RequestMethod {
		GET("GET"), //
		HEAD("HEAD"), //
		POST("POST"), //
		PUT("PUT"), //
		DELETE("DELETE"), //
		TRACE("TRACE"), //
		CONNECT("CONNECT"), //
		UNRECOGNIZED(null); //

		private final String requestMethod;

		RequestMethod(String requestMethod) {
			this.requestMethod = requestMethod;
		}
	}
	
	private static Logger log = Logger.getLogger(HTTPRequest.class);
	
	private List<String> headers = new ArrayList<String>();
	
	private RequestMethod requestMethod;	
	private String requestURI;
	private String requestProtocolVersion;
	
	public HTTPRequest(final InputStream is) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String str = reader.readLine();
		parseRequestLine(str);

		while (!str.equals("")) {
			str = reader.readLine();
			parseRequestHeader(str);
		}
	}
	
	private void parseRequestLine(String str) {
		log.info(str);
		String[] split = str.split("\\s+");
		try {
			requestMethod = RequestMethod.valueOf(split[0]);
		} catch (final Exception e) {
			log.debug("parseRequestLine(): invalid HTTP method detected - " + requestMethod);
			requestMethod = RequestMethod.UNRECOGNIZED;
		}
		requestURI = split[1];
		requestProtocolVersion = split[2];
	}

	private void parseRequestHeader(String str) {
		log.info(str);
		headers.add(str);
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(final String requestURI) {
		this.requestURI = requestURI;
	}

	public String getRequestProtocolVersion() {
		return requestProtocolVersion;
	}

	public void setRequestProtocolVersion(final String requestProtocolVersion) {
		this.requestProtocolVersion = requestProtocolVersion;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(final List<String> headers) {
		this.headers = headers;
	}
	

}
