package za.co.wethinkcode.fixme.market;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import za.co.wethinkcode.fixme.core.Core;
import za.co.wethinkcode.fixme.core.messages.ExecRejectMessage;
import za.co.wethinkcode.fixme.core.messages.Messaging;

public class Market {

	public static void main(String[] args) {
		
		int quantity = 50000;

		try {
			int connectionId 					= 0;
			AsynchronousSocketChannel channel 	= AsynchronousSocketChannel.open();
			Future<Void> result 				= channel.connect(new InetSocketAddress(Core.host, Core.portMarket));
			result.get();

			String msg = Messaging.fetchMessage(channel);
			connectionId = Integer.parseInt(msg.split(" ")[msg.split(" ").length - 1].trim());
			Core.log(msg);

			// Listening and processing incoming messages
			while (true) {
				msg = Messaging.fetchMessage(channel);
				
				// Shut down market instance if message is quit or empty.
				if (msg.trim().equalsIgnoreCase("quit") || msg.equalsIgnoreCase("")) {
					Core.log("Market " + connectionId + " shutting down.");
					break;
				} 
				// Else process order
				else {
					if ((Messaging.quantinty(msg) > quantity) && (Messaging.side(msg).equals("1"))) {
						// Reject order if requested quantity is greater than available quantity
						Messaging.send(channel, new ExecRejectMessage(msg, Integer.toString(connectionId), "8").getMessage());
					} else {
						// Accept and fill order if requested quantity is less than available quantity
						quantity -= Messaging.quantinty(msg);
						Messaging.send(channel, new ExecRejectMessage(msg, Integer.toString(connectionId), "B").getMessage());
					}
				}
				
				Core.log("[MSG] From Broker : " + msg.trim());
			}

		} catch (ExecutionException e) {
			Core.log("Error starting market. Connection refused. Router server may be offline!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
