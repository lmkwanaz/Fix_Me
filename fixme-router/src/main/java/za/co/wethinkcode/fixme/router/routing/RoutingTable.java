package za.co.wethinkcode.fixme.router.routing;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

import za.co.wethinkcode.fixme.core.messages.Messaging;

public class RoutingTable {
	static List<Route> routes = new ArrayList<Route>();

	public static void add(int id, AsynchronousSocketChannel client) {
		routes.add(new Route(id, client));
	}

	public static void remove(int id, AsynchronousSocketChannel client) {
		for (Route r : routes) {
			try {
				if (r.getRouteClient().getRemoteAddress().equals(client.getRemoteAddress()) && (r.getRouteId() == id)) {
					routes.remove(r);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void forward(String msg) {
		for (Route route : routes) {
			if (Messaging.destination(msg) == 0) {
				break;
			} else if (route.getRouteId() == Messaging.destination(msg)) {
				if (Messaging.checksum(msg).equals(Messaging.calcChecksum(msg))) {
					Messaging.send(route.getRouteClient(), msg);
				} else {
					System.out.println("Fail! " + Messaging.calcChecksum(msg));
				}
				break;
			}
		}
	}

}
