package com.coconutcoding.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Broad request handler interface, to handle network requests. 
 * @author Tom Sprudzans
 *
 */

public interface RequestHandler {	
	
	  public void handle(final Socket socket) throws IOException; 
	  
}
