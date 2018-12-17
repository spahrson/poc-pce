package pce;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.Set;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import client.core.Category;
import client.core.DirKey;
import client.core.Document;
import client.module.cac.KeyDeriveService;
import client.module.cac.PCEDocumentService;
import client.module.cac.RootKeyService;
import client.module.cac.impl.KeyServiceImpl;
import client.module.cac.impl.PCEDocumentServiceImpl;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCEDocumentTest {

	class ExampleDocument implements Document {

		String name = "";

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public void parse(byte[] bytes) {

		}

		@Override
		public byte[] getBytes() {
			return new byte[0];
		}

		@Override
		public Set<String> getKeywords() {
			return null;
		}
	}

	@Test
	public void baseTest() {
		
		// Testet, dass die AES Verschlüsselung korrekt implementiert ist.
		// D.h. dass für ein Dokument, dass 2x verschlüsselt wird zwei unterschiedliche Ciphers generiert werden. 
		
		PCEDocumentService cac = new PCEDocumentServiceImpl();
		RootKeyService keys = new KeyServiceImpl();

		Key key = keys.generateRootKey();
		Document doc = new ExampleDocument();

		byte[] ciphertext1 = cac.encrypt(key, doc);
		byte[] ciphertext2 = cac.encrypt(key, doc);

		Assert.assertThat(ciphertext1, IsNot.not(IsEqual.equalTo(ciphertext2)));

		byte[] plaintext1 = cac.decrypt(key, ciphertext1, new ExampleDocument()).getBytes();
		byte[] plaintext2 = cac.decrypt(key, ciphertext2, new ExampleDocument()).getBytes();

		assertArrayEquals(plaintext1, plaintext2);
	}

	@Test
	public void deriveTest() {

		Provider p = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);


		// Testet, dass die Schlüssel-Herleitung korrekt implementiert ist.
		// Und inbesondere, dass 2 logisch identische Schlüssel auch gleich sind.
		
		PCEDocumentService cac = new PCEDocumentServiceImpl();
		RootKeyService keys = new KeyServiceImpl();
		KeyDeriveService der = new KeyServiceImpl();

		Key t = keys.generateRootKey();
		DirKey key_r = der.deriveDirKey(t);
		Document doc = new ExampleDocument();

		Category a = new Category("a");
		Category b = new Category("b");
		Category c = new Category("c");
		a.addChild(b);
		b.addChild(c);
		c.addDocument(null, doc);

		DirKey key_a = der.deriveDirKey(key_r, new Category("a"));
		DirKey key_b = der.deriveDirKey(key_a, new Category("b"));
		DirKey key_b_2 = der.deriveDirKey(key_a, new Category("b"));
		DirKey key_c = der.deriveDirKey(key_b, new Category("c"));

		assertEquals(key_b.key, key_b_2.key);
		assertTrue(key_b.publicKeys.equals(key_b_2.publicKeys));

		byte[] ciphertext = cac.encrypt(key_c.key, doc);

		byte[] plaintext = cac.decrypt(key_c.key, ciphertext, new ExampleDocument()).getBytes();

		assertArrayEquals(doc.getBytes(), plaintext);
		
		try {
			cac.decrypt(key_b.key, ciphertext, new ExampleDocument());
		    fail("This may no be possible!");
		} catch (Exception expectedException) {
		}
	}

}
