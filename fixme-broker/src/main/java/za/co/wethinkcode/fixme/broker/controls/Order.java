package za.co.wethinkcode.fixme.broker.controls;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;

import za.co.wethinkcode.fixme.core.Core;
import za.co.wethinkcode.fixme.core.messages.BuySellMessage;
import za.co.wethinkcode.fixme.core.messages.Messaging;

public class Order {
	public static void send(Scanner input, int id, String side, AsynchronousSocketChannel channel) {
		String marketId = null;
		String quantity = null;
		String brokerId = Integer.toString(id);

		String val = null;

		while (true) {
			if (marketId == null) {
				if (val != null && Core.convertable(val)) {
					marketId = val;
					val = null;
					continue;
				} else {
					System.out.print("Market ID : ");
				}
			} else if (quantity == null) {
				if (val != null && Core.convertable(val)) {
					quantity = val;
					val = null;
					break;
				} else {
					System.out.print("Quantity  : ");
				}
			} 
			val = input.next();
		}
		
		Messaging.send(channel, new BuySellMessage(side, brokerId, marketId, quantity).getMessage());
	}
}
