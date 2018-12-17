package client.module.cac.impl;

import java.security.Key;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import client.module.cac.PCESearchClientService;
import client.module.cac.util.AESUtil;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCESearchClientServiceImpl implements PCESearchClientService {

	private AESUtil util = new AESUtil();

	private byte[] generateIndexEntry(Key key, String locator, String keyword) {
		byte[] byteKeyword = keyword.getBytes();
		byte[] byteLocator = locator.getBytes();
		byte[] plaintext = new byte[byteKeyword.length + byteLocator.length];			
		System.arraycopy(byteKeyword, 0, plaintext, 0, byteKeyword.length);
		System.arraycopy(byteLocator, 0, plaintext, byteKeyword.length, byteLocator.length);
		return util.encryptWithoutIV(key, plaintext);
	}


	@Override
	public Set<byte[]> generateIndex(Key key, String locator, String... keywords) {
		Set<byte[]> set = new HashSet<byte[]>();
		for(String keyword : keywords) {
			if(keyword!=null)
				set.add(generateIndexEntry(key, locator, keyword));
		}
		return set;
	}

	@Override
	public Set<byte[]> generateTrapdoor(Key key, String keyword, Set<String> locators) {
		Set<byte[]> set = new HashSet<byte[]>();
		for(String locator : locators) {
			set.add(generateIndexEntry(key, locator, keyword));
		}
		return set;
	}

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
