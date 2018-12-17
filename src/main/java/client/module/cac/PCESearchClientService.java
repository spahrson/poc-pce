package client.module.cac;

import java.security.Key;
import java.util.Set;

/**
 * Encrypted search
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface PCESearchClientService {
	
	public Set<byte[]> generateIndex(Key key, String locator, String... keywords);
	
	public Set<byte[]> generateTrapdoor(Key key, String keyword, Set<String> locators);

	public boolean match(Set<byte[]> index, Set<byte[]> trapdoor);
	
}
