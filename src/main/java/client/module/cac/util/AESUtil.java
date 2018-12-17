package client.module.cac.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class AESUtil {

	public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	public static final String PROVIDER = "BC";
	public static final String SECRET_ENCODING = "UTF-8";
	public static final String  ALGORITHM = "AES";
	public static final String KEY_GENERATOR = "PBKDF2WithHmacSHA256";
	public static final int KEY_LENGTH = 256;
	public static final int KEY_SEED_LENGTH = 8;
	public static final String MAC_ALGORITHM = "HMACSHA256";

	public AESUtil() {
		Security.addProvider(new BouncyCastleProvider());
	}

	public byte[] encrypt(Key key, byte[] plaintext) throws RuntimeException {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return encrypt(plaintext, key, cipher);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] encrypt(byte[] plaintext, Key key, Cipher cipher) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException {
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] tempCiphertext = cipher.doFinal(plaintext);
		byte[] ciphertext  = new byte[tempCiphertext.length + iv.length];
		System.arraycopy(tempCiphertext, 0, ciphertext, 0, tempCiphertext.length);
		System.arraycopy(iv, 0, ciphertext, tempCiphertext.length, 16);
		return ciphertext;
	}

	public byte[] encryptWithoutIV(Key key, byte[] plaintext) throws RuntimeException {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(TRANSFORMATION);
			// Initialisation vector: 00..00
			byte[] iv = new byte[cipher.getBlockSize()];
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			return encrypt(plaintext, key, cipher);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] decrypt(Key key, byte[] ciphertext) throws RuntimeException {
		int ivIndex = ciphertext.length - 16;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ciphertext, ivIndex, 16));
			return cipher.doFinal(ciphertext, 0, ivIndex);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public Key generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
		KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
		keyGen.init(KEY_LENGTH); // for example
		return keyGen.generateKey();
	}

	public Key generateKey(byte[] secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
		char[] char_secret = new String(secret, SECRET_ENCODING).toCharArray();
		byte[] seed = SecureRandom.getInstanceStrong().generateSeed(KEY_SEED_LENGTH);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATOR);
		KeySpec spec = new PBEKeySpec(char_secret, seed, 65536, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
	}

	public byte[] calculateMAC(Key key, byte[] message) throws InvalidKeyException, NoSuchAlgorithmException {
		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		mac.init(key);
		return mac.doFinal(message);
	}

}
