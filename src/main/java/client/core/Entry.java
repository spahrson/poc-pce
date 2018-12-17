	package client.core;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * DirecoryEntry object
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class Entry {
	
	public static final String DELIMITER = "||";
	
	public String locator;
	public String filename;
	public String category;
	
	public Entry() {};
	
	public Entry(String locator, String filename, String category) {
		this.locator = locator;
		this.filename = filename;
		this.category = category;
	}

	public Entry(byte[] plaintext) {
		String c = new String(plaintext, StandardCharsets.UTF_8); // ToDo: proper encoding
		String[] tokens = c.split((Pattern.quote(DELIMITER)));
		if(tokens.length!=4) throw new IllegalStateException("Splited plaintext must have 4 elements... something is wrong with this plaintext: "+c);
		this.locator = tokens[0];
		this.filename = tokens[1];
		this.category = tokens[2];
	}
	
	public byte[] getMessage() {
		return (locator + DELIMITER + filename + DELIMITER + category).getBytes();
	}
	
	public byte[] getPlaintext(byte[] mac) {
		byte[] message = getMessage();
		byte[] d = DELIMITER.getBytes(); 
		byte[] c = new byte[message.length + d.length + mac.length];
		System.arraycopy(message, 0, c, 0, message.length);
		System.arraycopy(d, 0, c, message.length, d.length);
		System.arraycopy(mac, 0, c, message.length+d.length, mac.length);
		return c;
	}

}
