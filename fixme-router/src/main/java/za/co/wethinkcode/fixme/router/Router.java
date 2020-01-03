package za.co.wethinkcode.fixme.router;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import za.co.wethinkcode.fixme.core.Core;

public class Router {

	public static int clientId = 100000;

	public static void main(String[] args) {
		ExecutorService threads = Executors.newCachedThreadPool();
		threads.submit(new Server(Core.host, Core.portBroker));
		threads.submit(new Server(Core.host, Core.portMarket));
		threads.shutdown();
	}

}
