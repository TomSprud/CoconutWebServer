package com.coconutcoding.server;

import java.net.Socket;

import org.apache.log4j.Logger;

public class HTTPServer extends Server  {      
	
	/**
	 * The HTTP Server implementation.
	 * @author Tom Sprudzans
	 */
	
	private static Logger log = Logger.getLogger(HTTPServer.class);

    public HTTPServer(final Socket socket) {
        this.socket = socket;
    }

    @Override
	public void run() {
    	log.trace("run(): entry");
    	try {
    		final RequestHandler handler = RequestHandlerFactory.getInstance().getRequestHandler(RequestHandlerFactory.HTTP_REQUEST);
    		handler.handle(socket);
    	} catch (final Exception e) {
    		log.error("Exception caught handling request: ", e);
    		
    	}
    	log.trace("run(): exit");
	}

}
