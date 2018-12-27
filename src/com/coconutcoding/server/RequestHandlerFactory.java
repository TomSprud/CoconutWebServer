package com.coconutcoding.server;

/**
 * RequestHandler Factory class. 
 * @author Tom Sprudzans
 */
public final class RequestHandlerFactory {
	
	public static final String HTTP_REQUEST = "HTTP_REQUEST";
	
	private static RequestHandlerFactory instance = new RequestHandlerFactory();
	
	/** 
	 * Default constr.
	 */
	public RequestHandlerFactory() {
	}
	
	/**
	 * This method returns the single instance.
	 * @return single instance
	 */
	public static RequestHandlerFactory getInstance() {
		return instance;
	}
	
	/**
	 * Returns the correct implementation of Request Handler based on request type.
	 * @param requestType request type
	 * @return request handler object
	 */
	public RequestHandler getRequestHandler(final String requestType) {
		if (requestType.equals(HTTP_REQUEST)) {
			return new HTTPRequestHandler();
		}
		return null;
	}
	
}
