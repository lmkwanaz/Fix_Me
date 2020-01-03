package za.co.wethinkcode.fixme.broker;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import za.co.wethinkcode.fixme.broker.controls.Order;
import za.co.wethinkcode.fixme.core.Core;
import za.co.wethinkcode.fixme.core.messages.Messaging;

public class Broker {
	public static void main(String[] args) {

		try {
			int connectionId 					= 0;
			Scanner input 						= new Scanner(System.in);
			AsynchronousSocketChannel channel	= AsynchronousSocketChannel.open();
			Future<Void> result 				= channel.connect(new InetSocketAddress(Core.host, Core.portBroker));
			
			result.get();
			clearScreen(Messaging.BROKER_COMMANDS);
			String msg 		= Messaging.fetchMessage(channel);
			connectionId	= Integer.parseInt(msg.split(" ")[msg.split(" ").length - 1].trim());
			Core.log(msg);
			
			ExecutorService listeners = Executors.newCachedThreadPool();
			listeners.submit(new Listener(channel, connectionId));
			listeners.shutdown();
			
			while (true) {
				System.out.println("Command: ");
				String cmd = input.next();
				
				clearScreen(Messaging.BROKER_COMMANDS);
				if (cmd.equalsIgnoreCase("Markets")) {
					System.out.println("Available Markets :");
					Messaging.send(channel, "req:markets");
				} else if (cmd.equalsIgnoreCase("Sell")) {
					System.out.println("Selling");
					Order.send(input, connectionId, "2", channel);
				} else if (cmd.equalsIgnoreCase("Buy")) {
					System.out.println("Buying");
					Order.send(input, connectionId, "1", channel);
				} else if (cmd.equalsIgnoreCase("Quit")) {
					input.close();
					break;
				}
			}
		} catch (ExecutionException e) {
			Core.log("Error starting broker. Connection refused. Router server may be offline!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static void clearScreen(String msg) {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		
		System.out.println(msg);
	}
}
