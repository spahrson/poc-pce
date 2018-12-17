package client.core;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Key object (Authorization)
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class DirKey {
	
	public Key key;
	public List<PublicKey> publicKeys = new ArrayList<PublicKey>();
	
}
