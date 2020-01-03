package za.co.wethinkcode.fixme.router;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import za.co.wethinkcode.fixme.core.Core;
import za.co.wethinkcode.fixme.core.messages.Messaging;
import za.co.wethinkcode.fixme.router.routing.RoutingTable;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
	@Override
	public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
		try {
			SocketAddress clientAddr = client.getRemoteAddress();
			int clientId = Router.clientId++;
			
			if (Integer.parseInt(client.getLocalAddress().toString().split(":")[1]) == Core.portBroker) {
				Core.log("Broker server accepted a  connection [" + clientId + "] from" + clientAddr);
			} else if (Integer.parseInt(client.getLocalAddress().toString().split(":")[1]) == Core.portMarket) {
				Core.log("Market server accepted a  connection [" + clientId + "] from" + clientAddr);
				Core.markets.add(clientId);
			}
			
			server.accept(server, this);
			
			RoutingTable.add(clientId, client);
			Messaging.send(client, "Connection accepted - Connection ID: " + clientId);
			while(true) {
				
				String msg = Messaging.fetchMessage(client);
				
				if (msg.startsWith("req:")) {
					if (msg.trim().endsWith("markets")) {
						Messaging.send(client, Core.connectedMarkets());
					}
				} else if (msg.trim().equalsIgnoreCase("quit") || msg.equalsIgnoreCase("")) {
					RoutingTable.remove(clientId, client);
					Core.removeMarket(clientId);
					client.close();
					Core.log("Stopped   listening to the   client " + clientAddr);
					break;
				} else {
					RoutingTable.forward(msg);
				}

				Core.log("[MSG] Client " + clientAddr + " : " + msg.trim());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable e, AsynchronousServerSocketChannel attach) {
		Core.log("Failed to accept a  connection.");
		e.printStackTrace();
	}
}
