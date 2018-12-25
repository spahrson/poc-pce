package client.module.cac.impl;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

import client.core.Category;
import client.core.DirKey;
import client.module.cac.KeyDeriveService;
import client.module.cac.RootKeyService;
import client.module.cac.util.AESUtil;
import client.module.cac.util.RSAUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class KeyServiceImpl implements KeyDeriveService, RootKeyService {

	private Key rootKey = null;

	AESUtil aesUtil = new AESUtil();

	private Key deriveKey(Key key, String input) {
		DerivationParameters kdfParam =  new HKDFParameters(key.getEncoded(), null, input.getBytes());
		HKDFBytesGenerator hkdfGen = new HKDFBytesGenerator(new SHA512Digest());		
		hkdfGen.init(kdfParam);
		byte[] newKey = new byte[key.getEncoded().length];
		hkdfGen.generateBytes(newKey, 0, key.getEncoded().length);
		return new SecretKeySpec(newKey, 0, newKey.length, AESUtil.ALGORITHM);
	}

	@Override
	public Key generateRootKey() throws RuntimeException {
		try {
			return aesUtil.generateKey();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Key getRootKey() {
		return this.rootKey;
	}

	public void storeKey(Key rootKey) {
		this.rootKey = rootKey;
	}

	public DirKey deriveDirKey(DirKey parentKey, Category category) {
		return deriveDirKey(parentKey, category.getLabel());
	}
	
	private DirKey deriveDirKey(DirKey parentKey, String categoryLabel) {
		// copy key object
		DirKey dirKey = new DirKey();
		dirKey.publicKeys.addAll(parentKey.publicKeys);
		// derive symmetric key
		dirKey.key = deriveKey(parentKey.key, "Del".concat(categoryLabel));
		// derivce public key
		dirKey.publicKeys.add(deriveKeyPair(dirKey.key).getPublic());
		return dirKey;
	}

	public DirKey deriveDirKey(Key key) {
		DirKey dirKey = new DirKey();
		dirKey.key = key;
		dirKey.publicKeys.add(deriveKeyPair(dirKey.key).getPublic());
		return dirKey;
	}

	public Key deriveMacKey(Key key) {
		return deriveKey(key, "MAC"); 
	}

	public KeyPair deriveKeyPair(Key k) {
		try {
			Key t = deriveKey(k, "PK");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSAUtil.ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			// SecureRandom depends on OS and hardware. So SHA1PRNG may be the only possibility to get the same key for same seed on multiple OS'es.
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// FIXME: use random seed
			random.setSeed(1000L);
			keyGen.initialize(2048, random); // The maximum length of a directory entry is keylength / 8 - 11 bytes!
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}

	public DirKey deriveKeyByPath(DirKey keyRoot, String keyRootCategory, String path) {
		if(!path.contains(keyRootCategory)) {
			throw new IllegalStateException("target category "+path+" is not a child of root category "+keyRootCategory+"!");
		}
		// "string" way to get part of the path, FIXME make this proper and use category objects (dijkstra path)
		String pathN = path.substring(path.indexOf(keyRootCategory));
		return deriveKeyByPath(keyRoot, pathN);
	}
	
	public DirKey deriveKeyByPath(DirKey keyTop, String path) {	
		// remove top category (because keyTop is key of top category in path --> no derive needed)
		if(!path.contains(Category.PATH_DELIMITER)) return keyTop; // path is only one level
		path = path.substring(path.indexOf(Category.PATH_DELIMITER)+1); 
		String[] cats = path.split(Category.PATH_DELIMITER);
		// copy DirKey object
		DirKey k = new DirKey();
		k.key = keyTop.key;
		k.publicKeys.addAll(keyTop.publicKeys);
		for(String s : cats) {
			k = deriveDirKey(k, s);
		}
		return k;
	}

}
