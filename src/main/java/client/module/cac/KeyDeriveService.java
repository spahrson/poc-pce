package client.module.cac;

import java.security.Key;
import java.security.KeyPair;

import client.core.Category;
import client.core.DirKey;

/**
 * Service for key derive
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface KeyDeriveService {
	
	/** 
	 * Derive DirKey of simple Key (RootKey)
	 * @param authorizationKey
	 * @return
	 */
	public DirKey deriveDirKey(Key authorizationKey);
	
	/**
	 * Derive DirKey for a category
	 * @param parentKey 
	 * @param category
	 * @return DirKey of category
	 */
	public DirKey deriveDirKey(DirKey parentKey, Category category);
	
	/**
	 * Derive MacKey for a category
	 * @param key
	 * @return Key
	 */
	public Key deriveMacKey(Key key);

	/**
	 * Derive KeyPair for a category
	 * @param key
	 * @return KeyPair
	 */
	public KeyPair deriveKeyPair(Key key);
	
	/**
	 * Derive DirKey by path of a category. keyRoot and keyRootCategory must match each other and be part of the path.
	 * @param keyRoot 
	 * @param keyRootCategory
	 * @param path
	 * @return
	 */
	public DirKey deriveKeyByPath(DirKey keyRoot, String keyRootCategory, String path);
	
}
