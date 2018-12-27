package com.coconutcoding.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * HTTP Request implementation of RequestHandler interface.
 * @author Tom Sprudzans
 */
public class HTTPRequestHandler implements RequestHandler {
	
	private static Logger log = Logger.getLogger(HTTPRequestHandler.class);
		
	private Socket socket;
	
	private HTTPResponse response;
	
	/**
	 * Default constructor.
	 */
	public HTTPRequestHandler() {
	}	
	
	@Override
	public void handle(final Socket socket) throws IOException {
		log.trace("handle(): entry");
		HTTPRequest request = new HTTPRequest(socket.getInputStream());
		HTTPResponse response = new HTTPResponse(socket);
		response.parseRequest(request);
		response.write();
		log.debug("handle(): request sent. Closing socket");
		socket.close();		
		log.trace("handle(): exit");
	}  
	
}





