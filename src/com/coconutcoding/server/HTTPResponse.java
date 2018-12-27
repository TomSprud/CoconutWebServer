package com.coconutcoding.server;

import java.io.DataOutputStream;

/**
 *  HTTPResponse class.
 *  @author Tom Sprudzans
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class HTTPResponse {
		
    private static final String SP = " ";
	private static final String CRLF = "\r\n";
	
	public enum Status {
		_100("100 Continue"), 
		_101("101 Switching Protocols"), 
		_200("200 OK"), 
		_201("201 Created"), 
		_202("202 Accepted"), 
		_203("203 Non-Authoritative Information"), 
		_204("204 No Content"), 
		_205("205 Reset Content"), 
		_206("206 Partial Content"), 
		_300("300 Multiple Choices"), 
		_301("301 Moved Permanently"), 
		_302("302 Found"), 
		_303("303 See Other"), 
		_304("304 Not Modified"), 
		_305("305 Use Proxy"), 
		_307("307 Temporary Redirect"), 
		_400("400 Bad Request"), 
		_401("401 Unauthorized"), 
		_402("402 Payment Required"), 
		_403("403 Forbidden"), 
		_404("404 Not Found"), 
		_405("405 Method Not Allowed"), 
		_406("406 Not Acceptable"), 
		_407("407 Proxy Authentication Required"), 
		_408("408 Request Time-out"), 
		_409("409 Conflict"), 
		_410("410 Gone"), 
		_411("411 Length Required"), 
		_412("412 Precondition Failed"), 
		_413("413 Request Entity Too Large"), 
		_414("414 Request-URI Too Large"), 
		_415("415 Unsupported Media Type"), 
		_416("416 Requested range not satisfiable"), 
		_417("417 Expectation Failed"), 
		_500("500 Internal Server Error"), 
		_501("501 Not Implemented"), 
		_502("502 Bad Gateway"), 
		_503("503 Service Unavailable"), 
		_504("504 Gateway Time-out"), 
		_505("505 HTTP Version not supported"); 

		private final String status;

		Status(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return status;
		}
	}
	
	private static Logger log = Logger.getLogger(HTTPResponse.class);

	public static final String VERSION = "HTTP/1.0";
	
	private List<String> headers = new ArrayList<String>();
	
	private Socket socket;
			
	private byte[] body;
	
	public HTTPResponse(final Socket socket) {
		this.socket = socket;
	}
	
	public void parseRequest(final HTTPRequest request) {
		switch (request.getRequestMethod()) {
			case HEAD:
				writeHeaders(Status._200);
				break;
			case GET:
			try {
				File file = new File("." + request.getRequestURI());
				if (file.isDirectory()) {	
					writeHeaders(Status._200);
					StringBuilder result = new StringBuilder("<html><head><title>Index of ");
					result.append(request.getRequestURI());
					result.append("</title></head><body><h1>Index of ");
					result.append(request.getRequestURI());
					result.append("</h1><hr><pre>");
					
					File[] files = file.listFiles();
					for (File subfile : files) {
						result.append(" <a href=\"" + subfile.getPath() + "\">" + subfile.getPath() + "</a>\n");
					}
					result.append("<hr></pre></body></html>");
					writeResponse(result.toString());
				} else if (file.exists()) {
				    writeHeaders(Status._200);
					writeResponse(getBytes(file));
				} else {
					log.info("File not found:" + request.getRequestURI());
					writeHeaders(Status._404);
					writeResponse(Status._404.toString());
				}
			} catch (final Exception e) {
				log.error("Response Error Encountered Whilst Processing Request", e);
				writeHeaders(Status._400);
				writeResponse(Status._400.toString());
			}
			break;
			case UNRECOGNIZED:
				writeHeaders(Status._400);
				writeResponse(Status._400.toString());
				break;
			default:
				writeHeaders(Status._501);
				writeResponse(Status._501.toString());
			}
	}		 
	
	public void writeResponse(final String response) {
		this.body = response.getBytes();
	}
	
	public void writeResponse(final byte[] response) {
		this.body = response;
	}
	
	public void write() throws IOException {
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		for (String header : headers) {
			output.writeBytes(header + CRLF);
		}
		output.writeBytes("\r\n");
		if (body != null) {
			output.write(body);
		}
		output.writeBytes("\r\n");
		output.flush();
	}	
		
	public void writeHeaders(final Status status) {
		final StringBuilder statusHeader = new StringBuilder()
				.append(VERSION)
				.append(SP)
				.append(status.toString());	
		
		headers.add(statusHeader.toString());
		headers.add("Content-Type: text/html;charset=utf-8");
		if (body != null && body.length > 0) {
			headers.add("Content-Length: " + body.length);
		}
		headers.add("Server: Coconut Web Server");
		headers.add("Date: " + todayDate());		
	}		
	
    private String todayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        Date now = Calendar.getInstance().getTime();
        return formatter.format(now);
    }
    
    private byte[] getBytes(final File file) throws IOException {
    	final int length = (int) file.length();
    	final byte[] byteArray = new byte[length];
    	final InputStream inputStream = new FileInputStream(file);
    	Integer offset = 0;
    	while (offset < length) {
    		final int count = inputStream.read(byteArray, offset, (length - offset));
    		offset += count;
    	}
    	inputStream.close();
    	return byteArray;
    }
    

}
