package za.co.wethinkcode.fixme.broker;

import java.nio.channels.AsynchronousSocketChannel;

import za.co.wethinkcode.fixme.core.Core;
import za.co.wethinkcode.fixme.core.messages.Messaging;

public class Listener implements Runnable {
	private AsynchronousSocketChannel client;
	private int connectionId;
	
	public Listener(AsynchronousSocketChannel client, int id) {
		this.client = client;
		this.connectionId = id;
	}
	
	@Override
	public void run() {
		while(true) {
			
			String msg = Messaging.fetchMessage(client);
			if (msg.trim().equalsIgnoreCase("quit") || msg.equalsIgnoreCase("")) {
				Core.log("Broker " + connectionId + " shutting down.");
				System.exit(0);
				break;
			}
			
			Core.log("[MSG] From Server :\n" + msg.trim());
		}
	}

}
