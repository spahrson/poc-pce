package server.module.data.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import server.module.data.DataService;
import server.module.persistence.Persistence;
import server.module.persistence.impl.PersistenceImpl;
import server.module.search.PCESearchServerService;
import server.module.search.impl.PCESearchServerServiceImpl;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class DataServiceImpl implements DataService {

	private PCESearchServerService sse = new PCESearchServerServiceImpl();
	
	private Persistence pers = new PersistenceImpl();

	@Override
	public String storeDocument(byte[] document) {
		String locator = UUID.randomUUID().toString();
		pers.getDocuments().put(locator, document);
		return locator;
	}

	@Override
	public byte[] readDocument(String locator) {
		return pers.getDocuments().get(locator);
	}

	@Override
	public boolean deleteDocument(String locator) {
		return (pers.getDocuments().remove(locator)!=null?true:false);
	}

	@Override
	public void storeIndex(String patient, String locator, Set<byte[]> index) {
		TreeSet<byte[]> set = pers.getIndexes(patient).get(locator);
		if(set==null) {
			set = new TreeSet<byte[]>(
					new Comparator<byte[]>() {
						@Override
						public int compare(byte[] left, byte[] right) {
							for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
								int a = (left[i] & 0xff);
								int b = (right[j] & 0xff);
								if (a != b) {
									return a - b;
								}
							}
							return left.length - right.length;		 
						}
					}
					);
		}
		set.addAll(index);
	}

	@Override
	public Set<byte[]> getIndex(String patient, String locator) {
		return pers.getIndexes(patient).get(locator);
	}

	@Override
	public boolean deleteIndex(String patient, String locator) {
		return (pers.getIndexes(patient).remove(locator)!=null?true:false);
	}

	@Override
	public Set<String> search(String patient, Set<byte[]> trapdoor) {
		Set<String> set = new HashSet<String>();
		for(Entry<String, TreeSet<byte[]>> e : pers.getIndexes(patient).entrySet()) {
			if(sse.match(e.getValue(), trapdoor)) set.add(e.getKey());
		}
		return set;
	}

	@Override
	public void storeDirectoryEntry(String patient, byte[] entry) {
		ArrayList<byte[]> list = pers.getDirectories().get(patient);
		if(list == null) { 
			list = new ArrayList<byte[]>();
			pers.getDirectories().put(patient, list);
		}
		list.add(entry);
	}

	@Override
	public List<byte[]> getDirectoryEntries(String patient) {
		return pers.getDirectories().get(patient);
	}

	@Override
	public boolean deleteDirectoryEntries(String patient) {
		ArrayList<byte[]> list = pers.getDirectories().get(patient);
		if(list != null) list.clear();
		return true;
	}

	@Override
	public void storeAuthorization(String sender, String receiver, String patient, byte[] key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getAuthorization(String sender, String receiver, String patient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAuthorization(String sender, String receiver, String patient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void storeAuthorizationRequest(String sender, String requestor, String patient) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getAuthorizationRequest(String sender, String requestor, String patient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAuthorizationRequest(String sender, String requestor, String patient) {
		// TODO Auto-generated method stub
		return false;
	}

}
