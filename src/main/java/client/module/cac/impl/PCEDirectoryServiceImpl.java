package client.module.cac.impl;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;

import client.core.Category;
import client.core.DirKey;
import client.core.Entry;
import client.module.cac.KeyDeriveService;
import client.module.cac.PCEDirectoryService;
import client.module.cac.util.AESUtil;
import client.module.cac.util.RSAUtil;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCEDirectoryServiceImpl implements PCEDirectoryService {

	private AESUtil aes = new AESUtil();
	private RSAUtil rsa = new RSAUtil();
	private KeyDeriveService keys = new KeyServiceImpl();

	@Override
	public List<byte[]> encrypt(DirKey key, Entry entry) {
		List<byte[]> list = new ArrayList<byte[]>();
		try {
			Key macKey = keys.deriveMacKey(key.key);
			for(PublicKey k : key.publicKeys) {
				byte[] mac = aes.calculateMAC(macKey, entry.getMessage());
				list.add(rsa.encrypt(k, entry.getPlaintext(mac)));
			}
			return list;
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<Entry> decrypt(DirKey key, List<byte[]> ciphertext) {
		Set<Entry> list = new HashSet<Entry>();
		KeyPair keyPair = keys.deriveKeyPair(key.key);
		for(byte[] c : ciphertext) {
			byte[] plaintext = null;
			try {
				plaintext = rsa.decrypt(keyPair.getPrivate(), c);
			} catch (BadPaddingException e) {
				// oops... that means no rights for this entry
				continue;
			}
			Entry entry = new Entry(plaintext);
			try {
				DirKey exactKey = keys.deriveKeyByPath(key,findAuthorizationLevel(key, entry.category), entry.category);
				Key macKey = keys.deriveMacKey(exactKey.key);
				byte[] mac;
				mac = aes.calculateMAC(macKey, entry.getMessage());
				byte[] macOrigin = Arrays.copyOfRange(plaintext, plaintext.length-mac.length, plaintext.length);
				if(Arrays.equals(macOrigin,mac)) {
					list.add(entry);
				}
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * Find the level of authorization.
	 * @param key - DirKey of authorization (also containing public-keys)
	 * @param path - Path of a category
	 * @return Category name of the authorization level out of the path
	 */
	public static String findAuthorizationLevel(DirKey key, String path) {
		// really ugly part of pce, see thesis for details
		// TODO: Improve return values in case of errors / wrong params
		int numPubKey = key.publicKeys.size();
		String[] tokens = path.split(Category.PATH_DELIMITER);
		int numCats = tokens.length;
		int diff = numCats - numPubKey;
		return tokens[numCats-diff-1];  
	}

}
