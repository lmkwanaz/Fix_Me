package za.co.wethinkcode.fixme.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Core {
	public static int connectionId = 100000;
	public static String host = "localhost";
	public static int portBroker = 5000;
	public static int portMarket = 5001;

	public static List<Integer> markets = new ArrayList<>();

	public static void log(String msg) {
		Date time = new Date();

		System.out.println(time.toString() + " - " + msg);
	}

	public static void removeMarket(int id) {
		for (int market = 0; market < markets.size(); market++) {

			if (markets.get(market) == id) {
				markets.remove(market);
				break;
			}

		}
	}

	public static String connectedMarkets() {
		String msg = "";

		for (int market : markets) {
			msg += "Market - " + market + "\n";
		}

		if (msg.isEmpty()) {
			msg = "No markets curently availble.";
		}

		return msg.trim();
	}
	public static boolean convertable(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) { return false; }
	}
}
