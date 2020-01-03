package za.co.wethinkcode.fixme.core.messages;

public class BuySellMessage {
	private String soh			= "" + (char)1;
	
	private String clientId		= "id=";
	private String fixVersion	= "8=FIX.4.2" + this.soh; 
	private String msgLength	= "9=";
	private String msgType		= "35=D";
	private String server		= "49=";
	private String client		= "56=";
	private String side			= "54=";
	private String quantity		= "38=";
	private String price		= "44=";
	private String checksum		= "10=";
	
	
	private String message = null;
	
	public BuySellMessage (String side, String src, String dest, String qty) {
		this.clientId += src + this.soh;
		this.quantity += qty + this.soh;
		this.side += side + this.soh;
		this.msgType += this.soh;
		this.server += dest + this.soh;
		this.client += src + this.soh;
		this.price += "1" + this.soh;
		
		this.message = this.msgType + this.server + this.client + this.side + this.quantity + this.price;
		this.msgLength += bodyLength(this.message) + this.soh;
		
		this.message = this.clientId + this.fixVersion + this.msgLength 
				+ this.message + this.checksum;
		this.message += calcChecksum(this.message) + this.soh;
	}
	
	private String bodyLength(String msg) {		
		return Integer.toString(msg.length());
	}
	
	private String calcChecksum(String msg) {
		String[] arr = msg.split("" + (char)1);
		int sum = 0;
		int checkSum = 0;

		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr[i].length(); j++) {
				sum += (int)arr[i].charAt(j);
			}
			sum += 1;
		}
		checkSum = sum % 256;
		
		return checkSum < 100 ? '0' + Integer.toString(checkSum) : Integer.toString(checkSum);
	}
	
	public String getMessage() { 
		return this.message.replace((char)1, '^');
	}
}
