package com.coconutcoding.server.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.coconutcoding.server.HTTPServer;

/**
 * Main class.
 * @author Tom Sprudzans
 * Date: 12/13/2018
 * Time: 1858
 */
public class CoconutServer {
	
	private static Logger log = Logger.getLogger(CoconutServer.class);
	
	private static final int DEFAULT_PORT = 8080;
	
	private static final int NUMBER_OF_THREADS = 50;
	
    public static void main(String[] args) throws IOException {
    	
    	try {
    		new CoconutServer().start(getPort(args));
    	} catch (final Exception e) {
    		log.error("main(): Exception caught on startup");
    	}
    }
    
    public void start(final int port) throws IOException {
		final ServerSocket serverSock = new ServerSocket(port);		
		log.debug("start(): Server listening on port " + port);
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		while (true) {
			log.debug("start(): new request received on port " + port);
			executor.submit(new HTTPServer(serverSock.accept()));
		}
	}   
    

	/**
	 * Parse port number from args
	 * 
	 * @return int valid port number or default value (8080)
	 */
	static int getPort(String args[]) throws NumberFormatException {
		if (args.length > 0) {
			try {
				int port = Integer.parseInt(args[0]);
				if (port > 0 && port < 65535) {
					return port;
				} else {
					throw new NumberFormatException("Invalid port number. Port Number must be a number between 0 and 65535.");
				}
			} catch (final Exception e) {
				throw new NumberFormatException("Invalid port number. Port Number must be a number between 0 and 65535.");
			}
		
		}
		return DEFAULT_PORT;
	}

}
