package client.module.cac.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class RSAUtil {

	public static final String ALGORITHM = "RSA";

	public RSAUtil() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public byte[] encrypt(PublicKey key, byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plaintext);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] decrypt(PrivateKey key, byte[] ciphertext) throws BadPaddingException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(ciphertext);
		} catch (IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

}