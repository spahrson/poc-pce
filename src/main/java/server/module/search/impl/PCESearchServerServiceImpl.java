package server.module.search.impl;

import java.util.Arrays;
import java.util.Set;

import server.module.search.PCESearchServerService;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCESearchServerServiceImpl implements PCESearchServerService {

	@Override
	public boolean match(Set<byte[]> index, Set<byte[]> trapdoor) {
		for(byte[] entry : index) {
			for(byte[] keyword : trapdoor) {
				if(Arrays.equals(keyword, entry)) {
					return true;
				}
			}
		}
		return false;
	}

}
