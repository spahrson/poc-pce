package client.module.cac.impl;

import java.security.Key;

import client.core.Document;
import client.module.cac.PCEDocumentService;
import client.module.cac.util.AESUtil;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCEDocumentServiceImpl implements PCEDocumentService {
	
	private AESUtil util = new AESUtil();
	
	@Override
	public byte[] encrypt(Key key, Document document) {
		return util.encrypt(key, document.getBytes());
	}
	
	@Override
	public <T extends Document> T decrypt(Key key, byte[] ciphertext, T document) {
		document.parse(util.decrypt(key, ciphertext));
		return document;
	}
	
}
