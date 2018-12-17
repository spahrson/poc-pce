package client.module.cac;

import java.util.List;
import java.util.Set;

import client.core.DirKey;
import client.core.Entry;

/**
 * Secure Directory
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface PCEDirectoryService {
	
	public List<byte[]> encrypt(DirKey key, Entry entry);
	
	public Set<Entry> decrypt(DirKey key, List<byte[]> ciphertext);
		
}
