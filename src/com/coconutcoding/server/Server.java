package com.coconutcoding.server;

import java.net.Socket;

public abstract class Server implements Runnable {
	
	protected Socket socket;
	
	public abstract void run();	

}
