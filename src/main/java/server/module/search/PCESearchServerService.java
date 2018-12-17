package server.module.search;

import java.util.Set;

/**
 * Symmetric Searchable Encryption Service for Server Subsystem.
 *
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface PCESearchServerService {
	
	public boolean match(Set<byte[]> index, Set<byte[]> trapdoor);
	
}
