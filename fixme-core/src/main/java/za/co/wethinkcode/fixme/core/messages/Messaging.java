package za.co.wethinkcode.fixme.core.messages;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

import za.co.wethinkcode.fixme.core.Core;

public class Messaging {
	static ByteBuffer buffer = ByteBuffer.allocate(2048);
	static Charset cs = Charset.forName("UTF-8");
	public static final String BROKER_COMMANDS = "Program Commands: \n"
			+ "* Markets - To See Available Markets \n"
			+ "* Sell - To sell \n"
			+ "* Buy - To buy \n"
			+ "* Quit - To exit.\n"
			+ "-----------------------\n";
	// Return String from the socket channel byte buffer
	public static String fetchMessage(AsynchronousSocketChannel channel) {
		Future<Integer> resultA = channel.read(buffer);
		
		while (!resultA.isDone()) {}

		buffer.flip();
		int limits = buffer.limit();
		byte bytes[] = new byte[limits];
		buffer.get(bytes, 0, limits);
		buffer.clear();
		
		return new String(bytes, cs);
	}
	
	// Return and print String from the socket channel byte buffer
	public static void printResponse(AsynchronousSocketChannel channel) {
		Future<Integer> resultA = channel.read(buffer);
		
		while (!resultA.isDone()) {}

		buffer.flip();
		int limits = buffer.limit();
		byte bytes[] = new byte[limits];
		buffer.get(bytes, 0, limits);
		System.out.println(new String(bytes, cs));
		buffer.clear();
	}
	
	// Write to the socket channel
	public static void send(AsynchronousSocketChannel channel, String msg) {
		byte[] data = msg.getBytes(cs);
		buffer.put(data);
		buffer.flip();
		channel.write(buffer);
		buffer.clear();
	}
	
	public static int destination(String msg) {
		try {
			return Integer.parseInt(msg.split("49=")[1].replace("^", ":").split(":")[0].trim());			
		} catch (Exception e) {
			Core.log("Recieved an invalid fix message :");
		}
		return 0;

	}
	
	public static int source(String msg) {
		try {
			return Integer.parseInt(msg.split("56=")[1].replace("^", ":").split(":")[0].trim());			
		} catch (Exception e) {
			Core.log("Message has an invaild client id : ");
		}
		return 0;

	}
	
	public static int quantinty(String msg) {
		try {
			return Integer.parseInt(msg.split("38=")[1].replace("^", ":").split(":")[0].trim());			
		} catch (Exception e) {
			Core.log("Message has an invaild quantity number : ");
		}
		return 0;
	}
	
	public static String side(String msg) {
		try {
			return msg.split("54=")[1].replace("^", ":").split(":")[0].trim();			
		} catch (Exception e) {
			Core.log("Message has an invaild side value : ");
		}
		return null;
	}
	
	public static String checksum(String msg) {
		try {
			return msg.split("10=")[1].replace("^", ":").split(":")[0].trim();			
		} catch (Exception e) {
			Core.log("Message has an invaild checksum value : ");
		}
		return null;
	}
	
	public static String calcChecksum(String msg) {
		String[] arr = msg.split("10=")[0].replace("^", ":").split(":");
		int sum = 0;
		int checkSum = 0;

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length(); j++) {
				sum += (int)arr[i].charAt(j);
			}
			sum += 1;
		}
		checkSum = sum % 256;
		
		return checkSum < 100 ? '0' + Integer.toString(checkSum) : Integer.toString(checkSum);
	}
}
