package za.co.wethinkcode.fixme.router;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import za.co.wethinkcode.fixme.core.Core;

public class Server implements Runnable {
	private String	host;
	private int		port;
	
	// Constructing new server instance
	public Server (String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			// Creating server channel and binding it to port
			AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel
					.open()
					.bind(new InetSocketAddress(this.host, this.port)
			);
			
			// Determining port
			if (this.port == Core.portBroker) {
				Core.log("Broker Server is listening at : " + serverChannel.getLocalAddress());
			} else if (this.port == Core.portMarket) {
				Core.log("Market Server is listening at : " + serverChannel.getLocalAddress());
			}
			
			// New connection instance
		    serverChannel.accept(serverChannel, new ConnectionHandler());
		    Thread.currentThread().join();
		} catch (BindException e) {
			Core.log("Error starting router. Port [" + this.port + "] is already in use!");
			System.exit(-1);
		} catch (Exception e) {
			Core.log("Error starting server : " + e.getMessage());
			System.exit(-1);
		} 
	}
}
