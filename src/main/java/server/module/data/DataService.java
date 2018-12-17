package server.module.data;

import java.util.List;
import java.util.Set;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface DataService {
	
	public String storeDocument(byte[] document);
	public byte[] readDocument(String locator);
	public boolean deleteDocument(String locator);
	
	public void storeIndex(String patient, String locator, Set<byte[]> index);
	public Set<byte[]> getIndex(String patient, String locator);
	public boolean deleteIndex(String patient, String locator);
	public Set<String> search(String patient, Set<byte[]> trapdoor);
	
	public void storeDirectoryEntry(String patient, byte[] entry);
	public List<byte[]> getDirectoryEntries(String patient);
	public boolean deleteDirectoryEntries(String patient);
	
	public void storeAuthorization(String sender, String receiver, String patient, byte[] key);
	public Object getAuthorization(String sender, String receiver, String patient);
	public boolean deleteAuthorization(String sender, String receiver, String patient);

	public void storeAuthorizationRequest(String sender, String requestor, String patient);
	public Object getAuthorizationRequest(String sender, String requestor, String patient);
	public boolean deleteAuthorizationRequest(String sender, String requestor, String patient);

}
