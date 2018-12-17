package server.module.persistence;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface Persistence {
	
	public Map<String, byte[]> getDocuments();
	public Map<String, TreeSet<byte[]>> getIndexes(String patient);
	public Map<String, ArrayList<byte[]>> getDirectories();

}

