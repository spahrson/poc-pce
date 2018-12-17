package client.core;

import java.util.Set;

/**
 * Document object
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface Document {

	public String getName();
	public void setName(String name);
	public void parse(byte[] bytes);
	public byte[] getBytes();
	public Set<String> getKeywords();
	
}
