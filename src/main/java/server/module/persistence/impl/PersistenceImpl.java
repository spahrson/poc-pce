package server.module.persistence.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import server.module.persistence.Persistence;

/**
 * Simplified persistence implementation. For testing purposes only.
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PersistenceImpl implements Persistence {
	
	HashMap<String, byte[]> documents = new HashMap<String, byte[]>();
	Map<String, HashMap<String, TreeSet<byte[]>>> indexes= new HashMap<String, HashMap<String, TreeSet<byte[]>>>();
	HashMap<String, ArrayList<byte[]>> directory = new HashMap<String, ArrayList<byte[]>>();

	@Override
	public Map<String, byte[]> getDocuments() {
		return documents;
	}

	@Override
	public Map<String, TreeSet<byte[]>> getIndexes(String patient) {
		if(!indexes.containsKey(patient)) // there's always at minimum an empty index
			indexes.put(patient, new HashMap<String, TreeSet<byte[]>>());
		return indexes.get(patient); 
	}

	@Override
	public Map<String, ArrayList<byte[]>> getDirectories() {
		return directory;
	}

}
