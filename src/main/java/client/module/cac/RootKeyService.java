package client.module.cac;

import java.security.Key;

/**
 * Simple RootKeyService for simulating a RootKey container
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface RootKeyService {
	
	public Key generateRootKey();
	
	public Key getRootKey();
	
}
