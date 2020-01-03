package za.co.wethinkcode.fixme.router.routing;

import java.nio.channels.AsynchronousSocketChannel;

public class Route {
	private int							routeId;
	private AsynchronousSocketChannel	routeClient;
	
	public Route(int clientId, AsynchronousSocketChannel client) {
		this.routeId		= clientId;
		this.routeClient	= client;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public AsynchronousSocketChannel getRouteClient() {
		return routeClient;
	}

	public void setRouteClient(AsynchronousSocketChannel routeClient) {
		this.routeClient = routeClient;
	}
	
}
