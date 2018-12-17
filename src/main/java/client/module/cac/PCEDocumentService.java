package client.module.cac;

import java.security.Key;

import client.core.Document;

/**
 * Secure Documents
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public interface PCEDocumentService {
	
	/**
	 * Encrypts a plaintext and retruns the corresponding ciphertext
	 * @return ciphertext
	 */
	public byte[] encrypt(Key key, Document document);
		
	/**
	 * Decrypts a ciphertext and retruns the corresponding document
	 * @return document
	 */
	public <T extends Document> T decrypt(Key key, byte[] ciphertext, T document);
	
}
